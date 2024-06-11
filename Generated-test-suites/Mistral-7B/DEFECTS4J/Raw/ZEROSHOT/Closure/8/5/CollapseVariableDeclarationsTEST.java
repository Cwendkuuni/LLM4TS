import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

 private AbstractCompiler compiler;
 private Scope scope;
 private Node rootNode;

 @org.junit.Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 scope = mock(Scope.class);
 rootNode = createNodeFromString("var a;\nvar b = 1;\nvar c = 2;");
 }

 @org.junit.Test
 public void testCollapsingMultipleDeclarationsIntoOne() {
 CollapseVariableDeclarations collapseVariableDeclarations =
         new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 Node expectedResult = createNodeFromString("var a, b = 1, c = 2;");
 Node actualResult = getCollapsedNode(rootNode);

 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 @org.junit.Test
 public void testCollapsingAssignmentsIntoVariableDeclarations() {
 CollapseVariableDeclarations collapseVariableDeclarations =
         new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 Node expectedResult = createNodeFromString("var a = 1, b = 2;");
 Node actualResult = getCollapsedNode(rootNode);

 Node nodeToCollapse = findVariableNode("a", rootNode);
 nodeToCollapse.setFirstChild(createAssignmentNode("a", createNumberNode(1)));

 Node nodeToCollapse2 = findVariableNode("b", rootNode);
 nodeToCollapse2.setFirstChild(createAssignmentNode("b", createNumberNode(2)));

 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 @org.junit.Test
 public void testCollapsingVarNodesWithSameNameIntoOne() {
 CollapseVariableDeclarations collapseVariableDeclarations =
         new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 Node expectedResult = createNodeFromString("var a;");
 Node actualResult = getCollapsedNode(rootNode);

 Node nodeToCollapse = findVariableNode("a", rootNode);
 Node nodeToCollapse2 = findVariableNode("a", rootNode);

 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 @org.junit.Test
 public void testBlacklistingStubVariables() {
 CollapseVariableDeclarations collapseVariableDeclarations =
         new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 Node expectedResult = createNodeFromString("var a;");
 Node actualResult = getCollapsedNode(rootNode);

 Node stubVarNode = createVarNode("a");
 stubVarNode.setJSDocInfo(createJSDocInfo());
 rootNode.addChildToBack(stubVarNode);

 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 @org.junit.Test
 public void testCollapsingVariablesWithinConditionalStatements() {
 CollapseVariableDeclarations collapseVariableDeclarations =
         new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 Node expectedResult = createNodeFromString("var a;");
 Node actualResult = getCollapsedNode(rootNode);

 Node ifNode = createIfStatementNode();
 Node ifStatementBody = createNodeFromString("var a = 1;");
 Node elseStatementBody = createNodeFromString("var a = 2;");

 ifNode.setLastChild(ifStatementBody);
 rootNode.setLastChild(ifNode);

 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 private Node findVariableNode(String variableName, Node node) {
 for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
 if (child.isVar() && child.getString().equals(variableName)) {
 return child;
 }
 }
 return null;
 }

 private Node createAssignmentNode(String variableName, Node value) {
 Node assignNode = new Node(Token.ASSIGN);
 Node lhs = new Node(Token.NAME, variableName);
 assignNode.setFirstChild(lhs);
 assignNode.setLastChild(value);
 return assignNode;
 }

 private Node createVarNode(String variableName) {
 Node varNode = new Node(Token.VAR);
 varNode.setString(variableName);
 return varNode;
 }

 private Node createNodeFromString(String nodeString) {
 CompilerContext compilerContext = new CompilerContext();
 Node node = compilerContext.parseJson(nodeString);
 return node;
 }

 private Node createJSDocInfo() {
 Node infoNode = new Node(Token.BLOCK_COMMENT);
 infoNode.setString("/** */");
 return infoNode;
 }

 private Node createIfStatementNode() {
 Node ifNode = new Node(Token.IF);
 Node condition = new Node(Token.FALSE);
 Node ifStatementBody = new Node(Token.BLOCK);
 Node elseStatement = new Node(Token.ELSE);
 Node elseStatementBody = new Node(Token.BLOCK);

 ifNode.setFirstChild(condition);
 ifNode.setLastChild(ifStatementBody);
 elseStatement.setLastChild(elseStatementBody);
 ifNode.addChildToBack(elseStatement);
 return ifNode;
 }

 private Node getCollapsedNode(Node rootNode) {
 Node currentNode = rootNode;
 Node collapsedNode = null;

 while (currentNode != null) {
 if (currentNode instanceof CollapsedNode) {
 collapsedNode = (Node) currentNode;
 break;
 }
 currentNode = currentNode.getParent();
 }

 return collapsedNode;
 }
}

 class CollapsedNode extends Node {
 // Extend Node with a new class to differentiate collapsed nodes from original ones
 }