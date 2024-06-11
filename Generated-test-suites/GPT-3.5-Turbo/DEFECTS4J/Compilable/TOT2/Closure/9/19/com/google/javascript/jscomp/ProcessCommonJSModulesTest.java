package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    assertEquals("module$example", pcm.guessCJSModuleName("example.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$example", ProcessCommonJSModules.toModuleName("example.js"));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$example", ProcessCommonJSModules.toModuleName("./example.js", "folder/"));
  }

  @Test
  public void testProcess() {
    // Write test case for the process method here
  }

  @Test
  public void testGetModule() {
    // Write test case for the getModule method here
  }

  @Test
  public void testSuffixVarsCallback() {
    // Write test case for the SuffixVarsCallback class here
  }
}