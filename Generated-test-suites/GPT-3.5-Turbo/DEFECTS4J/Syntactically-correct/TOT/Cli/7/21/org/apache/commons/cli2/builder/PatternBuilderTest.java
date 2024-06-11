import org.junit.Test;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.Option;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreateMethod() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Test case 1: Create option without any arguments
        patternBuilder.withPattern("a");
        Option option1 = patternBuilder.create();
        assertEquals(0, option1.getArguments().size());

        // Test case 2: Create option with an argument and being required
        patternBuilder.withPattern("b<>");
        Option option2 = patternBuilder.create();
        assertEquals(1, option2.getArguments().size());
        assertTrue(option2.isRequired());

        // Test case 3: Create option with a minimum of 1 argument and a validator
        patternBuilder.withPattern("c@");
        Option option3 = patternBuilder.create();
        assertEquals(1, option3.getArguments().size());
        assertEquals(1, option3.getArguments().get(0).getMinimum());
        assertNotNull(option3.getArguments().get(0).getValidator());

        // Test case 4: Create option with a maximum of 1 argument and an instance validator
        patternBuilder.withPattern("d+");
        Option option4 = patternBuilder.create();
        assertEquals(1, option4.getArguments().size());
        assertEquals(1, option4.getArguments().get(0).getMaximum());
        assertNotNull(option4.getArguments().get(0).getValidator());

        // Test case 5: Create option with a number validator
        patternBuilder.withPattern("e%");
        Option option5 = patternBuilder.create();
        assertEquals(1, option5.getArguments().size());
        assertNotNull(option5.getArguments().get(0).getValidator());

        // Test case 6: Create option with a date validator
        patternBuilder.withPattern("f#");
        Option option6 = patternBuilder.create();
        assertEquals(1, option6.getArguments().size());
        assertNotNull(option6.getArguments().get(0).getValidator());

        // Test case 7: Create option with an existing file validator
        patternBuilder.withPattern("g<");
        Option option7 = patternBuilder.create();
        assertEquals(1, option7.getArguments().size());
        assertTrue(option7.getArguments().get(0).getValidator().isExisting());
        assertTrue(option7.getArguments().get(0).getValidator().isFile());

        // Test case 8: Create option with a file validator
        patternBuilder.withPattern("h>");
        Option option8 = patternBuilder.create();
        assertEquals(1, option8.getArguments().size());
        assertNotNull(option8.getArguments().get(0).getValidator());

        // Test case 9: Create option with a URL validator
        patternBuilder.withPattern("i/");
        Option option9 = patternBuilder.create();
        assertEquals(1, option9.getArguments().size());
        assertNotNull(option9.getArguments().get(0).getValidator());

        // Test case 10: Create option with multiple options grouped together
        patternBuilder.withPattern("jkl");
        Option option10 = patternBuilder.create();
        assertEquals(3, option10.getArguments().size());
        assertEquals("k", option10.getArguments().get(0).getName());
        assertEquals("l", option10.getArguments().get(1).getName());
        assertEquals("l", option10.getArguments().get(2).getName());
    }

    @Test
    public void testResetMethod() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Create an option and reset the pattern builder
        patternBuilder.withPattern("a");
        patternBuilder.reset();

        // Ensure that the options set is empty
        assertEquals(0, patternBuilder.getOptions().size());
    }

    @Test
    public void testWithPatternMethod() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Test case 1: Create option with shorthand symbol for required argument
        patternBuilder.withPattern("!a@");
        Option option1 = patternBuilder.create();
        assertEquals(1, option1.getArguments().size());
        assertEquals(1, option1.getArguments().get(0).getMinimum());
        assertNotNull(option1.getArguments().get(0).getValidator());

        // Test case 2: Create option with shorthand symbol for multiple arguments
        patternBuilder.withPattern("a*");
        Option option2 = patternBuilder.create();
        assertEquals(1, option2.getArguments().size());
        assertEquals(0, option2.getArguments().get(0).getMinimum());
        assertEquals(Integer.MAX_VALUE, option2.getArguments().get(0).getMaximum());

        // Test case 3: Create option with shorthand symbol for URL
        patternBuilder.withPattern("a/");
        Option option3 = patternBuilder.create();
        assertEquals(1, option3.getArguments().size());
        assertNotNull(option3.getArguments().get(0).getValidator());

        // Test case 4: Create option with shorthand symbol for instance validation
        patternBuilder.withPattern("a+");
        Option option4 = patternBuilder.create();
        assertEquals(1, option4.getArguments().size());
        assertNotNull(option4.getArguments().get(0).getValidator());

        // Test case 5: Create option with shorthand symbols for existing file validation
        patternBuilder.withPattern("a<>");
        Option option5 = patternBuilder.create();
        assertEquals(1, option5.getArguments().size());
        assertTrue(option5.getArguments().get(0).getValidator().isExisting());
        assertTrue(option5.getArguments().get(0).getValidator().isFile());
    }
}