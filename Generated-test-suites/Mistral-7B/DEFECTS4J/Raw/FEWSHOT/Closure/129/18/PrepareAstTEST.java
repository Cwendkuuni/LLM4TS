package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = new Node(Token.BLOCK);
    Node call = new Node(Token.CALL, null, null);
    Node objlit = new Node(Token.OBJECT_LITERAL, null, null);
    Node ifStmt = new Node(Token.IF, null, null);
    Node block1 = new Node(Token.BLOCK, null, null);
    Node block2 = new Node(Token.BLOCK, null, null);

    call.addChild(new Node(Token.NAME, "eval", null));
    objlit.addChild(new Node(Token.NAME, "a", null));
    objlit.addChild(new Node(Token.NAME, "b", null));
    ifStmt.addChild(new Node(Token.BLOCK, null, null));
    ifStmt.addChild(new Node(Token.IF, null, null));
    ifStmt.addChild(new Node(Token.NAME, "condition", null));
    ifStmt.addChild(block1);
    root.addChild(ifStmt);
    ifStmt.getLastChild().addChild(block2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check if the root is still a block node after processing.
    assertTrue(root.getType() == Token.BLOCK);

    // Check if the call node has the FREE_CALL property set.
    assertTrue(call.getBooleanProp(Node.FREE_CALL));

    // Check if the direct eval node has the DIRECT_EVAL property set.
    assertTrue(call.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));

    // Check if the if statement has blocks added.
    assertTrue(block1.getType() == Token.BLOCK);
    assertTrue(block2.getType() == Token.BLOCK);

    // Check if the object literal nodes have no blocks.
    assertFalse(objlit.getFirstChild().getType() == Token.BLOCK);
    assertFalse(objlit.getLastChild().getType() == Token.BLOCK);
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = new Node(Token.BLOCK);
    Node objlit = new Node(Token.OBJECT_LITERAL, null, null);
    Node prop = new Node(Token.NAME, "a", null);
    Node func = new Node(Token.FUNCTION, null, null);

    // Set JSDoc info on the object literal node.
    objlit.putJSDocInfo(new JSDocInfo("JavaInterface"));

    // Add the property with a JSDoc comment.
    objlit.addChild(prop);
    prop.putJSDocInfo(new JSDocInfo("JavaProperty", "desc", "value"));

    // Add the function with a JSDoc comment.
    objlit.addChild(func);
    func.putJSDocInfo(new JSDocInfo("JavaMethod", "desc", "value"));

    root.addChild(objlit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check if the JSDoc info on the object literal node is propagated to the property.
    assertEquals(objlit.getJSDocInfo(), prop.getJSDocInfo());

    // Check if the JSDoc info on the property is propagated to the function.
    assertEquals(prop.getJSDocInfo(), func.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatch() {
    Node root = new Node(Token.BLOCK);
    Node obj = new Node(Token.NAME, "MyClass", null);
    Node func = new Node(Token.FUNCTION, null, null);

    Node objlit = new Node(Token.OBJECT_LITERAL, null, null);
    Node prop = new Node(Token.NAME, "foo", null);
    objlit.addChild(prop);
    prop.putJSDocInfo(new JSDocInfo("JavaProperty", "desc", "value"));

    obj.addChild(objlit);
    func.putJSDocInfo(new JSDocInfo("JavaMethod", "desc", "value"));
    func.addChild(new Node(Token.THIS, null, obj));
    func.addChild(new Node(Token.NAME, "dispatch", null));
    func.addChild(new Node(Token.NAME, "this", null));
    func.addChild(new Node(Token.CALL, null, func));

    root.addChild(func);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check if the function has the DISPATCHER property set.
    assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatchAndEmptyFunction() {
    Node root = new Node(Token.BLOCK);
    Node obj = new Node(Token.NAME, "MyClass", null);
    Node func = new Node(Token.FUNCTION, null, null);

    Node objlit = new Node(Token.OBJECT_LITERAL, null, null);
    Node prop = new Node(Token.NAME, "foo", null);
    objlit.addChild(prop);
    prop.putJSDocInfo(new JSDocInfo("JavaProperty", "desc", "value"));

    obj.addChild(objlit);
    func.addChild(new Node(Token.THIS, null, obj));
    func.addChild(new Node(Token.NAME, "dispatch", null));
    func.addChild(new Node(Token.NAME, "this", null));

    // Empty function body.
    func.addChild(new Node(Token.BLOCK, null, null));

    root.addChild(func);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check if the function has the DISPATCHER property set.
    assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatchAndEmptyObjectLiteral() {
    Node root = new Node(Token.BLOCK);
    Node obj = new Node(Token.NAME, "MyClass", null);
    Node func = new Node(Token.FUNCTION, null, null);

    Node objlit = new Node(Token.OBJECT_LITERAL, null, null);
    func.addChild(new Node(Token.THIS, null, obj));
    func.addChild(new Node(Token.NAME, "dispatch", null));
    func.addChild(new Node(Token.NAME, "this", null));

    // Empty object literal.
    objlit.addChildrenToFront(func);

    root.addChild(objlit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check if the function has the DISPATCHER property set.
    assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
  }
}