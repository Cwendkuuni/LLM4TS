package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.rhino.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  private Node rootNode;
  private MakeDeclaredNamesUnique renamer;

  @Before
  public void setUp() {
    rootNode = createTestNode();
    renamer = new MakeDeclaredNamesUnique();
  }

  @After
  public void tearDown() {
    renamer = null;
  }

  @Test
  public void testRenameVariables() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getScopeRoot()).thenReturn(rootNode);

    renamer.enterScope(traversal);

    Node varNode1 = createVariableNode("a");
    Node varNode2 = createVariableNode("b");
    Node varNode3 = createVariableNode("b");
    Node varNode4 = createVariableNode("c");
    Node varNode5 = createVariableNode("d");
    Node varNode6 = createVariableNode("e");

    rootNode.setFirstChild(varNode1);
    rootNode.getFirstChild().setNext(varNode2);
    rootNode.getFirstChild().getNext().setNext(varNode3);
    rootNode.getFirstChild().getNext().getNext().setNext(varNode4);
    rootNode.getFirstChild().getNext().getNext().getNext().setNext(varNode5);
    rootNode.getFirstChild().getNext().getNext().getNext().getNext().setNext(varNode6);

    renamer.visit(traversal, rootNode, null);

    String expectedUniqueName1 = "a";
    String expectedUniqueName2 = "b_0";
    String expectedUniqueName3 = "b_1";
    String expectedUniqueName4 = "c";
    String expectedUniqueName5 = "d_0";
    String expectedUniqueName6 = "e_0";

    List<String> actualNames = new ArrayList<>();
    for (Node node = rootNode.getFirstChild(); node != null; node = node.getNext()) {
      if (NodeUtil.isVarDeclaration(node)) {
        actualNames.add(node.getString());
      }
    }

    assertThat(actualNames).containsExactly(expectedUniqueName1, expectedUniqueName2, expectedUniqueName3, expectedUniqueName4, expectedUniqueName5, expectedUniqueName6);
  }

  @Test
  public void testRenameFunction() {
    Node functionNode = createFunctionNode("myFunction");

    rootNode.setFirstChild(functionNode);

    renamer.enterScope(mock(NodeTraversal.class));

    renamer.visit(mock(NodeTraversal.class), rootNode, null);

    String expectedUniqueName = "myFunction_0";

    String actualName = functionNode.getString();

    assertThat(actualName).isEqualTo(expectedUniqueName);
  }

  @Test
  public void testRenameFunctionWithRecursiveCall() {
    Node functionNode1 = createFunctionNode("myFunction1");
    Node functionNode2 = createFunctionNode("myFunction2");

    functionNode1.getFirstChild().setNext(createNameNode("myFunction1"));
    functionNode2.getFirstChild().setNext(functionNode1);

    rootNode.setFirstChild(functionNode1);

    renamer.enterScope(mock(NodeTraversal.class));

    renamer.visit(mock(NodeTraversal.class), rootNode, null);

    String expectedUniqueName1 = "myFunction1_0";
    String expectedUniqueName2 = "myFunction1_1";

    List<String> actualNames = new ArrayList<>();
    for (Node node = functionNode1.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        actualNames.add(node.getString());
      }
    }

    assertThat(actualNames).containsExactly(expectedUniqueName1, expectedUniqueName2);
  }

  @Test
  public void testDoNotRenameGlobalVariables() {
    Node globalVarNode1 = createVariableNode("globalVar1");
    Node globalVarNode2 = createVariableNode("globalVar2");

    rootNode.setFirstChild(globalVarNode1);
    rootNode.getFirstChild().setNext(globalVarNode2);

    renamer.enterScope(mock(NodeTraversal.class));

    renamer.visit(mock(NodeTraversal.class), rootNode, null);

    String actualName1 = globalVarNode1.getString();
    String actualName2 = globalVarNode2.getString();

    assertThat(actualName1).isEqualTo("globalVar1");
    assertThat(actualName2).isEqualTo("globalVar2");
  }

  @Test
  public void testRenameCatchBlockVariable() {
    Node functionNode = createFunctionNode("myFunction");
    Node catchBlockNode = createCatchBlockNode("catchBlock");

    functionNode.getFirstChild().setNext(catchBlockNode);

    rootNode.setFirstChild(functionNode);

    renamer.enterScope(mock(NodeTraversal.class));

    renamer.visit(mock(NodeTraversal.class), rootNode, null);

    String expectedUniqueName = "catchBlock_0";

    String actualName = catchBlockNode.getFirstChild().getString();

    assertThat(actualName).isEqualTo(expectedUniqueName);
  }

  private Node createTestNode() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.BLOCK);
    when(node.getNext()).thenReturn(null);
    return node;
  }

  private Node createVariableNode(String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.VAR);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private Node createFunctionNode(String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getFirstChild()).thenReturn(createNameNode(name));
    return node;
  }

  private Node createNameNode(String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.NAME);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private Node createCatchBlockNode(String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.CATCH);
    Node catchVarNode = createVariableNode(name);
    when(node.getFirstChild()).thenReturn(catchVarNode);
    return node;
  }

  private static List<String> asList(Object... objects) {
    return Lists.newArrayList(objects);
  }
}