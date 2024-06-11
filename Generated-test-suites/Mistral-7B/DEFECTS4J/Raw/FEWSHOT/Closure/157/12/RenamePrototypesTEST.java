package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Unit tests for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "var x = {\nbaz: 42\n};\n" +
        "Foo.prototype = x;");

    RenamePrototypes renamer = new RenamePrototypes(
        null, /* compiler */
        true, /* aggressiveRenaming */
        null, /* reservedCharacters */
        null); /* prevUsedRenameMap */

    Node root = input.getRootNode();
    renamer.process(input.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(root);

    // Check that the prototype property "bar" was renamed.
    Node prototypeBar = traversal.findFirstDescendantOfType(root, Token.GETPROP);
    assertNotNull(prototypeBar);
    assertEquals("barRenamed", prototypeBar.getFirstChild().getString());

    // Check that the object literal property "baz" was not renamed.
    Node objLitBaz = traversal.findFirstDescendantOfType(root, Token.NUMBER);
    assertNull(objLitBaz.getNext());
    assertEquals("baz", objLitBaz.getString());

    // Check that the prototype was renamed.
    Node prototype = traversal.findFirstDescendantOfType(root, Token.GETPROP);
    assertNotNull(prototype);
    assertEquals("xRenamed", prototype.getString());

    // Check that the prototype's "bar" property was not renamed.
    Node prototypeBarAgain = traversal.findFirstDescendantOfType(prototype, Token.GETPROP);
    assertNotNull(prototypeBarAgain);
    assertEquals("bar", prototypeBarAgain.getFirstChild().getString());
  }

  @Test
  public void testProcessWithReservedNames() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.indexOf = function() {}\n" +
        "var x = {\nbaz: 42\n};\n" +
        "Foo.prototype = x;");

    RenamePrototypes renamer = new RenamePrototypes(
        null, /* compiler */
        true, /* aggressiveRenaming */
        null, /* reservedCharacters */
        null); /* prevUsedRenameMap */

    Node root = input.getRootNode();
    renamer.process(input.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(root);

    // Check that the prototype property "bar" was not renamed because it's a reserved name.
    Node prototypeBar = traversal.findFirstDescendantOfType(root, Token.GETPROP);
    assertNotNull(prototypeBar);
    assertEquals("bar", prototypeBar.getFirstChild().getString());

    // Check that the prototype property "indexOf" was not renamed because it's a reserved name.
    Node prototypeIndexOf = traversal.findFirstDescendantOfType(root, Token.GETPROP);
    assertNotNull(prototypeIndexOf);
    assertEquals("indexOf", prototypeIndexOf.getFirstChild().getString());
  }

  @Test
  public void testProcessWithAggressiveRenaming() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "var x = {\nbaz: 42\n};\n" +
        "Foo.prototype = x;");

    RenamePrototypes renamer = new RenamePrototypes(
        null, /* compiler */
        true, /* aggressiveRenaming */
        null, /* reservedCharacters */
        null); /* prevUsedRenameMap */

    Node root = input.getRootNode();
    renamer.process(input.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(root);

    // Check that the prototype property "bar" was renamed aggressively.
    Node prototypeBar = traversal.findFirstDescendantOfType(root, Token.GETPROP);
    assertNotNull(prototypeBar);
    assertEquals("renamedBar", prototypeBar.getFirstChild().getString());

    // Check that the object literal property "baz" was not renamed.
    Node objLitBaz = traversal.findFirstDescendantOfType(root, Token.NUMBER);
    assertNull(objLitBaz.getNext());
    assertEquals("baz", objLitBaz.getString());
  }

  @Test
  public void testProcessWithEmptyInput() {
    RenamePrototypes renamer = new RenamePrototypes(
        null, /* compiler */
        true, /* aggressiveRenaming */
        null, /* reservedCharacters */
        null); /* prevUsedRenameMap */

    ComplierInput input = new ComplierInput("");
    Node root = input.getRootNode();
    renamer.process(input.getExterns(), root);

    // Nothing to check here.
  }

  @Test
  public void testGetPropertyMap() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.qux = function() {}\n" +
        "var x = {\nbaz: 42\n};\n" +
        "Foo.prototype = x;");

    RenamePrototypes renamer = new RenamePrototypes(
        null, /* compiler */
        true, /* aggressiveRenaming */
        null, /* reservedCharacters */
        null); /* prevUsedRenameMap */

    Node root = input.getRootNode();
    renamer.process(input.getExterns(), root);

    VariableMap propertyMap = renamer.getPropertyMap();

    // Check that the map contains entries for the renamed prototype properties.
    assertEquals(2, propertyMap.size());
    assertEquals("bar", propertyMap.lookupName("renamedBar"));
    assertEquals("qux", propertyMap.lookupName("quxRenamed"));

    // Check that the map does not contain entries for the non-renamed object literal properties.
    assertNull(propertyMap.lookupName("baz"));
  }
}