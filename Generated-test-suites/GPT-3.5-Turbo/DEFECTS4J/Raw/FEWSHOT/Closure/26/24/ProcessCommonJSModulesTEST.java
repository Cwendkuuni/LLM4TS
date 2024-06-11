package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules processCommonJSModules;
  
  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules();
  }
  
  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$example", processCommonJSModules.guessCJSModuleName(".example.js"));
    assertEquals("module$example_subfolder", processCommonJSModules.guessCJSModuleName("subfolder/example.js"));
    assertEquals("module$subfolder_module$example", processCommonJSModules.guessCJSModuleName("subfolder/module/example.js"));
  }
  
  @Test
  public void testToModuleName() {
    assertEquals("module$example", ProcessCommonJSModules.toModuleName(".example.js"));
    assertEquals("module$example_subfolder", ProcessCommonJSModules.toModuleName("subfolder/example.js"));
    assertEquals("module$subfolder_module$example", ProcessCommonJSModules.toModuleName("subfolder/module/example.js"));
  }
  
  @Test
  public void testToModuleNameWithCurrentFilename() {
    assertEquals("module$currentfolder_subfolder", ProcessCommonJSModules.toModuleName("subfolder", "currentfolder/example.js"));
    assertEquals("module$currentfolder_module$subfolder_module$example", ProcessCommonJSModules.toModuleName("module/example", "currentfolder/example.js"));
  }
  
  @Test
  public void testProcess() {
    // TODO: Add test case for the process method
  }
  
  @Test
  public void testVisitRequireCall() {
    // TODO: Add test case for the visitRequireCall method
  }
  
  @Test
  public void testVisitScript() {
    // TODO: Add test case for the visitScript method
  }
  
  @Test
  public void testEmitOptionalModuleExportsOverride() {
    // TODO: Add test case for the emitOptionalModuleExportsOverride method
  }
  
  @Test
  public void testVisitModuleExports() {
    // TODO: Add test case for the visitModuleExports method
  }
  
  @Test
  public void testSuffixVarsCallback() {
    // TODO: Add test case for the SuffixVarsCallback class
  }

}