package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
    traversal = new NodeTraversal(new Node(), checker);
  }

  @Test
  public void testShouldTraverse_function() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);

    Node parent = Node.newBlock();
    parent.addChild(functionNode);
    Node block = Node.newBlock();
    parent.addChild(block);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_constructor() {
    JSDocInfo constructorDoc = new JSDocInfo();
    constructorDoc.setIsConstructor(true);

    Node constructorNode = Node.newFunction("name", null, null, null, null, null, constructorDoc);

    Node parent = Node.newBlock();
    parent.addChild(constructorNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), constructorNode, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_thisAnnotatedFunction() {
    JSDocInfo thisAnnotatedFunctionDoc = new JSDocInfo();
    thisAnnotatedFunctionDoc.setHasThisType(true);

    Node thisAnnotatedFunctionNode = Node.newFunction("name", null, null, null, null, null, thisAnnotatedFunctionDoc);

    Node parent = Node.newBlock();
    parent.addChild(thisAnnotatedFunctionNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), thisAnnotatedFunctionNode, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoParent() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypePropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node prototypePropertyAccess = Node.newGetProp(functionNode, "prototype");

    Node parent = Node.newBlock();
    parent.addChild(prototypePropertyAccess);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypePropertyAccessThroughAnotherProperty() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node anotherProperty = Node.newName("anotherProperty");
    Node prototypePropertyAccess = Node.newGetProp(Node.newGetProp(anotherProperty, functionNode), "prototype");

    Node parent = Node.newBlock();
    parent.addChild(prototypePropertyAccess);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node parent = Node.newBlock();
    parent.addChild(functionNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithLeftSideAssignment() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node parent = Node.newBlock();
    parent.addChild(assignNode);
    parent.addChild(functionNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithLeftSideAssignmentAndDescendant() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node assignDescendantNode = Node.newAssign(Node.newName("c"), Node.newName("a"));
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node parent = Node.newBlock();
    parent.addChild(assignNode);
    parent.addChild(functionNode);
    parent.addChild(assignDescendantNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithLeftSideDeclaration() {
    Node declareNode = Node.newDeclare("a", Node.newThis());
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node parent = Node.newBlock();
    parent.addChild(declareNode);
    parent.addChild(functionNode);

    Node result = checker.shouldTraverse(new NodeTraversal(null, null), functionNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithLeftSideAssignment() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node parent = Node.newBlock();
    parent.addChild(assignNode);
    parent.addChild(functionNode);

    Node result = checker.shouldReportThis(assignNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithLeftSidePropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node getNode = Node.newGetProp(Node.newThis(), "a");
    Node parent = Node.newBlock();
    parent.addChild(getNode);
    parent.addChild(functionNode);

    Node result = checker.shouldReportThis(getNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithLeftSidePropertyAccessThroughAnotherProperty() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node anotherPropertyNode = Node.newName("anotherProperty");
    Node getNode = Node.newGetProp(Node.newGetProp(anotherPropertyNode, Node.newThis()), "a");
    Node parent = Node.newBlock();
    parent.addChild(getNode);
    parent.addChild(functionNode);

    Node result = checker.shouldReportThis(getNode, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_functionWithNoLogicalPlaceForThisAnnotationAndLeftSidePropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node anotherPropertyNode = Node.newName("anotherProperty");
    Node getNode = Node.newGetProp(Node.newGetProp(anotherPropertyNode, Node.newThis()), "a");
    Node parent = Node.newBlock();
    parent.addChild(getNode);
    parent.addChild(functionNode);

    Node result = checker.shouldReportThis(getNode, parent);
    assertThat(result).isFalse();
  }

  @Test
  public void testVisit_globalThis() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node thisNode = Node.newThis();
    Node parent = Node.newBlock();
    parent.addChild(functionNode);
    parent.addChild(thisNode);

    Node traversal = new NodeTraversal(new Node(), checker);
    checker.visit(traversal, parent, null);

    Node error = traversal.getError();
    assertThat(error).isNotNull();
    assertThat(error.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testVisit_globalThisWithParentPropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node parentPropertyNode = Node.newGetProp(Node.newName("parent"), Node.newThis());
    Node thisNode = Node.newThis();
    Node parent = Node.newBlock();
    parent.addChild(functionNode);
    parent.addChild(parentPropertyNode);
    parent.addChild(thisNode);

    Node traversal = new NodeTraversal(new Node(), checker);
    checker.visit(traversal, parent, null);

    Node error = traversal.getError();
    assertThat(error).isNotNull();
    assertThat(error.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testVisit_globalThisWithFunctionPropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node functionPropertyNode = Node.newGetProp(Node.newThis(), "functionName");
    Node thisNode = Node.newThis();
    Node parent = Node.newBlock();
    parent.addChild(functionNode);
    parent.addChild(functionPropertyNode);
    parent.addChild(thisNode);

    Node traversal = new NodeTraversal(new Node(), checker);
    checker.visit(traversal, parent, null);

    Node error = traversal.getError();
    assertNull(error);
  }

  @Test
  public void testVisit_globalThisWithPrototypePropertyAccess() {
    Node functionNode = Node.newFunction("name", null, null, null, null, null, null);
    Node prototypePropertyNode = Node.newGetProp(Node.newThis(), "prototype");
    Node thisNode = Node.newThis();
    Node parent = Node.newBlock();
    parent.addChild(functionNode);
    parent.addChild(prototypePropertyNode);
    parent.addChild(thisNode);

    Node traversal = new NodeTraversal(new Node(), checker);
    checker.visit(traversal, parent, null);

    Node error = traversal.getError();
    assertNull(error);
  }
}