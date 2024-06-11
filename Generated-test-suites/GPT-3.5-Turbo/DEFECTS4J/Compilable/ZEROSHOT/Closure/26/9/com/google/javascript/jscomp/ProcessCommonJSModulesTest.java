package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(null, "");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = processCommonJSModules.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./test2.js";
    String currentFilename = "test1.js";
    String expected = "module$test2";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    // TODO: Implement this test
  }

  @Test
  public void testVisitRequireCall() {
    // TODO: Implement this test
  }

  @Test
  public void testVisitScript() {
    // TODO: Implement this test
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    // TODO: Implement this test
  }

  @Test
  public void testVisitModuleExports() {
    // TODO: Implement this test
  }

  @Test
  public void testSuffixVarsCallback() {
    // TODO: Implement this test
  }
}