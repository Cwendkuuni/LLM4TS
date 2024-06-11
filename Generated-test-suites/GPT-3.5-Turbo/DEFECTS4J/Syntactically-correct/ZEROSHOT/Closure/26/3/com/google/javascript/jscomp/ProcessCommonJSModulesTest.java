import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules process;
  
  @Before
  public void setUp() {
    process = new ProcessCommonJSModules(null, "");
  }
  
  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$file1", process.guessCJSModuleName("file1"));
    assertEquals("module$functions$file2", process.guessCJSModuleName("functions/file2"));
    assertEquals("module$my$module$file3", process.guessCJSModuleName("my/module/file3"));
  }
  
  @Test
  public void testToModuleName() {
    assertEquals("module$file1", ProcessCommonJSModules.toModuleName("file1"));
    assertEquals("module$functions$file2", ProcessCommonJSModules.toModuleName("functions/file2"));
    assertEquals("module$my$module$file3", ProcessCommonJSModules.toModuleName("my/module/file3"));
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$file1", ProcessCommonJSModules.toModuleName("file1", "file1"));
    assertEquals("module$file2", ProcessCommonJSModules.toModuleName("./file2", "file1"));
    assertEquals("module$my$module$file3", ProcessCommonJSModules.toModuleName("../my/module/file3", "file1"));
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