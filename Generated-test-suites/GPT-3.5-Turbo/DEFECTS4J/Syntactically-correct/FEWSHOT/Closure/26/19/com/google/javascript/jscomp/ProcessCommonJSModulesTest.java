package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private String filenamePrefix;
    private boolean reportDependencies;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        filenamePrefix = "";
        reportDependencies = true;
    }

    @Test
    public void testProcess() {
        // TODO: Implement this test
    }

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        String moduleName = module.guessCJSModuleName("test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleName() {
        String moduleName = ProcessCommonJSModules.toModuleName("test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String moduleName = ProcessCommonJSModules.toModuleName("./test.js", "main.js");
        assertEquals("module$test", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("../test.js", "main.js");
        assertEquals("module$..$test", moduleName);
    }

    @Test
    public void testNormalizeSourceName() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, "prefix/");
        String normalizedFilename = module.normalizeSourceName("prefix/test.js");
        assertEquals("test.js", normalizedFilename);

        normalizedFilename = module.normalizeSourceName("test.js");
        assertEquals("test.js", normalizedFilename);
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Implement this test
    }

    @Test
    public void testVisitScript() {
        // TODO: Implement this test
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Implement this test
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Implement this test
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Implement this test
    }
}