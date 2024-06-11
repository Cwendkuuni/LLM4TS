import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssignment() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = Node.newBlock("root");
    Node a = Node.newVar("a");
    Node b = Node.newVar("b");
    Node assign = Node.newAssign(a, b);

    rootNode.addChild(a);
    rootNode.addChild(b);
    rootNode.addChild(assign);

    Node useA = Node.newIdent("a");
    analysis.compute(rootNode, null, null);
    Node defA = analysis.getDef("a", useA);

    assertEquals(b, defA);
  }

  @Test
  public void testSimpleConditional() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = Node.newBlock("root");
    Node a = Node.newVar("a");
    Node b = Node.newVar("b");
    Node cond = Node.newCondition(Node.newBinop("==", Node.newIdent("x"), Node.newInt(1)),
        Node.newBlock("then"), Node.newBlock("else"));

    Node thenBlock = Node.newBlock("then");
    Node assign = Node.newAssign(a, b);
    thenBlock.addChild(assign);

    Node elseBlock = Node.newBlock("else");
    Node assignElse = Node.newAssign(a, Node.newInt(2));
    elseBlock.addChild(assignElse);

    rootNode.addChild(a);
    rootNode.addChild(b);
    rootNode.addChild(cond);
    rootNode.addChild(thenBlock);
    rootNode.addChild(elseBlock);
    rootNode.addChild(Node.newIdent("x"));

    Node useA = Node.newIdent("a");
    analysis.compute(rootNode, null, null);
    Node defA = analysis.getDef("a", useA);

    // In this case, there are two possible definitions for "a" depending on the
    // value of "x". Since the analysis is forward-only, it will only pick the
    // definition that's reachable from the entry node. In this case, since "x" is
    // not a constant, there are multiple paths to reach "a", so the result should
    // be "null".
    assertNull(defA);
  }

  @Test
  public void testSimpleAssignmentWithEscapedVariable() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = Node.newBlock("root");
    Node a = Node.newVar("a");
    Node b = Node.newVar("b");
    Node escVar = Node.newVar("escaped");
    Node assign = Node.newAssign(a, Node.newName("arguments[0]"));
    Node assignEsc = Node.newAssign(escVar, b);
    Node assignToA = Node.newAssign(a, escVar);

    rootNode.addChild(a);
    rootNode.addChild(b);
    rootNode.addChild(escVar);
    rootNode.addChild(assign);
    rootNode.addChild(assignEsc);
    rootNode.addChild(assignToA);
    rootNode.addChild(Node.newIdent("arguments[0]"));

    Node useA = Node.newIdent("a");
    analysis.compute(rootNode, null, null);
    Node defA = analysis.getDef("a", useA);

    assertNull(defA);
  }

  @Test(expected = RuntimeException.class)
  public void testNullArgument() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = Node.newBlock("root");
    Node a = Node.newVar("a");
    Node useA = Node.newIdent("a");

    analysis.compute(rootNode, null, null);
    analysis.getDef("nonExistentVar", useA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    Scope jsScope = null;
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = null;
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullControlFlowGraph() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = null;
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testSimpleFunction() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = Node.newFunction("testFunction", Node.newList(), Node.newBlock("root"));
    Node a = Node.newVar("a");
    Node b = Node.newVar("b");
    Node assign = Node.newAssign(a, b);

    Node useA = Node.newIdent("a");
    rootNode.addChild(a);
    rootNode.addChild(b);
    rootNode.addChild(assign);
    rootNode.addChild(useA);

    analysis.compute(rootNode, null, null);
    Node defA = analysis.getDef("a", useA);

    assertEquals(b, defA);
  }
}