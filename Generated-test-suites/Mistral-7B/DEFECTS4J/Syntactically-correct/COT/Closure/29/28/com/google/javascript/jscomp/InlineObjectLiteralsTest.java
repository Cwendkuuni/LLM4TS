package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllExperimentalOptions(true);

    String jsCode = "function foo() {" +
                    "  var obj = {" +
                    "    a: 1," +
                    "    b: 2," +
                    "    c: {'d': 3}" +
                    "  };" +
                    "  var x = obj.a;" +
                    "  var y = obj.b;" +
                    "  var z = obj.c.d;" +
                    "  return [x, y, z];" +
                    "}";

    SourceFile sourceFile = new SourceFile("test.js", jsCode);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "JSCompiler_object_inline_");

    // When
    CompilerResult result = compiler.compile(Lists.newArrayList(sourceFile), options, pass);

    // Then
    Node rootNode = result.getOutput();
    Node arrayLiteral = rootNode.getFirstChild();
    Node expectedArrayLiteral = Node.newArrayLiteral();
    expectedArrayLiteral.addElement(Node.newNumber(1));
    expectedArrayLiteral.addElement(Node.newNumber(2));
    expectedArrayLiteral.addElement(Node.newNumber(3));

    assertEquals(expectedArrayLiteral.toString(), arrayLiteral.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithSelfReference() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllExperimentalOptions(true);

    String jsCode = "function foo() {" +
                    "  var obj = {" +
                    "    a: function() { return this.b; }," +
                    "    b: 2," +
                    "    c: {'d': 3}" +
                    "  };" +
                    "  return obj.a();" +
                    "}";

    SourceFile sourceFile = new SourceFile("test.js", jsCode);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "JSCompiler_object_inline_");

    // When & Then
    try {
      CompilerResult result = compiler.compile(Lists.newArrayList(sourceFile), options, pass);
      fail("Expected an AssertionError");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllExperimentalOptions(true);

    String jsCode = "var EXPORTED = {" +
                    "  a: 1," +
                    "  b: 2," +
                    "};" +
                    "function foo() {" +
                    "  return EXPORTED.a;" +
                    "}" +
                    "export {EXPORTED};";

    SourceFile sourceFile = new SourceFile("test.js", jsCode);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "JSCompiler_object_inline_");

    // When
    CompilerResult result = compiler.compile(Lists.newArrayList(sourceFile), options, pass);

    // Then
    Node rootNode = result.getOutput();
    Node functionDeclaration = rootNode.getFirstChild();
    Node expectedFunctionDeclaration = new Node(Token.FUNCTION_DECL, functionDeclaration.getLineNumber(), functionDeclaration.getLineOffsetInFile(), "foo");
    expectedFunctionDeclaration.setBody(new Node(Token.BLOCK, 0, 0, Lists.newArrayList()));
    Node variableDeclaration = new Node(Token.VAR, functionDeclaration.getLineNumber(), functionDeclaration.getLineOffsetInFile(), null, "EXPORTED");
    Node objLiteral = new Node(Token.OBJECT_LITERAL, 0, 0, Lists.newArrayList());
    Node propertyA = new Node(Token.PROPERTY, 0, 0, "a", new Node(Token.NUMBER, 0, 0, 1));
    objLiteral.addChild(propertyA);
    variableDeclaration.setInitializer(objLiteral);
    expectedFunctionDeclaration.getBody().addChild(variableDeclaration);
    expectedFunctionDeclaration.getBody().addChild(new Node(Token.RETURN, 0, 0, new Node(Token.NAME, 0, 0, "EXPORTED.a")));

    assertEquals(expectedFunctionDeclaration.toString(), functionDeclaration.toString());
  }
}