package com.google.javascript.jscomp;
// ProcessCommonJSModulesTest.java

import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    
    String filename1 = "file1.js";
    String expectedModuleName1 = "module$file1";
    String actualModuleName1 = pcm.guessCJSModuleName(filename1);
    assertEquals(expectedModuleName1, actualModuleName1);
    
    String filename2 = "./directory/file2.js";
    String expectedModuleName2 = "module$directory$file2";
    String actualModuleName2 = pcm.guessCJSModuleName(filename2);
    assertEquals(expectedModuleName2, actualModuleName2);
  }
  
  @Test
  public void testToModuleName() {
    String filename1 = "file1.js";
    String expectedModuleName1 = "module$file1";
    String actualModuleName1 = ProcessCommonJSModules.toModuleName(filename1);
    assertEquals(expectedModuleName1, actualModuleName1);
    
    String filename2 = "./directory/file2.js";
    String expectedModuleName2 = "module$directory$file2";
    String actualModuleName2 = ProcessCommonJSModules.toModuleName(filename2);
    assertEquals(expectedModuleName2, actualModuleName2);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./subdirectory/file3.js";
    String currentFilename = "./directory/file2.js";
    String expectedModuleName = "module$directory$subdirectory$file3";
    String actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expectedModuleName, actualModuleName);
  }
  
  @Test
  public void testProcess() {
    // TODO: Write test case for the process method
  }
  
  @Test
  public void testGetModule() {
    // TODO: Write test case for the getModule method
  }
  
}