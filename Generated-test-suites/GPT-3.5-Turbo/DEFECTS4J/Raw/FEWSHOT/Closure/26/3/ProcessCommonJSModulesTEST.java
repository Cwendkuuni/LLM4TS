package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    @Test
    public void testToModuleName() {
        assertEquals("module$filename",
                ProcessCommonJSModules.toModuleName("filename"));
        assertEquals("module$test$filename",
                ProcessCommonJSModules.toModuleName("test/filename"));
        assertEquals("module$directory$filename",
                ProcessCommonJSModules.toModuleName("directory/filename"));
        assertEquals("module$directory$test$filename",
                ProcessCommonJSModules.toModuleName("directory/test/filename"));
        assertEquals("module$directory$filename$_",
                ProcessCommonJSModules.toModuleName("directory/filename-"));
        assertEquals("module$_",
                ProcessCommonJSModules.toModuleName("."));
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        assertEquals("module$directory$filename",
                ProcessCommonJSModules.toModuleName("filename", "directory"));
        assertEquals("module$directory$test$filename",
                ProcessCommonJSModules.toModuleName("test/filename", "directory"));
        assertEquals("module$directory$filename",
                ProcessCommonJSModules.toModuleName("./filename", "directory"));
        assertEquals("module$directory$test$filename",
                ProcessCommonJSModules.toModuleName("./test/filename", "directory"));
        assertEquals("module$subdirectory$filename",
                ProcessCommonJSModules.toModuleName("../subdirectory/filename", "directory/test"));
        assertEquals("module$filename",
                ProcessCommonJSModules.toModuleName("filename", "."));
    }

    @Test(expected = RuntimeException.class)
    public void testToModuleNameWithRelativeAddressingThrowsException() {
        ProcessCommonJSModules.toModuleName("../subdirectory/filename", "directory");
    }

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = 
        		new ProcessCommonJSModules(new AbstractCompiler(), ".");
        assertEquals("module$test$filename",
                pcm.guessCJSModuleName("test/filename.js"));
        assertEquals("module$filename",
                pcm.guessCJSModuleName("filename.js"));
        assertEquals("module$test$filename",
                pcm.guessCJSModuleName("./test/filename.js"));
        assertEquals("module$filename",
                pcm.guessCJSModuleName("./filename.js"));
        assertEquals("module$subdirectory$filename",
                pcm.guessCJSModuleName("../subdirectory/filename.js"));
    }

    @Test
    public void testGetModule() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".");
        JSModule expectedModule = new JSModule("module");
        pcm.module = expectedModule;
        assertEquals(expectedModule, pcm.getModule());
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".");
        Node externs = null;
        Node root = new Node();
        pcm.process(externs, root);
    }
}