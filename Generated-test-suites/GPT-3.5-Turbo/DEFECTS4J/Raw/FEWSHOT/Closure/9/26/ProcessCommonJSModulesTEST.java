package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        assertEquals("module$file", pcm.guessCJSModuleName("file"));
        assertEquals("module$file$abc", pcm.guessCJSModuleName("file/abc"));
        assertEquals("module$file$abc$def", pcm.guessCJSModuleName("file/abc/def"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$file", ProcessCommonJSModules.toModuleName("file"));
        assertEquals("module$file$abc", ProcessCommonJSModules.toModuleName("file/abc"));
        assertEquals("module$file$abc$def", ProcessCommonJSModules.toModuleName("file/abc/def"));
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        assertEquals("module$abc",
                ProcessCommonJSModules.toModuleName("abc", "file"));
        assertEquals("module$abc$def",
                ProcessCommonJSModules.toModuleName("abc/def", "file"));
        assertEquals("module$abc$ghi$def",
                ProcessCommonJSModules.toModuleName("../abc/def", "file/ghi/jkl"));
        assertEquals("module$abc$def$ghi",
                ProcessCommonJSModules.toModuleName("./def/ghi", "file/abc"));
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file/");
        Node externs = null;
        Node root = null;
        pcm.process(externs, root);
        // Assert something here
    }

    @Test
    public void testProcessWithFileExtension() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file.js/");
        Node externs = null;
        Node root = null;
        pcm.process(externs, root);
        // Assert something here
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file/");
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
                pcm.new ProcessCommonJsModulesCallback();
        callback.visit(t, n, parent);
        // Assert something here
    }

    @Test
    public void testProcessCommonJsModulesCallbackWithRequireCall() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file/");
        NodeTraversal t = null;
        Node require = null;
        Node parent = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
                pcm.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);
        // Assert something here
    }

    @Test
    public void testProcessCommonJsModulesCallbackWithScriptNode() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file/");
        NodeTraversal t = null;
        Node script = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
                pcm.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);
        // Assert something here
    }

    @Test
    public void testProcessCommonJsModulesCallbackWithModuleExports() {
        AbstractCompiler compiler = null;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "file/");
        NodeTraversal t = null;
        Node prop = null;
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
                pcm.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);
        // Assert something here
    }

    @Test
    public void testSuffixVarsCallback() {
        String suffix = "file";
        ProcessCommonJSModules.SuffixVarsCallback callback =
                new ProcessCommonJSModules().new SuffixVarsCallback(suffix);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        callback.visit(t, n, parent);
        // Assert something here
    }
}