import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;
    private GroupBuilder groupBuilder;
    private DefaultOptionBuilder optionBuilder;
    private Argument argument;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
        groupBuilder = new GroupBuilder();
        optionBuilder = new DefaultOptionBuilder();
        argument = optionBuilder.createArgument();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_SingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_MultipleOptions() {
        patternBuilder.withPattern("a b c");
        GroupBuilder groupBuilder = new GroupBuilder();
        groupBuilder.withOption(optionBuilder.withShortName("a").create());
        groupBuilder.withOption(optionBuilder.withShortName("b").create());
        groupBuilder.withOption(optionBuilder.withShortName("c").create());
        Option option = groupBuilder.create();
        assertEquals(option, patternBuilder.create());
    }

    @Test
    public void testWithPattern_WithRequiredFlag() {
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPattern_WithArgumentType() {
        patternBuilder.withPattern("a@");
        Option option = patternBuilder.create();
        assertFalse(option.getArguments().isEmpty());
    }

    @Test
    public void testWithPattern_WithValidator() {
        patternBuilder.withPattern("a%");
        Option option = patternBuilder.create();
        assertFalse(option.getArguments().isEmpty());
        Argument argument = option.getArguments().get(0);
        assertNotNull(argument.getValidator());
    }

    @Test
    public void testWithPattern_WithMinimumAndMaximum() {
        patternBuilder.withPattern("a*");
        Option option = patternBuilder.create();
        assertFalse(option.getArguments().isEmpty());
        Argument argument = option.getArguments().get(0);
        assertEquals(0, argument.getMinimum());
        assertEquals(Integer.MAX_VALUE, argument.getMaximum());
    }

    @Test
    public void testWithPattern_WithFileValidator() {
        patternBuilder.withPattern("#");
        Option option = patternBuilder.create();
        assertFalse(option.getArguments().isEmpty());
        Argument argument = option.getArguments().get(0);
        assertNotNull(argument.getValidator());
    }

    @Test
    public void testWithPattern_WithUrlValidator() {
        patternBuilder.withPattern("/");
        Option option = patternBuilder.create();
        assertFalse(option.getArguments().isEmpty());
        Argument argument = option.getArguments().get(0);
        assertNotNull(argument.getValidator());
    }
}