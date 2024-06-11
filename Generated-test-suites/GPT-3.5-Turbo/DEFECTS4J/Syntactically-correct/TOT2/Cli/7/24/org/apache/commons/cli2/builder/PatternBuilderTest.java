import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateSingleOption() {
        patternBuilder.withPattern("-f!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        // Add more assertions as needed
    }

    @Test
    public void testCreateMultipleOptions() {
        patternBuilder.withPattern("-f! -s@");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
        // Add more assertions as needed
    }

    @Test
    public void testCreateOptionWithArgument() {
        patternBuilder.withPattern("-f@!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        // Add more assertions as needed
    }

    @Test
    public void testResetBuilder() {
        patternBuilder.withPattern("-f!");
        Option option1 = patternBuilder.create();
        assertNotNull(option1);

        patternBuilder.reset();
        Option option2 = patternBuilder.create();
        assertNull(option2);
    }

    @Test
    public void testInvalidPattern() {
        patternBuilder.withPattern("invalidPattern");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    // Add more test cases for edge cases and error scenarios

}