import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RuntimeTypeCheckTest {

 private AbstractCompiler compiler;
 private RuntimeTypeCheck runtimeTypeCheck;

 @Before
 public void setup() {
 compiler = new CompilerOptions();
 runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 }

 @Test
 public void testAddMarkersForConstructor() {
 Node rootNode = NodeUtil.createRootNode("test");
 FunctionType constructorType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestClass", -1, -1),
 null,
 ImmutableList.of());
 Node constructorNode = NodeUtil.newFunctionDeclaration(
 constructorType, NodeUtil.getFunctionName(constructorType.getName()),
 null,
 null);
 rootNode.addChildToFront(constructorNode);
 Node markersNode = runtimeTypeCheck.process(null, rootNode).getLastChild();
 assertNotNull(markersNode);
 }

 @Test
 public void testAddMarkersForInterfaceImplementation() {
 Node rootNode = NodeUtil.createRootNode("test");

 FunctionType testClassType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestClass", -1, -1),
 null,
 ImmutableList.of());
 FunctionType testInterfaceType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestInterface", -1, -1),
 null,
 ImmutableList.of());

 FunctionType constructorType = FunctionType.construct(
 NodeUtil.newQualifiedNameName("TestClass", -1, -1),
 null,
 ImmutableList.of());
 Node constructorNode = NodeUtil.newFunctionDeclaration(
 constructorType, NodeUtil.getFunctionName(constructorType.getName()),
 null,
 null);
 rootNode.addChildToFront(constructorNode);

 Node interfaceNode = NodeUtil.newQualifiedNameNode("TestInterface", -1, -1);
 Node implementNode = NodeUtil.newQualifiedNameNode(
 NodeUtil.getFunctionName(constructorType.getName()), -1, -1);
 Node implementsStatement = new Node(
 Token.ASSIGN,
 NodeUtil.newGetProperty(interfaceNode, "prototype"),
 NodeUtil.newSetProperty(implementNode, "implements__" +
 NodeUtil.getFunctionName(constructorType.getName()),
 NodeUtil.newBooleanLiteral(true)));
 Node interfaceDeclaration = NodeUtil.newFunctionDeclaration(
 NodeUtil.newFunctionType(
 NodeUtil.newQualifiedNameNode("TestInterface", -1, -1),
 null,
 ImmutableList.of(),
 null),
 NodeUtil.getFunctionName(constructorType.getName()),
 null,
 null);
 rootNode.addChildToFront(interfaceDeclaration);
 rootNode.addChildToBack(implementsStatement);

 Node markersNode = runtimeTypeCheck.process(null, rootNode).getLastChild();
 assertNotNull(markersNode);
 }

 @Test
 public void testAddChecksForFunction() {
 Node rootNode = NodeUtil.createRootNode("test");
 FunctionType testFunctionType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestFunction", -1, -1),
 null,
 ImmutableList.of());
 Node testFunctionNode = NodeUtil.newFunctionDeclaration(
 testFunctionType, NodeUtil.getFunctionName(testFunctionType.getName()),
 null,
 null);
 rootNode.addChildToFront(testFunctionNode);

 Node checkersNode = runtimeTypeCheck.process(null, rootNode).getLastChild();
 assertNotNull(checkersNode);
 }

 @Test
 public void testCheckTypeForBoolean() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node booleanNode = NodeUtil.newBooleanLiteral(true);
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 booleanNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("boolean")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test
 public void testCheckTypeForNumber() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node numberNode = NodeUtil.newNumberLiteral(1);
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 numberNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("number")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test
 public void testCheckTypeForString() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node stringNode = NodeUtil.newStringLiteral("test");
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 stringNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("string")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test
 public void testCheckTypeForClass() {
 Node rootNode = NodeUtil.createRootNode("test");
 FunctionType testClassType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestClass", -1, -1),
 null,
 ImmutableList.of());
 Node testClassInstanceNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("new", -1, -1),
 testClassType.getConstructor(),
 NodeUtil.newArrayLiteral());
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 testClassInstanceNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("classChecker", -1, -1),
 NodeUtil.newString(testClassType.getReferenceName())),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test
 public void testCheckTypeForInterface() {
 Node rootNode = NodeUtil.createRootNode("test");
 FunctionType testClassType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestClass", -1, -1),
 null,
 ImmutableList.of());
 FunctionType testInterfaceType = FunctionType.construct(
 NodeUtil.newQualifiedNameNode("TestInterface", -1, -1),
 null,
 ImmutableList.of());
 Node testClassInstanceNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("new", -1, -1),
 testClassType.getConstructor(),
 NodeUtil.newArrayLiteral());
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 testClassInstanceNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("interfaceChecker", -1, -1),
 NodeUtil.newString(testInterfaceType.getReferenceName())),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test
 public void testCheckTypeForNull() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node nullNode = NodeUtil.newNull();
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 nullNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("null")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertNull(resultNode.getFirstChild());
 }

 @Test
 public void testCheckTypeForUncheckedType() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node uncheckedNode = NodeUtil.newNumberLiteral(1);
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 uncheckedNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("number")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 assertTrue(resultNode.getFirstChild().getBoolValue());
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForError() {
 Node rootNode = NodeUtil.createRootNode("test");
 Node errorNode = NodeUtil.newCall(NodeUtil.newQualifiedNameNode("error", -1, -1));
 Node checkersNode = NodeUtil.newCall(
 NodeUtil.newQualifiedNameNode("checkType", -1, -1),
 errorNode,
 NodeUtil.newArrayLiteral(
 NodeUtil.newCall(NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
 NodeUtil.newString("number")),
 NodeUtil.newNull()));
 Node resultNode = NodeUtil.newCall(checkersNode, 0);
 }
}