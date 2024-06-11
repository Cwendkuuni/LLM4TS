import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules();
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = processCommonJSModules.guessCJSModuleName(filename);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameRelative() {
    String requiredFilename = "./test.js";
    String currentFilename = "src/test.js";
    String expected = "module$src$test";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameRelativeParent() {
    String requiredFilename = "../test.js";
    String currentFilename = "src/test.js";
    String expected = "module$src$test$test";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    // Not implemented yet
  }

  @Test
  public void testGetModule() {
    // Not implemented yet
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    // Not implemented yet
  }

  @Test
  public void testVisitRequireCall() {
    // Not implemented yet
  }

  @Test
  public void testVisitScript() {
    // Not implemented yet
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    // Not implemented yet
  }

  @Test
  public void testVisitModuleExports() {
    // Not implemented yet
  }

  @Test
  public void testSuffixVarsCallback() {
    // Not implemented yet
  }
}