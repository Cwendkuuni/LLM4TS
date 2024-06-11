package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Type;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

public class RenamePrototypesTest {

  Compiler compiler;
  RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionsWithName(true);
    compiler = new Compiler(options);
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
  }

  @Test
  public void testRenamePrototypeProperty() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node barNode = findNodeByName(prototype, "bar");
    assertNotNull(barNode);
    String newName = renamePrototypes.getPropertyMap().lookupNewName("bar");
    assertEquals(newName, barNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    String input = "_foo_prototype._bar = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "foo");
    Node barNode = findNodeByName(prototype, "_bar");
    assertNotNull(barNode);
    assertEquals("_bar", barNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithTrailingUnderscore() {
    String input = "function Foo() {} Foo.prototype.bar_ = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node barNode = findNodeByName(prototype, "bar");
    assertNotNull(barNode);
    assertEquals("bar", barNode.getName());
  }

  @Test
  public void testRenameObjLitProperty() {
    String input = "var obj = { bar: 1 };";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node objNode = findNodeByName(root, "obj");
    Node propertyNode = findNodeByName(objNode, "bar");
    assertNotNull(propertyNode);
    String newName = renamePrototypes.getPropertyMap().lookupNewName("bar");
    assertEquals(newName, propertyNode.getName());
  }

  @Test
  public void testDontRenameObjLitPropertyWithLeadingUnderscore() {
    String input = "var obj = { _bar: 1 };";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node objNode = findNodeByName(root, "obj");
    Node propertyNode = findNodeByName(objNode, "_bar");
    assertNotNull(propertyNode);
    assertEquals("_bar", propertyNode.getName());
  }

  @Test
  public void testDontRenameObjLitPropertyWithTrailingUnderscore() {
    String input = "var obj = { bar_: 1 };";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node objNode = findNodeByName(root, "obj");
    Node propertyNode = findNodeByName(objNode, "bar");
    assertNotNull(propertyNode);
    assertEquals("bar", propertyNode.getName());
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    String input = "function Foo() {} Foo.prototype.Baz = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node barNode = findNodeByName(prototype, "Baz");
    assertNotNull(barNode);
    String newName = renamePrototypes.getPropertyMap().lookupNewName("Baz");
    assertEquals(newName, barNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithMultipleProperties() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.baz = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node barNode = findNodeByName(prototype, "bar");
    Node bazNode = findNodeByName(prototype, "baz");
    assertNotNull(barNode);
    assertNotNull(bazNode);
    assertEquals("bar", barNode.getName());
    assertEquals("baz", bazNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithExternedName() {
    String input = "function Foo() {} Foo.prototype.indexOf = Array.prototype.indexOf;";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node indexOfNode = findNodeByName(prototype, "indexOf");
    assertNotNull(indexOfNode);
    assertEquals("indexOf", indexOfNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithReservedName() {
    String input = "function Foo() {} Foo.prototype.toString = Object.prototype.toString;";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node toStringNode = findNodeByName(prototype, "toString");
    assertNotNull(toStringNode);
    assertEquals("toString", toStringNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithSingleCharacterName() {
    String input = "function Foo() {} Foo.prototype.a = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node aNode = findNodeByName(prototype, "a");
    assertNotNull(aNode);
    assertEquals("a", aNode.getName());
  }

  @Test
  public void testRenamePrototypePropertyWithCustomHeuristic() {
    String input = "function Foo() {} Foo.prototype.Bazinga = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node barNode = findNodeByName(prototype, "Bazinga");
    assertNotNull(barNode);
    String newName = renamePrototypes.getPropertyMap().lookupNewName("Bazinga");
    assertEquals(newName, barNode.getName());
  }

  @Test
  public void testDontRenamePrototypePropertyWithNoRenamingHeuristic() {
    String input = "function Foo() {} Foo.prototype.foo = function() {};";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node prototype = findPrototypeNode(root, "Foo");
    Node fooNode = findNodeByName(prototype, "foo");
    assertNotNull(fooNode);
    assertEquals("foo", fooNode.getName());
  }

  @Test
  public void testDontRenameObjLitPropertyWithNoRenamingHeuristic() {
    String input = "var obj = { foo: 1 };";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node objNode = findNodeByName(root, "obj");
    Node fooNode = findNodeByName(objNode, "foo");
    assertNotNull(fooNode);
    assertEquals("foo", fooNode.getName());
  }

  @Test
  public void testRenameMultipleObjLitPropertiesWithNoRenamingHeuristic() {
    String input = "var obj = { foo: 1, bar: 2 };";
    CompilerResult result = compileAndProcess(input);
    assertEquals(Type.ERROR_FREE, result.getErrorCount(), "Error count");
    Node root = result.getOutput();
    Node objNode = findNodeByName(root, "obj");
    Node fooNode = findNodeByName(objNode, "foo");
    Node barNode = findNodeByName(objNode, "bar");
    assertNotNull(fooNode);
    assertNotNull(barNode);
    assertEquals("foo", fooNode.getName());
    assertEquals("bar", barNode.getName());
  }

  private Node findPrototypeNode(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP && child.getString().equals(name)) {
        return child.getLastChild();
      }
    }
    throw new RuntimeException("Prototype node not found");
  }

  private Node findNodeByName(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP || child.getType() == Token.GETELEM
          || (child.getType() == Token.OBJECTLIT && child.getString().equals(name))) {
        return child;
      }
    }
    throw new RuntimeException("Node not found");
  }

  private CompilerResult compileAndProcess(String input) {
    TokenStream ts = new TokenStream(input);
    Node externs = new Node();
    Node root = new Node(Token.PROGRAM, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(renamePrototypes);
    CompilerResult result = compiler.compile(ts, externs, root, passManager);
    return result;
  }
}