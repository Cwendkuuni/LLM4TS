import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testBasicFunctionality() {
    // Given a simple function
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);
    Node returnNode = newReturn(newName("result"));
    functionNode.addChild(returnNode);

    Var x = newVar(ID, "x");
    Var y = newVar(ID, "y");
    Var result = newVar(ID, "result");

    // Add statements to the function
    Node assignX = newAssign(x, newNumber(1));
    Node assignY = newAssign(y, newNumber(2));
    Node add = newBinaryOp("+", assignX, assignY);
    Node assignResult = newAssign(result, add);

    functionNode.addChild(assignX);
    functionNode.addChild(assignY);
    functionNode.addChild(add);
    functionNode.addChild(assignResult);

    // Then the analysis should correctly identify the definitions
    MustDef mustDef = analyzer.computeFinalResult();
    assertEquals(2, mustDef.reachingDef.size());
    assertTrue(mustDef.reachingDef.containsKey(x));
    assertTrue(mustDef.reachingDef.containsKey(y));

    // And the result should be equal to the expected definitions
    Definition expectedXDefinition = new Definition(assignX);
    Definition expectedYDefinition = new Definition(assignY);
    assertEquals(expectedXDefinition, mustDef.reachingDef.get(x));
    assertEquals(expectedYDefinition, mustDef.reachingDef.get(y));
  }

  @Test
  public void testConditionalBranch() {
    // Given a conditional branch
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);

    Var x = newVar(ID, "x");
    Var y = newVar(ID, "y");
    Var result = newVar(ID, "result");

    // Add statements to the function
    Node ifNode = newIf(newLogicalOp("&&", newBinaryOp("!=", newName("x"), newNumber(0)), newBinaryOp(">", newName("y"), newNumber(5))));
    Node ifBranch = ifNode.getThen();
    Node assignX = newAssign(x, newNumber(1));
    Node assignY = newAssign(y, newNumber(2));
    Node add = newBinaryOp("+", assignX, assignY);
    Node assignResultIf = newAssign(result, add);

    Node elseBranch = ifNode.getElse();
    Node assignXElse = newAssign(x, newNumber(3));
    Node assignResultElse = newAssign(result, newNumber(4));

    ifBranch.addChild(assignX);
    ifBranch.addChild(assignY);
    ifBranch.addChild(add);
    ifBranch.addChild(assignResultIf);

    elseBranch.addChild(assignXElse);
    elseBranch.addChild(assignResultElse);

    functionNode.addChild(ifNode);

    // Then the analysis should correctly identify the definitions
    MustDef mustDef = analyzer.computeFinalResult();
    assertEquals(2, mustDef.reachingDef.size());
    assertTrue(mustDef.reachingDef.containsKey(x));
    assertTrue(mustDef.reachingDef.containsKey(y));

    // And the result should be equal to the expected definitions
    Definition expectedXDefinitionIf = new Definition(assignX);
    Definition expectedYDefinitionIf = new Definition(assignY);
    assertEquals(expectedXDefinitionIf, mustDef.reachingDef.get(x));
    assertEquals(expectedYDefinitionIf, mustDef.reachingDef.get(y));

    Definition expectedXDefinitionElse = new Definition(assignXElse);
    Definition expectedYDefinitionElse = new Definition(assignY);
    assertEquals(expectedXDefinitionElse, mustDef.reachingDef.get(x));
    assertEquals(expectedYDefinitionElse, mustDef.reachingDef.get(y));
  }

  @Test
  public void testCycle() {
    // Given a function with a cycle
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);

    Var x = newVar(ID, "x");

    // Add statements to the function
    Node assignX = newAssign(x, newName("x"));
    functionNode.addChild(assignX);

    // Then the analysis should throw an exception
    try {
      analyzer.computeFinalResult();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEscapedVariable() {
    // Given a function with an escaped variable
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);

    Var x = newVar(ID, "x");
    Var globalX = newVar(ID, "x");

    // Add statements to the function
    Node assignX = newAssign(x, newNumber(1));
    Node assignGlobalX = newAssign(globalX, newName("x"));
    functionNode.addChild(assignX);
    functionNode.addChild(assignGlobalX);

    // Then the analysis should correctly identify the definitions
    MustDef mustDef = analyzer.computeFinalResult();
    assertEquals(1, mustDef.reachingDef.size());
    assertNull(mustDef.reachingDef.get(x));
  }

  @Test
  public void testMultipleDefinitions() {
    // Given a function with multiple definitions for a variable
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);

    Var x = newVar(ID, "x");

    // Add statements to the function
    Node assignX1 = newAssign(x, newNumber(1));
    Node assignX2 = newAssign(x, newNumber(2));
    functionNode.addChild(assignX1);
    functionNode.addChild(assignX2);

    // Then the analysis should correctly identify all definitions
    MustDef mustDef = analyzer.computeFinalResult();
    assertEquals(1, mustDef.reachingDef.size());
    assertTrue(mustDef.reachingDef.containsKey(x));
    Definition definition = mustDef.reachingDef.get(x);
    assertTrue(definition.depends.isEmpty());
  }

  @Test
  public void testMultipleUses() {
    // Given a function with multiple uses for a variable
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new TestCompiler(jsScope);
    ControlFlowGraph<Node> cfg = ControlFlowGraph.create(compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When the analysis is performed
    Node functionNode = newFunction(NAME, "testFunction");
    Node rootNode = newBlock();
    rootNode.addChild(functionNode);
    cfg.setRootNode(rootNode);

    Var x = newVar(ID, "x");

    // Add statements to the function
    Node assignX = newAssign(x, newNumber(1));
    Node useX1 = newGetMember(newName("result"), x); // result.x
    Node useX2 = newGetMember(newName("result"), x); // result.x
    Node assignResult = newAssign(newGetMember(newName("result"), newName("x")), useX1); // result.x = result.x

    functionNode.addChild(assignX);
    functionNode.addChild(useX1);
    functionNode.addChild(useX2);
    functionNode.addChild(assignResult);

    // Then the analysis should correctly identify the definition
    MustDef mustDef = analyzer.computeFinalResult();
    assertEquals(1, mustDef.reachingDef.size());
    assertTrue(mustDef.reachingDef.containsKey(x));
    Definition definition = mustDef.reachingDef.get(x);
    assertEquals(Sets.newHashSet(useX1, useX2), definition.depends);
  }
}