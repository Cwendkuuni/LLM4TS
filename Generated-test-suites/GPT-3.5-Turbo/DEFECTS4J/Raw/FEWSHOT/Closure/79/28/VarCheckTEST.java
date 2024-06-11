
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testVisit() {
        // Positive test case
        String code = "var a = 5;";
        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        compiler.process(externs, root);

        assertEquals(0, compiler.getErrors().length);
        assertEquals(0, compiler.getWarnings().length);
    }

    @Test
    public void testProcess() {
        // Positive test case
        String code = "var a = 5;";
        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        compiler.process(externs, root);

        assertEquals(0, compiler.getErrors().length);
        assertEquals(0, compiler.getWarnings().length);
    }
}
