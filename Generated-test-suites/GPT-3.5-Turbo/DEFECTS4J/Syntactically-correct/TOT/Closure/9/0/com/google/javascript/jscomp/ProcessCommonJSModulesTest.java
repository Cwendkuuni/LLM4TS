import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  @Test
  public void testProcess() {
    // Test for normal input
    String filenamePrefix = "./";
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
    String moduleName = pcm.toModuleName("test.js");
    assertEquals("module$test", moduleName);
    
    // Test for input with multiple slashes
    String moduleName2 = pcm.toModuleName("test/folder/script.js");
    assertEquals("module$test$folder$script", moduleName2);
    
    // Test for input with .js extension
    String moduleName3 = pcm.toModuleName("script.js");
    assertEquals("module$script", moduleName3);
  }
  
  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
    
    // Test for normal input
    String moduleName = pcm.guessCJSModuleName("test.js");
    assertEquals("module$test", moduleName);
    
    // Test for input with multiple slashes
    String moduleName2 = pcm.guessCJSModuleName("test/folder/script.js");
    assertEquals("module$test$folder$script", moduleName2);
    
    // Test for input with .js extension
    String moduleName3 = pcm.guessCJSModuleName("script.js");
    assertEquals("module$script", moduleName3);
  }
  
  @Test
  public void testToModuleName() {
    // Test for normal input
    String moduleName = ProcessCommonJSModules.toModuleName("test.js");
    assertEquals("module$test", moduleName);
    
    // Test for input with multiple slashes
    String moduleName2 = ProcessCommonJSModules.toModuleName("test/folder/script.js");
    assertEquals("module$test$folder$script", moduleName2);
    
    // Test for input with .js extension
    String moduleName3 = ProcessCommonJSModules.toModuleName("script.js");
    assertEquals("module$script", moduleName3);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./folder/test.js";
    String currentFilename = "main.js";
    
    // Test for relative addressing with single .
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals("module$folder$test", moduleName);
    
    requiredFilename = "../folder/test.js";
    currentFilename = "main/folder/script.js";
    
    // Test for relative addressing with ..
    String moduleName2 = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals("module$folder$test", moduleName2);
  }
  
  @Test
  public void testProcessModuleExports() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
    
    // Test for normal input
    Node prop = new Node(new Node(Token.NAME, "module"), new Node(Token.GETPROP, new Node(Token.NAME, "exports")));
    pcm.visitModuleExports(prop);
    assertTrue(prop.getString().equals("module$test"));
    assertTrue(prop.getChildAtIndex(1).getString().equals("module$exports"));

    // Test for input with different module
    Node prop2 = new Node(new Node(Token.NAME, "module2"), new Node(Token.GETPROP, new Node(Token.NAME, "exports")));
    pcm.visitModuleExports(prop2);
    assertTrue(prop2.getString().equals("module2"));
    assertTrue(prop2.getChildAtIndex(1).getString().equals("module2$exports"));
  }
  
  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
    ProcessCommonJSModules.SuffixVarsCallback svc = pcm.new SuffixVarsCallback("test");
    
    // Test for normal input
    NodeTraversal t = new NodeTraversal(compiler, new EmptyErrorReport());
    Node root = new Node(Token.SCRIPT);
    root.addChildToFront(new Node(Token.NAME, "var1"));
    root.addChildToBack(new Node(Token.NAME, "exports"));
    t.traverse(root);
    assertTrue(root.getChildAtIndex(0).getString().equals("var1$$test"));
    assertTrue(root.getChildAtIndex(1).getString().equals("exports$test"));
    
    // Test for input with no match
    Node root2 = new Node(Token.SCRIPT);
    root2.addChildToFront(new Node(Token.NAME, "var2"));
    root2.addChildToBack(new Node(Token.NAME, "exports"));
    t.traverse(root2);
    assertTrue(root2.getChildAtIndex(0).getString().equals("var2"));
    assertTrue(root2.getChildAtIndex(1).getString().equals("exports"));
  }
  
}