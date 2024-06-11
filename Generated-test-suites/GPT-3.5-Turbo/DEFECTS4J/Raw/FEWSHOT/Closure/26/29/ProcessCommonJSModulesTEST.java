package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename.js"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("./filename.js"));
        assertEquals("module$module1$filename", processCommonJSModules.guessCJSModuleName("../module1/filename.js"));
        assertEquals("module$module2$filename", processCommonJSModules.guessCJSModuleName("../../module2/filename.js"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename.js"));
        assertEquals("module$module1$filename", ProcessCommonJSModules.toModuleName("../module1/filename.js"));
        assertEquals("module$module2$filename", ProcessCommonJSModules.toModuleName("../../module2/filename.js"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename.js", "current.js"));
        assertEquals("module$module1$filename", ProcessCommonJSModules.toModuleName("../module1/filename.js", "current.js"));
        assertEquals("module$module2$filename", ProcessCommonJSModules.toModuleName("../../module2/filename.js", "current.js"));
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        Node externs = new Node();
        Node root = new Node();
        processCommonJSModules.process(externs, root);
    }

    @Test
    public void testProcessCallback() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node();
        Node parent = new Node();
        callback.visit(t, n, parent);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node require = new Node();
        Node parent = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node script = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node();
        String moduleName = "module$filename";
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, moduleName);
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node();
        Node parent = new Node();
        String suffix = "module$filename";
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback(suffix);
        callback.visit(t, n, parent);
    }
}