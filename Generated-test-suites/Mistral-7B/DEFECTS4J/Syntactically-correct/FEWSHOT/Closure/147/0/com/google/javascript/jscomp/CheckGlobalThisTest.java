package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node node = Node.newExpression("this");

    Node root = Node.newBlock(Node.newExpression("function foo() {}"));

    // Don't traverse functions that are constructors or have the @this or @override annotation.
    Node constructor = Node.newExpression("function Foo() {}");
    JSDocInfo constructorJsDoc = Node.newJsDocInfo();
    constructorJsDoc.setConstructor(true);
    constructor.setJSDocInfo(constructorJsDoc);

    Node thisAnnotation = Node.newJsDocTag("this");
    JSDocInfo thisAnnotationJsDoc = Node.newJsDocInfo();
    thisAnnotationJsDoc.setThisType(true);
    thisAnnotation.setJSDocInfo(thisAnnotationJsDoc);

    Node overrideAnnotation = Node.newJsDocTag("override");
    JSDocInfo overrideAnnotationJsDoc = Node.newJsDocInfo();
    overrideAnnotationJsDoc.setInterface(true);
    overrideAnnotation.setJSDocInfo(overrideAnnotationJsDoc);

    Node globalContext = Node.newVariable("globalContext", Node.newUndefined(), Node.NO_VALUE);
    root.addChildToEnd(globalContext);

    Node function1 = Node.newExpression("function foo() { this.prop = 'bar'; }");
    Node function1JsDoc = Node.newJsDocInfo();
    function1JsDoc.setPrototype(true);
    function1.setJSDocInfo(function1JsDoc);

    Node function2 = Node.newExpression("function bar() { this.prop = 'baz'; }");
    root.addChildToEnd(function2);

    Node function3 = Node.newExpression("function baz() { this.prop = 'qux'; }");
    root.addChildToEnd(function3);

    Node function4 = Node.newExpression("function qux() { this.prop = 'quux'; }");
    Node function4JsDoc = Node.newJsDocInfo();
    function4JsDoc.setOverride(true);
    function4.setJSDocInfo(function4JsDoc);

    Node function5 = Node.newExpression("function quux() { this.prop = 'corgi'; }");
    root.addChildToEnd(function5);

    Node thisInGlobalContext = Node.newExpression("this");
    root.addChildToEnd(thisInGlobalContext);

    Node assignToPrototype = Node.newExpression("function foo() { Object.prototype.prop = 'bar'; }");
    root.addChildToEnd(assignToPrototype);

    Node assignToPrototypeChain = Node.newExpression("function foo() { Object.prototype.qux.prop = 'quux'; }");
    root.addChildToEnd(assignToPrototypeChain);

    Node assignToThis = Node.newExpression("this = { prop: 'quux' }");
    root.addChildToEnd(assignToThis);

    Node assignToVariable = Node.newExpression("var x = this;");
    root.addChildToEnd(assignToVariable);

    Node assignToFunction = Node.newExpression("function foo() { this.bar = function() { this = 'qux'; } }");
    root.addChildToEnd(assignToFunction);

    Node assignToFunctionPrototype = Node.newExpression("function Foo() { Function.prototype.bar = function() { this = 'qux'; } }");
    root.addChildToEnd(assignToFunctionPrototype);

    Node assignToFunctionConstructor = Node.newExpression("function Foo() { Foo.prototype.bar = function() { this = 'qux'; } }");
    root.addChildToEnd(assignToFunctionConstructor);

    Node assignToFunctionWithJSDoc = Node.newExpression("/** @constructor */ function Foo() { this.bar = function() { this = 'qux'; } }");
    root.addChildToEnd(assignToFunctionWithJSDoc);

    Node assignToFunctionWithThisAnnotation = Node.newExpression("function Foo() { /** @this {foo: number} */ this.bar = function() { this = 'qux'; } }");
    root.addChildToEnd(assignToFunctionWithThisAnnotation);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should only report unsafe uses of this in the global context.
    assertThat(checker.getNumDiagnostics()).isEqualTo(1);
    Node diagnostic = checker.getDiagnostics().get(0);
    assertThat(diagnostic.getMessage()).isEqualTo("dangerous use of the global 'this' object");
    assertThat(diagnostic.getSourceName()).isEqualTo("test.js");
    Node diagnosticNode = diagnostic.getNode();
    assertThat(diagnosticNode).isEqualTo(thisInGlobalContext);
  }

  @Test
  public void testShouldNotTraverseConstructors() {
    Node constructorNode = constructor;

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, Node.newBlock(constructorNode), null);

    // Should not traverse constructors.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithThis() {
    Node functionWithThisAnnotationNode = function1;

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, Node.newBlock(functionWithThisAnnotationNode), null);

    // Should not traverse functions annotated with @this.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseFunctionsAnnotatedWithOverride() {
    Node functionWithOverrideAnnotationNode = function4;

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, Node.newBlock(functionWithOverrideAnnotationNode), null);

    // Should not traverse functions annotated with @override.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseFunctionsWithLogicalPlaceForThisAnnotation() {
    Node functionWithLogicalPlaceForThisAnnotationNode = function5;

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, Node.newBlock(functionWithLogicalPlaceForThisAnnotationNode), null);

    // Should not traverse functions with a logical place to put a @this annotation.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignLeftSideNode = assignLhsChild;

    Node root = Node.newBlock(assignLeftSideNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should report a dangerous use of this on the left side of an assignment.
    assertThat(checker.getNumDiagnostics()).isEqualTo(1);
    Node diagnostic = checker.getDiagnostics().get(0);
    assertThat(diagnostic.getMessage()).isEqualTo("dangerous use of the global 'this' object");
    assertThat(diagnostic.getSourceName()).isEqualTo("test.js");
    Node diagnosticNode = diagnostic.getNode();
    assertThat(diagnosticNode).isEqualTo(assignLeftSideNode);
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototype() {
    Node assignRightSideNode = assignToPrototype;

    Node root = Node.newBlock(assignRightSideNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse the right side of an assignment to a prototype property.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeChain() {
    Node assignRightSideNode = assignToPrototypeChain;

    Node root = Node.newBlock(assignRightSideNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse the right side of an assignment to a prototype chain property.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToThis() {
    Node assignToThisNode = assignToThis;

    Node root = Node.newBlock(assignToThisNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to this.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToVariable() {
    Node assignToVariableNode = assignToVariable;

    Node root = Node.newBlock(assignToVariableNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to a variable.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToFunction() {
    Node assignToFunctionNode = assignToFunction;

    Node root = Node.newBlock(assignToFunctionNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to a function.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToFunctionPrototype() {
    Node assignToFunctionPrototypeNode = assignToFunctionPrototype;

    Node root = Node.newBlock(assignToFunctionPrototypeNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to Function.prototype.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToFunctionConstructor() {
    Node assignToFunctionConstructorNode = assignToFunctionConstructor;

    Node root = Node.newBlock(assignToFunctionConstructorNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to Foo.prototype.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToFunctionWithJSDoc() {
    Node assignToFunctionWithJSDocNode = assignToFunctionWithJSDoc;

    Node root = Node.newBlock(assignToFunctionWithJSDocNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to a function with a @constructor JSDoc annotation.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }

  @Test
  public void testShouldNotTraverseAssignmentToFunctionWithThisAnnotation() {
    Node assignToFunctionWithThisAnnotationNode = assignToFunctionWithThisAnnotation;

    Node root = Node.newBlock(assignToFunctionWithThisAnnotationNode);

    when(compiler.getSourceName()).thenReturn("test.js");

    checker.traverse(traversal, root, null);

    // Should not traverse an assignment to a function with a @this JSDoc annotation.
    assertThat(checker.getNumDiagnostics()).isEqualTo(0);
  }
}