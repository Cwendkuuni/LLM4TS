import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    
    @Test
    public void testToModuleName() {
        String moduleName1 = ProcessCommonJSModules.toModuleName("./test.js");
        assertEquals("module$test", moduleName1);
        
        String moduleName2 = ProcessCommonJSModules.toModuleName("./nested/test.js", "module$root");
        assertEquals("module$root$nested$test", moduleName2);
        
        String moduleName3 = ProcessCommonJSModules.toModuleName("../util.js", "module$script");
        assertEquals("module$util", moduleName3);
    }
    
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, ".");
        String guessedModuleName = pcm.guessCJSModuleName("./test.js");
        assertEquals("module$test", guessedModuleName);
    }
    
    @Test
    public void testProcess() {
        // TODO: Write test case for ProcessCommonJSModules.process()
    }
    
    // TODO: Add more test cases for other public methods in ProcessCommonJSModules class
    
}