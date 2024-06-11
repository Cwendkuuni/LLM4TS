package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CollapseVariableDeclarationsTest {

  private static AbstractCompiler mockCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getLifeCycleStage()).thenReturn(mock(CompilerPass.LifeCycleStage.class));
    return compiler;
  }

  private static Node mockNode(Token token, String value) {
    Node node = new Node(token);
    node.setString(value);
    return node;
  }

  @Test
  public void testCollapseVariableDeclarations() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);
    Var var1 = mock(Var.class);
    Var var2 = mock(Var.class);
    when(scope.getVar("a")).thenReturn(var1);
    when(scope.getVar("b")).thenReturn(var2);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node var2Node = mockNode(Token.VAR, "b");
    Node assign1Node = new Node(Token.ASSIGN);
    Node assign2Node = new Node(Token.ASSIGN);
    Node valueNode1 = mockNode(Token.NUMBER, "1");
    Node valueNode2 = mockNode(Token.NUMBER, "2");
    assign1Node.setFirstChild(var1Node);
    assign1Node.setLastChild(valueNode1);
    assign2Node.setFirstChild(var2Node);
    assign2Node.setLastChild(valueNode2);
    root.addChildToBack(assign1Node);
    root.addChildToBack(assign2Node);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);
    callback.visit(traverser, var2Node, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertNotNull(collapses.get(0));
    CollapseVariableDeclarations.Collapse collapse = collapses.get(0);
    assertNotNull(collapse.startNode);
    assertNotNull(collapse.endNode);
    assertNotNull(collapse.parent);
    assertEquals(var1Node, collapse.startNode);
    assertEquals(assign2Node, collapse.endNode);
    assertEquals(root, collapse.parent);
  }

  @Test
  public void testCollapseVariableAssignments() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node assign1Node = new Node(Token.ASSIGN);
    Node valueNode1 = mockNode(Token.FALSE, "");
    assign1Node.setFirstChild(var1Node);
    assign1Node.setLastChild(valueNode1);
    root.addChildToBack(assign1Node);

    Node var2Node = mockNode(Token.VAR, "b");
    Node assign2Node = new Node(Token.ASSIGN);
    Node valueNode2 = mockNode(Token.FALSE, "");
    assign2Node.setFirstChild(var2Node);
    assign2Node.setLastChild(valueNode2);
    root.addChildToBack(assign2Node);

    Node var3Node = mockNode(Token.VAR, "c");
    Node assign3Node = new Node(Token.ASSIGN);
    Node valueNode3 = mockNode(Token.FALSE, "");
    assign3Node.setFirstChild(var3Node);
    assign3Node.setLastChild(valueNode3);
    root.addChildToBack(assign3Node);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);
    callback.visit(traverser, var2Node, root);
    callback.visit(traverser, var3Node, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertNotNull(collapses.get(0));
    CollapseVariableDeclarations.Collapse collapse1 = collapses.get(0);
    assertNotNull(collapse1.startNode);
    assertNotNull(collapse1.endNode);
    assertNotNull(collapse1.parent);
    assertEquals(assign1Node, collapse1.startNode);
    assertEquals(assign3Node, collapse1.endNode);
    assertEquals(root, collapse1.parent);

    // Collapse variable declarations and assignments should not be merged
    assertNull(collapses.get(1).startNode.getFirstChild());
  }

  @Test
  public void testCollapseVariableDeclarationsWithJSDocs() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node var1JSDoc = new Node(Token.JSDOCINFO);
    JSDocInfo jsdocInfo1 = new JSDocInfo();
    jsdocInfo1.addAnnotation(new Node("@param {number} a"));
    var1JSDoc.setJSDocInfo(jsdocInfo1);
    var1Node.addChildToBack(var1JSDoc);
    root.addChildToBack(var1Node);

    Node var2Node = mockNode(Token.VAR, "b");
    Node var2JSDoc = new Node(Token.JSDOCINFO);
    JSDocInfo jsdocInfo2 = new JSDocInfo();
    jsdocInfo2.addAnnotation(new Node("@param {string} b"));
    var2JSDoc.setJSDocInfo(jsdocInfo2);
    var2Node.addChildToBack(var2JSDoc);
    root.addChildToBack(var2Node);

    Node assignNode = new Node(Token.ASSIGN);
    Node valueNode = mockNode(Token.NUMBER, "1");
    assignNode.setFirstChild(var1Node);
    assignNode.setLastChild(valueNode);
    root.addChildToBack(assignNode);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);
    callback.visit(traverser, var2Node, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertNotNull(collapses.get(0));
    CollapseVariableDeclarations.Collapse collapse1 = collapses.get(0);
    assertNotNull(collapse1.startNode);
    assertNotNull(collapse1.endNode);
    assertNotNull(collapse1.parent);
    assertEquals(assignNode, collapse1.startNode);
    assertEquals(var2Node, collapse1.endNode);
    assertEquals(root, collapse1.parent);

    // JSDocs should not be merged
    assertNotNull(collapse1.startNode.getFirstChild());
    assertNotNull(collapse1.startNode.getFirstChild().getFirstChild());
    assertEquals("@param {number} a", collapse1.startNode.getFirstChild().getFirstChild().getString());
  }

  @Test
  public void testCollapseVariableDeclarationsEmpty() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Node root = new Node(Token.PROGRAM);
    pass.process(null, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertTrue(collapses.isEmpty());
  }

  @Test
  public void testCollapseVariableDeclarationsWithDuplicates() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node var1NodeDuplicate = mockNode(Token.VAR, "a");
    Node assign1Node = new Node(Token.ASSIGN);
    Node valueNode1 = mockNode(Token.FALSE, "");
    assign1Node.setFirstChild(var1Node);
    assign1Node.setLastChild(valueNode1);
    root.addChildToBack(assign1Node);
    root.addChildToBack(var1NodeDuplicate);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);
    callback.visit(traverser, var1NodeDuplicate, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertNotNull(collapses.get(0));
    CollapseVariableDeclarations.Collapse collapse1 = collapses.get(0);
    assertNotNull(collapse1.startNode);
    assertNotNull(collapse1.endNode);
    assertNotNull(collapse1.parent);
    assertEquals(assign1Node, collapse1.startNode);
    assertEquals(var1NodeDuplicate, collapse1.endNode);
    assertEquals(root, collapse1.parent);

    // Collapsed variable node should have unique name
    assertEquals("a", collapse1.startNode.getString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithVariablesInsideCondition() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node conditionNode = new Node(Token.IF);
    conditionNode.setFirstChild(new Node(Token.EQ, var1Node, new Node(Token.NUMBER, "1")));
    Node assignNode = new Node(Token.ASSIGN);
    Node valueNode = mockNode(Token.NUMBER, "1");
    assignNode.setFirstChild(var1Node);
    assignNode.setLastChild(valueNode);
    root.addChildToBack(conditionNode);
    root.addChildToBack(assignNode);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertTrue(collapses.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithVariablesInsideLoop() {
    AbstractCompiler compiler = mockCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    Scope scope = mock(Scope.class);

    Node root = new Node(Token.PROGRAM);
    Node var1Node = mockNode(Token.VAR, "a");
    Node forNode = new Node(Token.FOR);
    forNode.setFirstChild(var1Node);
    Node assignNode = new Node(Token.ASSIGN);
    Node valueNode = mockNode(Token.NUMBER, "1");
    assignNode.setFirstChild(var1Node);
    assignNode.setLastChild(valueNode);
    root.addChildToBack(forNode);
    root.addChildToBack(assignNode);

    NodeTraversal traverser = spy(new NodeTraversal(compiler, root, null, null, null));
    pass.process(null, root);

    ArgumentCaptor<CollapseVariableDeclarations.Collapse> collapseArgumentCaptor = ArgumentCaptor.forClass(CollapseVariableDeclarations.Collapse.class);
    AbstractPostOrderCallback callback = traverser.getCallback();
    callback.visit(traverser, var1Node, root);

    List<CollapseVariableDeclarations.Collapse> collapses = pass.collapses;
    assertNotNull(collapses);
    assertTrue(collapses.isEmpty());
  }
}