package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(compiler);

        // create test input nodes
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        // call process method with test input nodes
        collapse.process(externs, root);

        // Assert
        // TODO: Add assertions based on expected behavior of process method
    }

    @Test
    public void testGatherCollapses() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(compiler);
        collapse.collapses.add(new Collapse(new Node(), new Node(), new Node()));
        collapse.nodesToCollapse.add(new Node());

        NodeTraversal t = new NodeTraversal(compiler, null);

        // create test input nodes
        NodeTraversal.NodePath n = new NodeTraversal.NodePath();

        // call GatherCollapses callback method with test input nodes
        collapse.new GatherCollapses().visit(t, n.getNode(), n.getParent());

        // Assert
        // TODO: Add assertions based on expected behavior of GatherCollapses callback method
    }

    @Test
    public void testBlacklistStubVars() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(compiler);
        collapse.blacklistedVars.add(new Var(compiler.getScope(), compiler.getUniqueNameIdSupplier().get(), null));

        NodeTraversal t = new NodeTraversal(compiler, null);

        // create test input nodes
        Node varNode = new Node(Token.VAR);
        NodeTraversal.NodePath n = new NodeTraversal.NodePath();

        // call blacklistStubVars method with test input nodes
        collapse.new GatherCollapses().blacklistStubVars(t, varNode);

        // Assert
        // TODO: Add assertions based on expected behavior of blacklistStubVars method
    }

    @Test
    public void testCanBeRedeclared() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(compiler);
        collapse.blacklistedVars.add(new Var(compiler.getScope(), compiler.getUniqueNameIdSupplier().get(), null));

        NodeTraversal t = new NodeTraversal(compiler, null);

        // create test input nodes
        Node n = new Node(Token.EXPR_RESULT);
        n.addChildToFront(new Node(Token.ASSIGN));
        NodeTraversal.NodePath assignPath = new NodeTraversal.NodePath();
        assignPath.push(n);

        Scope s = compiler.getScope();

        // call canBeRedeclared method with test input nodes
        boolean result = collapse.new GatherCollapses().canBeRedeclared(n, s);

        // Assert
        // TODO: Add assertions based on expected behavior of canBeRedeclared method
    }

    @Test
    public void testApplyCollapses() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(compiler);
        Collapse c = new Collapse(new Node(Token.VAR), new Node(), new Node());
        collapse.collapses.add(c);

        // create test input nodes
        Node var = new Node(Token.VAR);
        var.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        var.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        var.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        c.endNode.addChildToBack(var);

        // call applyCollapses method
        collapse.applyCollapses();

        // Assert
        // TODO: Add assertions based on expected behavior of applyCollapses method
    }
}