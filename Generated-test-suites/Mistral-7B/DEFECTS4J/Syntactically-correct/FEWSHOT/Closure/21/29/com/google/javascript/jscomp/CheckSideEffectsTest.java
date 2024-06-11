package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parsing.parser.ParseTree;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    @Test
    public void testSideEffectChecking() {
        CompilerOptions options = new CompilerOptions(null);
        options.setWarningLevel(CheckLevel.WARNING, "JSC_USELESS_CODE");

        String sourceCode = "function foo() { return 1; } " +
                "var x = foo(); " +
                "var s = \"this string is continued on the next line but you forgot the +\"; ";

        ParseTree tree = new ParseTree();
        Node root = tree.buildAST(null, null, sourceCode, null);

        Compiler compiler = new Compiler();
        compiler.init(new CompilerInput(sourceCode), options);
        compiler.compileSingleModule(root, null);

        CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
        CheckSideEffects.StripProtection protector = new CheckSideEffects.StripProtection(compiler);

        NodeTraversal.traverse(compiler, root, checker);
        NodeTraversal.traverse(compiler, root, protector);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());

        // Test with a statement that has side-effects
        sourceCode = "function foo() { alert('Hello, World!'); } " +
                "foo();";

        tree = new ParseTree();
        root = tree.buildAST(null, null, sourceCode, null);

        compiler.init(new CompilerInput(sourceCode), options);
        compiler.compileSingleModule(root, null);

        checker = new CheckSideEffects(compiler, WARNING, true);
        protector = new CheckSideEffects.StripProtection(compiler);

        NodeTraversal.traverse(compiler, root, checker);
        NodeTraversal.traverse(compiler, root, protector);

        problemNodes = checker.problemNodes;
        assertTrue(problemNodes.size() > 0);
    }
}