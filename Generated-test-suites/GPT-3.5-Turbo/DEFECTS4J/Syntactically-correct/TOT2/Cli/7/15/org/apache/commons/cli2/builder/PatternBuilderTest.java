import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Typical use case
        // Create PatternBuilder instance
        PatternBuilder patternBuilder = new PatternBuilder();
        
        // Add options
        patternBuilder.withPattern("+@:*");
        
        // Create Option
        Option option = patternBuilder.create();
        
        // Assertion
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        // Edge case: Reset with no options
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        
        // Assertion
        assertTrue(patternBuilder.reset().options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        // Error scenario: Invalid pattern
        PatternBuilder patternBuilder = new PatternBuilder();
        
        // Add invalid pattern
        patternBuilder.withPattern("+@%");
        
        // Assertion: Should throw exception
        assertThrows(IllegalArgumentException.class, () -> patternBuilder.create());
    }
}