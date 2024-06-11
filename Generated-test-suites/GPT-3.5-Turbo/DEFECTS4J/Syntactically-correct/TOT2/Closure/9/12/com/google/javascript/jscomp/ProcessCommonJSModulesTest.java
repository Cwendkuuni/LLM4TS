import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testToModuleName() {
        // Typical use case
        assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js"));
        
        // Edge case: Leading ./
        assertEquals("module$test", ProcessCommonJSModules.toModuleName("./test.js"));
        
        // Edge case: Replaces / with $
        assertEquals("module$test$module$sub", ProcessCommonJSModules.toModuleName("test/sub.js"));
        
        // Edge case: Removes trailing .js
        assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js"));
        
        // Edge case: Replaces - with _
        assertEquals("module$test_module$sub", ProcessCommonJSModules.toModuleName("test-module/sub.js"));
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        // Typical use case
        assertEquals("module$test$sub", ProcessCommonJSModules.toModuleName("sub.js", "test.js"));
        
        // Edge case: Relative addressing with ./ 
        assertEquals("module$test$sub", ProcessCommonJSModules.toModuleName("./sub.js", "test.js"));
        
        // Edge case: Relative addressing with ../
        assertEquals("module$sub_module$sub", ProcessCommonJSModules.toModuleName("../sub.js", "test.js"));
    }
}