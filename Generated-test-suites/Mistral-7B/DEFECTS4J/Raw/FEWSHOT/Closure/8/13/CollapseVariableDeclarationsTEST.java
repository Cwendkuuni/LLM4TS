package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR, "a");
    Node var2 = new Node(Token.VAR, "b");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);
    Node assign3 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign4 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);

    root.addChildToBack(assign1);
    root.addChildToBack(assign2);
    root.addChildToBack(assign3);
    root.addChildToBack(assign4);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a, b");
    Node expectedAssign = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, expected);

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expectedAssign.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithJSDocs() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR, "a", createJSDocInfo("a"));
    Node var2 = new Node(Token.VAR, "b", createJSDocInfo("b"));
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);
    Node assign3 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign4 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);

    root.addChildToBack(assign1);
    root.addChildToBack(assign2);
    root.addChildToBack(assign3);
    root.addChildToBack(assign4);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a, b", createJSDocInfo("a, b"));
    Node expectedAssign = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, expected);

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expectedAssign.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableAssignments() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR, "a");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);

    root.addChildToBack(assign1);
    root.addChildToBack(assign2);

    pass.process(null, root);

    Node expected = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testDontCollapseVariableDeclarationsInConditional() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node ifNode = new Node(Token.IF, null, null);
    Node var1 = new Node(Token.VAR, "a");
    Node var2 = new Node(Token.VAR, "b");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);

    root.addChildToBack(ifNode);
    ifNode.addChildToBack(new Node(Token.VAR, "c"));
    ifNode.addChildToBack(new Node(Token.IF, null, null));
    ifNode.addChildToBack(new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1));
    ifNode.addChildToBack(new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2));

    pass.process(null, root);

    Node expected = new Node(Token.BLOCK, null, null);
    Node ifNodeExpected = new Node(Token.IF, null, null);
    Node var1Expected = new Node(Token.VAR, "a");
    Node var2Expected = new Node(Token.VAR, "b");
    Node assign1Expected = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1Expected);
    Node assign2Expected = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2Expected);

    root.accept(new Traverse());

    Node actual = root;
    Node ifNodeActual = null;

    while (actual != null) {
      if (actual.isIf()) {
        ifNodeActual = actual;
        break;
      }
      actual = actual.getNext();
    }

    assertEquals(ifNodeExpected.toString(), ifNodeActual.toString());
    assertEquals(3, ifNodeActual.getChildCount());
    assertEquals(var1Expected.toString(), ifNodeActual.getChild(0).toString());
    assertEquals(assign1Expected.toString(), ifNodeActual.getChild(1).toString());
    assertEquals(var2Expected.toString(), ifNodeActual.getChild(2).toString());
  }

  @Test
  public void testCollapseVariableDeclarationsInLoops() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node forNode = new Node(Token.FOR, null, null);
    Node var1 = new Node(Token.VAR, "i");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node statement1 = new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.NUM, 0));

    root.addChildToBack(forNode);
    forNode.addChildToBack(var1);
    forNode.addChildToBack(assign1);
    forNode.addChildToBack(statement1);

    Node var2 = new Node(Token.VAR, "j");
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);
    Node statement2 = new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.NUM, 0));

    forNode.addChildToBack(var2);
    forNode.addChildToBack(assign2);
    forNode.addChildToBack(statement2);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "i, j");
    Node expectedAssign = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, expected);

    root.accept(new Traverse());

    Node actual = root;
    Node forNodeActual = null;

    while (actual != null) {
      if (actual.isFor()) {
        forNodeActual = actual;
        break;
      }
      actual = actual.getNext();
    }

    assertEquals(expectedAssign.toString(), forNodeActual.getChild(1).toString());
  }

  @Test
  public void testCollapseVariableDeclarationsInFunctions() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.FUNCTION, null, null);
    Node var1 = new Node(Token.VAR, "a");
    Node var2 = new Node(Token.VAR, "b");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var2);

    root.addChildToBack(var1);
    root.addChildToBack(var2);
    root.addChildToBack(new Node(Token.RETURN, null, null));
    root.addChildToBack(assign1);
    root.addChildToBack(assign2);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a, b");
    Node expectedAssign = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, expected);

    root.accept(new Traverse());

    Node actual = root;
    Node assignActual = null;

    while (actual != null) {
      if (actual.isAssign()) {
        assignActual = actual;
        break;
      }
      actual = actual.getNext();
    }

    assertEquals(expectedAssign.toString(), assignActual.toString());
  }

  @Test
  public void testDontCollapseDuplicateVariableDeclarations() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR, "a");
    Node var2 = new Node(Token.VAR, "a");

    root.addChildToBack(var1);
    root.addChildToBack(var2);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a");

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsInConditionalElsePart() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node ifNode = new Node(Token.IF, null, null);
    Node var1 = new Node(Token.VAR, "a");
    Node assign1 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node assign2 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);
    Node elseNode = new Node(Token.ELSE);
    Node assign3 = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, var1);

    root.addChildToBack(ifNode);
    ifNode.addChildToBack(var1);
    ifNode.addChildToBack(assign1);
    ifNode.addChildToBack(assign2);
    ifNode.addChildToBack(new Node(Token.RETURN, null, null));
    ifNode.addChildToBack(elseNode);
    elseNode.addChildToBack(assign3);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a");
    Node expectedAssign = new Node(Token.ASSIGN, Token.ASSIGN_ASSIGN, expected);

    root.accept(new Traverse());

    Node actual = root;
    Node ifNodeActual = null;
    Node assignActual = null;

    while (actual != null) {
      if (actual.isIf()) {
        ifNodeActual = actual;
        break;
      }
      actual = actual.getNext();
    }

    assertNotNull(ifNodeActual);
    assertEquals(1, ifNodeActual.getChildCount());
    assertEquals(expectedAssign.toString(), ifNodeActual.getChild(0).toString());

    actual = root;
    while (actual != null && !actual.isElse()) {
      actual = actual.getNext();
    }

    assertNotNull(actual);
    assertEquals(assign3.toString(), actual.toString());
  }

  @Test
  public void testCollapseEmptyVariableDeclarations() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.BLOCK);
    Node var1 = new Node(Token.VAR, "");
    Node var2 = new Node(Token.VAR, "");

    root.addChildToBack(var1);
    root.addChildToBack(var2);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "");

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected.toString(), actual.toString());
  }

  private JSDocInfo createJSDocInfo(String name) {
    JSDocInfo info = new JSDocInfo();
    info.put("name", name);
    return info;
  }

  private static class Traverse implements NodeVisitor {
    @Override
    public void visit(Node n) {
      n.acceptChildren(this);
    }
  }
}