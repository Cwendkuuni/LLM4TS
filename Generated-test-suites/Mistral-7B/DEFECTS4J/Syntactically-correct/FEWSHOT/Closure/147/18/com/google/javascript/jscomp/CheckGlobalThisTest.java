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
import org.mockito.ArgumentMatcher;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker, null);
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    Node functionNode = Node.newFunction("evil", new int[]{}, false);
    Node parentNode = Node.newBlock();
    parentNode.addChild(functionNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, parentNode);
    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocConstructor() {
    Node constructorNode = Node.newFunction("Constructor", new int[]{}, true);
    Node parentNode = Node.newBlock();
    parentNode.addChild(constructorNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), constructorNode, parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocThis() {
    Node thisAnnotatedNode = Node.newFunction("ThisAnnotatedFunction", new int[]{}, null);
    thisAnnotatedNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, new String[]{"@this"}));
    Node parentNode = Node.newBlock();
    parentNode.addChild(thisAnnotatedNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), thisAnnotatedNode, parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocInterface() {
    Node interfaceNode = Node.newFunction("InterfaceFunction", new int[]{}, null);
    this.when(interfaceNode.getJSDocInfo()).thenReturn(new JSDocInfo(null, null, null, null, null, new String[]{"@interface"}));
    Node parentNode = Node.newBlock();
    parentNode.addChild(interfaceNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), interfaceNode, parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocOverride() {
    Node overrideNode = Node.newFunction("OverrideFunction", new int[]{}, null);
    this.when(overrideNode.getJSDocInfo()).thenReturn(new JSDocInfo(null, null, null, null, null, new String[]{"@override"}));
    Node parentNode = Node.newBlock();
    parentNode.addChild(overrideNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), overrideNode, parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionNotInGlobalContext() {
    Node functionNode = Node.newFunction("evil", new int[]{}, false);
    Node objectNode = Node.newObject("Object");
    objectNode.addChild(functionNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, objectNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionInGlobalContext() {
    Node functionNode = Node.newFunction("evil", new int[]{}, false);
    Node parentNode = Node.newScript();
    parentNode.addChild(functionNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, parentNode);
    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), Node.newThis(), parentNode);
    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), Node.newName("a"), parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfGetProperty() {
    Node getPropertyNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));
    Node parentNode = Node.newBlock();
    parentNode.addChild(getPropertyNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), Node.newName("a"), parentNode);
    assertThat(shouldTraverse).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfGetProperty() {
    Node getPropertyNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));
    Node parentNode = Node.newBlock();
    parentNode.addChild(getPropertyNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), Node.newName("prototype"), parentNode);
    assertThat(shouldTraverse).isFalse();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node functionNode = Node.newFunction("evil", new int[]{}, false);
    Node parentNode = Node.newScript();
    parentNode.addChild(functionNode);

    Node globalThisNode = Node.newThis();
    Node shouldTraverseNode = Node.newAssign(Node.newName("a"), globalThisNode);

    boolean shouldTraverse = checker.shouldTraverse(NodeTraversal.create(null, null), globalThisNode, parentNode);
    assertThat(shouldTraverse).isTrue();

    checker.visit(traversal, null, null);
    assertThat(compiler.getDiagnostics()).hasSize(1);
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertThat(diagnosticType).isEqualTo(DiagnosticType.warning(CheckGlobalThis.GLOBAL_THIS, "dangerous use of the global 'this' object"));
  }
}

class ArgumentMatcherWithName {
  private final String name;

  ArgumentMatcherWithName(String name) {
    this.name = name;
  }

  @Override
  public boolean matches(Object argument) {
    if (argument instanceof Node) {
      return ((Node) argument).getName().equals(name);
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("Node with name %s", name);
  }
}