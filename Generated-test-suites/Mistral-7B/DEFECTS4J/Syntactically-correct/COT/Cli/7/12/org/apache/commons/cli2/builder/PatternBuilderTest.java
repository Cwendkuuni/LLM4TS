package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.reset();
        Option option1 = patternBuilder.create();
        assertEquals(option1, patternBuilder.create());

        patternBuilder.reset();
        patternBuilder.withPattern("a:");
        Option option2 = patternBuilder.create();
        assertEquals("a", option2.getShortName());
        assertEquals(Option.UNBOUNDED_VALUE, option2.getArgs().length);
        assertNull(option2.getLongName());
        assertNull(option2.getDescription());
        assertNull(option2.getValue());

        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        Option option3 = patternBuilder.create();
        assertEquals("a", option3.getShortName());
        assertEquals("s", option3.getLongName());
        assertEquals(Option.UNBOUNDED_VALUE, option3.getArgs().length);
        assertNull(option3.getDescription());
        assertNull(option3.getValue());

        patternBuilder.reset();
        patternBuilder.withPattern("a:s,b:i");
        Option option4 = patternBuilder.create();
        assertEquals("a", option4.getShortName());
        assertEquals("s", option4.getLongName());
        assertEquals(Option.UNBOUNDED_VALUE, option4.getArgs().length);
        assertNull(option4.getDescription());
        assertNull(option4.getValue());
        Option option5 = patternBuilder.create();
        assertEquals("b", option5.getShortName());
        assertEquals(null, option5.getLongName());
        assertEquals(Option.VALUE_OPTIONAL, option5.getArgs().length);
        assertEquals("An integer value", option5.getDescription());
        assertNull(option5.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("a:!s");
        patternBuilder.create();
    }

    @Test
    public void testCreateWithClassValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("C:@java.lang.String");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("C").isRequired(false).withValidator(new ClassValidator(String.class)).create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithNumberValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("%d");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("d").isRequired(false).withValidator(NumberValidator.getNumberInstance()).create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithDateValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("#d");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("d").isRequired(false).withValidator(DateValidator.getDateInstance()).create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithFileValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("<file>");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("file").isRequired(false).withValidator(new FileValidator()).create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithMultipleValidators() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("/url:@java.util.Date<file>");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("url")
            .isRequired(false)
            .withValidator(new UrlValidator())
            .withValidator(new DateValidator())
            .withValidator(new FileValidator())
            .create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithFileValidatorExisting() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("<file>:<existing>");
        Option option = patternBuilder.create();
        Option validatedOption = new OptionBuilder("file")
            .isRequired(false)
            .withValidator(new FileValidator(true, true))
            .create();
        assertEquals(validatedOption, option);
    }

    @Test
    public void testCreateWithMultipleArguments() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        patternBuilder.withPattern("a:*:b:*");
        Option option = patternBuilder.create();
        Argument argumentA = new Argument("a").isRequired(true).isMultipleValues(true);
        Argument argumentB = new Argument("b").isRequired(true).isMultipleValues(true);
        Option validatedOption = new OptionBuilder("")
            .withArgument(argumentA)
            .withArgument(argumentB)
            .create();
        assertEquals(validatedOption, option);
    }
}