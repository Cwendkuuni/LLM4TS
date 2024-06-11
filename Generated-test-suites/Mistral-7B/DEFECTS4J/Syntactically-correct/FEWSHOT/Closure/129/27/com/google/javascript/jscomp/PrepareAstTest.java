package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  private static Node createNode(int type, int line, int lineOffset) {
    return NodeFactory.createNodeFromToken(type, line, lineOffset);
  }

  @Test
  public void testPrepareAstWithNullCompiler() {
    assertNull(new PrepareAst(null));
  }

  @Test
  public void testPrepareAst() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode(Token.BLOCK, 1, 0);
    prepareAst.process(null, root);
  }

  @Test
  public void testNormalizeBlocks() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node ifNode = createNode(Token.IF, 1, 0);
    Node codeBlock = createNode(Token.BLOCK, 1, 0);

    ifNode.addChild(codeBlock);
    Node condition = createNode(Token.IDENT, 1, 1);

    prepareAst.process(null, ifNode);

    assertTrue(codeBlock.isBlock());
  }

  @Test
  public void testCheckOnlyMode() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);

    Node root = createNode(Token.BLOCK, 1, 0);
    prepareAst.process(null, root);

    checkState(false, "normalizeNodeType constraints violated");
  }

  @Test
  public void testNormalizeNodeTypes() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node voidNode = createNode(Token.EXPR_VOID, 1, 0);

    Node resultNode = createNode(Token.EXPR_RESULT, 1, 0);

    resultNode.addChild(voidNode);

    Node root = createNode(Token.BLOCK, 1, 0);
    root.addChild(resultNode);

    prepareAst.process(null, root);

    assertTrue(resultNode.getType() == Token.EXPR_RESULT);
  }

  @Test
  public void testAnnotateCalls() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node callNode = createNode(Token.CALL, 1, 0);
    Node thisNode = createNode(Token.IDENT, 1, 1);
    Node functionNode = createNode(Token.FUNCTION, 1, 2);

    callNode.addChild(thisNode);
    callNode.addChild(functionNode);

    Node root = createNode(Token.BLOCK, 1, 0);
    root.addChild(callNode);

    prepareAst.process(null, root);

    assertFalse(callNode.getBooleanProp(Node.FREE_CALL));

    callNode.setFirstChild(createNode(Token.VOID, 1, 0));

    prepareAst.process(null, root);

    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testAnnotateDispatchers() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node assignNode = createNode(Token.ASSIGN, 1, 0);
    Node functionNode = createNode(Token.FUNCTION, 1, 1);
    Node callNode = createNode(Token.CALL, 1, 2);

    callNode.addChild(createNode(Token.IDENT, 1, 3));
    callNode.addChild(functionNode);

    assignNode.addChild(createNode(Token.IDENT, 1, 4));
    assignNode.addChild(callNode);

    Node root = createNode(Token.BLOCK, 1, 0);
    root.addChild(assignNode);

    prepareAst.process(null, root);

    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    TestCompiler compiler = new TestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node objectLiteralNode = createNode(Token.BLOCK, 1, 0);

    Node key1Node = createNode(Token.IDENT, 1, 1);
    Node key1ValueNode = createNode(Token.STRING, 1, 2);

    Node key2Node = createNode(Token.IDENT, 1, 3);
    Node key2ValueNode = createNode(Token.FUNCTION, 1, 4);

    key1Node.addChild(key1ValueNode);
    objectLiteralNode.addChild(key1Node);

    Node jsdocInfo = createNode(Token.JSDOC_INFO, 1, 5);
    jsdocInfo.putProperty("type", "property");
    jsdocInfo.putProperty("name", "a");

    key2Node.addChild(jsdocInfo);

    prepareAst.process(null, objectLiteralNode);

    assertTrue(key1ValueNode.getJSDocInfo() == null);
    assertTrue(key2ValueNode.getJSDocInfo() != null);
  }
}