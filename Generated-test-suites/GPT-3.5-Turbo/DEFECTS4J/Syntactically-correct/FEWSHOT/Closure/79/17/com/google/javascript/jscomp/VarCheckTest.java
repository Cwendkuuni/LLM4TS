package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VarCheckTest {
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new Compiler();
        compiler.initOptions(new CompilerOptions());
    }

    @After
    public void tearDown() {
        compiler = null;
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        VarCheck varCheck = new VarCheck(compiler);
        varCheck.process(externs, root);

        assertEquals(0, varCheck.varsToDeclareInExterns.size());
        assertTrue(compiler.getModuleGraph().isEmpty());
    }

    @Test
    public void testVisit_WithNameEmpty() {
        NodeTraversal traversal = new NodeTraversal(compiler, new VarCheck(compiler));
        Scope scope = new Scope();
        scope.addLocal("x");
        Node node = new Node(Token.GETPROP);
        node.addChildToFront(new Node(Token.NAME, ""));
        traversal.getCallback().visit(traversal, node, null);

        assertNotNull(node.getString());
    }

    @Test
    public void testVisit_WithUndefinedVar() {
        NodeTraversal traversal = new NodeTraversal(compiler, new VarCheck(compiler));
        Scope scope = new Scope();
        scope.addLocal("x");
        Node node = new Node(Token.GETPROP);
        node.addChildToFront(new Node(Token.NAME, "y"));
        traversal.getCallback().visit(traversal, node, null);

        assertEquals("y", node.getString());
        assertEquals(Token.NULL, node.getFirstChild().getType());
    }

    @Test
    public void testVisit_WithDefinedVar() {
        NodeTraversal traversal = new NodeTraversal(compiler, new VarCheck(compiler));
        Scope scope = new Scope();
        scope.addLocal("x");
        Node node = new Node(Token.GETPROP);
        node.addChildToFront(new Node(Token.NAME, "x"));
        traversal.getCallback().visit(traversal, node, null);

        assertEquals("x", node.getString());
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        VarCheck varCheck = new VarCheck(compiler);
        varCheck.createSynthesizedExternVar("x");

        assertEquals(0, varCheck.varsToDeclareInExterns.size());
        assertNotNull(varCheck.getSynthesizedExternsInput());
        assertNotNull(varCheck.getSynthesizedExternsRoot());
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        VarCheck varCheck = new VarCheck(compiler);
        CompilerInput input = varCheck.getSynthesizedExternsInput();

        assertNotNull(input);
        assertEquals("{SyntheticVarsDeclar}", input.getName());
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        VarCheck varCheck = new VarCheck(compiler);
        Node root = varCheck.getSynthesizedExternsRoot();

        assertNotNull(root);
        assertEquals(Token.ROOT, root.getType());
    }

    @Test
    public void testNameRefInExternsCheck() {
        NodeTraversal traversal = new NodeTraversal(compiler, new VarCheck.NameRefInExternsCheck());
        Scope scope = new Scope();
        scope.addLocal("x");

        Node varNode = new Node(Token.VAR);
        varNode.addChildToFront(new Node(Token.NAME, "y"));

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.NAME, "z"));

        Node lpNode = new Node(Token.LP);
        lpNode.addChildToFront(new Node(Token.NAME, "a"));

        traversal.traverse(new Node(Token.ROOT, varNode, functionNode, lpNode));

        Node firstChild = varNode.getFirstChild();
        assertNotNull(firstChild);
        assertEquals(Token.NAME, firstChild.getType());

        firstChild = functionNode.getFirstChild();
        assertNull(firstChild);

        firstChild = lpNode.getFirstChild();
        assertNull(firstChild);
    }
}