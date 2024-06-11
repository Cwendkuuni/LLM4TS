import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testCheckGlobalThis() {
        // Create test instances of AbstractCompiler and CheckLevel
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        
        // Create an instance of CheckGlobalThis
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        
        // Test the constructor
        
        // Assert that the compiler and level are set correctly
        assertEquals(compiler, checker.compiler);
        assertEquals(level, checker.level);
    }
}