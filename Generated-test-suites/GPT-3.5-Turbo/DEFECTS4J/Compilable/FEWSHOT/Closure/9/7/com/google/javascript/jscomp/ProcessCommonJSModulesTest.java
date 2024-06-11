package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processor;

    @Before
    public void SetUp() {
        processor = new ProcessCommonJSModules(null, "");
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$test", processor.guessCJSModuleName("test.js"));
        assertEquals("module$dir$test", processor.guessCJSModuleName("dir/test.js"));
        assertEquals("module$$test", processor.guessCJSModuleName("./test.js"));
        assertEquals("module$dir$test", processor.guessCJSModuleName("./dir/test.js"));
        assertEquals("module$$test", processor.guessCJSModuleName("../test.js"));
        assertEquals("module$dir$test", processor.guessCJSModuleName("../dir/test.js"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$test", processor.toModuleName("test.js"));
        assertEquals("module$test", processor.toModuleName("./test.js"));
        assertEquals("module$dir$test", processor.toModuleName("dir/test.js"));
        assertEquals("module$dir$test", processor.toModuleName("./dir/test.js"));
        assertEquals("module$$test", processor.toModuleName("../test.js"));
        assertEquals("module$dir$test", processor.toModuleName("../dir/test.js"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$test", processor.toModuleName("test.js", "index.js"));
        assertEquals("module$test", processor.toModuleName("./test.js", "index.js"));
        assertEquals("module$dir$test", processor.toModuleName("dir/test.js", "index.js"));
        assertEquals("module$dir$test", processor.toModuleName("./dir/test.js", "index.js"));
        assertEquals("module$index$test", processor.toModuleName("../test.js", "index.js"));
        assertEquals("module$index$dir$test", processor.toModuleName("../dir/test.js", "index.js"));
    }

    @Test
    public void testProcess() {
        // Test process method here
    }
}