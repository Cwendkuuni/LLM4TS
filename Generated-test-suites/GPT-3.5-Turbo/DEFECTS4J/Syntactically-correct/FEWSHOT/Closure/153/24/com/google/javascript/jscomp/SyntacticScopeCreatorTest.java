package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_functionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope();
        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(parent, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }

    @Test
    public void testCreateScope_functionExpression() {
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope parent = new Scope();
        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(parent, scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals("test.js", scopeCompiler.getInput().getName());
    }
}