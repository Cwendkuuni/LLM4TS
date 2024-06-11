import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules();
    assertEquals("module$example", pcm.guessCJSModuleName("example.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$example", ProcessCommonJSModules.toModuleName("example.js"));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$lib$example", ProcessCommonJSModules.toModuleName("./lib/example.js", "test.js"));
  }

  // Add more test cases for other methods in ProcessCommonJSModules class

}