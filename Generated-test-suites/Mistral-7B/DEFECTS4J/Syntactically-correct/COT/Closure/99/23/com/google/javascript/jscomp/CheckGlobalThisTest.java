import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CheckGlobalThis checker;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 checker = new CheckGlobalThis(compiler, WARNING);
 }

 @Test
 public void testShouldTraverseFunctions() {
 Node functionNode = new Node(Token.FUNCTION);
 Node functionJsDoc = new JSDocInfo("myFunction");
 functionNode.setJSDocInfo(functionJsDoc);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 checker.shouldTraverse(traversal, functionNode, parentNode);
 assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseConstructors() {
 Node constructorNode = new Node(Token.FUNCTION);
 constructorNode.setJSDocInfo(new JSDocInfo("myConstructor"));
 constructorNode.setIsConstructor(true);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(constructorNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertFalse(checker.shouldTraverse(traversal, constructorNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseFunctionsWithThisAnnotation() {
 Node functionNode = new Node(Token.FUNCTION);
 Node functionJsDoc = new JSDocInfo("myFunction").put("this", "MyType");
 functionNode.setJSDocInfo(functionJsDoc);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldTraverseFunctionsWithNoAnnotationInCorrectContext() {
 Node functionNode = new Node(Token.FUNCTION);
 Node parentNode = new Node(Token.BLOCK);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldTraverseFunctionsWithNoAnnotationInWrongContext() {
 Node functionNode = new Node(Token.FUNCTION);
 Node parentNode = new Node(Token.NAME);
 parentNode.setString("MyVariable");
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldTraverseLeftSideOfAssignment() {
 Node lhsNode = new Node(Token.NAME);
 lhsNode.setString("myVariable");
 Node rhsNode = new Node(Token.THIS);
 Node assignNode = new Node(Token.ASSIGN);
 assignNode.appendChild(lhsNode);
 assignNode.appendChild(rhsNode);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertTrue(checker.shouldTraverse(traversal, rhsNode, parentNode));
 }

 @Test
 public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeProperty() {
 Node lhsNode = new Node(Token.GETPROP);
 lhsNode.setString("prototype");
 lhsNode.setLastChild(new Node(Token.NAME).setString("myVariable"));
 Node rhsNode = new Node(Token.THIS);
 Node assignNode = new Node(Token.ASSIGN);
 assignNode.appendChild(lhsNode);
 assignNode.appendChild(rhsNode);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertFalse(checker.shouldTraverse(traversal, rhsNode, parentNode));
 }

 @Test
 public void testShouldTraverseLeftSideOfAssignmentToNonPrototypeProperty() {
 Node lhsNode = new Node(Token.NAME);
 lhsNode.setString("myVariable");
 Node rhsNode = new Node(Token.THIS);
 Node assignNode = new Node(Token.ASSIGN);
 assignNode.appendChild(lhsNode);
 assignNode.appendChild(rhsNode);

 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertTrue(checker.shouldTraverse(traversal, rhsNode, parentNode));
 }

 @Test(expected = java.lang.AssertionError.class)
 public void testShouldReportThisInsideFunction() {
 Node functionNode = new Node(Token.FUNCTION);
 functionNode.setBody(new Node(Token.BLOCK).appendChild(new Node(Token.THIS)));
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 checker.shouldTraverse(traversal, functionNode, parentNode);
 fail("Should have reported an error");
 }

 @Test(expected = java.lang.AssertionError.class)
 public void testShouldReportThisOnLeftSideOfAssignment() {
 Node lhsNode = new Node(Token.NAME).setString("myVariable");
 Node rhsNode = new Node(Token.THIS);
 Node assignNode = new Node(Token.ASSIGN);
 assignNode.appendChild(lhsNode);
 assignNode.appendChild(rhsNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(assignNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 checker.shouldTraverse(traversal, rhsNode, parentNode);
 fail("Should have reported an error");
 }

 @Test
 public void testShouldNotReportThisInsideFunctionWithGetProperty() {
 Node functionNode = new Node(Token.FUNCTION);
 functionNode.setBody(new Node(Token.BLOCK).appendChild(new Node(Token.GETPROP).setString("prototype").setLastChild(new Node(Token.THIS))));
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertFalse(checker.shouldReportThis(traversal, functionNode, parentNode));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfAssignmentInsideFunctionWithGetProperty() {
 Node lhsNode = new Node(Token.GETPROP).setString("prototype");
 Node lhsPropertyNode = lhsNode.getLastChild();
 lhsNode.setLastChild(new Node(Token.ASSIGN));
 Node assignNode = lhsNode.getLastChild();
 assignNode.appendChild(lhsPropertyNode);
 assignNode.appendChild(new Node(Token.THIS));
 Node rhsNode = new Node(Token.THIS);
 Node functionNode = new Node(Token.FUNCTION).setBody(new Node(Token.BLOCK).appendChild(assignNode));
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.appendChild(functionNode);

 NodeTraversal traversal = new NodeTraversal(compiler, parentNode, null);
 assertTrue(checker.shouldReportThis(traversal, rhsNode, parentNode));
 }

}