package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for RenamePrototypes.
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    // Given
    TokenStream input = new TokenStream(new StringReader("var Foo = {\n" +
        "  bar: function() {},\n" +
        "  prototype: {\n" +
        "    getBaz: function() {}\n" +
        "  },\n" +
        "  baz: 42\n" +
        "};\n"));
    AbstractCompiler compiler = new AbstractCompiler(new SourceFile("Foo.js"));
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    Node externs = Node.root();
    Node root = Node.root();
    renamePrototypes.process(externs, root);

    // Then
    assertNotNull(renamePrototypes.getPropertyMap());

    // Check that properties were renamed if they don't have leading or trailing
    // underscores and don't contain reserved characters
    Map<String, String> map = renamePrototypes.getPropertyMap();
    assertEquals("getBaz", map.get("getBaz"));
    assertEquals("prototype_getBaz", map.get("getBaz")); // leading underscore
    assertNull(map.get("indexOf")); // reserved name
    assertNull(map.get("prototype.getBaz")); // externed name
    assertNull(map.get("prototype.prototype.getBaz")); // nested externed name
    assertEquals("bar", map.get("bar"));
    assertEquals("prototype.bar", map.get("prototype.bar")); // leading underscore in prototype name
    assertNull(map.get("prototype_bar")); // leading underscore in property name
    assertNull(map.get("prototype.prototype_bar")); // leading underscores in both
    assertEquals("baz", map.get("baz"));
    assertNull(map.get("prototype.baz")); // leading underscore in prototype name
    assertEquals("prototype_baz", map.get("prototype.baz")); // leading underscore in property name
    assertNull(map.get("prototype.prototype_baz")); // leading underscores in both

    // Check that the generated names don't contain reserved characters
    char[] reservedCharacters = renamePrototypes.reservedCharacters;
    StringBuilder debug = new StringBuilder();
    for (Property a : renamePrototypes.properties.values()) {
      if (a.newName != null) {
        for (char c : a.newName.toCharArray()) {
          assertFalse("Generated name contains reserved character: " + c,
                      Arrays.binarySearch(reservedCharacters, c) >= 0);
        }
      }
    }
  }

  @Test
  public void testAggressiveRenaming() {
    // Given
    TokenStream input = new TokenStream(new StringReader("var Foo = {\n" +
        "  bar: function() {},\n" +
        "  prototype: {\n" +
        "    getBaz: function() {}\n" +
        "  },\n" +
        "  baz: 42\n" +
        "};\n"));
    AbstractCompiler compiler = new AbstractCompiler(new SourceFile("Foo.js"));
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node externs = Node.root();
    Node root = Node.root();
    renamePrototypes.process(externs, root);

    // Then
    Map<String, String> map = renamePrototypes.getPropertyMap();
    assertEquals("getBaz", map.get("getBaz"));
    assertEquals("prototype_getBaz", map.get("getBaz")); // leading underscore
    assertNull(map.get("indexOf")); // reserved name
    assertNull(map.get("prototype.getBaz")); // externed name
    assertNull(map.get("prototype.prototype.getBaz")); // nested externed name
    assertEquals("bar", map.get("bar"));
    assertEquals("prototype_bar", map.get("prototype.bar")); // leading underscore in prototype name
    assertNull(map.get("prototype_bar")); // leading underscore in property name
    assertNull(map.get("prototype.prototype_bar")); // leading underscores in both
    assertEquals("baz", map.get("baz"));
    assertEquals("prototype_baz", map.get("prototype.baz")); // leading underscore in prototype name
    assertNull(map.get("prototype.prototype_baz")); // leading underscores in both

    // But the behavior is different for aggressive renaming
    RenamePrototypes aggressiveRenamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node externsAggressive = Node.root();
    Node rootAggressive = Node.root();
    aggressiveRenamePrototypes.process(externsAggressive, rootAggressive);

    // Then
    Map<String, String> mapAggressive = aggressiveRenamePrototypes.getPropertyMap();
    assertEquals("getBaz", mapAggressive.get("getBaz"));
    assertEquals("prototype_getBaz", mapAggressive.get("getBaz")); // leading underscore
    assertNull(mapAggressive.get("indexOf")); // reserved name
    assertNull(mapAggressive.get("prototype.getBaz")); // externed name
    assertNull(mapAggressive.get("prototype.prototype.getBaz")); // nested externed name
    assertEquals("bar", mapAggressive.get("bar"));
    assertEquals("prototype_bar", mapAggressive.get("prototype.bar")); // leading underscore in prototype name
    assertNull(mapAggressive.get("prototype_bar")); // leading underscore in property name
    assertNull(mapAggressive.get("prototype.prototype_bar")); // leading underscores in both
    assertEquals("baz", mapAggressive.get("baz"));
    assertEquals("prototype_baz", mapAggressive.get("prototype.baz")); // leading underscore in prototype name
    assertEquals("prototype_prototype_baz", mapAggressive.get("prototype.prototype.baz")); // leading underscores in both
  }
}