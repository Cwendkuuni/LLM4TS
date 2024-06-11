import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateWithOneOption() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertTrue(option.getOptions().contains("a"));
    }

    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.withPattern("-a-b-c");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(3, option.getOptions().size());
        assertTrue(option.getOptions().contains("a"));
        assertTrue(option.getOptions().contains("b"));
        assertTrue(option.getOptions().contains("c"));
    }

    @Test
    public void testCreateWithRequiredOption() {
        patternBuilder.withPattern("!-a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertTrue(option.isRequired());
        assertTrue(option.getOptions().contains("a"));
    }

    @Test
    public void testCreateWithArgument() {
        patternBuilder.withPattern("-a@");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
    }

    @Test
    public void testCreateWithMinimumAndMaximumArguments() {
        patternBuilder.withPattern("-a+");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
    }

    @Test
    public void testCreateWithValidator() {
        patternBuilder.withPattern("-a:%");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument().getValidator());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getOptions().isEmpty());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("-a-b-c");
        Option option1 = patternBuilder.create();

        patternBuilder.reset();
        patternBuilder.withPattern("@-*");
        Option option2 = patternBuilder.create();

        assertEquals(3, option1.getOptions().size());
        assertTrue(option1.getOptions().contains("a"));
        assertTrue(option1.getOptions().contains("b"));
        assertTrue(option1.getOptions().contains("c"));

        assertEquals(2, option2.getOptions().size());
        assertNotNull(option2.getArgument());
        assertEquals('*', option2.getOptions().iterator().next().charAt(0));
    }

    @Test
    public void testValidator() {
        assertNotNull(patternBuilder.validator('@'));
        assertNotNull(patternBuilder.validator('+'));
        assertNotNull(patternBuilder.validator('%'));
        assertNotNull(patternBuilder.validator('#'));
        assertNotNull(patternBuilder.validator('<'));
        assertNotNull(patternBuilder.validator('>'));
        assertNotNull(patternBuilder.validator('*'));
        assertNotNull(patternBuilder.validator('/'));
        assertNull(patternBuilder.validator(':'));
        assertNull(patternBuilder.validator(' '));
    }
}