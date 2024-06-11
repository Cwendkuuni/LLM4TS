package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        assertEquals("module$fileName", pcm.guessCJSModuleName("fileName"));
        assertEquals("module$subdir$fileName", pcm.guessCJSModuleName("subdir/fileName"));
        assertEquals("module$subdir$subdir2$fileName", pcm.guessCJSModuleName("subdir/subdir2/fileName"));
    }

    @Test
    public void testToModuleName1() {
        assertEquals("module$fileName", ProcessCommonJSModules.toModuleName("fileName"));
    }

    @Test
    public void testToModuleName2() {
        assertEquals("module$subdir$fileName", ProcessCommonJSModules.toModuleName("fileName", "subdir/parentFile"));
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.process(null, null);

        // Test if process is executed without any exceptions
        assertNull(pcm.getModule());
    }

    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules.SuffixVarsCallback svc = new ProcessCommonJSModules.SuffixVarsCallback("suffix");

        NodeTraversal t = null;
        Node n = new Node(Token.NAME, "name");
        Node parent = null;
        svc.visit(t, n, parent);

        // Test if visit is executed without any exceptions
    }
}