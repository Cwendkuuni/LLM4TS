import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(null, "");
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$test", processCommonJSModules.guessCJSModuleName("/test"));
    assertEquals("module$sub$test", processCommonJSModules.guessCJSModuleName("/sub/test"));
    assertEquals("module$sub$sub$test", processCommonJSModules.guessCJSModuleName("/sub/sub/test"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$test", ProcessCommonJSModules.toModuleName("/test"));
    assertEquals("module$sub$test", ProcessCommonJSModules.toModuleName("/sub/test"));
    assertEquals("module$sub$sub$test", ProcessCommonJSModules.toModuleName("/sub/sub/test"));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$sub$sub$sub$test", ProcessCommonJSModules.toModuleName("./sub/sub/test", "/test"));
    assertEquals("module$sub$test", ProcessCommonJSModules.toModuleName("../sub/test", "/sub/sub/test"));
  }

}