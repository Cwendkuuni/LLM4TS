package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
    
    private AbstractCompiler compiler;
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("./filename"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("../filename"));
        assertEquals("module$_filename", processCommonJSModules.guessCJSModuleName("./_filename"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("./filename"));
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("../filename"));
        assertEquals("module$_filename", ProcessCommonJSModules.toModuleName("./_filename"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$required", ProcessCommonJSModules.toModuleName("required", "current"));
        assertEquals("module$required", ProcessCommonJSModules.toModuleName("./required", "current"));
        assertEquals("module$current$required", ProcessCommonJSModules.toModuleName("../required", "current"));
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
        processCommonJSModules.process(externs, root);

        // Assert something
    }

    @Test
    public void testVisit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visit(t, n, parent);

        // Assert something
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = null;
        Node require = null;
        Node parent = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);

        // Assert something
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = null;
        Node script = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);

        // Assert something
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = null;
        String moduleName = "";
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, moduleName);

        // Assert something
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);

        // Assert something
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        String suffix = "";
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback(suffix);
        callback.visit(t, n, parent);

        // Assert something
    }
}