import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeType;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
  }

  @Test
  public void testVisit_VoidNode() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.VOID);
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_ParentIsCallNodeWithEval() {
    Node root = new Node(Token.BLOCK);
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "eval"));
    Node node = new Node(Token.VOID);
    callNode.addChildToBack(node);
    root.addChildToBack(callNode);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_ParentIsCallNodeWithNoArg() {
    Node root = new Node(Token.BLOCK);
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "foo"));
    Node node = new Node(Token.VOID);
    callNode.addChildToBack(node);
    root.addChildToBack(callNode);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_ParentIsForStatement() {
    Node root = new Node(Token.BLOCK);
    Node forNode = new Node(Token.FOR, 0, null, null, null, null, null, null);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    forNode.setFirstChild(node);
    root.addChildToBack(forNode);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_ParentIsExprResult() {
    Node root = new Node(Token.BLOCK);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.VOID);
    exprResult.addChildToBack(node);
    root.addChildToBack(exprResult);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_ParentIsBlock() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);

    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    assertEquals(1, problemNodes.size());
    assertEquals(node, problemNodes.get(0));
  }

  @Test
  public void testVisit_SimpleOp() {
    Node root = new Node(Token.BLOCK);
    Node plusNode = new Node(Token.BINARY_PLUS, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), plusNode);
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    // No assertion needed, as no warning should be reported.
  }

  @Test
  public void testVisit_MayHaveSideEffects() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);

    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    assertEquals(1, problemNodes.size());
    assertEquals(node, problemNodes.get(0));
  }

  @Test(expected = AssertionError.class)
  public void testVisit_String() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.STRING, "this string is continued on the next line but you forgot the +");
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);
    fail("Expected warning: Is there a missing '+' on the previous line?");
  }

  @Test
  public void testVisit_ProtectSideEffects() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    root.addChildToBack(node);
    checkSideEffects.visit(NodeTraversal.create(compiler, root), root, null);

    checkSideEffects.protectSideEffects();

    Node extern = checkSideEffects.getCompiler().getSynthesizedExternsInput().getAstRoot(checkSideEffects.getCompiler());
    Node protectorFn = extern.getFirstChild();
    Node parameterNode = protectorFn.getLastChild();
    Node nodeToProtect = extern.getLastChild();

    assertEquals(2, extern.getChildCount());
    assertEquals(protectorFn.getString(), "JSCOMPILER_PRESERVE");
    assertEquals(1, parameterNode.getChildCount());
    assertEquals(nodeToProtect.getString(), root.toString());
  }

  @Test
  public void testProcess() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    root.addChildToBack(node);

    checkSideEffects.process(new CompilerInput(null, null, null), root);

    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    assertEquals(1, problemNodes.size());
    assertEquals(node, problemNodes.get(0));
  }

  @Test
  public void testHotSwapScript() {
    Node root = new Node(Token.BLOCK);
    Node node = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    root.addChildToBack(node);

    checkSideEffects.hotSwapScript(null, root);

    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    assertEquals(1, problemNodes.size());
    assertEquals(node, problemNodes.get(0));
  }

  @Test
  public void testProtectSideEffects_EmptyProblemNodesList() {
    Node root = new Node(Token.BLOCK);
    checkSideEffects.process(new CompilerInput(null, null, null), root);

    checkSideEffects.protectSideEffects();

    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    assertEquals(0, problemNodes.size());
  }

  @Test
  public void testStripProtection() {
    Node root = new Node(Token.BLOCK);
    Node protectorFnNode = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), null);
    Node nodeToProtect = new Node(Token.ASSIGN, Token.OP_ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
    protectorFnNode.addChildToBack(nodeToProtect);
    root.addChildToBack(protectorFnNode);

    StripProtection stripProtection = new StripProtection(checkSideEffects.getCompiler());
    stripProtection.process(null, root);

    assertEquals(1, root.getChildCount());
    assertEquals(nodeToProtect, root.getFirstChild());
  }
}