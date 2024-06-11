package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Test cases for the MakeDeclaredNamesUnique class.
 */
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Scope scope = mock(Scope.class);
    when(nodeTraversal.getScope()).thenReturn(scope);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node rootNode = createNode("function foo() {}");
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    // No names were declared in this scope.
    assertNull(scope.getVar("foo"));

    // Test the name is not renamed if it is declared in a parent scope.
    Scope parentScope = mock(Scope.class);
    Var parentVar = new Var("foo", rootNode.getFirstChild(), null, null);
    parentScope.declare(parentVar.getName(), parentVar.nameNode, null, null);
    when(scope.getParentScope()).thenReturn(parentScope);

    Node childRootNode = createNode("function bar() {}");
    makeDeclaredNamesUnique.traverse(nodeTraversal, childRootNode, rootNode);

    // The name should not be renamed, as it was already declared in the parent scope.
    assertEquals("foo", scope.getVar("foo").getName());

    // Test that the name is renamed if it is declared in the current scope.
    Var var = new Var("foo", rootNode.getFirstChild(), null, null);
    scope.declare(var.getName(), var.nameNode, null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    String expectedName = "foo$$0";
    assertEquals(expectedName, scope.getVar("foo").getName());

    // Test that the name is renamed even if it is already declared with a different suffix.
    scope.declare(var.getName(), var.nameNode, null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    expectedName = "foo$$1";
    assertEquals(expectedName, scope.getVar("foo").getName());

    // Test that the name is not renamed if it is not declared in the current scope.
    Var var2 = new Var("bar", rootNode.getFirstChild().getNext(), null, null);
    scope.declare(var2.getName(), var2.nameNode, null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    assertEquals("bar", scope.getVar("bar").getName());

    // Test that the name is not renamed if it is already a constant.
    rootNode.getFirstChild().setProp(Node.IS_CONSTANT_NAME, true);
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    assertEquals("foo", scope.getVar("foo").getName());
  }

  @Test
  public void testInlineRenamer() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Scope scope = mock(Scope.class);
    when(nodeTraversal.getScope()).thenReturn(scope);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(
            () -> "prefix_" + System.currentTimeMillis(),
            "",
            true));

    Node rootNode = createNode("function foo() {}");
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    String expectedName = "fooprefix_";
    assertEquals(expectedName, scope.getVar("foo").getName());

    // Test that the name is not renamed if it is already declared in the parent scope.
    Scope parentScope = mock(Scope.class);
    Var parentVar = new Var("foo", rootNode.getFirstChild(), null, null);
    parentScope.declare(parentVar.getName(), parentVar.nameNode, null, null);
    when(scope.getParentScope()).thenReturn(parentScope);

    Node childRootNode = createNode("function bar() {}");
    makeDeclaredNamesUnique.traverse(nodeTraversal, childRootNode, rootNode);

    assertEquals("foo", scope.getVar("foo").getName());
    assertEquals("fooprefix_", scope.getVar("foo").getName());
  }

  @Test
  public void testContextualRenameInverter() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Scope scope = mock(Scope.class);
    when(nodeTraversal.getScope()).thenReturn(scope);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    ContextualRenameInverter contextualRenameInverter = new ContextualRenameInverter(makeDeclaredNamesUnique.compiler);

    Node rootNode = createNode("function foo() {}");
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    // No names were declared in this scope.
    assertNull(scope.getVar("foo"));

    // Test that the name is not inverted if it is not declared in the current scope.
    Scope parentScope = mock(Scope.class);
    Var parentVar = new Var("foo", rootNode.getFirstChild(), null, null);
    parentScope.declare(parentVar.getName(), parentVar.nameNode, null, null);
    when(scope.getParentScope()).thenReturn(parentScope);

    Node childRootNode = createNode("function bar() {}");
    contextualRenameInverter.process(null, rootNode);
    nodeTraversal.traverse(makeDeclaredNamesUnique.compiler, childRootNode, null);

    // The name should not be inverted, as it was not declared in the current scope.
    assertEquals("foo", scope.getVar("foo").getName());

    // Test that the name is inverted if it is declared in the current scope.
    Var var = new Var("foo", rootNode.getFirstChild(), null, null);
    scope.declare(var.getName(), var.nameNode, null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

    contextualRenameInverter.process(null, rootNode);
    nodeTraversal.traverse(makeDeclaredNamesUnique.compiler, rootNode);

    String expectedName = "foo";
    assertEquals(expectedName, scope.getVar("foo").getName());
  }

  private Node createNode(String javaScriptCode) {
    TokenStream tokenStream = new TokenStream(new StringReader(javaScriptCode));
    Node rootNode = new Node(Token.SCOPE, null);
    new NodeTraversal(RootTraversal.getDefaultVisitor(), null, null)
        .traverse(null, tokenStream, rootNode);
    return rootNode;
  }
}