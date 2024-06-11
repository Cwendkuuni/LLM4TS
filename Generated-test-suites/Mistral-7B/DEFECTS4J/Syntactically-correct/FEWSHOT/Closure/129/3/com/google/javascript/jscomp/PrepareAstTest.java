package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);

    Node callNode = new Node(Token.CALL, null, null);
    callNode.addChildToEnd(new Node(Token.NAME, "print", null));
    callNode.addChildToEnd(new Node(Token.STRING, "world", null));
    root.addChildToEnd(callNode);

    prepareAst.process(null, root);

    assertTrue(root.getFirstChild().isBlock());
    assertTrue(callNode.isCall());
    assertTrue(callNode.getFirstChild().isName());
    assertTrue(callNode.getLastChild().isString());
    assertFalse(callNode.getFirstChild().getFirstChild().isBlock());
  }

  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);

    Node ifNode = new Node(Token.IF, null, null);
    Node condition = new Node(Token.NAME, "x", null);
    condition.putBooleanProp(Node.IS_CONST, true);
    condition.putIntProp("value", 5);
    ifNode.addChildToEnd(condition);
    Node statement = new Node(Token.PRINT, null, null);
    statement.addChildToEnd(new Node(Token.STRING, "world", null));
    ifNode.addChildToEnd(statement);
    root.addChildToEnd(ifNode);

    prepareAst.process(null, root);

    Node ifNodeBlock = ifNode.getFirstChild();
    assertTrue(ifNodeBlock.isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);

    Node objLiteral = new Node(Token.OBJECT_LITERAL, null, null);
    root.addChildToEnd(objLiteral);

    Node key1 = new Node(Token.NAME, "a", null);
    Node value1 = new Node(Token.NUMBER, 1, null);
    value1.setJSDocInfo(new JSDocInfo("@type", "string"));
    objLiteral.addChildToEnd(key1);
    objLiteral.addChildToEnd(value1);

    Node key2 = new Node(Token.NAME, "b", null);
    Node value2 = new Node(Token.FUNCTION, null, null);
    value2.setJSDocInfo(new JSDocInfo("@type", "Function"));
    objLiteral.addChildToEnd(key2);
    objLiteral.addChildToEnd(value2);

    prepareAst.process(null, root);

    Node value1Annotated = value1.getFirstChild();
    assertTrue(value1Annotated.isJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsOnFunction() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);

    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node nameNode = new Node(Token.NAME, "myFunction", null);
    nameNode.setJSDocInfo(new JSDocInfo("@this { foo: 'bar' }"));
    functionNode.addChildToEnd(nameNode);
    functionNode.addChildToEnd(new Node(Token.LEFT_BRACE, null, null));
    functionNode.addChildToEnd(new Node(Token.RETURN, null, null));
    functionNode.addChildToEnd(new Node(Token.RIGHT_BRACE, null, null));
    root.addChildToEnd(functionNode);

    prepareAst.process(null, root);

    Node functionNodeAnnotated = functionNode.getFirstChild();
    Node nameNodeAnnotated = functionNodeAnnotated.getFirstChild();
    assertTrue(nameNodeAnnotated.getJSDocInfo().getThis().equals("{ foo: 'bar' }"));
  }
}