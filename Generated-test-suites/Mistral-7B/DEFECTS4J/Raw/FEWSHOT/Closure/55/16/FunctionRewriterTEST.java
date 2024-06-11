package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

public class FunctionRewriterTest {

 private static class FunctionRewriterTestCallback implements Callback {
   FunctionRewriter functionRewriter;

   FunctionRewriterTestCallback(FunctionRewriter functionRewriter) {
      this.functionRewriter = functionRewriter;
   }

   @Override
   public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      if (node.getType() == Token.FUNCTION && isReduceableFunctionExpression(node)) {
         Node newNode = functionRewriter.reduce(node);
         if (newNode != node) {
            traversal.setCurrentNode(newNode);
            return false;
         }
      }
      return true;
   }

   @Override
   public void visit(NodeTraversal traversal, Node node, Node parent) {
   }

   private static boolean isReduceableFunctionExpression(Node node) {
      return NodeUtil.isFunctionExpression(node);
   }
 }

 @Test
 public void testFunctionRewriter() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node root = Node.newBlock("testFunctionRewriter", 1, 1);
    root.addChildToBack(Node.newFunction("test", 0, 0, Node.newBlock(1, 1), null, null));

    NodeTraversal traverser = new NodeTraversal(root, new FunctionRewriterTestCallback(functionRewriter));
    traverser.traverse();

    Node testFunction = root.getFirstChild();
    assertEquals(1, testFunction.getNumberOfChildren());

    Node testFunctionBody = testFunction.getLastChild();
    assertEquals(Token.BLOCK, testFunctionBody.getType());

    Node helperCode = functionRewriter.parseHelperCode(new IdentityReducer());
    assertNull(helperCode);
 }

 @Test
 public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node root = Node.newBlock("testIdentityReducer", 1, 1);
    root.addChildToBack(Node.newFunction("test", 0, 0, Node.newBlock(1, 1), null, null));

    Node functionNode = root.getLastChild().getLastChild();
    Node expected = Node.newCall(Node.newString("getter"), Node.newString("identity"), null);
    Node actual = functionRewriter.reduce(functionNode);

    assertEquals(expected, actual);
 }

 @Test
 public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node root = Node.newBlock("testEmptyFunctionReducer", 1, 1);
    root.addChildToBack(Node.newFunction("test", 0, 0, Node.newBlock(1, 1), null, null));

    Node functionNode = root.getLastChild().getLastChild();
    Node expected = Node.newCall(Node.newString("emptyFn"), null, null);
    Node actual = functionRewriter.reduce(functionNode);

    assertEquals(expected, actual);
 }

 @Test
 public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node root = Node.newBlock("testReturnConstantReducer", 1, 1);
    root.addChildToBack(Node.newFunction("test", 0, 0, Node.newBlock(1, 1), null, null));

    Node functionNode = root.getLastChild().getLastChild();
    Node expectedConstant = Node.newNumber(10, null, null);
    Node expected = Node.newCall(Node.newString("returnconst"), expectedConstant, null);
    Node actual = functionRewriter.reduce(functionNode);

    assertEquals(expected, actual);
 }
}