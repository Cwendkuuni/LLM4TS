package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testGlobalThis() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "function evil() {" +
            "this.property = 'evil';" +
            "}";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getSeverity());
  }

  @Test
  public void testGlobalThisPrototype() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "var obj = {" +
            "prototype: {" +
                "evil: function() {" +
                    "this.property = 'evil';" +
                "}" +
            "}" +
        "};" +
        "obj.prototype.evil();";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getSeverity());
  }

  @Test
  public void testGlobalThisInFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "function evil() {" +
            "function innerFunction() {" +
                "this.property = 'evil';" +
            "}" +
            "innerFunction();" +
        "}";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInConstructor() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "function Constructor() {" +
            "this.property = 'constructor';" +
        "}";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);
    Node constructor = root.getFirstChild();

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithJSDoc() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "/** @constructor */\n" +
        "function evil() {" +
            "this.property = 'evil';" +
        "}";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);
    Node constructor = root.getFirstChild();

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithThisAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);

    String code =
        "/** @this {Object} */\n" +
        "function evil() {" +
            "this.property = 'evil';" +
        "}";

    Parser parser = new Parser();
    AbstractCompiler compiler = new AbstractCompiler(parser, options);
    Node root = parser.parse(code, null, null);
    Node constructor = root.getFirstChild();

    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(compiler, new CheckLevel(WARNING)));
    traversal.traverse(root);

    assertEquals(0, compiler.getDiagnostics().size());
  }
}