import org.junit.Test;

import com.google.javascript.jscomp.*;

import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = new Node(Token.NAME, "use");
    Node defNode = new Node(Token.NAME, "def");
    cfg.createNode(defNode);
    MustBeReachingVariableDef.MustDef mustDef = analysis.createEntryLattice();
    mustDef.reachingDef.put(jsScope.getVar("def"), new MustBeReachingVariableDef.Definition(defNode));
    GraphNode<Node, ControlFlowGraph.Branch> graphNode = new GraphNode<>(useNode);
    graphNode.setAnnotation(new FlowState<>(mustDef, mustDef));
    cfg.createNode(useNode);
    cfg.putEdge(defNode, useNode, ControlFlowGraph.Branch.ON_TRUE);
    
    Node result = analysis.getDef("def", useNode);
    
    assertEquals(defNode, result);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = new Node(Token.NAME, "use");
    Node defNode = new Node(Token.NAME, "def");
    cfg.createNode(defNode);
    MustBeReachingVariableDef.MustDef mustDef = analysis.createEntryLattice();
    mustDef.reachingDef.put(jsScope.getVar("def"), new MustBeReachingVariableDef.Definition(defNode));
    mustDef.reachingDef.put(jsScope.getVar("outer"), new MustBeReachingVariableDef.Definition(defNode));
    mustDef.reachingDef.get(jsScope.getVar("def")).depends.add(jsScope.getVar("outer"));
    GraphNode<Node, ControlFlowGraph.Branch> graphNode = new GraphNode<>(useNode);
    graphNode.setAnnotation(new FlowState<>(mustDef, mustDef));
    cfg.createNode(useNode);
    cfg.putEdge(defNode, useNode, ControlFlowGraph.Branch.ON_TRUE);
    
    boolean result = analysis.dependsOnOuterScopeVars("def", useNode);
    
    assertTrue(result);
  }

}