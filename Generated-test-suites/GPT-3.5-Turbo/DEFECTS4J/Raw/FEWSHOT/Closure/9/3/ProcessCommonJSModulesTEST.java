package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "."); 
        assertEquals("module$filename", pcm.guessCJSModuleName("filename.js"));
        assertEquals("module$dir$filename", pcm.guessCJSModuleName("dir/filename.js"));
        assertEquals("module$dir1$dir2$filename", pcm.guessCJSModuleName("dir1/dir2/filename.js"));
        assertEquals("module$..filename", pcm.guessCJSModuleName("../filename.js"));
        assertEquals("module$..$filename", pcm.guessCJSModuleName("../../filename.js"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename.js"));
        assertEquals("module$dir$filename", ProcessCommonJSModules.toModuleName("dir/filename.js"));
        assertEquals("module$dir1$dir2$filename", ProcessCommonJSModules.toModuleName("dir1/dir2/filename.js"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename.js", "path/currentfile.js"));
        assertEquals("module$dir$filename", ProcessCommonJSModules.toModuleName("dir/filename.js", "path/currentfile.js"));
        assertEquals("module$dir1$dir2$filename", ProcessCommonJSModules.toModuleName("dir1/dir2/filename.js", "path/currentfile.js"));
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.process(null, null);
    }

    @Test
    public void testVisitRequireCall() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.visitRequireCall(null, null, null);
    }

    @Test
    public void testVisitScript() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.visitScript(null, null);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.emitOptionalModuleExportsOverride(null, null);
    }

    @Test
    public void testVisitModuleExports() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        pcm.visitModuleExports(null);
    }

    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules.SuffixVarsCallback svc = null;
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        svc.visit(t, n, parent);
    }
}