package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

        // Typical use case
        String input = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedOutput = "var a, b = 1, c = 2;";
        assertEquals(expectedOutput, collapse.collapseVariableDeclarations(input));

        // Edge case: No variable
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, collapse.collapseVariableDeclarations(input));

        // Error scenario: Missing semicolon after assignment
        input = "var a = 1\nvar b = 2;";
        expectedOutput = "var a = 1; var b = 2;";
        assertEquals(expectedOutput, collapse.collapseVariableDeclarations(input));
    }
}