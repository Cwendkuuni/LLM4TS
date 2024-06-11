import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_WithSingleOption_ReturnsOptionInstance() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
    }

    @Test
    public void testCreate_WithMultipleOptions_ReturnsOptionGroup() {
        patternBuilder.withPattern("-a -b -c");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(3, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
        assertEquals("-b", option.getOptions().get(1));
        assertEquals("-c", option.getOptions().get(2));
    }

    @Test
    public void testCreate_WithOptionAndArgument_ReturnsOptionWithArgument() {
        patternBuilder.withPattern("-a@");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
        assertNotNull(option.getArgs());
        assertEquals(1, option.getArgs().size());
        assertEquals("class", option.getArgs().get(0));
    }

    @Test
    public void testCreate_WithRequiredOptionAndArgument_ReturnsOptionWithRequiredArgument() {
        patternBuilder.withPattern("-a!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_WithMultipleOptionsAndArguments_ReturnsOptionGroupWithArguments() {
        patternBuilder.withPattern("-a@ -b!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
        assertEquals("-b", option.getOptions().get(1));
        assertNotNull(option.getArgs());
        assertEquals(2, option.getArgs().size());
        assertEquals("class", option.getArgs().get(0));
        assertEquals(1, option.getArgs().get(1));
    }

    @Test
    public void testReset_EmptyPatternBuilder_ReturnsEmptyOption() {
        Option option = patternBuilder.reset().create();
        assertNotNull(option);
        assertTrue(option.getOptions().isEmpty());
        assertNull(option.getArgs());
    }

    @Test
    public void testCreate_WithPatternContainingInvalidChar_ReturnsOptionWithNullValidator() {
        patternBuilder.withPattern("-a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals("-a", option.getOptions().get(0));
        assertNull(option.getArgs().get(0).getValidator());
    }
}