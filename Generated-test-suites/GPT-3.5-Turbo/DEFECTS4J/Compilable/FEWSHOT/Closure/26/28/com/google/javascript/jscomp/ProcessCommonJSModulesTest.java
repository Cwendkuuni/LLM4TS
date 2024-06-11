package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(null, "");
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testGuessCJSModuleNameWithPrefix() {
        String filename = "./src/test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$src$test", moduleName);
    }

    @Test
    public void testGuessCJSModuleNameWithRelativePath() {
        String filename = "../src/test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$src$test", moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleNameWithPrefix() {
        String filename = "./src/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$src$test", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativePath() {
        String filename = "../src/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$src$test", moduleName);
    }

    @Test
    public void testToModuleNameWithRequiredFilename() {
        String requiredFilename = "./src/foo.js";
        String currentFilename = "test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$src$foo", moduleName);
    }

    @Test
    public void testToModuleNameWithRequiredFilenameAndRelativePath() {
        String requiredFilename = "../src/foo.js";
        String currentFilename = "test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$src$foo", moduleName);
    }

    @Test
    public void testProcess() {
        // TODO: Implement test case
    }

    @Test
    public void testGetModule() {
        assertNotNull(processCommonJSModules.getModule());
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Implement test case
    }

    @Test
    public void testVisitScript() {
        // TODO: Implement test case
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Implement test case
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Implement test case
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Implement test case
    }
}