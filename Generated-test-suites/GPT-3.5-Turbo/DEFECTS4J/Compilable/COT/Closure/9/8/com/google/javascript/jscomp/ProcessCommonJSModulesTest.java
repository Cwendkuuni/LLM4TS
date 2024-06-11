package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
    assertEquals("module$filename", pcm.guessCJSModuleName("./filename"));
    assertEquals("module$dir$filename", pcm.guessCJSModuleName("../dir/filename"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$filename", ProcessCommonJSModules.toModuleName("./filename"));
    assertEquals("module$dir$filename", ProcessCommonJSModules.toModuleName("../dir/filename"));
  }

  @Test
  public void testToModuleNameWithCurrentFilename() {
    assertEquals("module$filename", ProcessCommonJSModules.toModuleName("./filename", "current"));
    assertEquals("module$dir1$filename", ProcessCommonJSModules.toModuleName("../dir1/filename", "current"));
    assertEquals("module$filename", ProcessCommonJSModules.toModuleName("./filename.js", "current"));
    assertEquals("module$dir1$filename", ProcessCommonJSModules.toModuleName("../dir1/filename.js", "current"));
  }

  @Test
  public void testProcess() {
    // TODO: Write test case for process method
  }

  @Test
  public void testVisitRequireCall() {
    // TODO: Write test case for visitRequireCall method
  }

  @Test
  public void testVisitScript() {
    // TODO: Write test case for visitScript method
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    // TODO: Write test case for emitOptionalModuleExportsOverride method
  }

  @Test
  public void testVisitModuleExports() {
    // TODO: Write test case for visitModuleExports method
  }

  @Test
  public void testSuffixVarsCallback() {
    // TODO: Write test case for SuffixVarsCallback class
  }
}