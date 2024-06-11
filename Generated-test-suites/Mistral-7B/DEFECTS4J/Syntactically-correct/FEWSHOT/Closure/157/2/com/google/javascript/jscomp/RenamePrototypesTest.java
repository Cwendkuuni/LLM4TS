package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Test cases for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  @Test
  public void testPropertyRenaming() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When processing a code snippet
    Node root = createTestNode();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should have occurred
    assertEquals("prop_Renamed", root.getFirstChild().getLastChild().getString());
    assertEquals("prop_Renamed", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingWithAggressiveFalse() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    // When processing a code snippet
    Node root = createTestNode();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should not have occurred
    assertEquals("prop", root.getFirstChild().getLastChild().getString());
    assertEquals("prop", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingHeuristic() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When processing a code snippet
    Node root = createTestNodeWithHeuristicProperty();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should have occurred
    assertEquals("propWithNumberHeuristic_Renamed", root.getFirstChild().getLastChild().getString());
    assertEquals("propWithNumberHeuristic_Renamed", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingWithExternedProperty() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When processing a code snippet
    Node root = createTestNodeWithExternedProperty();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should not have occurred
    assertEquals("externedProp", root.getFirstChild().getLastChild().getString());
    assertEquals("externedProp", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingWithReservedCharacter() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    char reservedCharacter = '!';
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, new char[] { reservedCharacter }, null);

    // When processing a code snippet
    Node root = createTestNodeWithReservedCharacter();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should have occurred, but with a modified name
    assertEquals("propWithReservedCharacter_Renamed", root.getFirstChild().getLastChild().getString());
    assertEquals("propWithReservedCharacter_Renamed", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingWithPreviousUsedRenameMap() {
    // Given a compiler with a specific configuration and previous used rename map
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes prevRenamePrototypes = new RenamePrototypes(compiler, true, null, new VariableMap(Map.of("prop", "prop_Renamed")));

    // When setting up the current renaming processor
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevRenamePrototypes.getPropertyMap());

    // When processing a code snippet
    Node root = createTestNode();
    Node externs = createTestNode();
    renamePrototypes.process(externs, root);

    // Then the property renaming should not have occurred, since the name is already renamed
    assertEquals("prop_Renamed", root.getFirstChild().getLastChild().getString());
    assertEquals("prop_Renamed", root.getLastChild().getLastChild().getString());
  }

  @Test
  public void testPropertyRenamingWithEmptyInput() {
    // Given a compiler with a specific configuration
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When processing an empty input
    Node root = createEmptyNode();
    Node externs = createEmptyNode();
    renamePrototypes.process(externs, root);

    // Then no changes should have occurred
    assertEquals(0, root.getChildCount());
    assertEquals(0, externs.getChildCount());
  }

  private Node createTestNode() {
    TokenStream ts = new TokenStream();
    ts.putNext(new Token(Token.ASSIGN, 0, 0));
    ts.putNext(new Token(Token.STRING, "prop", 0, 0));
    ts.putNext(new Token(Token.GETPROP, 0, 0));
    ts.putNext(new Token(Token.STRING, "obj", 0, 0));
    ts.putNext(new Token(Token.DOT, 0, 0));
    ts.putNext(new Token(Token.FUNCTION, 0, 0));
    ts.putNext(new Token(Token.LEFTBRACE, 0, 0));
    ts.putNext(new Token(Token.RETURN, 0, 0));
    ts.putNext(new Token(Token.STRING, "result", 0, 0));
    ts.putNext(new Token(Token.RIGHTBRACE, 0, 0));
    return CompilerOptions.createCompilerInput("sourceFile", ts.makeTree()).getRootNode();
  }

  private Node createTestNodeWithHeuristicProperty() {
    TokenStream ts = new TokenStream();
    ts.putNext(new Token(Token.ASSIGN, 0, 0));
    ts.putNext(new Token(Token.STRING, "propWithNumberHeuristic", 0, 0));
    ts.putNext(new Token(Token.GETPROP, 0, 0));
    ts.putNext(new Token(Token.STRING, "obj", 0, 0));
    ts.putNext(new Token(Token.DOT, 0, 0));
    ts.putNext(new Token(Token.FUNCTION, 0, 0));
    ts.putNext(new Token(Token.LEFTBRACE, 0, 0));
    ts.putNext(new Token(Token.RETURN, 0, 0));
    ts.putNext(new Token(Token.STRING, "result", 0, 0));
    ts.putNext(new Token(Token.RIGHTBRACE, 0, 0));
    return CompilerOptions.createCompilerInput("sourceFile", ts.makeTree()).getRootNode();
  }

  private Node createTestNodeWithExternedProperty() {
    TokenStream ts = new TokenStream();
    ts.putNext(new Token(Token.ASSIGN, 0, 0));
    ts.putNext(new Token(Token.STRING, "externedProp", 0, 0));
    ts.putNext(new Token(Token.GETPROP, 0, 0));
    ts.putNext(new Token(Token.STRING, "obj", 0, 0));
    ts.putNext(new Token(Token.DOT, 0, 0));
    ts.putNext(new Token(Token.FUNCTION, 0, 0));
    ts.putNext(new Token(Token.LEFTBRACE, 0, 0));
    ts.putNext(new Token(Token.RETURN, 0, 0));
    ts.putNext(new Token(Token.STRING, "result", 0, 0));
    ts.putNext(new Token(Token.EXTERN, 0, 0));
    ts.putNext(new Token(Token.RIGHTBRACE, 0, 0));
    return CompilerOptions.createCompilerInput("sourceFile", ts.makeTree()).getRootNode();
  }

  private Node createTestNodeWithReservedCharacter() {
    TokenStream ts = new TokenStream();
    ts.putNext(new Token(Token.ASSIGN, 0, 0));
    ts.putNext(new Token(Token.STRING, "propWithReservedCharacter", 0, 0));
    ts.putNext(new Token(Token.GETPROP, 0, 0));
    ts.putNext(new Token(Token.STRING, "obj", 0, 0));
    ts.putNext(new Token(Token.DOT, 0, 0));
    ts.putNext(new Token(Token.FUNCTION, 0, 0));
    ts.putNext(new Token(Token.LEFTBRACE, 0, 0));
    ts.putNext(new Token(Token.RETURN, 0, 0));
    ts.putNext(new Token(Token.STRING, "result", 0, 0));
    ts.putNext(new Token(Token.RIGHTBRACE, 0, 0));
    ts.putNext(new Token(Token.STRING, "\u0021propWithReservedCharacter", 0, 0)); // '!'
    return CompilerOptions.createCompilerInput("sourceFile", ts.makeTree()).getRootNode();
  }

  private Node createEmptyNode() {
    return null;
  }
}