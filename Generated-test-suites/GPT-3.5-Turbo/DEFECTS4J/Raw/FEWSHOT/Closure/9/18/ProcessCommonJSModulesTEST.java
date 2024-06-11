package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".");
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
        assertEquals("module$folder$filename", processCommonJSModules.guessCJSModuleName("folder/filename"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("./filename"));
        assertEquals("module$folder$filename", processCommonJSModules.guessCJSModuleName("../folder/filename"));
        assertEquals("module$folder$filename", processCommonJSModules.guessCJSModuleName("folder/../folder/filename"));
        assertEquals("module$folder$filename", processCommonJSModules.guessCJSModuleName("folder/./folder/filename"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
        assertEquals("module$folder$filename", ProcessCommonJSModules.toModuleName("folder/filename"));
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("./filename"));
        assertEquals("module$folder$filename", ProcessCommonJSModules.toModuleName("../folder/filename"));
        assertEquals("module$folder$filename", ProcessCommonJSModules.toModuleName("folder/../folder/filename"));
        assertEquals("module$folder$filename", ProcessCommonJSModules.toModuleName("folder/./folder/filename"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$folder$filename",
                ProcessCommonJSModules.toModuleName("filename", "folder/"));
        assertEquals("module$folder$folder$filename",
                ProcessCommonJSModules.toModuleName("../filename", "folder/"));
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        processCommonJSModules.process(externs, root);
    }
}