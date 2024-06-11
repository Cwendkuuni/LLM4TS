package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ArgumentBuilder;
import org.apache.commons.cli2.DefaultOptionBuilder;
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
        patternBuilder.create();
        Option option = patternBuilder.create();
        
        assertNull(option);

        Option expectedOption = new DefaultOptionBuilder()
                                  .withArgument(null)
                                  .withShortName("")
                                  .withRequired(false)
                                  .create();

        assertEquals(expectedOption, option);
    }
    
    @Test
    public void testReset() {
        patternBuilder.reset();

        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreate_withMultipleOptions() {
        patternBuilder.create();

        patternBuilder.withPattern("abc");
        Option option1 = patternBuilder.create();

        patternBuilder.withPattern("xyz");
        Option option2 = patternBuilder.create();

        // Option1
        Argument argument1 = new ArgumentBuilder()
                                .reset()
                                .withValidator(null)
                                .create();

        Option expectedOption1 = new DefaultOptionBuilder()
                                   .withArgument(argument1)
                                   .withShortName("a")
                                   .withRequired(false)
                                   .create();
        
        assertEquals(expectedOption1, option1);

        // Option2
        Argument argument2 = new ArgumentBuilder()
                                .reset()
                                .withValidator(null)
                                .create();

        Option expectedOption2 = new DefaultOptionBuilder()
                                   .withArgument(argument2)
                                   .withShortName("x")
                                   .withRequired(false)
                                   .create();

        assertEquals(expectedOption2, option2);
    }

    @Test
    public void testWithPattern_withEmptyPattern() {
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();

        Option expectedOption = new DefaultOptionBuilder()
                                  .withArgument(null)
                                  .withShortName("")
                                  .withRequired(false)
                                  .create();

        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithPattern_withValidPattern() {
        patternBuilder.withPattern("a!x:y:z");
        Option option = patternBuilder.create();

        // Option
        Argument argument = new ArgumentBuilder()
                               .reset()
                               .withValidator(null)
                               .withMinimum(1)
                               .withMaximum(1)
                               .create();

        Option expectedOption = new DefaultOptionBuilder()
                                  .withArgument(argument)
                                  .withShortName("a")
                                  .withRequired(true)
                                  .create();

        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithPattern_withInvalidPattern() {
        patternBuilder.withPattern("@:!:#");

        Option option1 = patternBuilder.create();

        Argument argument1 = new ArgumentBuilder()
                                 .reset()
                                 .withValidator(null)
                                 .create();

        Option expectedOption1 = new DefaultOptionBuilder()
                                    .withArgument(argument1)
                                    .withShortName("@")
                                    .withRequired(false)
                                    .create();

        assertEquals(expectedOption1, option1);

        patternBuilder.withPattern("<>");

        Option option2 = patternBuilder.create();

        Argument argument2 = new ArgumentBuilder()
                                 .reset()
                                 .withValidator(null)
                                 .create();

        Option expectedOption2 = new DefaultOptionBuilder()
                                    .withArgument(argument2)
                                    .withShortName("<")
                                    .withRequired(false)
                                    .create();

        assertEquals(expectedOption2, option2);
    }

    @Test
    public void testValidator_withValidType() {
        Validator validator = PatternBuilder.validator('%');

        assertTrue(validator instanceof NumberValidator);
    }

    @Test
    public void testValidator_withInvalidType() {
        Validator validator = PatternBuilder.validator('&');

        assertNull(validator);
    }
}