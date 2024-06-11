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
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal nodeTraversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    nodeTraversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseNode() {
    Node node = Node.newNode(Token.THIS, null, null, null);
    Node parent = Node.newNode(Token.ASSIGN, null, node, null);
    NodeContext context = new NodeContext();

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructor = Node.newNode(Token.FUNCTION, null, null, null);
    Node constructorParent = Node.newNode(Token.SCRIPT, null, constructor, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setConstructor(true);

    constructor.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(constructorParent)))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node function = Node.newNode(Token.FUNCTION, null, null, null);
    Node functionParent = Node.newNode(Token.ASSIGN, null, function, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setThisType(true);

    function.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(functionParent)))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node function = Node.newNode(Token.FUNCTION, null, null, null);
    Node functionParent = Node.newNode(Token.ASSIGN, null, function, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setInterface(true);

    function.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(functionParent)))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node function = Node.newNode(Token.FUNCTION, null, null, null);
    Node functionParent = Node.newNode(Token.ASSIGN, null, function, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setOverride(true);

    function.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(functionParent)))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseNodeOutsideOfScriptOrBlockContext() {
    Node node = Node.newNode(Token.THIS, null, null, null);
    Node parent = Node.newNode(Token.NAME, null, null, node);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isFalse();
  }

  @Test
  public void testShouldTraverseNodeOnLeftSideOfAssignment() {
    Node leftSide = Node.newNode(Token.THIS, null, null, null);
    Node assignment = Node.newNode(Token.ASSIGN, null, leftSide, null);
    Node parent = Node.newNode(Token.SCRIPT, null, assignment, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isTrue();
  }

  @Test
  public void testShouldTraverseNodeOnLeftSideOfGetter() {
    Node getter = Node.newNode(Token.GETPROP, null, Node.newNode(Token.THIS, null, null, null), null);
    Node parent = Node.newNode(Token.SCRIPT, null, getter, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isTrue();
  }

  @Test
  public void testShouldNotTraverseNodeOnProxyPropertyGetter() {
    Node getter = Node.newNode(Token.GETPROP, null, Node.newNode(Token.THIS, null, null, null), Node.newNode(Token.STRING, null, null, null));
    Node parent = Node.newNode(Token.SCRIPT, null, getter, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isFalse();
  }

  @Test
  public void testShouldTraverseNodeOnProxyPropertyGetterWithDotNotation() {
    Node getter = Node.newNode(Token.GETPROP, null, Node.newNode(Token.GETPROP, null, Node.newNode(Token.THIS, null, null, null), Node.newNode(Token.STRING, null, null, null)), null);
    Node parent = Node.newNode(Token.SCRIPT, null, getter, null);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(nodeTraversal), ArgumentMatchers.eq(null), ArgumentMatchers.eq(parent)))
        .isTrue();
  }
}