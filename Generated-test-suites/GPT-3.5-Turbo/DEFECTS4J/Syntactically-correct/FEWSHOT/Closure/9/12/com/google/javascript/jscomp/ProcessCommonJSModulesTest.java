package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".//");
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$foo", processCommonJSModules.guessCJSModuleName("foo.js"));
        assertEquals("module$bar", processCommonJSModules.guessCJSModuleName("dir/bar.js"));
        assertEquals("module$baz", processCommonJSModules.guessCJSModuleName("/baz.js"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$foo", ProcessCommonJSModules.toModuleName("foo.js"));
        assertEquals("module$bar", ProcessCommonJSModules.toModuleName("dir/bar.js"));
        assertEquals("module$baz", ProcessCommonJSModules.toModuleName("/baz.js"));
    }

    @Test
    public void testToModuleNameRelativeToCurrent() {
        assertEquals("module$dir$bar", ProcessCommonJSModules.toModuleName("dir/bar.js", "dir/foo.js"));
        assertEquals("module$dir$baz", ProcessCommonJSModules.toModuleName("../dir/baz.js", "dir/foo.js"));
        assertEquals("module$foo", ProcessCommonJSModules.toModuleName("foo.js", ""));
        assertEquals("module$otherDir$otherDir2$bar", ProcessCommonJSModules.toModuleName("otherDir/otherDir2/bar.js",
                "dir/foo.js"));
    }

    @Test
    public void testProcess() {
        // TODO: Write test cases for process method
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Write test cases for visitRequireCall method
    }

    @Test
    public void testVisitScript() {
        // TODO: Write test cases for visitScript method
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Write test cases for emitOptionalModuleExportsOverride method
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Write test cases for visitModuleExports method
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Write test cases for SuffixVarsCallback class
    }
}