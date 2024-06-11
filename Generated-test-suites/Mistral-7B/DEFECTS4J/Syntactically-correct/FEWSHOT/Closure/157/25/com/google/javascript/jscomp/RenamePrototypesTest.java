package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

/**
 * Unit tests for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    // Create test input
    TokenStream input = new TokenStream(new StringBuilder("var Foo = function() { this.bar = 1; }; Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.qux = function() {};"));
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node tree = compiler.parse(input, null);

    // Process the tree
    renamePrototypes.process(null, tree);

    // Verify the results
    assertEquals(5, compiler.getLifeCycleStage().getNumCodeUnits());
    assertEquals(3, compiler.getDebugInfo().length());

    // Verify that properties were renamed correctly
    Node getBarNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "getBar");
    assertNotNull(getBarNode);
    assertEquals("getBar_", getBarNode.getFirstChild().getString());

    Node quxNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "qux");
    assertNotNull(quxNode);
    assertEquals("qux_", quxNode.getFirstChild().getString());

    Node barNode = tree.findFirstChildByType(Token.ASSIGN, "Foo.prototype", "bar");
    assertNotNull(barNode);
    assertEquals("_c__", barNode.getLastChild().getString());

    Node getBarAssignedNode = tree.findFirstChildByType(Token.ASSIGN, "Foo.prototype", "_c__");
    assertNotNull(getBarAssignedNode);
    assertEquals("getBar_", getBarAssignedNode.getFirstChild().getString());

    Node quxAssignedNode = tree.findFirstChildByType(Token.ASSIGN, "Foo.prototype", "_d__");
    assertNotNull(quxAssignedNode);
    assertEquals("qux_", quxAssignedNode.getFirstChild().getString());
  }

  @Test
  public void testProcess_aggressiveRenaming() {
    // Create test input
    TokenStream input = new TokenStream(new StringBuilder("var Foo = function() { this.qux = 1; }; Foo.prototype.getBar = function() { return this.qux; }; Foo.prototype.qux = function() {};"));
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node tree = compiler.parse(input, null);

    // Process the tree
    renamePrototypes.process(null, tree);

    // Verify the results
    assertEquals(5, compiler.getLifeCycleStage().getNumCodeUnits());
    assertEquals(3, compiler.getDebugInfo().length());

    // Verify that properties were renamed correctly
    Node getBarNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "getBar");
    assertNotNull(getBarNode);
    assertEquals("getBar__", getBarNode.getFirstChild().getString());

    Node quxNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "qux");
    assertNotNull(quxNode);
    assertEquals("qux__", quxNode.getFirstChild().getString());

    Node quxAssignmentNode = tree.findFirstChildByType(Token.ASSIGN, "Foo.prototype", "qux");
    assertNotNull(quxAssignmentNode);
    assertEquals("qux__", quxAssignmentNode.getFirstChild().getString());

    Node getBarAssignmentNode = tree.findFirstChildByType(Token.ASSIGN, "Foo.prototype", "getBar");
    assertNotNull(getBarAssignmentNode);
    assertEquals("getBar__", getBarAssignmentNode.getFirstChild().getString());
  }

  @Test
  public void testProcess_noRenaming() {
    // Create test input
    TokenStream input = new TokenStream(new StringBuilder("var Foo = function() { this.indexOf = 1; }; Foo.prototype.toString = function() { return 'Foo'; }; Foo.prototype.valueOf = function() { return this; };"));
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node tree = compiler.parse(input, null);

    // Process the tree
    renamePrototypes.process(null, tree);

    // Verify the results
    assertEquals(5, compiler.getLifeCycleStage().getNumCodeUnits());
    assertEquals(3, compiler.getDebugInfo().length());

    // Verify that properties were not renamed
    Node toStringNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "toString");
    assertNotNull(toStringNode);
    assertEquals("toString", toStringNode.getFirstChild().getString());

    Node valueOfNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "valueOf");
    assertNotNull(valueOfNode);
    assertEquals("valueOf", valueOfNode.getFirstChild().getString());

    Node indexOfNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "indexOf");
    assertNotNull(indexOfNode);
    assertEquals("indexOf", indexOfNode.getFirstChild().getString());
  }

  @Test
  public void testProcess_noRenaming_aggressive() {
    // Create test input
    TokenStream input = new TokenStream(new StringBuilder("var Foo = function() { this.indexOf = 1; }; Foo.prototype.toString = function() { return 'Foo'; }; Foo.prototype.valueOf = function() { return this; };"));
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node tree = compiler.parse(input, null);

    // Process the tree
    renamePrototypes.process(null, tree);

    // Verify the results
    assertEquals(5, compiler.getLifeCycleStage().getNumCodeUnits());
    assertEquals(3, compiler.getDebugInfo().length());

    // Verify that properties were not renamed
    Node toStringNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "toString");
    assertNotNull(toStringNode);
    assertEquals("toString", toStringNode.getFirstChild().getString());

    Node valueOfNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "valueOf");
    assertNotNull(valueOfNode);
    assertEquals("valueOf", valueOfNode.getFirstChild().getString());

    Node indexOfNode = tree.findFirstChildByType(Token.GETPROP, "Foo.prototype", "indexOf");
    assertNotNull(indexOfNode);
    assertEquals("indexOf", indexOfNode.getFirstChild().getString());
  }
}