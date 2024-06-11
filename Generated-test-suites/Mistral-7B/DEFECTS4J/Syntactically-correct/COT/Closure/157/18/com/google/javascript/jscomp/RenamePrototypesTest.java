package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerInput;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.ErrorReporter;
import com.google.javascript.jscomp.compiler.Node;
import com.google.javascript.jscomp.compiler.Token;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for RenamePrototypes.
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    

    // Given
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    ErrorReporter errorReporter = new ErrorReporter() {};
    CompilerPass renamePrototypes = new RenamePrototypes(options, true, null, null);

    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "var obj = {\n" +
                    "  prototype: Foo.prototype,\n" +
                    "  getFoo: function() {}\n" +
                    "}";

    Node root = new NodeTraversal(new NodeTraversal.RootVisitor() {
      public Node visit(NodeTraversal nt, Node node, Node parent) {
        return node;
      }
    }).traverse(new TokenStream(new TokenStream.Tokenizer(jsCode)), tokenizer -> tokenizer.nextToken())
        .getRootNode();

    CompilerInput input = new CompilerInput(new TokenStream(new TokenStream.Tokenizer(jsCode)), "", 0, 0);

    // When
    renamePrototypes.process(null, root);

    // Then
    Map<String, String> expectedRenamedNames = new HashMap<>();
    expectedRenamedNames.put("prototype", "prototype_1_");
    expectedRenamedNames.put("foo", "foo_1_");
    expectedRenamedNames.put("bar", "bar_1_");

    Map<String, String> actualRenamedNames = renamePrototypes.getPropertyMap().getValues();
    assertEquals("Unexpected number of renamed names", expectedRenamedNames.size(), actualRenamedNames.size());

    Function<String, String> getExpectedName = (String name) -> expectedRenamedNames.getOrDefault(name, name);
    Function<Node, String> getActualName = new Function<Node, String>() {
      @Override
      public String apply(Node node) {
        return node.getString();
      }
    };

    for (Map.Entry<String, Property> entry : renamePrototypes.properties.entrySet()) {
      Property property = entry.getValue();
      String actualName = getActualName.apply(property.oldName);
      String expectedName = getExpectedName.apply(actualName);
      assertEquals("Unexpected renamed name for " + actualName, expectedName, actualName);
    }

    
  }

  @Test
  public void testRenamePrototypesWithAggressiveRenamingOff() {
    

    // Given
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    ErrorReporter errorReporter = new ErrorReporter() {};
    CompilerPass renamePrototypes = new RenamePrototypes(options, false, null, null);

    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "var obj = {\n" +
                    "  prototype: Foo.prototype,\n" +
                    "  getFoo: function() {}\n" +
                    "}";

    Node root = new NodeTraversal(new NodeTraversal.RootVisitor() {
      public Node visit(NodeTraversal nt, Node node, Node parent) {
        return node;
      }
    }).traverse(new TokenStream(new TokenStream.Tokenizer(jsCode)), tokenizer -> tokenizer.nextToken())
        .getRootNode();

    CompilerInput input = new CompilerInput(new TokenStream(new TokenStream.Tokenizer(jsCode)), "", 0, 0);

    // When
    renamePrototypes.process(null, root);

    // Then
    Map<String, String> expectedRenamedNames = new HashMap<>();

    // Private and built-in names are not renamed
    expectedRenamedNames.put("prototype", "prototype");
    expectedRenamedNames.put("foo", "foo");
    expectedRenamedNames.put("bar", "bar");

    Map<String, String> actualRenamedNames = renamePrototypes.getPropertyMap().getValues();

    assertEquals("Unexpected number of renamed names", expectedRenamedNames.size(), actualRenamedNames.size());

    Function<String, String> getExpectedName = (String name) -> expectedRenamedNames.getOrDefault(name, name);
    Function<Node, String> getActualName = new Function<Node, String>() {
      @Override
      public String apply(Node node) {
        return node.getString();
      }
    };

    for (Map.Entry<String, Property> entry : renamePrototypes.properties.entrySet()) {
      Property property = entry.getValue();
      String actualName = getActualName.apply(property.oldName);
      String expectedName = getExpectedName.apply(actualName);
      assertEquals("Unexpected renamed name for " + actualName, expectedName, actualName);
    }

    
  }

  @Test
  public void testRenamePrototypesWithAggressiveRenamingOn() {
    

    // Given
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    ErrorReporter errorReporter = new ErrorReporter() {};
    CompilerPass renamePrototypes = new RenamePrototypes(options, true, null, null);

    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "var obj = {\n" +
                    "  prototype: Foo.prototype,\n" +
                    "  getFoo: function() {}\n" +
                    "}";

    Node root = new NodeTraversal(new NodeTraversal.RootVisitor() {
      public Node visit(NodeTraversal nt, Node node, Node parent) {
        return node;
      }
    }).traverse(new TokenStream(new TokenStream.Tokenizer(jsCode)), tokenizer -> tokenizer.nextToken())
        .getRootNode();

    CompilerInput input = new CompilerInput(new TokenStream(new TokenStream.Tokenizer(jsCode)), "", 0, 0);

    // When
    renamePrototypes.process(null, root);

    // Then
    Map<String, String> expectedRenamedNames = new HashMap<>();

    // Aggressive renaming renames all names except those starting with an underscore
    expectedRenamedNames.put("prototype", "prototype__");
    expectedRenamedNames.put("foo", "foo__");
    expectedRenamedNames.put("bar", "bar__");

    Map<String, String> actualRenamedNames = renamePrototypes.getPropertyMap().getValues();

    assertEquals("Unexpected number of renamed names", expectedRenamedNames.size(), actualRenamedNames.size());

    Function<String, String> getExpectedName = (String name) -> expectedRenamedNames.getOrDefault(name, name);
    Function<Node, String> getActualName = new Function<Node, String>() {
      @Override
      public String apply(Node node) {
        return node.getString();
      }
    };

    for (Map.Entry<String, Property> entry : renamePrototypes.properties.entrySet()) {
      Property property = entry.getValue();
      String actualName = getActualName.apply(property.oldName);
      String expectedName = getExpectedName.apply(actualName);
      assertEquals("Unexpected renamed name for " + actualName, expectedName, actualName);
    }

    
  }

  @Test
  public void testRenamePrototypesWithReservedCharacters() {
    

    // Given
    char[] reservedCharacters = {'@', '#'};
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    ErrorReporter errorReporter = new ErrorReporter() {};
    CompilerPass renamePrototypes = new RenamePrototypes(options, true, reservedCharacters, null);

    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.foo@ = function() {}\n" +
                    "Foo.prototype.bar# = function() {}\n" +
                    "var obj = {\n" +
                    "  prototype: Foo.prototype,\n" +
                    "  getFoo@: function() {}\n" +
                    "  getBar#: function() {}\n" +
                    "}";

    Node root = new NodeTraversal(new NodeTraversal.RootVisitor() {
      public Node visit(NodeTraversal nt, Node node, Node parent) {
        return node;
      }
    }).traverse(new TokenStream(new TokenStream.Tokenizer(jsCode)), tokenizer -> tokenizer.nextToken())
        .getRootNode();

    CompilerInput input = new CompilerInput(new TokenStream(new TokenStream.Tokenizer(jsCode)), "", 0, 0);

    // When
    renamePrototypes.process(null, root);

    // Then
    Map<String, String> expectedRenamedNames = new HashMap<>();

    // Names with reserved characters are not renamed
    expectedRenamedNames.put("prototype", "prototype");
    expectedRenamedNames.put("foo@", "foo@");
    expectedRenamedNames.put("bar#", "bar#");

    Map<String, String> actualRenamedNames = renamePrototypes.getPropertyMap().getValues();

    assertEquals("Unexpected number of renamed names", expectedRenamedNames.size(), actualRenamedNames.size());

    Function<String, String> getExpectedName = (String name) -> expectedRenamedNames.getOrDefault(name, name);
    Function<Node, String> getActualName = new Function<Node, String>() {
      @Override
      public String apply(Node node) {
        return node.getString();
      }
    };

    for (Map.Entry<String, Property> entry : renamePrototypes.properties.entrySet()) {
      Property property = entry.getValue();
      String actualName = getActualName.apply(property.oldName);
      String expectedName = getExpectedName.apply(actualName);
      assertEquals("Unexpected renamed name for " + actualName, expectedName, actualName);
    }

    
  }
}