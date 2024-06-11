package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RuntimeTypeCheckTest {
    @Test
    public void testProcess() {
        Compiler compiler = new Compiler();
        AbstractCompiler abstractCompiler = compiler;
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(abstractCompiler, null);
        Node externs = new Node();
        Node root = new Node();
        runtimeTypeCheck.process(externs, root);
    }

    @Test
    public void testBoilerplateCode() {
        Compiler compiler = new Compiler();
        Node js = RuntimeTypeCheck.getBoilerplateCode(compiler, null);
        assertNotNull(js);
    }
}