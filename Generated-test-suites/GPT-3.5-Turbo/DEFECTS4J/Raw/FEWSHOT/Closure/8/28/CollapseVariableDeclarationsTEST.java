package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    CollapseVariableDeclarations collapseVariableDeclarations;
    
    @Before
    public void setUp() {
        Compiler compiler = new Compiler();
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testProcess_emptyTree() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("");
        collapseVariableDeclarations.process(externs, root);
        String expected = "";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_singleDeclaration() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("var a;");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_singleAssignment() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("a = 1;");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a = 1;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_multipleDeclarations() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("var a; var b; var c;");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a, b, c;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_multipleAssignments() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("a = 1; b = 2; c = 3;");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var c = b = a = 1, 2, 3;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_mixedDeclarationsAndAssignments() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("var a; b = 2; c = 3;");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a, c = b = 2, 3;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_nestedDeclarations() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("var a; { var b; var c; }");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a; { var c, b; }";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_nestedAssignments() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("a = 1; { b = 2; c = 3; }");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var c = b = a = 1, 2, 3;";
        String actual = root.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_mixedNestedDeclarationsAndAssignments() {
        Node externs = null;
        Node root = new Compiler().parseTestCode("var a; { b = 2; var c; }");
        collapseVariableDeclarations.process(externs, root);
        String expected = "var a; { var c, b = 2; }";
        String actual = root.toString();
        assertEquals(expected, actual);
    }
}