package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testEnterScope() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());
  }

  @Test
  public void testExitScope() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 1);
    assertTrue(traversal.hasErrors());
    assertEquals(0, traversal.getScopeCalls().size());
  }

  @Test
  public void testShouldTraverse() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    Node node = root.getChildAtIndex(1);
    assertFalse(scopedAliases.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testEnterScopeWithScopeCall() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 2);
    assertFalse(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());
  }

  @Test
  public void testExitScopeWithScopeCall() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 3);
    assertTrue(traversal.hasErrors());
    assertEquals(0, traversal.getScopeCalls().size());
  }

  @Test
  public void testApplyAliases() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 2);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());

    // Apply aliases
    for (Node aliasDefinition : traversal.getAliasDefinitionsInOrder()) {
      aliasDefinition.applyAlias();
    }

    // Check aliases were applied
    Node node = root.getChildAtIndex(1).getChildAtIndex(1);
    assertEquals("goog.dom", node.getString());
  }

  @Test
  public void testRemoveAliasDefinition() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 2);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());

    // Remove alias definition
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    aliasDefinition.detachFromParent();

    // Check alias definition was removed
    assertNull(traversal.getAliasDefinitionsInOrder().get(0));
  }

  @Test
  public void testCollapseScopes() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 2);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());

    // Collapse scopes
    for (Node scopeCall : traversal.getScopeCalls()) {
      Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
      scopeClosureBlock.detachFromParent();
      Node expressionWithScopeCall = scopeCall.getParent();
      expressionWithScopeCall.getParent().replaceChild(
          expressionWithScopeCall,
          scopeClosureBlock);
      NodeUtil.tryMergeBlock(scopeClosureBlock);
    }

    // Check scopes were collapsed
    assertEquals(1, root.getChildCount());
    Node collapsedScope = root.getChildAtIndex(0);
    assertEquals(11, collapsedScope.getChildCount());
  }

  @Test
  public void testReportError() {
    Node root = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(null, root, traversal, 2);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("goog.scope", traversal.getScopeCalls().get(0).getFirstChild().getString());

    // Report error
    SourcePosition<AliasTransformation> pos = new SourcePosition<>();
    Node node = root.getChildAtIndex(1).getChildAtIndex(1);
    scopedAliases.report(traversal, node, GOOG_SCOPE_REFERENCES_THIS);

    // Check error was reported
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The body of a goog.scope function cannot reference 'this'.", traversal.getDiagnostics().get(0).getMessage());
  }

  private Node createTestNode() {
    Node root = new Node();
    root.setType(Token.SCRIPT);

    Node googScope = new Node();
    googScope.setType(Token.CALL);
    googScope.getFirstChild().setString("goog.scope");

    Node function = new Node();
    function.setType(Token.FUNCTION);
    function.setName("goog.scope");
    function.setSourcePosition(new SourcePosition<AliasTransformation>());

    Node params = new Node();
    params.setType(Token.LIST);

    Node anonFunc = new Node();
    anonFunc.setType(Token.FUNCTION);
    anonFunc.setName(null);
    anonFunc.setSourcePosition(new SourcePosition<AliasTransformation>());

    Node body = new Node();
    body.setType(Token.BLOCK);

    Node dom = new Node();
    dom.setType(Token.CALL);
    dom.getFirstChild().setString("goog.dom");

    Node createElement = new Node();
    createElement.setType(Token.CALL);
    createElement.getFirstChild().setString("createElement");

    Node div = new Node();
    div.setType(Token.STRING);
    div.setString("DIV");

    Node thisRef = new Node();
    thisRef.setType(Token.THIS);

    Node g = new Node();
    g.setType(Token.NAME);
    g.setString("g");

    Node domRef = new Node();
    domRef.setType(Token.NAME);
    domRef.setString("dom");

    Node aliasDefinition = new Node();
    aliasDefinition.setType(Token.ASSIGN);
    aliasDefinition.getLeft().setString("goog.dom");
    aliasDefinition.getRight().setString("dom");

    Node aliasUsage = new Node();
    aliasUsage.setType(Token.NAME);
    aliasUsage.setString("dom");

    Node aliasDefinition2 = new Node();
    aliasDefinition2.setType(Token.ASSIGN);
    aliasDefinition2.getLeft().setString("g");
    aliasDefinition2.getRight().setString("goog");

    Node aliasUsage2 = new Node();
    aliasUsage2.setType(Token.NAME);
    aliasUsage2.setString("g");

    function.getFirstChild().addChild(params);
    params.addChild(anonFunc);
    anonFunc.addChild(body);
    body.addChild(dom);
    body.addChild(createElement);
    createElement.addChild(aliasUsage);
    aliasUsage.addChild(thisRef);
    thisRef.addChild(g);
    g.addChild(aliasDefinition);
    aliasDefinition.addChild(domRef);
    domRef.addChild(div);

    function.addChild(aliasDefinition2);
    aliasDefinition2.addChild(g);
    aliasDefinition2.addChild(aliasUsage2);

    root.addChild(googScope);
    root.addChild(function);

    return root;
  }
}