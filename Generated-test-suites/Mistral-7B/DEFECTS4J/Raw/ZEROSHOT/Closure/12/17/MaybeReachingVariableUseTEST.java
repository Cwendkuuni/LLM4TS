import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleCase() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node1 = Node.newExpr("A = 1;");
    Node node2 = Node.newExpr("alert(A);");

    cfg.addEdge(new DiGraphEdge(node1, node2));

    ReachingUses result = analysis.computeResult(node1);

    Collection<Node> uses = analysis.getUses("A", node1);
    assertTrue(uses.contains(node2));
  }

  @Test
  public void testConditional() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeIf = Node.newExpr("if (x) { A = 1; } alert(A);");
    Node nodeElse = Node.newExpr("if (x) { } alert(A);");

    cfg.addEdge(new DiGraphEdge(NodeUtil.getConditionExpression(nodeIf), nodeIf));
    cfg.addEdge(new DiGraphEdge(NodeUtil.getConditionExpression(nodeElse), nodeElse));

    analysis.computeResult(NodeUtil.getConditionExpression(nodeIf));

    Collection<Node> usesIf = analysis.getUses("A", NodeUtil.getConditionExpression(nodeIf));
    Collection<Node> usesElse = analysis.getUses("A", NodeUtil.getConditionExpression(nodeElse));

    assertFalse(usesIf.containsAll(usesElse));
  }

  @Test
  public void testForLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeFor = Node.newExpr("for (x in y) { alert(x); }");

    analysis.computeResult(NodeUtil.getConditionExpression(nodeFor));

    Collection<Node> uses = analysis.getUsagesFor("x");

    assertTrue(uses.containsAll(NodeUtil.getForIteratorNext(nodeFor).getChildren()));
  }

  @Test
  public void testNestedConditional() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeIf1 = Node.newExpr("if (x) { if (y) { A = 1; } alert(A); }");

    analysis.computeResult(NodeUtil.getConditionExpression(nodeIf1));

    Collection<Node> usesIf1 = analysis.getUses("A", NodeUtil.getConditionExpression(nodeIf1));

    Node nodeIf2 = NodeUtil.getChild(NodeUtil.getChild(nodeIf1, 0), 0);
    Collection<Node> usesIf2 = analysis.getUses("A", nodeIf2);

    assertFalse(usesIf1.containsAll(usesIf2));
  }

  @Test
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeEscapedVariable = Node.newExpr("var x = 1; function f() { alert(x); } f();");

    analysis.computeResult(NodeUtil.getFunctionBody(nodeEscapedVariable));

    Collection<Node> uses = analysis.getUses("x", nodeEscapedVariable);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testVariableRedeclaration() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeRedeclaration = Node.newExpr("var A = 1; var A = 2; alert(A);");

    analysis.computeResult(nodeRedeclaration);

    Collection<Node> uses = analysis.getUses("A", nodeRedeclaration);
    assertTrue(uses.containsAll(Node.newExpr("alert(A)").getChildren()));
  }

  @Test
  public void testBlockScope() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeBlockScope = Node.newExpr("{\\n  var A = 1;\n  if (true) {\n    var A = 2;\n  }\n  alert(A);\\n}");

    analysis.computeResult(NodeUtil.getFunctionBody(Node.newExpr("function f() {" + nodeBlockScope.toSource() + "}")));

    Collection<Node> uses = analysis.getUses("A", nodeBlockScope);
    assertTrue(uses.containsAll(Node.newExpr("alert(A)").getChildren()));
  }

  @Test
  public void testFunctionScope() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeFunctionScope = Node.newExpr("function f() {\n  var A = 1;\n  alert(A);\n}");
    Node nodeCallFunction = Node.newExpr("f();");

    cfg.addEdge(new DiGraphEdge(NodeUtil.getFunctionBody(nodeFunctionScope), nodeCallFunction));

    analysis.computeResult(nodeCallFunction);

    Collection<Node> uses = analysis.getUses("A", nodeFunctionScope);
    assertTrue(uses.containsAll(Node.newExpr("alert(A)").getChildren()));
  }

  @Test
  public void testVariableDeclarationWithAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithAssignment = Node.newExpr("var A = 1; A += 1; alert(A);");

    analysis.computeResult(nodeVariableDeclarationWithAssignment);

    Collection<Node> uses = analysis.getUses("A", nodeVariableDeclarationWithAssignment);
    assertTrue(uses.containsAll(Node.newExpr("alert(A)").getChildren()));
  }

  @Test
  public void testVariableDeclarationWithoutAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithoutAssignment = Node.newExpr("var A; alert(A);");

    analysis.computeResult(nodeVariableDeclarationWithoutAssignment);

    Collection<Node> uses = analysis.getUses("A", nodeVariableDeclarationWithoutAssignment);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testVariableDeclarationWithoutAssignmentInLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithoutAssignmentInLoop = Node.newExpr("for (var A; ) { alert(A); }");

    analysis.computeResult(NodeUtil.getConditionExpression(NodeUtil.getForIteratorInit(nodeVariableDeclarationWithoutAssignmentInLoop)));

    Collection<Node> uses = analysis.getUsagesFor("A");
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testVariableDeclarationWithInitializerInLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithInitializerInLoop = Node.newExpr("for (var A = 1; ; ) { alert(A); }");

    analysis.computeResult(NodeUtil.getConditionExpression(NodeUtil.getForIteratorInit(nodeVariableDeclarationWithInitializerInLoop)));

    Collection<Node> uses = analysis.getUsagesFor("A");
    Collection<Node> expectedUses = Sets.newHashSet(Node.newExpr("alert(A)").getChildren());
    assertEquals(expectedUses, uses);
  }

  @Test
  public void testVariableDeclarationWithInitializerInConditional() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithInitializerInConditional = Node.newExpr("if (x) { var A = 1; } alert(A);");

    analysis.computeResult(NodeUtil.getConditionExpression(nodeVariableDeclarationWithInitializerInConditional));

    Collection<Node> uses = analysis.getUses("A", nodeVariableDeclarationWithInitializerInConditional);
    assertTrue(uses.containsAll(Node.newExpr("alert(A)").getChildren()));
  }

  @Test
  public void testVariableDeclarationWithInitializerInFunction() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node nodeVariableDeclarationWithInitializerInFunction = Node.newExpr("function f() { var A = 1; return A; } alert(f());");

    analysis.computeResult(NodeUtil.getFunctionBody(Node.newExpr("function f() {" + nodeVariableDeclarationWithInitializerInFunction.toSource() + "}")));

    Collection<Node> uses = analysis.getUses("A", nodeVariableDeclarationWithInitializerInFunction);
    assertTrue(uses.containsAll(Node.newExpr("alert(f())").getChildren()));
  }
}