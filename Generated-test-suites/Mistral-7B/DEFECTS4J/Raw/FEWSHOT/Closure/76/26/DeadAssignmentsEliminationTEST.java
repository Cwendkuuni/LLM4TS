package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.CompilerException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseContext;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;

public class DeadAssignmentsEliminationTest {
    @Test
    public void testDeadAssignmentsElimination() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1, y = 1;" +
                "x = y;" +
                "y = 2;" +
                "return x + y;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xAssignmentNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getFirstChild().getType() == Token.VAR && n.getString().equals("x"));
        Node yAssignmentNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("y"));

        assertTrue(graphTraversal.isReachable(yAssignmentNode, xAssignmentNode));
        assertFalse(graphTraversal.isReachable(xAssignmentNode, yAssignmentNode));
        assertNull(NodeUtil.findFirstChildByPredicate(xAssignmentNode, matchRemovableAssigns));
        assertTrue(NodeUtil.findFirstChildByPredicate(yAssignmentNode, matchRemovableAssigns) != null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 3; }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationNoAssignment() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1, y = 1;" +
                "return x + y;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("x"));
        Node yNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("y"));

        assertTrue(graphTraversal.isReachable(xNode, yNode));
        assertTrue(graphTraversal.isReachable(yNode, xNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(xNode, matchRemovableAssigns) == null);
        assertTrue(NodeUtil.findFirstChildByPredicate(yNode, matchRemovableAssigns) == null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 2; }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationNoDeadVariable() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1, y = 1;" +
                "x = y;" +
                "y = x;" +
                "return x + y;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("x"));
        Node yNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("y"));

        assertTrue(graphTraversal.isReachable(xNode, yNode));
        assertTrue(graphTraversal.isReachable(yNode, xNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(xNode, matchRemovableAssigns) != null);
        assertTrue(NodeUtil.findFirstChildByPredicate(yNode, matchRemovableAssigns) != null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 2; }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationWithFunctionCall() throws CompilerException {
        String sourceCode = "function foo(x, y) {" +
                "var a = 1, b = 1;" +
                "a = b;" +
                "b = 2;" +
                "return bar(x, a + b);" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node aNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("a"));
        Node bNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("b"));

        assertTrue(graphTraversal.isReachable(bNode, aNode));
        assertTrue(graphTraversal.isReachable(aNode, bNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(aNode, matchRemovableAssigns) != null);
        assertTrue(NodeUtil.findFirstChildByPredicate(bNode, matchRemovableAssigns) != null);

        String result = cu.compile(options);
        assertEquals("function foo(x, y) { return bar(x, 3); }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationWithReturnStatement() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1, y = 1;" +
                "x = y;" +
                "return x;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("x"));
        Node yNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("y"));

        assertTrue(graphTraversal.isReachable(xNode, yNode));
        assertTrue(graphTraversal.isReachable(yNode, xNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(xNode, matchRemovableAssigns) != null);
        assertTrue(NodeUtil.findFirstChildByPredicate(yNode, matchRemovableAssigns) != null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 1; }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationWithIncrementOperator() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1;" +
                "x++;" +
                "x++;" +
                "return x;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("x"));

        assertTrue(graphTraversal.isReachable(xNode, xNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(xNode, matchRemovableAssigns) == null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 2; }", result);
    }

    @Test
    public void testDeadAssignmentsEliminationWithDecrementOperator() throws CompilerException {
        String sourceCode = "function foo() {" +
                "var x = 1;" +
                "x--;" +
                "x--;" +
                "return x;" +
                "}";

        CompilerOptions options = new CompilerOptions();
        options.setKeepSyntacticSugar(true);

        AbstractCompiler compiler = new AbstractCompiler();
        CompilerPassManager passManager = new CompilerPassManager(compiler);
        passManager.addPass(new DeadAssignmentsElimination(compiler));

        CompilationUnit cu = new CompilationUnit(sourceCode);
        Options parsedOptions = new DefaultParser().parse(cu.getSourceCode(), false);
        ParseContext context = new ParseContext(parsedOptions);
        Node rootNode = NodeTraversal.traverse(compiler, cu.getRootNode(), new NodeTraversal.EmptyRootCallback())
                .getRootNode();

        passManager.run(context, rootNode);
        GraphTraversal graphTraversal = new GraphTraversal(rootNode);

        Node xNode = NodeUtil.findFirstChildByPredicate(rootNode, n -> n.getString().equals("x"));

        assertTrue(graphTraversal.isReachable(xNode, xNode));
        assertTrue(NodeUtil.findFirstChildByPredicate(xNode, matchRemovableAssigns) == null);

        String result = cu.compile(options);
        assertEquals("function foo() { return 0; }", result);
    }
}