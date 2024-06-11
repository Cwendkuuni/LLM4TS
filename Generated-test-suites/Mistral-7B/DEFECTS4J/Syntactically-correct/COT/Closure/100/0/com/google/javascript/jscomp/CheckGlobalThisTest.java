package com.google.javascript.jscomp.test; // Replace 'com.google.javascript.jscomp.test' with the actual package name if needed

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;
import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler.getRoot(), checker);

    // Test a valid code without any global 'this' usage
    String validCode = "function validFunction() {}\n";
    compileAndCheck(nodeTraversal, validCode);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a global 'this' usage on the left side of an assignment
    String codeWithGlobalThisAssignment = "function evilFunction() {\n" +
            "  this.property = 42;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithGlobalThisAssignment);
    assertEquals(1, nodeTraversal.getDiagnostics().size());
    DiagnosticType diagnostic = nodeTraversal.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());

    // Test a code with a global 'this' usage on the right side of an assignment
    String codeWithGlobalThisAssignmentRightSide = "function evilFunction() {\n" +
            "  var localThis = this;\n" +
            "  localThis.property = 42;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithGlobalThisAssignmentRightSide);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a constructor function
    String codeWithConstructor = "function MyConstructor() {}\n";
    compileAndCheck(nodeTraversal, codeWithConstructor);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function annotated with @constructor
    String codeWithJsDocConstructor = "/** @constructor */\n" +
            "function MyConstructor() {}\n";
    compileAndCheck(nodeTraversal, codeWithJsDocConstructor);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function annotated with @this
    String codeWithJsDocThis = "/** @this MyType */\n" +
            "function myFunction() {}\n";
    compileAndCheck(nodeTraversal, codeWithJsDocThis);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a getter and setter property
    String codeWithGetterSetter = "function MyType() {}\n" +
            "MyType.prototype = {\n" +
            "  get property() {\n" +
            "    return this.internalProperty;\n" +
            "  },\n" +
            "  set property(value) {\n" +
            "    this.internalProperty = value;\n" +
            "  }\n" +
            "}";
    compileAndCheck(nodeTraversal, codeWithGetterSetter);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a prototype method
    String codeWithPrototypeMethod = "function MyType() {}\n" +
            "MyType.prototype.myMethod = function() {\n" +
            "  // Test code goes here\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithPrototypeMethod);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with multiple functions and global 'this' usages
    String codeWithMultipleFunctions = "function evilFunction1() {\n" +
            "  this.property1 = 42;\n" +
            "}\n" +
            "\n" +
            "function evilFunction2() {\n" +
            "  this.property2 = 43;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithMultipleFunctions);
    assertEquals(2, nodeTraversal.getDiagnostics().size());
    diagnostic = nodeTraversal.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
    diagnostic = nodeTraversal.getDiagnostics().get(1).getType();
    assertEquals(DiagnosticType.WARNING, diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());

    // Test a code with an assignment to a prototype property
    String codeWithAssignmentToPrototypeProperty = "function MyType() {}\n" +
            "MyType.prototype.x = function() {\n" +
            "  this.property = 42;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithAssignmentToPrototypeProperty);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with an assignment to a property on a prototype property
    String codeWithAssignmentToPropertyOnPrototypeProperty = "function MyType() {}\n" +
            "MyType.prototype.x = {\n" +
            "  get property() {\n" +
            "    return this.internalProperty;\n" +
            "  },\n" +
            "  set property(value) {\n" +
            "    this.internalProperty = value;\n" +
            "  }\n" +
            "}\n" +
            "MyType.prototype.x.property = 42;\n";
    compileAndCheck(nodeTraversal, codeWithAssignmentToPropertyOnPrototypeProperty);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function inside a constructor
    String codeWithFunctionInsideConstructor = "function MyType() {\n" +
            "  function innerFunction() {\n" +
            "    this.property = 42;\n" +
            "  }\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithFunctionInsideConstructor);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function inside a constructor annotated with @constructor
    String codeWithJsDocFunctionInsideConstructor = "function MyType() {\n" +
            "  /** @constructor */\n" +
            "  function innerFunction() {\n" +
            "    this.property = 42;\n" +
            "  }\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithJsDocFunctionInsideConstructor);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function inside a constructor annotated with @this
    String codeWithJsDocFunctionInsideConstructorWithThis = "function MyType() {\n" +
            "  /** @this MyType */\n" +
            "  function innerFunction() {\n" +
            "    this.property = 42;\n" +
            "  }\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithJsDocFunctionInsideConstructorWithThis);
    assertTrue(nodeTraversal.getDiagnostics().isEmpty());

    // Test a code with a function inside a constructor and global 'this' usage
    String codeWithFunctionInsideConstructorAndGlobalThis = "function MyType() {\n" +
            "  function innerFunction() {\n" +
            "    this.property = 42;\n" +
            "  }\n" +
            "  this.property = 43;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithFunctionInsideConstructorAndGlobalThis);
    assertEquals(1, nodeTraversal.getDiagnostics().size());
    diagnostic = nodeTraversal.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());

    // Test a code with a function inside a constructor and global 'this' usage on the left side of an assignment
    String codeWithFunctionInsideConstructorAndGlobalThisAssignment = "function MyType() {\n" +
            "  function innerFunction() {\n" +
            "  }\n" +
            "  this.property = innerFunction;\n" +
            "}\n";
    compileAndCheck(nodeTraversal, codeWithFunctionInsideConstructorAndGlobalThisAssignment);
    assertEquals(1, nodeTraversal.getDiagnostics().size());
    diagnostic = nodeTraversal.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnostic);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
  }

  private void compileAndCheck(NodeTraversal nodeTraversal, String code) {
    Assert.assertNotNull(nodeTraversal);
    Assert.assertNotNull(code);
    nodeTraversal.process(Node.fromJson(code, null));
  }
}