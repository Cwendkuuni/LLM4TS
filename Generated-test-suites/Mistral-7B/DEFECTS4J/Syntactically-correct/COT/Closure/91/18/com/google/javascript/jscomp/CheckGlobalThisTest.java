import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

 private CheckGlobalThis checkGlobalThis;
 @Mock private AbstractCompiler compiler;

 @Before
 public void setup() {
 checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
 }

 // Basic test case for the constructor
 @Test
 public void testCheckGlobalThisConstructor() {
 CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
 assertNotNull(checkGlobalThis);
 }

 // Basic test case for the shouldTraverse method
 @Test
 public void testShouldTraverseFunction() {
 Node functionNode = new Node(Token.FUNCTION);
 assertTrue(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
 }

 // Test case for shouldTraverse when function is a constructor or has @this or @override annotation
 @Test
 public void testShouldTraverseConstructorAndAnnotatedFunctions() {
 Node constructorNode = new Node(Token.FUNCTION, "Constructor");
 constructorNode.setJSDocInfo(new JSDocInfo("Constructor"));
 constructorNode.setJSDocInfoAnnotation("constructor", true);
 Node annotatedFunctionNode = new Node(Token.FUNCTION, "AnnotatedFunction");
 constructorNode.setJSDocInfoAnnotation("this", true);
 Node overrideFunctionNode = new Node(Token.FUNCTION, "OverrideFunction");
 constructorNode.setJSDocInfoAnnotation("override", true);
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, constructorNode, null));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, annotatedFunctionNode, null));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, overrideFunctionNode, null));
 }

 // Test case for shouldTraverse when function is not in a valid context
 @Test
 public void testShouldTraverseInvalidFunctions() {
 Node invalidFunctionNode1 = new Node(Token.FUNCTION, "InvalidFunction1");
 Node invalidFunctionNode2 = new Node(Token.FUNCTION, "InvalidFunction2");
 Node variableNode = new Node(Token.VAR, "Variable");
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node arrayNode = new Node(Token.ARRAY, null);
 Node stringNode = new Node(Token.STRING, "String");
 Node numberNode = new Node(Token.NUMBER, "Number");
 Node blockNode = new Node(Token.BLOCK, null);
 Node scriptNode = new Node(Token.SCRIPT, null);
 Node nameNode = new Node(Token.NAME, "Name");
 Node getPropertyNode = new Node(Token.GETPROP, null);
 Node prototypeNode = new Node(Token.NAME, "prototype");
 Node getPrototypeNode = new Node(Token.GETPROP, prototypeNode);
 Node prototypePropertyNode = new Node(Token.NAME, "property");
 Node prototypeSubPropertyNode = new Node(Token.NAME, "subProperty");
 Node assignmentLhsNode = new Node(Token.NAME, "assignmentLhs");
 Node getNode = new Node(Token.GETPROP, assignmentLhsNode);
 Node getPrototypeAssignNode = new Node(Token.ASSIGN, getPrototypeNode);

 // Function not inside a valid context (not in a script, block, or object literal)
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));

 // Function inside an invalid context (not in a script, block, or object literal)
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, scriptNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, blockNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, variableNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, assignmentNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, arrayNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, stringNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, numberNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, nameNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, getPropertyNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, getPrototypeNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypePropertyNode));
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeSubPropertyNode));

 // Function getting lent to a prototype
 assertFalse(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, getPrototypeAssignNode));
 }

 // Test case for shouldTraverse when function is being assigned to a prototype property
 @Test
 public void testShouldTraverseFunctionAssignedToPrototypeProperty() {
 Node prototypeNode = new Node(Token.NAME, "prototype");
 Node getPrototypeNode = new Node(Token.GETPROP, prototypeNode);
 Node prototypePropertyNode = new Node(Token.NAME, "property");
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node functionNode = new Node(Token.FUNCTION, "Function");
 Node functionLhsNode = new Node(Token.NAME, "functionLhs");
 Node getFunctionNode = new Node(Token.GETPROP, functionLhsNode);
 Node functionRhsNode = new Node(Token.FUNCTION, "FunctionRhs");
 Node prototypeAssignNode = new Node(Token.ASSIGN, getPrototypeNode);
 Node prototypeAssignLhsNode = prototypeAssignNode.getFirstChild();
 Node prototypeAssignRhsNode = prototypeAssignNode.getLastChild();
 Node functionAssignNode = new Node(Token.ASSIGN, getFunctionNode);
 Node functionAssignLhsNode = functionAssignNode.getFirstChild();
 Node functionAssignRhsNode = functionAssignNode.getLastChild();

 // Function being assigned to a prototype property
 functionAssignNode.setParent(prototypeAssignNode);
 functionAssignLhsNode.setParent(prototypeAssignLhsNode);
 functionAssignRhsNode.setParent(functionAssignRhsNode);
 functionRhsNode.setParent(functionAssignRhsNode);

 assertTrue(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeNode));
 }

 // Test case for shouldTraverse when function is being assigned to a prototype sub property
 @Test
 public void testShouldTraverseFunctionAssignedToPrototypeSubProperty() {
 Node prototypeNode = new Node(Token.NAME, "prototype");
 Node getPrototypeNode = new Node(Token.GETPROP, prototypeNode);
 Node prototypePropertyNode1 = new Node(Token.NAME, "property1");
 Node prototypePropertyNode2 = new Node(Token.NAME, "property2");
 Node getPrototypePropertyNode1 = new Node(Token.GETPROP, prototypePropertyNode1);
 Node getPrototypePropertyNode2 = new Node(Token.GETPROP, prototypePropertyNode2);
 Node prototypeSubPropertyNode = new Node(Token.NAME, "subProperty");
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node functionNode = new Node(Token.FUNCTION, "Function");
 Node functionLhsNode = new Node(Token.NAME, "functionLhs");
 Node getFunctionNode = new Node(Token.GETPROP, functionLhsNode);
 Node functionRhsNode = new Node(Token.FUNCTION, "FunctionRhs");
 Node prototypeAssignNode = new Node(Token.ASSIGN, getPrototypeNode);
 Node prototypeAssignLhsNode = prototypeAssignNode.getFirstChild();
 Node prototypeAssignRhsNode = prototypeAssignNode.getLastChild();
 Node functionAssignNode = new Node(Token.ASSIGN, getPrototypePropertyNode1);
 Node functionAssignLhsNode = functionAssignNode.getFirstChild();
 Node functionAssignRhsNode = functionAssignNode.getLastChild();
 Node prototypeSubAssignNode = new Node(Token.ASSIGN, getPrototypePropertyNode2);
 Node prototypeSubAssignLhsNode = prototypeSubAssignNode.getFirstChild();
 Node prototypeSubAssignRhsNode = prototypeSubAssignNode.getLastChild();

 // Function being assigned to a prototype sub property
 functionAssignNode.setParent(prototypeSubAssignNode);
 functionAssignLhsNode.setParent(prototypeSubAssignLhsNode);
 functionAssignRhsNode.setParent(functionAssignRhsNode);
 functionRhsNode.setParent(functionAssignRhsNode);
 prototypeSubAssignNode.setParent(getPrototypePropertyNode2);
 prototypeSubAssignLhsNode.setParent(prototypeAssignLhsNode);
 prototypeSubAssignRhsNode.setParent(prototypeAssignRhsNode);

 assertTrue(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeNode));
 }

 // Test case for visit when THIS keyword is encountered on the left side of an assignment
 @Test
 public void testVisitTHISKeywordLeftSideOfAssignment() {
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node lhsNode = new Node(Token.NAME, "lhs");
 Node rhsNode = new Node(Token.THIS, null);
 Node thisNode = rhsNode;
 assignmentNode.setParent(assignmentNode);
 lhsNode.setParent(assignmentNode);
 rhsNode.setParent(rhsNode);

 checkGlobalThis.visit(NodeTraversal.EMPTY, thisNode, assignmentNode);
 assertEquals(GLOBAL_THIS, checkGlobalThis.compiler.getLatestError().getType());
 }

 // Test case for visit when THIS keyword is encountered on the right side of an assignment
 @Test
 public void testVisitTHISKeywordRightSideOfAssignment() {
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node lhsNode = new Node(Token.NAME, "lhs");
 Node rhsNode = new Node(Token.THIS, null);
 Node thisNode = rhsNode;
 assignmentNode.setParent(assignmentNode);
 lhsNode.setParent(assignmentNode);
 rhsNode.setParent(rhsNode);

 checkGlobalThis.visit(NodeTraversal.EMPTY, lhsNode, assignmentNode);
 assertNull(checkGlobalThis.compiler.getLatestError());
 }

 // Test case for visit when THIS keyword is encountered on the left side of a property access
 @Test(expected = AssertionError.class)
 public void testVisitTHISKeywordLeftSideOfPropertyAccess() {
 Node getPropertyNode = new Node(Token.GETPROP, null);
 Node lhsNode = new Node(Token.THIS, null);
 Node thisNode = lhsNode;
 getPropertyNode.setParent(getPropertyNode);
 lhsNode.setParent(lhsNode);

 checkGlobalThis.visit(NodeTraversal.EMPTY, thisNode, getPropertyNode);
 }

 // Test case for visit when THIS keyword is encountered on the right side of a property access
 @Test
 public void testVisitTHISKeywordRightSideOfPropertyAccess() {
 Node getPropertyNode = new Node(Token.GETPROP, null);
 Node lhsNode = new Node(Token.NAME, "lhs");
 Node rhsNode = new Node(Token.THIS, null);
 Node thisNode = rhsNode;
 getPropertyNode.setParent(getPropertyNode);
 lhsNode.setParent(getPropertyNode);
 rhsNode.setParent(rhsNode);

 checkGlobalThis.visit(NodeTraversal.EMPTY, lhsNode, getPropertyNode);
 assertNull(checkGlobalThis.compiler.getLatestError());
 }
}