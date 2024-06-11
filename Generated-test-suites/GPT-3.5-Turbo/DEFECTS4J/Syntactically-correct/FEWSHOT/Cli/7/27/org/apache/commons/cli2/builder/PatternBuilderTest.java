import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_withSingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testCreate_withRequiredOption() {
        patternBuilder.withPattern("a!");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testCreate_withMultipleOptions() {
        patternBuilder.withPattern("a:b");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(false, option.isRequired());
        assertEquals(1, option.getArgument().getMinimumCount());
        assertEquals(1, option.getArgument().getMaximumCount());

        Option option2 = option.getChildOptions().get(0);
        assertEquals("b", option2.getShortName());
        assertNull(option2.getArgument());
        assertEquals(false, option2.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("a:b:c");
        option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(false, option.isRequired());
        assertEquals(1, option.getArgument().getMinimumCount());
        assertEquals(1, option.getArgument().getMaximumCount());

        option2 = option.getChildOptions().get(0);
        assertEquals("b", option2.getShortName());
        assertNotNull(option2.getArgument());
        assertEquals(false, option2.isRequired());
        assertEquals(1, option2.getArgument().getMinimumCount());
        assertEquals(1, option2.getArgument().getMaximumCount());

        Option option3 = option2.getChildOptions().get(0);
        assertEquals("c", option3.getShortName());
        assertNull(option3.getArgument());
        assertEquals(false, option3.isRequired());
    }

    @Test
    public void testCreate_withMultipleOptionsAndArguments() {
        patternBuilder.withPattern("a@:b%");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(false, option.isRequired());
        assertEquals(1, option.getArgument().getMinimumCount());
        assertEquals(1, option.getArgument().getMaximumCount());
        assertEquals(ClassValidator.class, option.getArgument().getValidator().getClass());

        Option option2 = option.getChildOptions().get(0);
        assertEquals("b", option2.getShortName());
        assertNotNull(option2.getArgument());
        assertEquals(false, option2.isRequired());
        assertEquals(1, option2.getArgument().getMinimumCount());
        assertEquals(1, option2.getArgument().getMaximumCount());
        assertEquals(NumberValidator.class, option2.getArgument().getValidator().getClass());
    }

    @Test
    public void testCreate_withComplexPattern() {
        patternBuilder.withPattern("a:b:c!");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(false, option.isRequired());
        assertEquals(1, option.getArgument().getMinimumCount());
        assertEquals(1, option.getArgument().getMaximumCount());

        Option option2 = option.getChildOptions().get(0);
        assertEquals("b", option2.getShortName());
        assertNotNull(option2.getArgument());
        assertEquals(false, option2.isRequired());
        assertEquals(1, option2.getArgument().getMinimumCount());
        assertEquals(1, option2.getArgument().getMaximumCount());

        Option option3 = option2.getChildOptions().get(0);
        assertEquals("c", option3.getShortName());
        assertNull(option3.getArgument());
        assertEquals(true, option3.isRequired());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a:b:c");
        patternBuilder.reset();

        Option option = patternBuilder.create();

        assertNull(option);
    }
}