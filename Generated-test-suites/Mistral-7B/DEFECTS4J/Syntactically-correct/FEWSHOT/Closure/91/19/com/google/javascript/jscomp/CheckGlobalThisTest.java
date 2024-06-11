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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    rootNode = createRootNode();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node parent = createScriptNode();
    Node functionNode = createFunctionNode("function a() {}");
    Node functionWithThisNode = createFunctionNode("function b() { this.x = 0; }");
    parent.addChild(functionNode);
    parent.addChild(functionWithThisNode);
    parent.addChild(rootNode);

    when(compiler.report(ArgumentMatchers.any()))
        .thenReturn(new Object());

    checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node parent = createBlockNode();
    Node functionNode = createFunctionNode("function a() {}");
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node constructorNode = createFunctionNode("function Foo() {}");
    Node parent = createScriptNode();
    parent.addChild(constructorNode);
    parent.addChild(rootNode);

    checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node thisAnnotationNode = createNameNode("@this");
    Node functionNode = createFunctionNode("function Foo@this() {}");
    Node parent = createScriptNode();
    parent.addChild(thisAnnotationNode);
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoAnnotation() {
    Node functionNode = createFunctionNode("function a() {}");
    Node parent = createScriptNode();
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(), ArgumentMatchers.any(), parent));
  }

  @Test
  public void testShouldReportGlobalThisInFunction() {
    Node functionNode = createFunctionNode("function a() { this.x = 0; }");
    Node parent = createScriptNode();
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    when(compiler.report(ArgumentMatchers.any()))
        .thenReturn(new Object());

    Node globalThisNode = checker.traverse(ArgumentMatchers.any(), functionNode, parent);
    assertNotNull(globalThisNode);
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypeProperty() {
    Node functionNode = createFunctionNode("function a() {}");
    Node prototypeNode = createNameNode("prototype");
    Node propertyNode = createGetPropertyNode("a.prototype");
    Node prototypeChildNode = createNameNode("prototype");
    Node parent = createObjectLiteralNode();
    parent.addChild(prototypeNode);
    parent.addChild(propertyNode);
    parent.addChild(prototypeChildNode);
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    when(compiler.report(ArgumentMatchers.any()))
        .thenReturn(new Object());

    checker.traverse(ArgumentMatchers.any(), functionNode, parent);
    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testShouldReportGlobalThisInPropertyAccess() {
    Node functionNode = createFunctionNode("function a() {}");
    Node parent = createScriptNode();
    parent.addChild(functionNode);
    parent.addChild(rootNode);
    Node globalThisNode = createGetPropertyNode("this.x");

    when(compiler.report(ArgumentMatchers.any()))
        .thenReturn(new Object());

    Node thisNode = checker.traverse(ArgumentMatchers.any(), globalThisNode, parent);
    assertNotNull(thisNode);
  }

  @Test
  public void testShouldNotReportGlobalThisInVariableDeclaration() {
    Node functionNode = createFunctionNode("function a() { var this = 0; }");
    Node parent = createScriptNode();
    parent.addChild(functionNode);
    parent.addChild(rootNode);

    when(compiler.report(ArgumentMatchers.any()))
        .thenReturn(new Object());

    Node thisVariableNode = checker.traverse(ArgumentMatchers.any(), functionNode, parent);
    assertNull(thisVariableNode);
  }

  private Node createRootNode() {
    return new ObjectLiteralNode();
  }

  private Node createScriptNode() {
    return new ScriptNode();
  }

  private Node createBlockNode() {
    return new BlockNode();
  }

  private Node createFunctionNode(String code) {
    return new FunctionNode(Token.FUNCTION, null, null, code);
  }

  private Node createObjectLiteralNode() {
    return new ObjectLiteralNode();
  }

  private Node createNameNode(String name) {
    return new NameNode(name);
  }

  private Node createGetPropertyNode(String property) {
    return new GetPropertyNode("Object", property);
  }
}