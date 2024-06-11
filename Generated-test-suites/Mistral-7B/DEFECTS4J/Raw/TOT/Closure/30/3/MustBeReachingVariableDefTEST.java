import static com.google.javascript.jscomp.MustBeReachingVariableDef.Definition;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  private static ControlFlowGraph<Node> createTestGraph() {
    // A simple control flow graph for testing
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    // Entry node
    Node entry = Node.newBlock("entry");
    cfg.addNode(entry);

    // Assign a to b
    Node assignAtoB = Node.newExpression("a = b");
    cfg.addNode(assignAtoB);
    cfg.addEdge(entry, assignAtoB);

    // Assign b to a
    Node assignBtoA = Node.newExpression("b = a");
    cfg.addNode(assignBtoA);
    cfg.addEdge(assignAtoB, assignBtoA);

    // Assign c to a
    Node assignCtoA = Node.newExpression("c = a");
    cfg.addNode(assignCtoA);
    cfg.addEdge(assignBtoA, assignCtoA);

    // Use a
    Node useA = Node.newExpression("alert(a)");
    cfg.addNode(useA);
    cfg.addEdge(assignCtoA, useA);

    return cfg;
  }

  private static Scope createTestScope() {
    // Test scope
    Scope scope = new Scope();

    // Test variables
    Var a = new Var("a", Node.newName("a"), null);
    Var b = new Var("b", Node.newName("b"), null);
    Var c = new Var("c", Node.newName("c"), null);

    // Add variables to the scope
    scope.addVar(a);
    scope.addVar(b);
    scope.addVar(c);

    return scope;
  }

  private static AbstractCompiler createTestCompiler() {
    // Test compiler
    AbstractCompiler compiler = new AbstractCompiler();
    return compiler;
  }

  @org.junit.Test
  public void testMustBeReachingVariableDefSuccess() {
    // Create test graph, scope, and compiler
    ControlFlowGraph<Node> cfg = createTestGraph();
    Scope jsScope = createTestScope();
    AbstractCompiler compiler = createTestCompiler();

    // Create MustBeReachingVariableDef analyzer
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Perform analysis
    MustDef result = analyzer.compute();

    // Test the results
    assertEquals(3, result.reachingDef.size());

    // Test reaching definitions for variables "a", "b", and "c"
    Map.Entry<Var, Definition> aEntry = result.reachingDef.entrySet().iterator().next();
    assertEquals("entry", aEntry.getValue().node.getString());
    assertSame(a, aEntry.getKey());

    Map.Entry<Var, Definition> bEntry = result.reachingDef.entrySet().iterator().next();
    assertSame(b, bEntry.getKey());
    assertSame(assignAtoB, bEntry.getValue().node);

    Map.Entry<Var, Definition> cEntry = result.reachingDef.entrySet().iterator().next();
    assertSame(c, cEntry.getKey());
    assertSame(assignCtoA, cEntry.getValue().node);

    // Test that variables dependencies are correct
    assertEquals(Arrays.asList(a, b), bEntry.getValue().depends);
    assertEquals(Collections.singletonList(b), cEntry.getValue().depends);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDefMissingDefinition() {
    // Create test graph, scope, and compiler
    ControlFlowGraph<Node> cfg = createTestGraph();
    Scope jsScope = createTestScope();
    AbstractCompiler compiler = createTestCompiler();

    // Create MustBeReachingVariableDef analyzer
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Remove definition node for variable "a"
    Node useA = cfg.getNode("useA");
    cfg.removeEdge(cfg.getNode("assignCtoA"), useA);

    // Perform analysis
    MustDef result = analyzer.compute();

    // Test the results
    assertEquals(3, result.reachingDef.size());

    // Test reaching definitions for variables "a", "b", and "c"
    assertNull(result.reachingDef.get(jsScope.getVar("a")));
    assertSame(assignBtoA, result.reachingDef.get(jsScope.getVar("b")).node);
    assertSame(assignCtoA, result.reachingDef.get(jsScope.getVar("c")).node);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDefEscapedVariable() {
    // Create test graph, scope, and compiler
    ControlFlowGraph<Node> cfg = createTestGraph();
    Scope jsScope = createTestScope();
    AbstractCompiler compiler = createTestCompiler();

    // Create MustBeReachingVariableDef analyzer
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add an escaped variable to the scope
    Var escapedVar = new Var("escapedVar", Node.newName("escapedVar"), null);
    escapedVar.setEscaped(true);
    jsScope.addVar(escapedVar);

    // Perform analysis
    MustDef result = analyzer.compute();

    // Test the results
    assertEquals(3, result.reachingDef.size());

    // Test reaching definitions for variables "a", "b", and "c"
    Map.Entry<Var, Definition> aEntry = result.reachingDef.entrySet().iterator().next();
    assertEquals("entry", aEntry.getValue().node.getString());
    assertSame(a, aEntry.getKey());

    Map.Entry<Var, Definition> bEntry = result.reachingDef.entrySet().iterator().next();
    assertSame(b, bEntry.getKey());
    assertSame(assignAtoB, bEntry.getValue().node);

    Map.Entry<Var, Definition> cEntry = result.reachingDef.entrySet().iterator().next();
    assertSame(c, cEntry.getKey());
    assertSame(assignCtoA, cEntry.getValue().node);

    // Test that escaped variable "escapedVar" is not in the lattice
    assertNull(result.reachingDef.get(escapedVar));
  }

  @org.junit.Test
  public void testMustBeReachingVariableDefReachingDefinitionIsNotFound() {
    // Create test graph, scope, and compiler
    ControlFlowGraph<Node> cfg = createTestGraph();
    Scope jsScope = createTestScope();
    AbstractCompiler compiler = createTestCompiler();

    // Create MustBeReachingVariableDef analyzer
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Use a non-existent definition node for variable "a"
    Node nonExistentUseA = Node.newExpression("alert(a)");

    // Perform analysis
    MustDef result = analyzer.compute();

    // Test the results
    assertEquals(3, result.reachingDef.size());

    // Test reaching definitions for variables "a", "b", and "c"
    assertNull(result.reachingDef.get(jsScope.getVar("a")));
    assertSame(assignBtoA, result.reachingDef.get(jsScope.getVar("b")).node);
    assertSame(assignCtoA, result.reachingDef.get(jsScope.getVar("c")).node);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDefReachingDefinitionIsMultiple() {
    // Create test graph, scope, and compiler
    ControlFlowGraph<Node> cfg = createTestGraph();
    Scope jsScope = createTestScope();
    AbstractCompiler compiler = createTestCompiler();

    // Create MustBeReachingVariableDef analyzer
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add a loop to the control flow graph
    Node loopCondition = Node.newExpression("true");
    Node loopStart = Node.newExpression("while(true){");
    Node loopEnd = Node.newExpression("}");
    cfg.addNode(loopCondition);
    cfg.addNode(loopStart);
    cfg.addNode(loopEnd);
    cfg.addEdge(loopCondition, loopStart);
    cfg.addEdge(loopEnd, loopCondition);

    // Perform analysis
    MustDef result = analyzer.compute();

    // Test the results
    assertEquals(3, result.reachingDef.size());

    // Test reaching definitions for variables "a", "b", and "c"
    Map<Var, Definition> reachingDefinitions = result.reachingDef;

    // Test reaching definition for variable "a"
    Set<Definition> aDefinitions = new HashSet<>();
    for (Definition def : reachingDefinitions.values()) {
      if (def.node == cfg.getNode("assignAtoB")) {
        aDefinitions.add(def);
      }
      if (def.node == cfg.getNode("assignCtoA")) {
        aDefinitions.add(def);
      }
    }
    assertEquals(2, aDefinitions.size());
    // This test is not guaranteed to pass since there is no way to know
    // which definition should be chosen as the reaching definition.
    //assertSame(assignAtoB, aDefinitions.iterator().next());

    // Test reaching definition for variable "b"
    assertSame(assignAtoB, reachingDefinitions.get(jsScope.getVar("b")).node);

    // Test reaching definition for variable "c"
    assertSame(assignCtoA, reachingDefinitions.get(jsScope.getVar("c")).node);
  }
}