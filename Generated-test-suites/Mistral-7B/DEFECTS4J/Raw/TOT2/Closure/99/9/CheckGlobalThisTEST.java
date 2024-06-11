package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    // Test case for CheckGlobalThis.shouldTraverse() method with a function that uses 'this' on the left side of an assignment or property access
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("function evil() { this.useful = undefined; }");
    assertTrue(checker.shouldTraverse(null, rootNode, null));
  }

  @Test
  public void testSafeUsage() {
    // Test case for CheckGlobalThis.shouldTraverse() method with a safe function
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("function safe() {}");
    assertFalse(checker.shouldTraverse(null, rootNode, null));
  }

  @Test
  public void testConstructorFunction() {
    // Test case for CheckGlobalThis.shouldTraverse() method with a constructor function
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("function Constructor() { }");
    assertTrue(checker.shouldTraverse(null, rootNode, null));
  }

  @Test
  public void testPrototypeMethod() {
    // Test case for CheckGlobalThis.shouldTraverse() method with a prototype method
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("function Constructor() { }.prototype.method = function() {};");
    assertTrue(checker.shouldTraverse(null, rootNode, null));
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    // Test case for CheckGlobalThis.shouldTraverse() method with a function annotated with @this
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("/** @this {MyType} */ function Constructor() { }");
    assertFalse(checker.shouldTraverse(null, rootNode, null));
  }

  @Test
  public void testAssignmentToPrototype() {
    // Test case for CheckGlobalThis.visit() method with an assignment to a prototype
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("var a = function() {}; a.prototype = this;");
    Node nodeToTest = rootNode.getFirstChild();
    checker.visit(null, nodeToTest, null);
    // This test case should throw an AssertionError since assigning 'this' to a prototype is not allowed
    fail("Expected an AssertionError but didn't receive one.");
  }

  @Test
  public void testAssignmentToNonExistentPrototype() {
    // Test case for CheckGlobalThis.visit() method with an assignment to a non-existent prototype
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node rootNode = Node.parse("var a = function() {}; a.prototype.nonExistent = this;");
    Node nodeToTest = rootNode.getFirstChild();
    checker.visit(null, nodeToTest, null);
    // This test case should throw an AssertionError since assigning 'this' to a non-existent prototype is not allowed
    fail("Expected an AssertionError but didn't receive one.");
  }
}