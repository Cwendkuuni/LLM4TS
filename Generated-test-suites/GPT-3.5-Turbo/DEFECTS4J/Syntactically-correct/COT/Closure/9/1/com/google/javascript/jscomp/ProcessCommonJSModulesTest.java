
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test/module.js";
    String expected = "module$test$module";
    String actual = processCommonJSModules.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test/module.js";
    String expected = "module$test$module";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./test/module.js";
    String currentFilename = "parent.js";
    String expected = "module$test$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativeAddressingAndParentDirectory() {
    String requiredFilename = "../test/module.js";
    String currentFilename = "parent.js";
    String expected = "module$test$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    // TODO: Test process method
  }

}
