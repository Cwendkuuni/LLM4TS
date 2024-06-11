package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class VarCheckTest {
    @Test
    public void testProcess() {
        // Initialize the compiler and pass a test externs and root
        Compiler compiler = new Compiler();
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        VarCheck varCheck = new VarCheck(compiler);

        // Call the process method and assert that the compiler input and root are correct
        varCheck.process(externs, root);
        assertEquals(externs, compiler.getRoot().getFirstChild());
        assertEquals(root, compiler.getRoot().getLastChild());
    }

    @Test
    public void testVisit_undefinedVarError() {
        // Initialize the compiler and pass a test input with an undefined variable
        Compiler compiler = new Compiler();
        Node input = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(nameNode);
        input.addChildToBack(varNode);
        VarCheck varCheck = new VarCheck(compiler);

        // Call the visit method and assert that a UNDEFINED_VAR_ERROR is reported
        NodeTraversal.traverse(compiler, input, varCheck);
        assertTrue(compiler.getErrorManager().hasErrors());

        JSError error = compiler.getErrorManager().getErrors()[0];
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, error.getType());
        assertEquals("x", error.description);

        // Assert that the error node is the name node
        assertEquals(nameNode, error.node);
    }

    @Test
    public void testVisit_invalidFunctionDecl() {
        // Initialize the compiler and pass a test input with an invalid function declaration
        Compiler compiler = new Compiler();
        Node input = new Node(Token.SCRIPT);
        Node funcNode = new Node(Token.FUNCTION);
        input.addChildToBack(funcNode);
        VarCheck varCheck = new VarCheck(compiler);

        // Call the visit method and assert that an INVALID_FUNCTION_DECL error is reported
        NodeTraversal.traverse(compiler, input, varCheck);
        assertTrue(compiler.getErrorManager().hasErrors());

        JSError error = compiler.getErrorManager().getErrors()[0];
        assertEquals(VarCheck.INVALID_FUNCTION_DECL, error.getType());

        // Assert that the error node is the function node
        assertEquals(funcNode, error.node);
    }

    @Test
    public void testVisit() {
        // Initialize the compiler and pass a test input with a valid variable declaration
        Compiler compiler = new Compiler();
        Node input = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(nameNode);
        input.addChildToBack(varNode);
        VarCheck varCheck = new VarCheck(compiler);

        // Call the visit method and assert that no errors are reported
        NodeTraversal.traverse(compiler, input, varCheck);
        assertFalse(compiler.getErrorManager().hasErrors());
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        // Initialize the compiler and add a variable to the varsToDeclareInExterns set
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);
        varCheck.varsToDeclareInExterns.add("x");

        // Call the createSynthesizedExternVar method and assert that the variable is declared in the synthesizesExternsRoot
        varCheck.createSynthesizedExternVar("x");
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        assertEquals(Token.VAR, synthesizedExternsRoot.getFirstChild().getType());
        assertEquals("x", synthesizedExternsRoot.getFirstChild().getFirstChild().getString());
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        // Initialize the compiler and call the getSynthesizedExternsInput method
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);
        varCheck.getSynthesizedExternsInput();

        // Assert that the synthesizedExternsInput is not null
        assertNotNull(varCheck.synthesizedExternsInput);
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        // Initialize the compiler and call the getSynthesizedExternsRoot method
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);
        varCheck.getSynthesizedExternsRoot();

        // Assert that the synthesizedExternsRoot is not null
        assertNotNull(varCheck.synthesizedExternsRoot);
    }
}