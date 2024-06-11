import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef_SimpleAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.VAR, "a"));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5)));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 10));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    Node expectedDefNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5));
    assertEquals(expectedDefNode, analysis.getDef("a", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_AssignmentWithDependency() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.VAR, "a"));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5)));
    rootNode.addChild(new Node(Token.VAR, "b"));
    rootNode.addChild(new Node(Token.ASSIGN, "b", new Node(Token.NUM, 10)));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NAME, "b")));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    Node expectedDefNode = new Node(Token.ASSIGN, "b", new Node(Token.NUM, 10));
    assertEquals(expectedDefNode, analysis.getDef("a", useNode));
    assertTrue(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_MultipleAssignments() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.VAR, "a"));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5)));
    rootNode.addChild(new Node(Token.VAR, "b"));
    rootNode.addChild(new Node(Token.ASSIGN, "b", new Node(Token.NUM, 10)));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NAME, "b")));
    rootNode.addChild(new Node(Token.ASSIGN, "b", new Node(Token.NUM, 20)));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    Node expectedDefNode = new Node(Token.ASSIGN, "b", new Node(Token.NUM, 20));
    assertEquals(expectedDefNode, analysis.getDef("a", useNode));
    assertTrue(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_NestedBlocks() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.BLOCK, "", new Node(Token.VAR, "a")));
    rootNode.addChild(new Node(Token.BLOCK, "", new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5))));
    rootNode.addChild(new Node(Token.BLOCK, "", new Node(Token.ASSIGN, "a", new Node(Token.NUM, 10))));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1)));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    Node expectedDefNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 10));
    assertEquals(expectedDefNode, analysis.getDef("a", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_ConditionalBranch() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.VAR, "a"));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5)));
    rootNode.addChild(new Node(Token.IF, new Node(Token.NUM, 0), new Node(Token.BLOCK), new Node(Token.ASSIGN, "a", new Node(Token.NUM, 10))));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNodeIfTrue = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef resultIfTrue = analysis.computeNext(initialAnalysis, useNodeIfTrue);
    Node expectedDefNodeIfTrue = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 10));
    assertEquals(expectedDefNodeIfTrue, analysis.getDef("a", useNodeIfTrue));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNodeIfTrue));

    Node useNodeIfFalse = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 2));
    MustDef resultIfFalse = analysis.computeNext(initialAnalysis, useNodeIfFalse);
    Node expectedDefNodeIfFalse = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 5));
    assertEquals(expectedDefNodeIfFalse, analysis.getDef("a", useNodeIfFalse));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNodeIfFalse));
  }

  @Test
  public void testMustBeReachingVariableDef_Loop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.VAR, "a"));
    rootNode.addChild(new Node(Token.WHILE, new Node(Token.NUM, 0), new Node(Token.BLOCK), new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1))));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    Node expectedDefNode = new Node(Token.VAR, "a");
    assertEquals(expectedDefNode, analysis.getDef("a", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_FunctionCall() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.FUNCTION, "foo", new Node(Token.BLOCK), new Node(Token.RETURN, new Node(Token.NUM, 1))));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NAME, "foo")));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    // We don't have enough information to determine the reaching definition of
    // the variable "a" in this test case.
    assertNull(analysis.getDef("a", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_FunctionParameter() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.FUNCTION, "foo", new Node(Token.PARAM, "x"), new Node(Token.BLOCK), new Node(Token.RETURN, new Node(Token.NUM, 1))));
    rootNode.addChild(new Node(Token.ASSIGN, "x", new Node(Token.NUM, 1)));
    rootNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NAME, "foo")));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NUM, 1));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    // We don't have enough information to determine the reaching definition of
    // the variable "a" in this test case.
    assertNull(analysis.getDef("a", useNode));
    assertFalse(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testMustBeReachingVariableDef_EscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChild(new Node(Token.FUNCTION, "foo", new Node(Token.BLOCK), new Node(Token.RETURN, new Node(Token.NUM, 1))));
    rootNode.addChild(new Node(Token.VAR, "x"));
    rootNode.addChild(new Node(Token.ASSIGN, "x", new Node(Token.NAME, "foo")));
    rootNode.addChild(new Node(Token.RETURN, new Node(Token.NAME, "x")));

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initialAnalysis = analysis.computeNext(analysis.createInitialEstimateLattice(), rootNode);

    Node useNode = new Node(Token.ASSIGN, "a", new Node(Token.NAME, "x"));
    MustDef result = analysis.computeNext(initialAnalysis, useNode);

    // We don't have enough information to determine the reaching definition of
    // the variable "a" in this test case.
    assertNull(analysis.getDef("a", useNode));
    assertTrue(analysis.dependsOnOuterScopeVars("a", useNode));
  }
}