package com.google.javascript.jscomp;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, null);
        String result = processCommonJSModules.guessCJSModuleName("example.js");
        assertEquals("module$example", result);
    }

    @Test
    public void testToModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, null);
        String result = processCommonJSModules.toModuleName("./example.js");
        assertEquals("module$example", result);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, null);
        String result = processCommonJSModules.toModuleName("../example.js", "/src/main.js");
        assertEquals("module$src$example", result);
    }

    @Test
    public void testProcess() {
        // TODO: Write test case for the process() method
    }

    @Test
    public void testGetModule() {
        // TODO: Write test case for the getModule() method
    }
}