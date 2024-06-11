package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = new Node(Token.BLOCK);
    Node function = new Node(Token.FUNCTION, null, null, null);
    Node call = new Node(Token.CALL, null, function);
    root.setFirstChild(call);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check that a function call is marked as a free call without an explicit 'this' value
    assertTrue(call.getBooleanProp(Node.FREE_CALL));
    assertFalse(call.getBooleanProp(Node.DIRECT_EVAL));

    // Check that a direct eval call is marked as a direct eval
    Node evalCall = new Node(Token.CALL, null, new Node(Token.NAME, "eval"));
    root.setFirstChild(evalCall);
    prepareAst.process(null, root);
    assertTrue(evalCall.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations() {
    Node objectLit = new Node(Token.OBJECT_LITERAL, null, null);

    Node key1 = new Node(Token.NAME, "a");
    Node value1 = new Node(Token.STRING_LITERAL, "b");
    Node key2 = new Node(Token.NAME, "c");
    Node value2 = new Node(Token.FUNCTION, null, null, null);

    value2.setFirstChild(new Node(Token.FUNCTION, null, null, null));
    value2.setLastChild(new Node(Token.RETURN, null, null, null));

    Node key3 = new Node(Token.NAME, "d");

    objectLit.setFirstChild(key1);
    objectLit.setSecondChild(value1);
    objectLit.setLastChild(key3);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, null);
    prepareAst.process(null, objectLit);

    // Check that JSDoc annotations are moved to the value nodes
    assertNull(key1.getJSDocInfo());
    assertNull(key2.getJSDocInfo());
    assertNull(key3.getJSDocInfo());
    assertEquals("a JSDoc comment", value1.getJSDocInfo().getDescription());
    assertEquals("c JSDoc comment", value2.getJSDocInfo().getDescription());
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatch() {
    Node obj = new Node(Token.VAR, "obj", null, null);
    Node call = new Node(Token.CALL, null, new Node(Token.NAME, "dispatch"));
    Node func = new Node(Token.FUNCTION, null, null, null);
    func.setFirstChild(new Node(Token.VAR, "thisArg", null, null));
    func.setLastChild(new Node(Token.RETURN, null, null, null));
    call.setLastChild(func);
    obj.setFirstChild(call);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, null);
    prepareAst.process(null, obj);

    assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeBlocks() {
    Node ifStmt = new Node(Token.IF, null, null);
    Node cond = new Node(Token.CALL, null, new Node(Token.NAME, "get condition"));
    Node block = new Node(Token.BLOCK);
    ifStmt.setFirstChild(cond);
    ifStmt.setLastChild(block);

    Node whileStmt = new Node(Token.WHILE, null, null);
    Node condition = new Node(Token.NAME, "condition");
    Node loopBlock = new Node(Token.BLOCK);
    whileStmt.setFirstChild(condition);
    whileStmt.setLastChild(loopBlock);

    Node forStmt = new Node(Token.FOR, null, null);
    Node init = new Node(Token.BLOCK);
    Node condition2 = new Node(Token.NAME, "condition");
    Node loop2 = new Node(Token.BLOCK);
    Node update = new Node(Token.BLOCK);
    forStmt.setFirstChild(init);
    forStmt.setSecondChild(condition2);
    forStmt.setThirdChild(loop2);
    forStmt.setFourthChild(update);

    Node root = new Node(Token.BLOCK);
    root.setFirstChild(ifStmt);
    root.setSecondChild(whileStmt);
    root.setThirdChild(forStmt);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check that blocks are added to control structures
    assertTrue(ifStmt.getFirstChild().isBlock());
    assertTrue(whileStmt.getLastChild().isBlock());
    assertTrue(forStmt.getFirstChild().isBlock());
    assertTrue(forStmt.getSecondChild().isBlock());
    assertTrue(forStmt.getThirdChild().isBlock());
  }
}