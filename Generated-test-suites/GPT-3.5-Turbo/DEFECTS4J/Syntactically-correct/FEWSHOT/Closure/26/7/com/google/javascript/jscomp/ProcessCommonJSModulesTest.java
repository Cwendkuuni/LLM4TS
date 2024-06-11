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
        String filename = "path/to/file.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$path$to$file", moduleName);

        filename = "./file.js";
        moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);

        filename = "../file.js";
        moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);

        filename = "file.js";
        moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = "path/to/file.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$path$to$file", moduleName);

        filename = "./file.js";
        moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);

        filename = "../file.js";
        moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);

        filename = "file.js";
        moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertNotNull(moduleName);
        assertEquals("module$file", moduleName);
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String requiredFilename = "./foo/bar.js";
        String currentFilename = "path/to/file.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertNotNull(moduleName);
        assertEquals("module$path$to$foo$bar", moduleName);

        requiredFilename = "../foo/bar.js";
        currentFilename = "path/to/file.js";
        moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertNotNull(moduleName);
        assertEquals("module$path$to$foo$bar", moduleName);

        requiredFilename = "foo/bar.js";
        currentFilename = "path/to/file.js";
        moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertNotNull(moduleName);
        assertEquals("module$path$to$foo$bar", moduleName);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = ".path/to/file.js";
        String normalizedFilename = processCommonJSModules.normalizeSourceName(filename);
        assertNotNull(normalizedFilename);
        assertEquals("path/to/file.js", normalizedFilename);

        filename = "path/to/file.js";
        normalizedFilename = processCommonJSModules.normalizeSourceName(filename);
        assertNotNull(normalizedFilename);
        assertEquals("path/to/file.js", normalizedFilename);

        filename = "file.js";
        normalizedFilename = processCommonJSModules.normalizeSourceName(filename);
        assertNotNull(normalizedFilename);
        assertEquals("file.js", normalizedFilename);
    }

    @Test
    public void testProcess() {
        // TODO: Implement test method
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Implement test method
    }

    @Test
    public void testVisitScript() {
        // TODO: Implement test method
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Implement test method
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Implement test method
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Implement test method
    }
}