import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    String filename1 = "./dir1/file1.js";
    String filename2 = "../dir2/file2.js";
    
    String result1 = pcm.guessCJSModuleName(filename1);
    String result2 = pcm.guessCJSModuleName(filename2);
    
    assertEquals("module$file1", result1);
    assertEquals("module$file2", result2);
  }
  
  @Test
  public void testToModuleName() {
    String filename1 = "./dir1/file1.js";
    String filename2 = "../dir2/file2.js";
    
    String result1 = ProcessCommonJSModules.toModuleName(filename1);
    String result2 = ProcessCommonJSModules.toModuleName(filename2);
    
    assertEquals("module$file1", result1);
    assertEquals("module$file2", result2);
  }
  
  @Test
  public void testToModuleName_RelativeAddressing() {
    String requiredFilename1 = "./dir1/required1.js";
    String requiredFilename2 = "../dir2/required2.js";
    String currentFilename1 = "./dir1/current1.js";
    String currentFilename2 = "../dir2/current2.js";
    
    String result1 = ProcessCommonJSModules.toModuleName(requiredFilename1, currentFilename1);
    String result2 = ProcessCommonJSModules.toModuleName(requiredFilename2, currentFilename2);
    
    assertEquals("module$dir1$required1", result1);
    assertEquals("module$dir2$required2", result2);
  }
  
  @Test
  public void testProcess() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    Node externs = null;
    Node root = null;
    
    pcm.process(externs, root);
    
    // assert something
  }
  
  @Test
  public void testVisitRequireCall() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    NodeTraversal t = null;
    Node require = null;
    Node parent = null;
    
    // create some nodes and setup test
    
    pcm.visitRequireCall(t, require, parent);
    
    // assert something
  }
  
  @Test
  public void testVisitScript() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    NodeTraversal t = null;
    Node script = null;
    
    // create some nodes and setup test
    
    pcm.visitScript(t, script);
    
    // assert something
  }
  
  @Test
  public void testEmitOptionalModuleExportsOverride() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    Node script = null;
    String moduleName = "module$file";
    
    // create some nodes and setup test
    
    pcm.emitOptionalModuleExportsOverride(script, moduleName);
    
    // assert something
  }
  
  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    Node prop = null;
    
    // create some nodes and setup test
    
    pcm.visitModuleExports(prop);
    
    // assert something
  }
  
  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules.SuffixVarsCallback svc = new ProcessCommonJSModules.SuffixVarsCallback("module$file");
    NodeTraversal t = null;
    Node node = null;
    Node parent = null;
    
    // create some nodes and setup test
    
    svc.visit(t, node, parent);
    
    // assert something
  }
}