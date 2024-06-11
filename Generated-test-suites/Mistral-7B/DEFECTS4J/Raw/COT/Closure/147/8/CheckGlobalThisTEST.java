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

  @org.junit.Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    // Test case for a simple function
    Node functionNode = Node.newFunctionNode("evil");
    Node traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    functionNode = Node.newFunctionNode("evil", new Node[0], Node.newBlock(new Node[0]));
    traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    List<DiagnosticPosition> diagnostics = checker.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("dangerous use of the global 'this' object", diagnostics.get(0).message);

    // Test case for a constructor
    functionNode = Node.newConstructorNode("Constructor");
    traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a function with @constructor JSDoc annotation
    JSDocInfo constructorJSDocInfo = new JSDocInfo(Node.newIdentifierNode("Constructor"), new Node[0], null, "constructor");
    functionNode = Node.newFunctionNode("Constructor", new Node[0], Node.newBlock(new Node[0]));
    functionNode.setJSDocInfo(constructorJSDocInfo);
    traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a function with @this JSDoc annotation
    JSDocInfo thisJSDocInfo = new JSDocInfo(Node.newIdentifierNode("thisFunction"), new Node[0], null, null);
    thisJSDocInfo.setThisType(Node.newThisKeyword());
    functionNode = Node.newFunctionNode("thisFunction", new Node[0], Node.newBlock(new Node[0]));
    functionNode.setJSDocInfo(thisJSDocInfo);
    traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a function where there's no logical place to put a this annotation
    functionNode = Node.newFunctionNode("anonymousFunction");
    traverser = new NodeTraversal(functionNode, checker);
    checker.visit(traverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for assignment to a variable
    Node assignmentNode = Node.newAssign(Node.newVariable("a"), Node.newThisKeyword());
    Node assignmentParentNode = Node.newExpressionStatement(assignmentNode);
    Node assignmentTraverser = new NodeTraversal(assignmentParentNode, checker);
    checker.visit(assignmentTraverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for assignment to a property of an object
    Node objectNode = Node.newObjectLiteral();
    Node propertyNode = Node.newProperty(Node.newIdentifierNode("x"), Node.newThisKeyword());
    Node assignmentNode = Node.newAssign(propertyNode, Node.newNumericLiteral(42));
    Node assignmentParentNode = Node.newExpressionStatement(assignmentNode);
    Node assignmentTraverser = new NodeTraversal(assignmentParentNode, checker);
    checker.visit(assignmentTraverser, null, objectNode);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a getter property
    Node getterNode = Node.newGetProp(Node.newThisKeyword(), "x");
    Node getterParentNode = Node.newExpressionStatement(getterNode);
    Node getterTraverser = new NodeTraversal(getterParentNode, checker);
    checker.visit(getterTraverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a getter property on a prototype
    Node prototypeObjectNode = Node.newObjectLiteral();
    Node prototypeGetterNode = Node.newGetProp(Node.newThisKeyword(), "prototype");
    Node prototypeGetterParentNode = Node.newExpressionStatement(prototypeGetterNode);
    Node prototypeTraverser = new NodeTraversal(prototypeGetterParentNode, checker);
    checker.visit(prototypeTraverser, null, prototypeObjectNode);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a getter property on a prototype with a nested getter property
    Node nestedPrototypeObjectNode = Node.newObjectLiteral();
    Node nestedPrototypeGetterNode = Node.newGetProp(Node.newGetProp(Node.newThisKeyword(), "prototype"), "nestedPrototype");
    Node nestedPrototypeGetterParentNode = Node.newExpressionStatement(nestedPrototypeGetterNode);
    Node nestedPrototypeTraverser = new NodeTraversal( nestedPrototypeGetterParentNode, checker);
    checker.visit(nestedPrototypeTraverser, null, nestedPrototypeObjectNode);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a method call
    Node methodCallNode = Node.newCall(Node.newIdentifierNode("globalThisMethod"), new Node[0]);
    Node methodCallParentNode = Node.newExpressionStatement(methodCallNode);
    Node methodCallTraverser = new NodeTraversal(methodCallParentNode, checker);
    checker.visit(methodCallTraverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a method call on a prototype
    Node prototypeMethodCallNode = Node.newCall(Node.newGetProp(Node.newThisKeyword(), "prototype"), Node.newIdentifierNode("prototypeMethod"));
    Node prototypeMethodCallParentNode = Node.newExpressionStatement(prototypeMethodCallNode);
    Node prototypeMethodCallTraverser = new NodeTraversal(prototypeMethodCallParentNode, checker);
    checker.visit(prototypeMethodCallTraverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a method call on a prototype with a nested method call
    Node nestedPrototypeMethodCallNode = Node.newCall(Node.newGetProp(Node.newGetProp(Node.newThisKeyword(), "prototype"), "nestedPrototype"), Node.newIdentifierNode("nestedPrototypeMethod"));
    Node nestedPrototypeMethodCallParentNode = Node.newExpressionStatement(nestedPrototypeMethodCallNode);
    Node nestedPrototypeMethodCallTraverser = new NodeTraversal(nestedPrototypeMethodCallParentNode, checker);
    checker.visit(nestedPrototypeMethodCallTraverser, null, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a prototype property with a getter method
    Node prototypeObjectWithGetterNode = Node.newObjectLiteral();
    Node prototypeGetterNode = Node.newGetProp(Node.newThisKeyword(), "prototype");
    Node prototypePropertyNode = Node.newProperty(Node.newIdentifierNode("x"), prototypeGetterNode);
    Node prototypeObjectWithGetterParentNode = Node.newExpressionStatement(Node.newAssign(prototypePropertyNode, Node.newNumericLiteral(42)));
    Node prototypeTraverser = new NodeTraversal(prototypeObjectWithGetterParentNode, checker);
    checker.visit(prototypeTraverser, null, prototypeObjectWithGetterNode);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test case for a prototype property with a getter method that calls a method on the prototype
    Node nestedPrototypeObjectWithGetterNode = Node.newObjectLiteral();
    Node nestedPrototypeMethodCallNode = Node.newCall(Node.newGetProp(Node.newGetProp(Node.newThisKeyword(), "prototype"), "nestedPrototype"), Node.newIdentifierNode("nestedPrototypeMethod"));
    Node nestedPrototypePropertyNode = Node.newProperty(Node.newIdentifierNode("x"), Node.newGetProp(Node.newThisKeyword(), "prototype"));
    Node nestedPrototypeObjectWithGetterParentNode = Node.newExpressionStatement(Node.newAssign(nestedPrototypePropertyNode, nestedPrototypeMethodCallNode));
    Node nestedPrototypeTraverser = new NodeTraversal(nestedPrototypeObjectWithGetterParentNode, checker);
    checker.visit(nestedPrototypeTraverser, null, nestedPrototypeObjectWithGetterNode);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }
}