import org.junit.*;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules tester = new ProcessCommonJSModules();
    
    // Test with leading "./"
    String result1 = tester.guessCJSModuleName("./test/module.js");
    Assert.assertEquals("module$test$module", result1);
    
    // Test with leading "../"
    String result2 = tester.guessCJSModuleName("../test/module.js");
    Assert.assertEquals("module$test$module", result2);
    
    // Test without leading "./" or "../"
    String result3 = tester.guessCJSModuleName("test/module.js");
    Assert.assertEquals("module$test$module", result3);
    
    // Test with "." in the name
    String result4 = tester.guessCJSModuleName("./test/module.test.js");
    Assert.assertEquals("module$test$module$test", result4);
    
    // Test without leading "./" or "../" and with "." in the name
    String result5 = tester.guessCJSModuleName("test/module.test.js");
    Assert.assertEquals("module$test$module$test", result5);
  }

  @Test
  public void testToModuleName() {
    ProcessCommonJSModules tester = new ProcessCommonJSModules();
    
    // Test with leading "./"
    String result1 = tester.toModuleName("./test/module.js");
    Assert.assertEquals("module$test$module", result1);
    
    // Test with leading "../"
    String result2 = tester.toModuleName("../test/module.js");
    Assert.assertEquals("module$test$module", result2);
    
    // Test without leading "./" or "../"
    String result3 = tester.toModuleName("test/module.js");
    Assert.assertEquals("module$test$module", result3);
    
    // Test with "." in the name
    String result4 = tester.toModuleName("./test/module.test.js");
    Assert.assertEquals("module$test$module$test", result4);
    
    // Test without leading "./" or "../" and with "." in the name
    String result5 = tester.toModuleName("test/module.test.js");
    Assert.assertEquals("module$test$module$test", result5);
  }

  @Test
  public void testToModuleNameWithcurrentFilename() {
    ProcessCommonJSModules tester = new ProcessCommonJSModules();
    
    String result1 = tester.toModuleName("./module.js", "test/base.js");
    Assert.assertEquals("module$test$module", result1);
    
    String result2 = tester.toModuleName("../module.js", "test/base.js");
    Assert.assertEquals("module$module", result2);
    
    String result3 = tester.toModuleName("module.js", "test/base.js");
    Assert.assertEquals("module$test$module", result3);
    
    String result4 = tester.toModuleName("./module.test.js", "test/base.js");
    Assert.assertEquals("module$test$module$test", result4);
    
    String result5 = tester.toModuleName("module.test.js", "test/base.js");
    Assert.assertEquals("module$test$module$test", result5);
  }

}