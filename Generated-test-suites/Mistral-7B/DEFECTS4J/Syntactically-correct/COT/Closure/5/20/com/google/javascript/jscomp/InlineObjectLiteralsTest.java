package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    

    // Create a compiler and configure it
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintErrorMessages(false);
    options.setPrintProgress(false);
    options.setCompilerPasses(Lists.newArrayList(
        new CompilerPass() {
          @Override
          public void process(Node externs, Node root) {
            new InlineObjectLiterals(compiler, () -> "inline_name_id_supplier").process(externs, root);
          }
        }));

    // Set up the test code as the input
    String testCode = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2, d: 3}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  var z = obj.b.d;" +
        "  testObjInline(obj);" +
        "}" +
        "function testObjInline(obj) {" +
        "  var a = obj.a;" +
        "  var c = obj.b.c;" +
        "  var d = obj.b.d;" +
        "}" +
        "";

    // Compile the test code
    CompilerResult result = compiler.compile(testCode, options);

    // Check the compilation result
    Node root = result.toAbstractSyntaxTree();
    assertFalse("Expected no errors during compilation", result.hasErrors());
    assertTrue("Expected the test function to be defined", root.isFunctionDeclaration());

    
  }

  @Test
  public void testInlineObjectLiteralsSelfReference() {
    

    // Create a compiler and configure it
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintErrorMessages(false);
    options.setPrintProgress(false);
    options.setCompilerPasses(Lists.newArrayList(
        new CompilerPass() {
          @Override
          public void process(Node externs, Node root) {
            new InlineObjectLiterals(compiler, () -> "inline_name_id_supplier").process(externs, root);
          }
        }));

    // Set up the test code as the input
    String testCode = "function test() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: obj.a.b" +
        "    }" +
        "  };" +
        "}" +
        "";

    // Compile the test code
    CompilerResult result = compiler.compile(testCode, options);

    // Check the compilation result
    Node root = result.toAbstractSyntaxTree();
    assertFalse("Expected no errors during compilation", result.hasErrors());

    
  }

  @Test
  public void testInlineObjectLiteralsWithInvalidProperty() {
    

    // Create a compiler and configure it
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintErrorMessages(true);
    options.setPrintProgress(false);
    options.setCompilerPasses(Lists.newArrayList(
        new CompilerPass() {
          @Override
          public void process(Node externs, Node root) {
            new InlineObjectLiterals(compiler, () -> "inline_name_id_supplier").process(externs, root);
          }
        }));

    // Set up the test code as the input
    String testCode = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  x = obj.invalid_property;" +
        "}" +
        "";

    // Compile the test code
    CompilerResult result = compiler.compile(testCode, options);

    // Check the compilation result
    Node root = result.toAbstractSyntaxTree();
    assertTrue("Expected an error during compilation", result.hasErrors());
    assertTrue("Expected the compiler to report an error about an invalid property",
        root.getErrorMessages().get(0).getMessage().contains("invalid_property"));

    
  }
}