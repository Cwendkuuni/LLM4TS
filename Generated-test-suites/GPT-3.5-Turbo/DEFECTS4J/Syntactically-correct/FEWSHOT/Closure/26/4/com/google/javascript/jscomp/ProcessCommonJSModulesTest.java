package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        assertEquals("module$filename", module.guessCJSModuleName("filename"));
        assertEquals("module$src$filename", module.guessCJSModuleName("src/filename"));
        assertEquals("module$src$foo$filename", module.guessCJSModuleName("src/foo/filename"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
        assertEquals("module$src$filename", ProcessCommonJSModules.toModuleName("src/filename"));
        assertEquals("module$src$foo$filename", ProcessCommonJSModules.toModuleName("src/foo/filename"));
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename", "current"));
        assertEquals("module$current$filename", ProcessCommonJSModules.toModuleName("./filename", "current"));
        assertEquals("module$src$filename", ProcessCommonJSModules.toModuleName("../src/filename", "current"));
        assertEquals("module$src$foo$filename", ProcessCommonJSModules.toModuleName("../../src/foo/filename", "current"));
    }
    
    @Test
    public void testProcess() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        module.process(null, null);
        // Add assertions here
    }
    
    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules.SuffixVarsCallback callback = new ProcessCommonJSModules.SuffixVarsCallback("");
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        callback.visit(t, n, parent);
        // Add assertions here
    }
}