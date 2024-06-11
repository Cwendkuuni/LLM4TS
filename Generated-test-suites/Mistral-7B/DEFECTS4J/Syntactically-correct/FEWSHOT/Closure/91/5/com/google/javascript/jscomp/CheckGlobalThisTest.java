package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.CallbackType.SKIP_CHILDREN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.node.NodeFactory;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(PowerMockRunner.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    nodeFactory = new NodeFactory();
    options = new CompilerOptions(true);
    diagnostics = new Diagnostics();
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = nodeFactory.createFunctionDeclaration("f", null, null, null, null, null);
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = nodeFactory.createFunctionDeclaration("Foo", null, null, null, null, new Node[] {
        nodeFactory.createAnnotation("@constructor")
    });
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithJSDocThis() {
    Node functionNode = nodeFactory.createFunctionDeclaration("f", null, null, null, null, null);
    JSDocInfo jsDoc = nodeFactory.createJSDocInfo("@this {type: 'MyType'}");
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = nodeFactory.createFunctionDeclaration("f", null, null, null, null, null);
    JSDocInfo jsDoc = nodeFactory.createJSDocInfo("@override");
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    Node functionNode = nodeFactory.createFunctionDeclaration("f", null, null, null, null, null);
    JSDocInfo jsDoc = nodeFactory.createJSDocInfo("@interface");
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, null));
  }

  @Test
  public void testShouldTraverseOnLeftSideOfAssignment() {
    Node assignmentNode = nodeFactory.createAssignment(Token.ASSIGN, null, nodeFactory.createThis());
    Node leftHandSideNode = assignmentNode.getFirstChild();
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, leftHandSideNode));
  }

  @Test
  public void testShouldTraverseOnLeftSideOfAssignmentWithPropertyAccess() {
    Node assignmentNode = nodeFactory.createAssignment(Token.ASSIGN, null, nodeFactory.createGetProperty(null, "x"), nodeFactory.createThis());
    Node leftHandSideNode = assignmentNode.getFirstChild();
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, leftHandSideNode));
  }

  @Test
  public void testShouldTraverseOnRightSideOfAssignment() {
    Node assignmentNode = nodeFactory.createAssignment(Token.ASSIGN, nodeFactory.createThis(), null);
    Node rightHandSideNode = assignmentNode.getLastChild();
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, rightHandSideNode));
  }

  @Test
  public void testShouldTraverseOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = nodeFactory.createGetProperty(null, "prototype");
    Node leftHandSideNode = propertyAccessNode.getFirstChild();
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, leftHandSideNode));
  }

  @Test
  public void testShouldTraverseOnLeftSideOfPropertyAccessWithNestedGetProperty() {
    Node propertyAccessNode = nodeFactory.createGetProperty(null, "prototype");
    Node leftHandSideNode = propertyAccessNode.getFirstChild();
    Node nestedGetPropertyNode = nodeFactory.createGetProperty(leftHandSideNode, "x");
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, nestedGetPropertyNode));
  }

  @Test
  public void testShouldNotTraverseOnLeftSideOfPropertyAccessWithGetPropertyOnPrototype() {
    Node propertyAccessNode = nodeFactory.createGetProperty(null, "prototype");
    Node leftHandSideNode = propertyAccessNode.getFirstChild();
    Node getPropertyNodeOnPrototype = nodeFactory.createGetProperty(leftHandSideNode, "prototype");
    assertFalse(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, getPropertyNodeOnPrototype));
  }

  @Test
  public void testShouldTraverseOnLeftSideOfPrototypeMethod() {
    Node prototypeNode = nodeFactory.createObjectLiteral();
    Node prototypePropertyNode = nodeFactory.createProperty(null, null, null, null, null, null);
    Node methodNode = nodeFactory.createFunctionDeclaration("m", null, null, null, null, null);
    prototypePropertyNode.setValue(methodNode);
    prototypeNode.setFirstChild(prototypePropertyNode);
    Node leftHandSideNode = methodNode.getFirstChild();
    assertTrue(new CheckGlobalThis(compiler, WARNING).shouldTraverse(null, null, leftHandSideNode));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = nodeFactory.createThis();
    Node parentNode = nodeFactory.createGetProperty(null, "x");
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, options, diagnostics, checkGlobalThis, SKIP_CHILDREN);
    nodeTraversal.traverse(parentNode, globalThisNode);
    assertTrue(diagnostics.getDiagnostics().size() > 0);
  }

  @Test
  public void testShouldNotReportGlobalThisOnLeftSideOfAssignment() {
    Node assignmentNode = nodeFactory.createAssignment(Token.ASSIGN, null, nodeFactory.createThis(), null);
    Node leftHandSideNode = assignmentNode.getFirstChild();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, options, diagnostics, checkGlobalThis, SKIP_CHILDREN);
    nodeTraversal.traverse(null, leftHandSideNode);
    assertTrue(diagnostics.getDiagnostics().size() == 0);
  }

  @Test
  public void testShouldNotReportGlobalThisOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = nodeFactory.createGetProperty(null, "prototype");
    Node leftHandSideNode = propertyAccessNode.getFirstChild();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, options, diagnostics, checkGlobalThis, SKIP_CHILDREN);
    nodeTraversal.traverse(null, leftHandSideNode);
    assertTrue(diagnostics.getDiagnostics().size() == 0);
  }
}