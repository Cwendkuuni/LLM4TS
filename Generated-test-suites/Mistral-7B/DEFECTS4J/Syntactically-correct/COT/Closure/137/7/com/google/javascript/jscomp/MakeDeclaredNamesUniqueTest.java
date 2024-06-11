package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testBasicFunctionDeclaration() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNode = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNode);
    when(nodeTraversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

    Node functionDeclaration = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "testFunction");
    Node functionBody = new Node(Token.BLOCK);
    functionDeclaration.addChild(nameNode);
    functionDeclaration.addChild(functionBody);
    rootNode.addChild(functionDeclaration);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedName = "testFunction_0";
    assertThat(nameNode.getString()).isEqualTo(expectedName);
  }

  @Test
  public void testFunctionDeclarationWithSameNameInParentScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNodeParent = new Node(Token.PROGRAM);
    Node rootNodeChild = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNodeParent);

    Node functionDeclarationParent = new Node(Token.FUNCTION);
    Node nameNodeParent = new Node(Token.NAME, "testFunction");
    Node functionBodyParent = new Node(Token.BLOCK);
    functionDeclarationParent.addChild(nameNodeParent);
    functionDeclarationParent.addChild(functionBodyParent);
    rootNodeParent.addChild(functionDeclarationParent);

    Node functionDeclarationChild = new Node(Token.FUNCTION);
    Node nameNodeChild = new Node(Token.NAME, "testFunction");
    Node functionBodyChild = new Node(Token.BLOCK);
    functionDeclarationChild.addChild(nameNodeChild);
    functionDeclarationChild.addChild(functionBodyChild);
    rootNodeChild.addChild(functionDeclarationChild);

    when(nodeTraversal.inGlobalScope()).thenReturn(false);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedNameParent = "testFunction_0";
    String expectedNameChild = "testFunction_1";
    assertThat(nameNodeParent.getString()).isEqualTo(expectedNameParent);
    assertThat(nameNodeChild.getString()).isEqualTo(expectedNameChild);
  }

  @Test
  public void testVariableDeclarationWithSameNameInParentScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNodeParent = new Node(Token.PROGRAM);
    Node rootNodeChild = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNodeParent);

    Node varDeclarationParent = new Node(Token.VAR);
    Node nameNodeParent = new Node(Token.NAME, "testVariable");
    Node initialValueParent = new Node(Token.NULL);
    varDeclarationParent.addChild(nameNodeParent);
    varDeclarationParent.addChild(initialValueParent);
    rootNodeParent.addChild(varDeclarationParent);

    Node varDeclarationChild = new Node(Token.VAR);
    Node nameNodeChild = new Node(Token.NAME, "testVariable");
    Node initialValueChild = new Node(Token.NULL);
    varDeclarationChild.addChild(nameNodeChild);
    varDeclarationChild.addChild(initialValueChild);
    rootNodeChild.addChild(varDeclarationChild);

    when(nodeTraversal.inGlobalScope()).thenReturn(false);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedNameParent = "testVariable_0";
    String expectedNameChild = "testVariable_1";
    assertThat(nameNodeParent.getString()).isEqualTo(expectedNameParent);
    assertThat(nameNodeChild.getString()).isEqualTo(expectedNameChild);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionDeclarationWithoutName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNode = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNode);
    when(nodeTraversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

    Node functionDeclaration = new Node(Token.FUNCTION);
    Node functionBody = new Node(Token.BLOCK);
    functionDeclaration.addChild(functionBody);
    rootNode.addChild(functionDeclaration);

    makeDeclaredNamesUnique.traverse(nodeTraversal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableDeclarationWithoutName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNode = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNode);
    when(nodeTraversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

    Node varDeclaration = new Node(Token.VAR);
    Node initialValue = new Node(Token.NULL);
    rootNode.addChild(varDeclaration);
    varDeclaration.addChild(initialValue);

    makeDeclaredNamesUnique.traverse(nodeTraversal);
  }

  @Test
  public void testFunctionDeclarationWithSameNameInParentAndChildScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNodeParent = new Node(Token.PROGRAM);
    Node rootNodeChild = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNodeParent);

    Node functionDeclarationParent = new Node(Token.FUNCTION);
    Node nameNodeParent = new Node(Token.NAME, "testFunction");
    Node functionBodyParent = new Node(Token.BLOCK);
    functionDeclarationParent.addChild(nameNodeParent);
    functionDeclarationParent.addChild(functionBodyParent);
    rootNodeParent.addChild(functionDeclarationParent);

    Node functionDeclarationChild = new Node(Token.FUNCTION);
    Node nameNodeChild = new Node(Token.NAME, "testFunction");
    Node functionBodyChild = new Node(Token.BLOCK);
    functionDeclarationChild.addChild(nameNodeChild);
    functionDeclarationChild.addChild(functionBodyChild);
    rootNodeChild.addChild(functionDeclarationChild);

    when(nodeTraversal.inGlobalScope()).thenReturn(false);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedNameParent = "testFunction_0";
    String expectedNameChild = "testFunction_2";
    assertThat(nameNodeParent.getString()).isEqualTo(expectedNameParent);
    assertThat(nameNodeChild.getString()).isEqualTo(expectedNameChild);
  }

  @Test
  public void testVariableDeclarationWithSameNameInParentAndChildScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNodeParent = new Node(Token.PROGRAM);
    Node rootNodeChild = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNodeParent);

    Node varDeclarationParent = new Node(Token.VAR);
    Node nameNodeParent = new Node(Token.NAME, "testVariable");
    Node initialValueParent = new Node(Token.NULL);
    varDeclarationParent.addChild(nameNodeParent);
    varDeclarationParent.addChild(initialValueParent);
    rootNodeParent.addChild(varDeclarationParent);

    Node varDeclarationChild = new Node(Token.VAR);
    Node nameNodeChild = new Node(Token.NAME, "testVariable");
    Node initialValueChild = new Node(Token.NULL);
    varDeclarationChild.addChild(nameNodeChild);
    varDeclarationChild.addChild(initialValueChild);
    rootNodeChild.addChild(varDeclarationChild);

    when(nodeTraversal.inGlobalScope()).thenReturn(false);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedNameParent = "testVariable_0";
    String expectedNameChild = "testVariable_1";
    assertThat(nameNodeParent.getString()).isEqualTo(expectedNameParent);
    assertThat(nameNodeChild.getString()).isEqualTo(expectedNameChild);
  }

  @Test
  public void testFunctionDeclarationWithSameNameInMultipleSiblings() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNode = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNode);
    when(nodeTraversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

    Node functionDeclaration1 = new Node(Token.FUNCTION);
    Node nameNode1 = new Node(Token.NAME, "testFunction");
    Node functionBody1 = new Node(Token.BLOCK);
    functionDeclaration1.addChild(nameNode1);
    functionDeclaration1.addChild(functionBody1);
    rootNode.addChild(functionDeclaration1);

    Node functionDeclaration2 = new Node(Token.FUNCTION);
    Node nameNode2 = new Node(Token.NAME, "testFunction");
    Node functionBody2 = new Node(Token.BLOCK);
    functionDeclaration2.addChild(nameNode2);
    functionDeclaration2.addChild(functionBody2);
    rootNode.addChild(functionDeclaration2);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedName1 = "testFunction_0";
    String expectedName2 = "testFunction_1";
    assertThat(nameNode1.getString()).isEqualTo(expectedName1);
    assertThat(nameNode2.getString()).isEqualTo(expectedName2);
  }

  @Test
  public void testVariableDeclarationWithSameNameInMultipleSiblings() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node rootNode = new Node(Token.PROGRAM);
    when(nodeTraversal.getScopeRoot()).thenReturn(rootNode);
    when(nodeTraversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

    Node varDeclaration1 = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "testVariable");
    Node initialValue1 = new Node(Token.NULL);
    varDeclaration1.addChild(nameNode1);
    varDeclaration1.addChild(initialValue1);
    rootNode.addChild(varDeclaration1);

    Node varDeclaration2 = new Node(Token.VAR);
    Node nameNode2 = new Node(Token.NAME, "testVariable");
    Node initialValue2 = new Node(Token.NULL);
    varDeclaration2.addChild(nameNode2);
    varDeclaration2.addChild(initialValue2);
    rootNode.addChild(varDeclaration2);

    makeDeclaredNamesUnique.traverse(nodeTraversal);

    String expectedName1 = "testVariable_0";
    String expectedName2 = "testVariable_1";
    assertThat(nameNode1.getString()).isEqualTo(expectedName1);
    assertThat(nameNode2.getString()).isEqualTo(expectedName2);
  }
}