package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testProcess() throws Exception {
        // TODO: Implement test case
    }

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, null);
        String cjsModuleName = processCommonJSModules.guessCJSModuleName("filename.js");
        assertEquals("module$filename", cjsModuleName);
    }

    @Test
    public void testToModuleName() {
        String moduleName = ProcessCommonJSModules.toModuleName("filename.js");
        assertEquals("module$filename", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("./filename.js");
        assertEquals("module$filename", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("../filename.js");
        assertEquals("module$filename", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("test/filename.js");
        assertEquals("module$test$filename", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("test/filename-min.js");
        assertEquals("module$test$filename_min", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("test/filename-test.js");
        assertEquals("module$test$filename_test", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String moduleName = ProcessCommonJSModules.toModuleName("./required.js", "current.js");
        assertEquals("module$current$required", moduleName);

        moduleName = ProcessCommonJSModules.toModuleName("../required.js", "current/directory/current.js");
        assertEquals("module$required", moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        // TODO: Implement test case
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