package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Result;
import com.google.javascript.junit.CompilerTestCase;
import com.google.javascript.rhino.NodeTraversal;

public class RemoveConstantExpressionsTest extends CompilerTestCase {

 private AbstractCompiler compiler;
 private CompilerPass pass;

 @Override
 public void setUp() throws Exception {
 super.setUp();
 compiler = newTestingAbstractCompiler();
 pass = new RemoveConstantExpressions(compiler);
 }

 @org.junit.Test
 public void testSimpleExpressionWithNoSideEffects() {
 Node source = newNode("1 + 2 + 3");
 Result result = new Result();
 Node root = compileSingleFileToNode(source, null, result);
 Node removedNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.EXPR_RESULT));
 assertEquals(3, removedNode.getChildCount());
 Node firstChild = removedNode.getFirstChild();
 Node secondChild = removedNode.getChild(1);
 Node thirdChild = removedNode.getChild(2);
 assertEquals(Token.NUMBER, firstChild.getType());
 assertEquals(Token.PLUS, firstChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, firstChild.getFirstChild().getFirstChild().getType());
 assertEquals(Token.PLUS, firstChild.getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, secondChild.getType());
 assertEquals(Token.PLUS, secondChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, secondChild.getFirstChild().getFirstChild().getType());
 assertEquals(Token.PLUS, secondChild.getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, thirdChild.getType());

 Node simplifiedNode = traverse(null, root, new NodeTraversal.Callback() {
 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (node.getType() == Token.EXPR_RESULT && node.getFirstChild().getType() == Token.EXPR_LIST) {
 removeNode(parent, node);
 return;
 }
 }
 });

 assertEquals(Token.EXPR_LIST, simplifiedNode.getType());
 NodeList nodeList = simplifiedNode.asList();
 assertEquals(3, nodeList.size());
 Node simplifiedFirstChild = nodeList.get(0);
 Node simplifiedSecondChild = nodeList.get(1);
 Node simplifiedThirdChild = nodeList.get(2);
 assertEquals(Token.EXPR_RESULT, simplifiedFirstChild.getType());
 assertEquals(Token.NUMBER, simplifiedFirstChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedFirstChild.getFirstChild().getValue(), 1.0);
 assertEquals(Token.EXPR_RESULT, simplifiedSecondChild.getType());
 assertEquals(Token.NUMBER, simplifiedSecondChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedSecondChild.getFirstChild().getValue(), 2.0);
 assertEquals(Token.EXPR_RESULT, simplifiedThirdChild.getType());
 assertEquals(Token.NUMBER, simplifiedThirdChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedThirdChild.getFirstChild().getValue(), 3.0);
 }

 @org.junit.Test
 public void testExpressionWithSideEffects() {
 Node source = newNode("x += 1; y += 1; x + y");
 Result result = new Result();
 Node root = compileSingleFileToNode(source, null, result);
 Node removedNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.EXPR_RESULT));
 Node firstChild = removedNode.getFirstChild();
 Node secondChild = removedNode.getChild(1);
 assertEquals(Token.ASSIGN, firstChild.getType());
 assertEquals(Token.VAR_NAME, firstChild.getFirstChild().getFirstChild().getFirstChild().getValue(), "x");
 assertEquals(Token.PLUS_EQUALS, firstChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, firstChild.getFirstChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, firstChild.getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.ASSIGN, secondChild.getType());
 assertEquals(Token.VAR_NAME, secondChild.getFirstChild().getFirstChild().getFirstChild().getValue(), "y");
 assertEquals(Token.PLUS_EQUALS, secondChild.getType());
 assertEquals(Token.NUMBER, secondChild.getFirstChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, secondChild.getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.EXPR_RESULT, removedNode.getNextSibling().getType());
 assertEquals(Token.PLUS, removedNode.getNextSibling().getFirstChild().getType());
 assertEquals(Token.EXPR_RESULT, removedNode.getNextSibling().getFirstChild().getFirstChild().getType());
 assertEquals(Token.VAR_NAME, removedNode.getNextSibling().getNextSibling().getFirstChild().getValue(), "x");
 assertEquals(Token.VAR_NAME, removedNode.getNextSibling().getNextSibling().getNextSibling().getFirstChild().getValue(), "y");

 Node simplifiedNode = traverse(null, root, pass.new RemoveConstantRValuesCallback());
 Node simplifiedFirstChild = simplifiedNode.asList().get(0);
 Node simplifiedSecondChild = simplifiedNode.asList().get(1);
 Node simplifiedThirdChild = simplifiedNode.asList().get(2);
 assertEquals(Token.EXPR_LIST, simplifiedNode.getType());
 assertEquals(Token.EXPR_RESULT, simplifiedFirstChild.getType());
 assertEquals(Token.ASSIGN, simplifiedFirstChild.getFirstChild().getType());
 assertEquals(Token.VAR_NAME, simplifiedFirstChild.getFirstChild().getFirstChild().getFirstChild().getValue(), "x");
 assertEquals(Token.PLUS_EQUALS, simplifiedFirstChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedFirstChild.getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedFirstChild.getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.EXPR_RESULT, simplifiedSecondChild.getType());
 assertEquals(Token.ASSIGN, simplifiedSecondChild.getFirstChild().getType());
 assertEquals(Token.VAR_NAME, simplifiedSecondChild.getFirstChild().getFirstChild().getFirstChild().getValue(), "y");
 assertEquals(Token.PLUS_EQUALS, simplifiedSecondChild.getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedSecondChild.getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedSecondChild.getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.PLUS, simplifiedNode.getNextSibling().getType());
 assertEquals(Token.EXPR_RESULT, simplifiedNode.getNextSibling().getFirstChild().getType());
 assertEquals(Token.VAR_NAME, simplifiedNode.getNextSibling().getNextSibling().getFirstChild().getValue(), "x");
 assertEquals(Token.VAR_NAME, simplifiedNode.getNextSibling().getNextSibling().getNextSibling().getFirstChild().getValue(), "y");
 }

 @org.junit.Test
 public void testReturnConstantExpressionWithSideEffect() {
 Node source = newNode("function foo() { x += 1; return 42; }");
 Result result = new Result();
 Node root = compileSingleFileToNode(source, null, result);
 Node functionNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.FUNCTION));
 Node statementNode = traverse(null, functionNode, new FindingFirstNodeCallback(NodeType.STMT));
 Node returnNode = statementNode.getNextSibling();
 Node expressionNode = returnNode.getFirstChild();
 Node removedNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.EXPR_RESULT));
 Node simplifiedNode = traverse(null, root, pass.new RemoveConstantRValuesCallback());

 assertEquals(Token.EXPR_LIST, simplifiedNode.getType());
 assertEquals(Token.FUNCTION, simplifiedNode.getFirstChild().getType());
 assertEquals(Token.FUNCTION_DECL, simplifiedNode.getFirstChild().getFirstChild().getType());
 assertEquals(Token.VAR_NAME, simplifiedNode.getFirstChild().getFirstChild().getFirstChild().getNextSibling().getFirstChild().getValue(), "x");
 assertEquals(Token.ASSIGN, simplifiedNode.getFirstChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.PLUS_EQUALS, simplifiedNode.getFirstChild().getFirstChild().getNextSibling().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getFirstChild().getNextSibling().getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getFirstChild().getNextSibling().getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.EXPR_LIST, functionNode.getLastChild().getType());
 assertEquals(Token.EXPR_STATEMENT, functionNode.getLastChild().getFirstChild().getType());
 assertEquals(Token.ASSIGN, functionNode.getLastChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.VAR_NAME, functionNode.getLastChild().getFirstChild().getNextSibling().getFirstChild().getFirstChild().getValue(), "x");
 assertEquals(Token.PLUS_EQUALS, functionNode.getLastChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, functionNode.getLastChild().getFirstChild().getNextSibling().getFirstChild().getType());
 assertEquals(Token.NUMBER, functionNode.getLastChild().getFirstChild().getNextSibling().getFirstChild().getValue(), 1.0);
 assertEquals(Token.RETURN, functionNode.getLastChild().getNextSibling().getType());
 assertEquals(Token.EXPR_RESULT, functionNode.getLastChild().getNextSibling().getFirstChild().getType());
 assertEquals(Token.NUMBER, functionNode.getLastChild().getNextSibling().getFirstChild().getValue(), 42.0);
 }

 @org.junit.Test
 public void testConstantExpressionWithSideEffects() {
 Node source = newNode("x += 1; 1 + 1");
 Result result = new Result();
 Node root = compileSingleFileToNode(source, null, result);
 Node xAssignmentNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.ASSIGN));
 Node expressionNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.EXPR_RESULT));
 Node simplifiedNode = traverse(null, root, pass.new RemoveConstantRValuesCallback());

 assertEquals(Token.EXPR_LIST, simplifiedNode.getType());
 assertEquals(Token.EXPR_RESULT, simplifiedNode.getFirstChild().getType());
 assertEquals(Token.ASSIGN, simplifiedNode.getFirstChild().getFirstChild().getType());
 assertEquals(Token.VAR_NAME, simplifiedNode.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getValue(), "x");
 assertEquals(Token.PLUS_EQUALS, simplifiedNode.getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getFirstChild().getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getFirstChild().getFirstChild().getNextSibling().getValue(), 1.0);
 assertEquals(Token.EXPR_RESULT, simplifiedNode.getFirstChild().getNextSibling().getType());
 assertEquals(Token.PLUS, simplifiedNode.getFirstChild().getNextSibling().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getNextSibling().getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getNextSibling().getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, simplifiedNode.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getValue(), 1.0);

 Node originalNode = traverse(null, root, new FindingFirstNodeCallback(NodeType.ASSIGN));
 Node originalAssignmentNode = originalNode;
 while (originalAssignmentNode.getPreviousSibling() != null) {
 originalAssignmentNode = originalAssignmentNode.getPreviousSibling();
 }
 Node originalExpressionNode = originalAssignmentNode.getNextSibling();
 Node originalExpressionFirstChild = originalExpressionNode.getFirstChild();
 Node originalExpressionSecondChild = originalExpressionNode.getFirstChild().getNextSibling();
 assertEquals(Token.ASSIGN, originalAssignmentNode.getType());
 assertEquals(Token.VAR_NAME, originalAssignmentNode.getFirstChild().getFirstChild().getValue(), "x");
 assertEquals(Token.PLUS_EQUALS, originalAssignmentNode.getType());
 assertEquals(Token.NUMBER, originalAssignmentNode.getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, originalAssignmentNode.getFirstChild().getNextSibling().getValue(), 1.0);
 assertEquals(Token.EXPR_RESULT, originalExpressionNode.getType());
 assertEquals(Token.PLUS, originalExpressionNode.getFirstChild().getType());
 assertEquals(Token.NUMBER, originalExpressionNode.getFirstChild().getFirstChild().getType());
 assertEquals(Token.NUMBER, originalExpressionNode.getFirstChild().getFirstChild().getValue(), 1.0);
 assertEquals(Token.NUMBER, originalExpressionNode.getFirstChild().getNextSibling().getType());
 assertEquals(Token.NUMBER, originalExpressionNode.getFirstChild().getNextSibling().getValue(), 1.0);
 }
}

class FindingFirstNodeCallback extends NodeTraversal.Callback {
 NodeType nodeType;

 FindingFirstNodeCallback(NodeType nodeType) {
 this.nodeType = nodeType;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (node.getType() == nodeType) {
 findNode(node);
 return;
 }
 }

 private void findNode(Node node) {
 if (firstNode == null) {
 firstNode = node;
 }
 }

 Node firstNode;
}

class CopySideEffectSubexpressions implements NodeTraversal.Callback {
 Result result;
 List<Node> nodes;

 CopySideEffectSubexpressions(Result result, List<Node> nodes) {
 this.result = result;
 this.nodes = nodes;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (NodeUtil.nodeTypeMayHaveSideEffects(node) && node.getParent() != parent) {
 nodes.add(node);
 }
 }
}

class GatherSideEffectSubexpressionsCallback extends NodeTraversal.Callback {
 Result result;
 CopySideEffectSubexpressions copySideEffectSubexpressions;

 GatherSideEffectSubexpressionsCallback(Result result, CopySideEffectSubexpressions copySideEffectSubexpressions) {
 this.result = result;
 this.copySideEffectSubexpressions = copySideEffectSubexpressions;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
 copySideEffectSubexpressions.traverse(null, node, this);
 }
 }
}