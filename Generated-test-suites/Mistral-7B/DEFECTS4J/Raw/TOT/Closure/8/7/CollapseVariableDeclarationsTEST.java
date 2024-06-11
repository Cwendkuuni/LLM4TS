package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerPass.*;
import static com.google.javascript.jscomp.test.TestUtils.*;
import static com.google.common.truth.Truth.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.TestSourceFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {
  private Compiler compiler;
  private TestSourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = newCompiler();
    sourceFile = new TestSourceFile();
  }

  @Test
  public void testCollapsingSimpleVariableDeclarations() {
    String source = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  var a, b = 1, c = 2, d = 3;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingMultipleAssignments() {
    String source = "function foo() {\n" +
        "  var a = true;\n" +
        "  var b = true;\n" +
        "  var c = true;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  var c = b = a = true;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsInsideFunction() {
    String source = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  function bar() {\n" +
        "    var a, b = 1, c = 2;\n" +
        "  }\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithEmptyStatement() {
    String source = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  ;\n" +
        "  var c = 2;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithIfStatement() {
    String source = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  } else {\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  if (true) {\n" +
        "    var a, b = 1;\n" +
        "  } else {\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithCyclicReference() {
    String source = "function foo() {\n" +
        "  var a;\n" +
        "  var b = {\n" +
        "    getA: function() { return a; },\n" +
        "    setA: function(value) { a = value; },\n" +
        "  };\n" +
        "  var c = a;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    // The compiler should not fail due to a cyclic reference, and the
    // variable declarations should still be collapsed.
    expectEqualCode("function foo() {\n" +
        "  var a, b = { getA: function() { return a; }, setA: function(value) { a = value; }, }, c = a;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithDuplicateNames() {
    String source = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  var a = 2;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithMixedDeclarations() {
    String source = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 1;\n" +
        "  var c;\n" +
        "}";

    sourceFile.setSourceCode(source);
    compiler.compile(sourceFile.getSourceFileName(), sourceFile.getSourceCode());

    Node root = compiler.getAbstractSyntacticTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse(new CollapseVariableDeclarations(compiler));

    expectEqualCode("function foo() {\n" +
        "  var a = 1, b = 1, c;\n" +
        "}", compiler.getSourceFileName(), compiler.getCompiledSource());
  }
}