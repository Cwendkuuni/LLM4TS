package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        CompilerOptions options = new CompilerOptions();
        compiler = new AbstractCompiler(options);
    }

    @Test
    public void testEmptyStatement() {
        String code = "function foo() {}";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() {}",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingReturn() {
        String code = "function foo() { return; alert('unreachable'); }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { return; }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingIfStatement() {
        String code = "function foo() { if (true) { return; alert('unreachable'); } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { if (true) { return; } }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingIfElseStatement() {
        String code = "function foo() { if (true) { return; alert('unreachable'); } else { return; } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { if (true) { return; } }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingSwitchStatement() {
        String code = "function foo() { switch (1) { case 1: return; alert('unreachable'); break; } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { switch (1) { case 1: return; } }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingWhileStatement() {
        String code = "function foo() { while (true) { return; alert('unreachable'); } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { while (true) {} }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingForStatement() {
        String code = "function foo() { for (; true;) { return; alert('unreachable'); } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { for (;;) {} }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeFollowingDoWhileStatement() {
        String code = "function foo() { do { return; alert('unreachable'); } while (false); }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { do {} while (false); }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithMultipleReturnStatements() {
        String code = "function foo() { return; return; }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { return; }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithFunctionStatement() {
        String code = "function foo() { function bar() { return; alert('unreachable'); } }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { function bar() {} }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithVariableDeclarationStatement() {
        String code = "function foo() { var x; return; alert('unreachable'); }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { var x; }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithExpressionStatement() {
        String code = "function foo() { true; alert('unreachable'); }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithEmptyBlock() {
        String code = "function foo() { {} alert('unreachable'); }";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() { }",
            compiler.getSourceFile("test.js").getCode());
    }

    @Test
    public void testRemoveDeadCodeWithComplexExample() {
        String code = "function foo() { var a, x = 0;\n" +
                "  if (true) {\n" +
                "    return;\n" +
                "    alert('unreachable');\n" +
                "  } else if (true) {\n" +
                "    return;\n" +
                "  } else if (true) {\n" +
                "    return;\n" +
                "  } else {\n" +
                "    x = 5;\n" +
                "  }\n" +
                "  if (x > 0) {\n" +
                "    return;\n" +
                "  } else {\n" +
                "    alert('unreachable');\n" +
                "  }\n" +
                "  a = 10;\n" +
                "}";
        SourceFile input = new SourceFile("test.js", code);
        Node root = compiler.parse(input, null);

        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traverser = new NodeTraversal(root, pass);
        traverser.traverseAll();

        NodeUtil.assertCodeEquals(
            "function foo() {\n" +
                "  var x = 0;\n" +
                "  if (true) {\n" +
                "  } else if (true) {\n" +
                "  } else if (true) {\n" +
                "  } else {\n" +
                "    var a;\n" +
                "  }\n" +
                "  if (x <= 0) {\n" +
                "    var a;\n" +
                "  }\n" +
                "}",
            compiler.getSourceFile("test.js").getCode());
    }
}