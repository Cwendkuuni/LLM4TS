import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Typical use case
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        assertEquals("Option created successfully", "-a", patternBuilder.create().toString());

        // Edge case with multiple options
        patternBuilder.reset();
        patternBuilder.withPattern("-a -b");
        assertEquals("Options created successfully", 2, patternBuilder.create().getOptions().size());

        // Error scenario with invalid pattern
        patternBuilder.reset();
        patternBuilder.withPattern("");
        assertEquals("No option created for empty pattern", 0, patternBuilder.create().getOptions().size());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        assertEquals("Options cleared successfully", 0, patternBuilder.create().getOptions().size());
    }

    @Test
    public void testWithPattern() {
        // Typical use case
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a -b -c");
        assertEquals("Options added successfully", 3, patternBuilder.create().getOptions().size());

        // Edge case with different types of options
        patternBuilder.reset();
        patternBuilder.withPattern("-a:b!@ -c*");
        assertEquals("Options added successfully with different types", 3, patternBuilder.create().getOptions().size());

        // Error scenario with invalid pattern
        patternBuilder.reset();
        patternBuilder.withPattern("invalid");
        assertEquals("No option created for invalid pattern", 0, patternBuilder.create().getOptions().size());
    }
}