import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeTraversal.Callback;

public class CheckGlobalThisTest {

 private CheckGlobalThis checkGlobalThis;
 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testShouldTraverseFunctionAnnotatedWithConstructor() {
 Node constructorNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.SEMICOLON});
 Node constructorJsDocInfo = Node.newJSDocInfoNode("constructor");
 when(constructorNode.getJSDocInfo()).thenReturn(constructorJsDocInfo);

 NodeFunction nodeFunction = new NodeFunction(constructorNode);

 assert(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
 }

 @Test
 public void testShouldTraverseFunctionAnnotatedWithThis() {
 Node thisAnnotatedFunctionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.SEMICOLON});
 Node thisAnnotatedFunctionJsDocInfo = Node.newJSDocInfoNode("@this");
 when(thisAnnotatedFunctionNode.getJSDocInfo()).thenReturn(thisAnnotatedFunctionJsDocInfo);

 NodeFunction nodeFunction = new NodeFunction(thisAnnotatedFunctionNode);

 assert(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
 }

 @Test
 public void testShouldTraverseFunctionWithoutJsDoc() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.SEMICOLON});
 NodeFunction nodeFunction = new NodeFunction(functionNode);

 assert(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
 }

 @Test
 public void testShouldTraverseFunctionWithAssignLhsChild() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.ASSIGN, Token.NAME, Token.SEMICOLON});
 Node lhsNode = functionNode.getChildAt(1);
 Node rhsNode = functionNode.getChildAt(3);
 Node assignNode = functionNode.getParent();

 NodeFunction nodeFunction = new NodeFunction(functionNode);
 Node assignLhsChild = new NodeAssignment(lhsNode, rhsNode);
 checkGlobalThis.visit(NodeTraversal.EMPTY, assignLhsChild, assignNode);

 NodeFunction updatedNodeFunction = new NodeFunction(functionNode);
 updatedNodeFunction.assignLhsChild = assignLhsChild;

 assertTrue(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, updatedNodeFunction, null));
 }

 @Test
 public void testShouldNotTraversePrototypeProperty() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.ASSIGN, Token.GETPROP, Token.NAME, Token.SEMICOLON});
 Node prototypePropertyNode = functionNode.getChildAt(3);
 Node prototypeNode = prototypePropertyNode.getLastChild();

 NodeFunction nodeFunction = new NodeFunction(functionNode);

 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
 }

 @Test
 public void testShouldNotTraversePrototypeSubProperty() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.ASSIGN, Token.GETPROP, Token.NAME, Token.GETPROP, Token.NAME, Token.SEMICOLON});
 Node prototypePropertyNode = functionNode.getChildAt(3);
 Node prototypeNode = prototypePropertyNode.getLastChild();
 Node subPropertyNode = prototypePropertyNode.getParent().getLastChild();

 NodeFunction nodeFunction = new NodeFunction(functionNode);

 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
 }

 @Test(expected = AssertionError.class)
 public void testReportThis() {
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 checkGlobalThis.visit(NodeTraversal.EMPTY, thisNode, null);
 }

 @Test
 public void testShouldNotReportThisOnLeftSideOfAssignment() {
 Node lhsNode = Node.newNameNode("a");
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 Node rhsNode = Node.newNameNode("this");
 Node assignmentNode = Node.newAssignment(lhsNode, thisNode);
 Node statementNode = Node.newExpressionStatement(rhsNode);
 Node functionNode = Node.newBlock(statementNode, assignmentNode);

 NodeFunction nodeFunction = new NodeFunction(functionNode);

 assertFalse(checkGlobalThis.shouldReportThis(nodeFunction.getNode(), nodeFunction.getNode()));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfAssignmentWithAssignLhsChild() {
 Node lhsNode = Node.newNameNode("a");
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 Node rhsNode = Node.newNameNode("this");
 Node assignmentNode = Node.newAssignment(lhsNode, thisNode);
 Node statementNode = Node.newExpressionStatement(rhsNode);
 Node functionNode = Node.newBlock(statementNode, assignmentNode);
 NodeFunction nodeFunction = new NodeFunction(functionNode);
 nodeFunction.assignLhsChild = lhsNode;

 assertTrue(checkGlobalThis.shouldReportThis(nodeFunction.getNode(), nodeFunction.getNode()));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfAssignmentInsideFunction() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.ASSIGN, Token.NAME, Token.FUNCTION, Token.BRACE});
 Node lhsNode = functionNode.getChildAt(1);
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 Node rhsNode = Node.newNameNode("this");
 Node assignmentNode = Node.newAssignment(lhsNode, thisNode);
 Node statementNode = Node.newExpressionStatement(rhsNode);
 Node innerFunctionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.SEMICOLON});
 Node innerFunctionStatementNode = Node.newExpressionStatement(innerFunctionNode);
 Node innerFunctionBlockNode = Node.newBlock(statementNode, innerFunctionStatementNode);
 Node innerFunctionNodeInFunction = Node.newgetChildNode(functionNode, 3);
 Node innerFunction = new NodeFunction(innerFunctionNodeInFunction);
 innerFunction.assignLhsChild = lhsNode;

 assertTrue(checkGlobalThis.shouldReportThis(functionNode, innerFunction.getNode()));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfPropertyAccess() {
 Node lhsNode = Node.newGetProperty(Node.newNameNode("a"), Node.newNameNode("prototype"));
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 Node rhsNode = Node.newNameNode("this");
 Node assignmentNode = Node.newAssignment(lhsNode, thisNode);
 Node statementNode = Node.newExpressionStatement(rhsNode);
 Node functionNode = Node.newBlock(statementNode, assignmentNode);

 NodeFunction nodeFunction = new NodeFunction(functionNode);

 assertFalse(checkGlobalThis.shouldReportThis(nodeFunction.getNode(), nodeFunction.getNode()));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfPropertyAccessInsideFunction() {
 Node functionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.ASSIGN, Token.GETPROP, Node.newNameNode("prototype"), Token.NAME, Token.FUNCTION, Token.BRACE});
 Node lhsNode = functionNode.getChildAt(1);
 Node thisNode = Node.newExpressionStatement(Node.newThisExpression());
 Node rhsNode = Node.newNameNode("this");
 Node assignmentNode = Node.newAssignment(lhsNode, thisNode);
 Node statementNode = Node.newExpressionStatement(rhsNode);
 Node innerFunctionNode = Node.newFunctionNode(
 Token.FUNCTION, new int[]{}, new int[]{Token.RETURN, Token.NAME, Token.SEMICOLON});
 Node innerFunctionStatementNode = Node.newExpressionStatement(innerFunctionNode);
 Node innerFunctionBlockNode = Node.newBlock(statementNode, innerFunctionStatementNode);
 Node innerFunctionNodeInFunction = Node.newgetChildNode(functionNode, 3);
 Node innerFunction = new NodeFunction(innerFunctionNodeInFunction);
 innerFunction.assignLhsChild = lhsNode;

 assertTrue(checkGlobalThis.shouldReportThis(functionNode, innerFunction.getNode()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVisitNullNode() {
 checkGlobalThis.visit(NodeTraversal.EMPTY, null, null);
 }
}