package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class PrepareAstTest {
  @Test
  public void testNormalizeNodeTypes() {
    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.CALL, null);
    Node n2 = new Node(Token.FUNCTION, null);
    Node n3 = new Node(Token.IDENT, "func");
    Node n4 = new Node(Token.IDENT, "n");
    Node n5 = new Node(Token.NUMBER, 1);
    Node n6 = new Node(Token.STRING, "str");

    n1.addChildrenToFront(n3, n4, n5);
    n2.addChildrenToFront(n3, n4);
    root.addChildrenToFront(n1, n2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertFalse(root.isFreeCall());
    assertFalse(n1.isFreeCall());
    assertTrue(n2.isFreeCall());
    assertNull(n1.directEval);
    assertNull(n2.directEval);

    // Test with checkOnly = true
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
    Preconditions.checkState(false, "normalizeNodeType constraints violated");
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = new Node(Token.BLOCK, null);
    Node ifNode = new Node(Token.IF, null);
    Node expr = new Node(Token.COND, null);
    Node thenBlock = new Node(Token.BLOCK, null);
    Node elseBlock = new Node(Token.BLOCK, null);

    expr.addChildrenToFront(new Node(Token.NUMBER, 1));
    thenBlock.addChildrenToFront(new Node(Token.RETURN, null, new Node(Token.NUMBER, 2)));
    elseBlock.addChildrenToFront(new Node(Token.RETURN, null, new Node(Token.NUMBER, 3)));
    ifNode.addChildrenToFront(expr, thenBlock, elseBlock);
    root.addChildrenToFront(ifNode);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertTrue(ifNode.isBlock());
    assertTrue(thenBlock.isBlock());
    assertTrue(elseBlock.isBlock());

    NodeTraversal.traverse(
        null, root, new NodeTraversal.Callback() {
          @Override
          public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
            if (n.isBlock()) {
              System.out.println("Found a block: " + n);
            }
            return true;
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
          }
        });
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = new Node(Token.BLOCK, null);
    Node objLit = new Node(Token.OBJECT_LITERAL, null);
    Node propName1 = new Node(Token.IDENT, "prop1");
    Node propName2 = new Node(Token.IDENT, "prop2");
    Node propVal1 = new Node(Token.FUNCTION, null);
    Node propVal2 = new Node(Token.FUNCTION, null);

    propVal1.addChildrenToFront(new Node(Token.FUNCTION, null, new Node(Token.IDENT, "func1")));
    propVal2.addChildrenToFront(new Node(Token.FUNCTION, null, new Node(Token.IDENT, "func2")));
    objLit.addChildrenToFront(propName1, new Node(Token.COLON, null), propVal1);
    objLit.addChildrenToFront(propName2, new Node(Token.COLON, null), propVal2);
    root.addChildrenToFront(objLit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertEquals("func1", objLit.getFirstChild().getFirstChild().getString());
    assertEquals("func2", objLit.getFirstChild(1).getFirstChild().getString());
  }

  @Test
  public void testAnnotateCalls() {
    Node root = new Node(Token.BLOCK, null);
    Node callNode1 = new Node(Token.CALL, null);
    Node funcNode1 = new Node(Token.FUNCTION, null);
    Node funcNameNode1 = new Node(Token.IDENT, "func1");
    Node callNode2 = new Node(Token.CALL, null);
    Node funcNode2 = new Node(Token.FUNCTION, null);
    Node funcNameNode2 = new Node(Token.IDENT, "func2");
    Node exprNode = new Node(Token.IDENT, "expr");

    funcNode1.addChildrenToFront(funcNameNode1);
    callNode1.addChildrenToFront(funcNode1, exprNode);
    funcNode2.addChildrenToFront(funcNameNode2);
    callNode2.addChildrenToFront(funcNode2, exprNode);
    root.addChildrenToFront(callNode1, callNode2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertTrue(callNode1.isFreeCall());
    assertFalse(callNode2.isFreeCall());
  }

  @Test
  public void testAnnotateDispatchers() {
    Node root = new Node(Token.BLOCK, null);
    Node assignNode = new Node(Token.ASSIGN, null);
    Node funcNode = new Node(Token.FUNCTION, null);
    Node funcNameNode = new Node(Token.IDENT, "func");

    funcNode.addChildrenToFront(funcNameNode);
    assignNode.addChildrenToFront(funcNode);
    root.addChildrenToFront(new Node(Token.VAR, null, new Node(Token.IDENT, "x")), assignNode);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertTrue(assignNode.isDispatcher());
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node root = new Node(Token.BLOCK, null);
    Node objLit = new Node(Token.OBJECT_LITERAL, null);
    Node keyNode1 = new Node(Token.IDENT, "key1");
    Node valueNode1 = new Node(Token.FUNCTION, null);
    Node keyNode2 = new Node(Token.IDENT, "key2");
    Node valueNode2 = new Node(Token.FUNCTION, null);
    Node jsDocInfo1 = new Node(Token.JSDOCINFO, null);
    Node jsDocInfo2 = new Node(Token.JSDOCINFO, null);

    jsDocInfo1.putProperty("name", "JSDoc1");
    jsDocInfo2.putProperty("name", "JSDoc2");

    valueNode1.addChildrenToFront(new Node(Token.FUNCTION, null, new Node(Token.IDENT, "func1")));
    valueNode2.addChildrenToFront(new Node(Token.FUNCTION, null, new Node(Token.IDENT, "func2")));
    objLit.addChildrenToFront(
        keyNode1, new Node(Token.COLON, null), valueNode1,
        keyNode2, new Node(Token.COLON, null), valueNode2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertEquals("func1", valueNode1.getFirstChild().getString());
    assertEquals("func2", valueNode2.getFirstChild().getString());
    assertEquals("JSDoc1", objLit.getFirstChild(0).getJSDocInfo().getName());
    assertEquals("JSDoc2", objLit.getFirstChild(1).getJSDocInfo().getName());
  }
}