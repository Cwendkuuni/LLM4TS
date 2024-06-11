import static org.junit.Assert.*;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testBasic() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = createNode("useA");
    Node defNodeA = createNode("defA");
    Node defNodeB = createNode("defB");
    Node condition = createNode("condition");

    cfg.addEdge(createNode("entry"), defNodeA, Branch.CONTROL);
    cfg.addEdge(defNodeA, useNode, Branch.CONTROL);
    cfg.addEdge(defNodeA, defNodeB, Branch.CONTROL);
    cfg.addEdge(condition, useNode, Branch.IF_TRUE);
    cfg.addEdge(cfg.getNode(0), condition, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = defNodeA;
    Node actualDefA = analysis.getDef("A", useNode);
    assertEquals(expectedDefA, actualDefA);

    Node expectedDefB = null; // B should not have a definition in this test case
    Node actualDefB = analysis.getDef("B", useNode);
    assertNull(actualDefB);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNode);
    assertFalse(actualDependsOnOuterScope);
  }

  @org.junit.Test
  public void testConditional() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNodeA = createNode("useA");
    Node defNodeA = createNode("defA");
    Node condition = createNode("condition");

    cfg.addEdge(createNode("entry"), defNodeA, Branch.CONTROL);
    cfg.addEdge(defNodeA, useNodeA, Branch.CONTROL);
    cfg.addEdge(createNode("else"), useNodeA, Branch.IF_FALSE);
    cfg.addEdge(cfg.getNode(0), condition, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = defNodeA;
    Node actualDefA = analysis.getDef("A", useNodeA);
    assertEquals(expectedDefA, actualDefA);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNodeA);
    assertFalse(actualDependsOnOuterScope);
  }

  @org.junit.Test
  public void testMultipleDefinitions() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = createNode("useA");
    Node defNode1A = createNode("defA1");
    Node defNode2A = createNode("defA2");

    cfg.addEdge(createNode("entry"), defNode1A, Branch.CONTROL);
    cfg.addEdge(defNode1A, useNode, Branch.CONTROL);
    cfg.addEdge(defNode1A, defNode2A, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = null; // A should not have a unique definition in this test case
    Node actualDefA = analysis.getDef("A", useNode);
    assertNull(actualDefA);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNode);
    assertFalse(actualDependsOnOuterScope);
  }

  @org.junit.Test
  public void testEscapedVariable() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = createNode("useA");
    Node defNodeA = createNode("defA");
    Node escapeNode = createNode("escapeA");

    cfg.addEdge(createNode("entry"), defNodeA, Branch.CONTROL);
    cfg.addEdge(cfg.getNode(0), escapeNode, Branch.CONTROL);
    cfg.addEdge(escapeNode, useNode, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = null; // A should not be defined in this test case
    Node actualDefA = analysis.getDef("A", useNode);
    assertNull(actualDefA);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNode);
    assertFalse(actualDependsOnOuterScope);
  }

  @org.junit.Test
  public void testFunctionCall() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = createNode("useA");
    Node defNodeA = createNode("defA");
    Node functionNode = createNode("function");
    Node callNode = createNode("callFunction");

    cfg.addEdge(createNode("entry"), defNodeA, Branch.CONTROL);
    cfg.addEdge(defNodeA, functionNode, Branch.CONTROL);
    cfg.addEdge(functionNode, callNode, Branch.CONTROL);
    cfg.addEdge(callNode, useNode, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = defNodeA;
    Node actualDefA = analysis.getDef("A", useNode);
    assertEquals(expectedDefA, actualDefA);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNode);
    assertFalse(actualDependsOnOuterScope);
  }

  @org.junit.Test
  public void testMultipleFunctions() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(new SourceFile("Test.js"), jsScope);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope.getRootNode(), compiler);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = createNode("useA");
    Node defNodeA1 = createNode("defA1");
    Node defNodeA2 = createNode("defA2");
    Node functionNode1 = createNode("function1");
    Node functionNode2 = createNode("function2");

    cfg.addEdge(createNode("entry"), defNodeA1, Branch.CONTROL);
    cfg.addEdge(defNodeA1, functionNode1, Branch.CONTROL);
    cfg.addEdge(functionNode1, useNode, Branch.CONTROL);

    cfg.addEdge(createNode("entry"), defNodeA2, Branch.CONTROL);
    cfg.addEdge(defNodeA2, functionNode2, Branch.CONTROL);
    cfg.addEdge(functionNode2, useNode, Branch.CONTROL);

    analysis.compute();

    Node expectedDefA = null; // A should not have a unique definition in this test case
    Node actualDefA = analysis.getDef("A", useNode);
    assertNull(actualDefA);

    boolean expectedDependsOnOuterScope = false;
    boolean actualDependsOnOuterScope = analysis.dependsOnOuterScopeVars("C", useNode);
    assertFalse(actualDependsOnOuterScope);
  }

  private Node createNode(String name) {
    return new NodeToken(Token.NAME, name, null);
  }
}