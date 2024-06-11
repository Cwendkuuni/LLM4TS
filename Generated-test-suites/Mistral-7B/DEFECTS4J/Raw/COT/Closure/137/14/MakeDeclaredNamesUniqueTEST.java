package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MakeDeclaredNamesUniqueTest {

  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private Renamer rootRenamer;

  @Mock
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    rootRenamer = makeDeclaredNamesUnique.rootRenamer;
  }

  @Test
  public void testEnterScope() {
    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    verify(nodeTraversal).enterScope(any());
  }

  @Test
  public void testExitScope() {
    makeDeclaredNamesUnique.exitScope(nodeTraversal);
    verify(nodeTraversal).exitScope(any());
  }

  @Test
  public void testShouldTraverseFunction() {
    makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, null, null);
    // Add your assertions for this test case here.
  }

  @Test
  public void testVisitName() {
    String oldName = "testVariable";
    String newName = "testVariableRenamed";
    Node node = new Node(Token.NAME, oldName);

    makeDeclaredNamesUnique.visit(nodeTraversal, node, null);

    verify(nodeTraversal).visit(any(), any(), any());
    verify(node).setString(newName);
  }

  @Test
  public void testVisitFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node fnParams = new Node(Token.LIST, Arrays.asList(
        new Node(Token.NAME, "param1"),
        new Node(Token.NAME, "param2")
    ));
    functionNode.addChild(fnParams);

    makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);

    for (int i = 0; i < functionNode.getChildCount(); i++) {
      Node child = functionNode.getChild(i);
      if (child.getType() == Token.NAME) {
        String oldName = child.getString();
        String expectedNewName = null;
        if (i == 0) { // param1
          expectedNewName = "param1Renamed";
        } else if (i == 1) { // param2
          expectedNewName = "param2Renamed";
        }
        verify(nodeTraversal).visit(any(), child, any());
        verify(child).setString(expectedNewName);
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testVisitFunctionWithRecursiveName() {
    Node functionNode = new Node(Token.FUNCTION);
    Node fnParams = new Node(Token.LIST, Arrays.asList(
        new Node(Token.NAME, "testVariable") // recursive name
    ));
    functionNode.addChild(fnParams);

    makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
  }

  @Test
  public void testVisitCatch() {
    Node catchNode = new Node(Token.CATCH);
    Node exceptionNameNode = new Node(Token.NAME, "exceptionName");
    catchNode.addChild(exceptionNameNode);

    makeDeclaredNamesUnique.visit(nodeTraversal, catchNode, null);

    verify(nodeTraversal).visit(any(), any(), any());
    verify(exceptionNameNode).setString("exceptionNameRenamed");
  }

  @Test
  public void testGetReplacementName() {
    String oldName = "testVariable";
    String newName = "testVariableRenamed";

    String replacementName = rootRenamer.getReplacementName(oldName);

    assert (replacementName != null);
    assert (replacementName.equals(newName));
  }
}