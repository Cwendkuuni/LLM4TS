package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckType;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private static Node createFunctionNode(NodeFactory nodeFactory, String name, CheckType checkType) {
    Node n = nodeFactory.newFunction(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    n.setJSDocInfo(new JSDocInfo(name, null, null, null, null, null, null, checkType, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    return n;
  }

  private static Node createGetPropertyNode(NodeFactory nodeFactory, Node object, String name) {
    return nodeFactory.newGetProp(object, nodeFactory.newIdentifier(name), null);
  }

  @Test
  public void testShouldTraverse() {
    NodeFactory nodeFactory = new NodeFactory();
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node function = createFunctionNode(nodeFactory, "foo", null);
    Node block = nodeFactory.newBlock(null, null, null, null);
    Node assign = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, function, null, null, null, null, null, null, null, null, null, null);

    Scope scope = new Scope();
    NodeTraversal traversal = new NodeTraversal(scope, null);

    Node functionWithPrototype = createFunctionNode(nodeFactory, "foo", null);
    Node prototypeProperty = nodeFactory.newIdentifier("prototype");
    Node prototypeGetProperty = createGetPropertyNode(nodeFactory, functionWithPrototype, prototypeProperty.getName());

    Node functionWithQualifiedName = nodeFactory.newFunction(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    functionWithQualifiedName.setQualifiedName("Foo.bar");

    when(compiler.getDiagnostics()).thenReturn(new DiagnosticCollector());

    Node root = nodeFactory.newBlock(null, null, null, null);
    Node script = nodeFactory.newScript(root, null, null, null, null, null);

    // Don't traverse functions that are constructors or have the @constructor or @this annotation.
    checker.shouldTraverse(traversal, function, script);
    assertFalse(checker.shouldTraverse(traversal, functionWithPrototype, script));
    assertFalse(checker.shouldTraverse(traversal, functionWithQualifiedName, script));

    // Don't traverse functions unless they would normally have a @this annotation associated with them.
    Node assignment = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node functionWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, assignment, null, null, null, null, null, null, null, function, null, null, null, null, null, null, null, null, null, null);
    checker.shouldTraverse(traversal, function, functionWithParentAssign);
    checker.shouldTraverse(traversal, function, block);
    checker.shouldTraverse(traversal, function, script);
    checker.shouldTraverse(traversal, function, null);

    // Traverse the left side of an assignment.
    checker.shouldTraverse(traversal, assign.getLhs(), assign);

    // Don't traverse the right side of an assignment to a prototype property or subproperty.
    Node prototypeAssignment = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, assign.getLhs(), prototypeGetProperty, null, null, null, null, null, null, null, null, null);
    assertFalse(checker.shouldTraverse(traversal, assign.getRhs(), prototypeAssignment));
    Node qualifiedNameAssignment = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, assign.getLhs(), functionWithQualifiedName, null, null, null, null, null, null, null, null, null);
    assertFalse(checker.shouldTraverse(traversal, functionWithQualifiedName, qualifiedNameAssignment));

    // Report a THIS on the left side of an assignment.
    Node leftSideOfAssignment = nodeFactory.newThis();
    Node leftSideOfAssignmentWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, leftSideOfAssignment, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    assertTrue(checker.shouldReportThis(leftSideOfAssignment, script));
    assertTrue(checker.shouldReportThis(leftSideOfAssignment, leftSideOfAssignmentWithParentAssign));

    // Report a THIS with a property access.
    Node parentOfPropertyAccess = nodeFactory.newIdentifier("parent");
    Node propertyAccess = nodeFactory.newGetProp(leftSideOfAssignment, parentOfPropertyAccess, null);
    assertTrue(checker.shouldReportThis(propertyAccess, script));
  }

  private static boolean assertFalse(boolean condition) {
    if (condition) {
      fail();
    }
    return !condition;
  }

  private static boolean assertTrue(boolean condition) {
    if (!condition) {
      fail();
    }
    return condition;
  }

  @Test
  public void testVisit() {
    NodeFactory nodeFactory = new NodeFactory();
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node function = createFunctionNode(nodeFactory, "foo", null);
    Node block = nodeFactory.newBlock(null, null, null, null);
    Node assign = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, function, null, null, null, null, null, null, null, null, null, null);

    Node functionWithPrototype = createFunctionNode(nodeFactory, "foo", null);
    Node prototypeProperty = nodeFactory.newIdentifier("prototype");
    Node prototypeGetProperty = createGetPropertyNode(nodeFactory, functionWithPrototype, prototypeProperty.getName());

    Node functionWithQualifiedName = createFunctionNode(nodeFactory, "Foo.bar", null);

    Scope scope = new Scope();
    NodeTraversal traversal = new NodeTraversal(scope, null);

    Node root = nodeFactory.newBlock(null, null, null, null);
    Node script = nodeFactory.newScript(root, null, null, null, null, null);

    Node leftSideOfAssignment = nodeFactory.newThis();
    Node leftSideOfAssignmentWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, leftSideOfAssignment, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node propertyAccess = nodeFactory.newGetProp(leftSideOfAssignment, new NodeFactory().newIdentifier("parent"), null);

    Node functionWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, function, null, null, null, null, null, null, null, null, null, null);

    Node nodeToReport = null;
    when(compiler.getDiagnostics()).then(diagnostics -> {
      nodeToReport = diagnostics.getError(0).getSourceNode();
      return null;
    });

    // Don't report a THIS on the right side of an assignment or in a constructor or a function with the @constructor or @this annotation.
    checker.visit(traversal, function, script);
    assertNull(nodeToReport);

    checker.visit(traversal, functionWithPrototype, script);
    assertNull(nodeToReport);

    checker.visit(traversal, functionWithQualifiedName, script);
    assertNull(nodeToReport);

    // Report a THIS on the left side of an assignment.
    checker.visit(traversal, assign.getLhs(), assign);
    assertEquals(function, nodeToReport);

    // Report a THIS with a property access.
    checker.visit(traversal, propertyAccess, script);
    assertEquals(leftSideOfAssignment, nodeToReport);

    // Don't report a THIS in a function with a parent assign.
    checker.visit(traversal, functionWithParentAssign, script);
    assertNull(nodeToReport);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis checker = new CheckGlobalThis(null, null);

    Node function = createFunctionNode(nodeFactory, "foo", null);
    Node functionWithPrototype = createFunctionNode(nodeFactory, "foo", null);
    Node functionWithQualifiedName = createFunctionNode(nodeFactory, "Foo.bar", null);

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(function);
    assertEquals("foo", jsDocInfo.getName());
    assertEquals(null, jsDocInfo.getConstructor());
    assertEquals(null, jsDocInfo.getThisType());
    assertEquals(null, jsDocInfo.getOverride());

    jsDocInfo = checker.getFunctionJsDocInfo(functionWithPrototype);
    assertEquals(null, jsDocInfo);

    jsDocInfo = checker.getFunctionJsDocInfo(functionWithQualifiedName);
    assertEquals("Foo.bar", jsDocInfo.getName());
    assertEquals(null, jsDocInfo.getConstructor());
    assertEquals(null, jsDocInfo.getThisType());
    assertEquals(null, jsDocInfo.getOverride());
  }

  @Test
  public void testShouldReportThis() {
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis checker = new CheckGlobalThis(null, null);

    Node function = createFunctionNode(nodeFactory, "foo", null);
    Node assignment = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node leftSideOfAssignment = nodeFactory.newThis();

    Node functionWithPrototype = createFunctionNode(nodeFactory, "foo", null);
    Node prototypeProperty = nodeFactory.newIdentifier("prototype");
    Node prototypeGetProperty = createGetPropertyNode(nodeFactory, functionWithPrototype, prototypeProperty.getName());

    Node functionWithQualifiedName = createFunctionNode(nodeFactory, "Foo.bar", null);
    Node parentIdentifier = nodeFactory.newIdentifier("parent");
    Node propertyAccess = createGetPropertyNode(nodeFactory, functionWithQualifiedName, parentIdentifier.getName());

    Node functionWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, null, null, null, null, null, null, null, null, function, null, null, null, null, null, null, null, null, null, null);

    Node leftSideOfAssignmentWithParentAssign = nodeFactory.newAssign(Token.ASSIGN, leftSideOfAssignment, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    // Should report a THIS on the left side of an assignment.
    assertTrue(checker.shouldReportThis(leftSideOfAssignment, null));
    assertTrue(checker.shouldReportThis(leftSideOfAssignmentWithParentAssign, null));

    // Should not report a THIS on the right side of an assignment or in a constructor or a function with the @constructor or @this annotation.
    assertFalse(checker.shouldReportThis(function, null));
    assertFalse(checker.shouldReportThis(functionWithPrototype, null));
    assertFalse(checker.shouldReportThis(functionWithQualifiedName, null));

    // Should report a THIS with a property access.
    assertTrue(checker.shouldReportThis(propertyAccess, null));

    // Should not report a THIS in a function with a parent assign.
    assertFalse(checker.shouldReportThis(functionWithParentAssign, null));
  }
}