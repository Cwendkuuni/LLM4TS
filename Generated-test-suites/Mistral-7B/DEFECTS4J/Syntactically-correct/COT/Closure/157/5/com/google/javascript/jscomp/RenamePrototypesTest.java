package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.ErrorType;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenamePrototypesTest extends CompilerTestBase {

  private RenamePrototypes renamePrototypes;

  @Override
  protected CompilerPass getProcessor(CompilerImpl compiler) {
    return new RenamePrototypes(compiler, true, null, null);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
  }

  @Override
  protected CompilerOptions getDefaultOptions() {
    return defaultOptions().withOption("aggressiveRenaming", true)
        .withOption("charactersToSkipNames", "");
  }

  @org.junit.Test
  public void testRenamePrototypeProperties() throws Exception {
    String input = "function Foo() { this.bar = 1; this._bar = 2; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node fooPrototype = GraphTraversal.findFirstDescendantByType(root, Node.PROTOTYPE);

    Node barProperty = GraphTraversal.findFirstChildByType(fooPrototype, Node.GETPROPNAME);
    assertEquals("_bar", barProperty.getString());
    assertEquals("baz", getProperty(renamePrototypes, "bar").newName);
  }

  @org.junit.Test
  public void testDoNotRenameExportedProperties() throws Exception {
    String input = "function Foo() { exports.bar = 1; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node exports = GraphTraversal.findFirstDescendantByType(root, Node.EXPORT);
    Node barProperty = GraphTraversal.findFirstDescendantByType(exports, Node.ASSIGN);
    assertEquals("bar", barProperty.getFirstChild().getString());
  }

  @org.junit.Test
  public void testDoNotRenamePrivateProperties() throws Exception {
    String input = "function Foo() { var _bar = 1; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node _barProperty = GraphTraversal.findFirstDescendantByType(root, Node.ASSIGN);
    assertEquals("_bar", _barProperty.getString());
  }

  @org.junit.Test
  public void testDoNotRenameObjLitProperties() throws Exception {
    String input = "var obj = { bar: 1, baz: 2 };";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node obj = GraphTraversal.findFirstDescendantByType(root, Node.OBJECTLIT);
    Node barProperty = GraphTraversal.findFirstChildByType(obj, Node.ASSIGN);
    assertEquals("bar", barProperty.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() throws Exception {
    String input = "function Foo() { this.bar = 1; this._bar = 2; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node fooPrototype = GraphTraversal.findFirstDescendantByType(root, Node.PROTOTYPE);

    Node barProperty = GraphTraversal.findFirstChildByType(fooPrototype, Node.GETPROPNAME);
    assertNotNull(barProperty);

    // Verify that the property was renamed.
    assertNotNull(renamePrototypes.getPropertyMap());
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("baz", propertyMap.get("bar"));
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithReservedNames() throws Exception {
    String input = "function Foo() { this.indexOf = 1; this.toString = 2; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerOptions options = getDefaultOptions().withOption("charactersToSkipNames", "indexOf,toString");
    CompilerResult result = compileSingleFile(inputNode, RenamePrototypes.class, options);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node fooPrototype = GraphTraversal.findFirstDescendantByType(root, Node.PROTOTYPE);

    Node indexOfProperty = GraphTraversal.findFirstChildByType(fooPrototype, Node.GETPROPNAME);
    assertEquals("indexOf", indexOfProperty.getString());

    Node toStringProperty = GraphTraversal.findFirstChildByType(fooPrototype, Node.GETPROPNAME);
    assertEquals("toString", toStringProperty.getString());
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithCustomReservedNames() throws Exception {
    String input = "function Foo() { this.foo = 1; this._foo = 2; }";
    CompilerInput inputNode = createCompilerInput(input, "test.js");
    CompilerOptions options = getDefaultOptions().withOption("charactersToSkipNames", "foo");
    RenamePrototypes renamePrototypesWithCustomReservedNames = new RenamePrototypes(compiler, true, "foo".toCharArray(), null);
    CompilerResult result = compileSingleFile(inputNode, renamePrototypesWithCustomReservedNames);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, result.getLifeCycleStage());
    assertEquals(0, result.getErrorCount());

    Node root = result.getAst();
    Node fooPrototype = GraphTraversal.findFirstDescendantByType(root, Node.PROTOTYPE);

    Node fooProperty = GraphTraversal.findFirstChildByType(fooPrototype, Node.GETPROPNAME);
    assertNotNull(fooProperty);

    // Verify that the property was renamed.
    assertNotNull(renamePrototypesWithCustomReservedNames.getPropertyMap());
    Map<String, String> propertyMap = renamePrototypesWithCustomReservedNames.getPropertyMap();
    assertEquals("bar", propertyMap.get("foo"));
  }

  private NodeTraversal createTraverser(CompilerResult result) {
    return new NodeTraversal(compiler, result.getAst(), getErrorReporter());
  }

  private Property getProperty(RenamePrototypes renamePrototypes, String name) {
    return renamePrototypes.properties.get(name);
  }
}