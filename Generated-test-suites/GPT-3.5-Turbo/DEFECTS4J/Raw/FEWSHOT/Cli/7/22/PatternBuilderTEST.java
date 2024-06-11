import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Test create method with one option
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);

        // Test create method with multiple options
        patternBuilder.withPattern("-a -b");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());

        patternBuilder.reset();

        // Test create method with no options
        option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testReset() {
        // Test reset method
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a -b");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());

        patternBuilder.reset();
        option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        // Test withPattern method with empty pattern
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();
        assertNull(option);

        // Test withPattern method with single option
        patternBuilder.withPattern("-a");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());

        patternBuilder.reset();

        // Test withPattern method with multiple options
        patternBuilder.withPattern("-a -b");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());

        patternBuilder.reset();

        // Test withPattern method with required option
        patternBuilder.withPattern("!-a");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals(1, option.getOptions().get(0).getMinimum());

        patternBuilder.reset();

        // Test withPattern method with typed option
        patternBuilder.withPattern(":file");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getOptions().get(0).getArgument().getValidator());

        patternBuilder.reset();

        // Test withPattern method with optional argument
        patternBuilder.withPattern("*-a");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals(0, option.getOptions().get(0).getMaximum());

        patternBuilder.reset();
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        // Test validator method with different characters
        assertNotNull(patternBuilder.validator('@'));
        assertNotNull(patternBuilder.validator('+'));
        assertNotNull(patternBuilder.validator('%'));
        assertNotNull(patternBuilder.validator('#'));
        assertNotNull(patternBuilder.validator('<'));
        assertNotNull(patternBuilder.validator('>'));
        assertNotNull(patternBuilder.validator('*'));
        assertNotNull(patternBuilder.validator('/'));
        assertNull(patternBuilder.validator(':'));
    }
}