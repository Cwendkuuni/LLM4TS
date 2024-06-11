package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations
 */
@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");
    Node varC = new Node(Token.VAR, "c");

    Node assignAB = new Node(Token.ASSIGN, varB, new Node(Token.NUM, 1));
    Node assignAC = new Node(Token.ASSIGN, varC, new Node(Token.NUM, 2));

    Node ifStatement = new Node(Token.IF, null, null);
    Node assignAI = new Node(Token.ASSIGN, varA, new Node(Token.NUM, 3));

    Node varD = new Node(Token.VAR, "d");
    Node assignAD = new Node(Token.ASSIGN, varD, new Node(Token.NUM, 4));

    root.addChildToBack(varA);
    root.addChildToBack(assignAB);
    root.addChildToBack(varB);
    root.addChildToBack(varC);
    root.addChildToBack(assignAC);
    root.addChildToBack(ifStatement);
    root.addChildToBack(varD);
    root.addChildToBack(assignAD);

    Scope scope = new Scope(null);

    NodeTraversal nodeTraversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST, null);
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(new Compiler() {});
    collapseVariableDeclarations.process(null, root);

    Node collapsedVarA = null;
    Node collapsedVarB = null;
    Node collapsedVarC = null;

    for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
      if (node.isVar() && node.getString().equals("a")) {
        collapsedVarA = node;
      }
      if (node.isVar() && node.getString().equals("b")) {
        collapsedVarB = node;
      }
      if (node.isVar() && node.getString().equals("c")) {
        collapsedVarC = node;
      }
    }

    assertEquals(3, collapsedVarA.getNumChildren());
    assertEquals(1, collapsedVarB.getNumChildren());
    assertEquals(1, collapsedVarC.getNumChildren());

    assertEquals("a, b = 1, c = 2", collapsedVarA.getSource());
    assertEquals("b = 1, c = 2", collapsedVarB.getSource());
    assertEquals("c = 2", collapsedVarC.getSource());

    Node collapsibleVarD = null;
    for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
      if (node.isVar() && node.getString().equals("d")) {
        collapsibleVarD = node;
      }
    }

    assertNull(collapsibleVarD);
  }

  @Test
  public void testCollapseAssignments() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");

    Node assignAA = new Node(Token.ASSIGN, varA, new Node(Token.NUM, 1));
    Node assignAB = new Node(Token.ASSIGN, varB, varA);

    root.addChildToBack(varA);
    root.addChildToBack(assignAA);
    root.addChildToBack(varB);
    root.addChildToBack(assignAB);

    Scope scope = new Scope(null);

    NodeTraversal nodeTraversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST, null);
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(new Compiler() {});
    collapseVariableDeclarations.process(null, root);

    Node collapsedVarA = null;
    Node collapsedVarB = null;

    for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
      if (node.isVar() && node.getString().equals("a")) {
        collapsedVarA = node;
      }
      if (node.isVar() && node.getString().equals("b")) {
        collapsedVarB = node;
      }
    }

    assertEquals(2, collapsedVarA.getNumChildren());
    assertEquals(0, collapsedVarB.getNumChildren());

    assertEquals("a = 1, b = a", collapsedVarA.getSource());
    assertEquals("b = a", collapsedVarB.getSource());
  }

  @Test
  public void testCollapseVariableDeclarationsInsideIfStatement() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node ifStatement = new Node(Token.IF, null, null);
    Node varB = new Node(Token.VAR, "b");

    root.addChildToBack(varA);

    Node ifCondition = new Node(Token.NUM, 0);
    ifStatement.addChildToBack(ifCondition);
    root.addChildToBack(ifStatement);

    root.addChildToBack(varB);

    Scope scope = new Scope(null);

    NodeTraversal nodeTraversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST, null);
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(new Compiler() {});
    collapseVariableDeclarations.process(null, root);

    Node collapsedVarA = null;
    Node collapsedVarB = null;

    for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
      if (node.isVar() && node.getString().equals("a")) {
        collapsedVarA = node;
      }
      if (node.isVar() && node.getString().equals("b")) {
        collapsedVarB = node;
      }
    }

    assertEquals(1, collapsedVarA.getNumChildren());
    assertEquals(0, collapsedVarB.getNumChildren());

    assertEquals("a; if (0) { } a = b", collapsedVarA.getSource());
    assertEquals("b", collapsedVarB.getSource());
  }
}