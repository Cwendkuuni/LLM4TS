package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, null);
        
        String filename1 = "./name";
        String expected1 = "module$name";
        assertEquals(expected1, processCommonJSModules.guessCJSModuleName(filename1));
        
        String filename2 = "../name";
        String currentFilename2 = "foo/bar";
        String expected2 = "module$foo$name";
        assertEquals(expected2, processCommonJSModules.toModuleName(filename2, currentFilename2));
    }
    
    @Test
    public void testToModuleName() {
        String filename1 = "./name";
        String expected1 = "module$name";
        assertEquals(expected1, ProcessCommonJSModules.toModuleName(filename1));
        
        String filename2 = "../name";
        String expected2 = "module$name";
        assertEquals(expected2, ProcessCommonJSModules.toModuleName(filename2));
    }
    
    @Test
    public void testProcess() {
        // Add test code
    }
    
    @Test
    public void testGetModule() {
        // Add test code
    }
    
    @Test
    public void testNormalizeSourceName() {
        // Add test code
    }
    
    @Test
    public void testToModuleNameWithCurrentFilename() {
        // Add test code
    }
    
    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // Add test code
    }
    
    @Test
    public void testVisitRequireCall() {
        // Add test code
    }
    
    @Test
    public void testVisitScript() {
        // Add test code
    }
    
    @Test
    public void testVisitModuleExports() {
        // Add test code
    }
    
    @Test
    public void testSuffixVarsCallback() {
        // Add test code
    }
}
