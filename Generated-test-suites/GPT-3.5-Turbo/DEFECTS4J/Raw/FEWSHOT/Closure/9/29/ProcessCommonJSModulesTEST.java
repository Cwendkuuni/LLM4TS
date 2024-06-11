package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules processCommonJSModules;
  
  @Before
  public void setup() {
    processCommonJSModules = new ProcessCommonJSModules(null, "");
  }
  
  @Test
  public void testGuessCJSModuleName() {
    String filename1 = "file1.js";
    assertEquals("module$file1", processCommonJSModules.guessCJSModuleName(filename1));
    
    String filename2 = "dir/file2.js";
    assertEquals("module$dir$file2", processCommonJSModules.guessCJSModuleName(filename2));
  }
  
  @Test
  public void testToModuleName() {
    String filename = "dir/file.js";
    assertEquals("module$dir$file", ProcessCommonJSModules.toModuleName(filename));
    
    String requiredFilename1 = "./file.js";
    assertEquals("module$dir$file", ProcessCommonJSModules.toModuleName(requiredFilename1, filename));
    
    String requiredFilename2 = "../file.js";
    assertEquals("module$file", ProcessCommonJSModules.toModuleName(requiredFilename2, filename));
  }
  
  @Test
  public void testToModuleName_noModuleName() {
    String filename = "file.js";
    assertEquals("module$file", ProcessCommonJSModules.toModuleName(filename));
  }
  
  @Test
  public void testProcess() {
    // Need to mock AbstractCompiler and Node
  }
  
  @Test
  public void testGetModule() {
    // Need to mock AbstractCompiler and JSModule
  }
  
  @Test
  public void testVisitRequireCall() {
    // Need to mock NodeTraversal and Node
  }
  
  @Test
  public void testVisitScript() {
    // Need to mock NodeTraversal and Node
  }
  
  @Test
  public void testVisitModuleExports() {
    // Need to mock Node
  }
  
  @Test
  public void testSuffixVarsCallback() {
    // Need to mock NodeTraversal and Node
  }
}