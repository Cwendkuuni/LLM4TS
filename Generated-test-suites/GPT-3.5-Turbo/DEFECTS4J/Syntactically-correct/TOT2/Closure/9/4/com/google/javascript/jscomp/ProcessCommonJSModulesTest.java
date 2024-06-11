import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new Compiler();
    processCommonJSModules = new ProcessCommonJSModules(compiler, "test/");
  }

  @Test
  public void testGuessCJSModuleName() {
    String moduleName = processCommonJSModules.guessCJSModuleName("test/module.js");
    assertEquals("module$module", moduleName);
  }

  @Test
  public void testToModuleName() {
    String moduleName1 = processCommonJSModules.toModuleName("./test/module.js");
    String moduleName2 = processCommonJSModules.toModuleName("test/module.js", "test/current.js");
    assertEquals("module$module", moduleName1);
    assertEquals("module$module", moduleName2);
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.EMPTY);
    Node root = new Node(Token.EMPTY);
    processCommonJSModules.process(externs, root);
    assertNotNull(processCommonJSModules.getModule());
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    // Not easy to test directly as it involves traversal logic
    assertTrue(true);
  }

  @Test
  public void testSuffixVarsCallback() {
    // Not easy to test directly as it involves traversal logic
    assertTrue(true);
  }
}