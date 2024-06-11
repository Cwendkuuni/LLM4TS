package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX);
    assertEquals("module$filename", pcm.guessCJSModuleName("./*filename"));
    assertEquals("module$filename", pcm.guessCJSModuleName("./filename"));
    assertEquals("module$filename", pcm.guessCJSModuleName("filename"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$filename$prefix", ProcessCommonJSModules.toModuleName("./*filename$prefix"));
    assertEquals("module$filename$prefix", ProcessCommonJSModules.toModuleName("./filename$prefix"));
    assertEquals("module$filename$prefix", ProcessCommonJSModules.toModuleName("filename$prefix"));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$requiredFilename", ProcessCommonJSModules.toModuleName("./*requiredFilename", "currentFilename"));
    assertEquals("module$requiredFilename", ProcessCommonJSModules.toModuleName("../requiredFilename", "currentFilename"));
    assertEquals("module$requiredFilename", ProcessCommonJSModules.toModuleName("requiredFilename", "currentFilename"));
  }

  // Add more test cases for other public methods

}
