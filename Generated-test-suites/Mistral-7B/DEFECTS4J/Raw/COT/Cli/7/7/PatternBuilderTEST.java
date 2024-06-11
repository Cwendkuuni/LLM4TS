package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
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

    private PatternBuilder patternBuilder;

    @Test
    public void testPatternBuilderConstruction() {
        patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOption() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertNull(option.getArgument());

        patternBuilder.reset();

        patternBuilder.withPattern("a:b");
        option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        patternBuilder.withPattern("a::");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());

        patternBuilder.reset();

        patternBuilder.withPattern("a:b:");
        option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
    }

    @Test
    public void testCreateOptionWithValidators() {
        Set<Class<? extends Validator>> validators = new HashSet<>();
        validators.add(ClassValidator.class);
        validators.add(NumberValidator.class);

        patternBuilder.withPattern("a:@");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertFalse(option.hasDefaultValue());

        patternBuilder.withPattern("a:%");
        option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertFalse(option.hasDefaultValue());

        patternBuilder.withPattern("a:v:"); // No such validator, should be ignored
        option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertNull(option.getValidator());
        assertFalse(option.hasDefaultValue());
    }

    @Test
    public void testCreateOptionWithDifferentValidators() {
        patternBuilder.withPattern("a:@:b:#");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getArgument(0).getValidator() instanceof ClassValidator);
        assertTrue(option.getArgument(1).getValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("a:<");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(option.hasDefaultValue());
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        patternBuilder.withPattern("a:/");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
        assertFalse(option.hasDefaultValue());
    }

    @Test
    public void testCreateOptionWithArgumentArray() {
        patternBuilder.withPattern("a:*:b:*:c:");
        Option option = patternBuilder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getArguments().size() == 3);
        assertNull(option.getArgument(0).getValidator());
        assertNull(option.getArgument(1).getValidator());
        assertNull(option.getArgument(2).getValidator());
    }

    @Test
    public void testCreateMultipleOptions() {
        patternBuilder.withPattern("a:@ b:# c:/ d:< e:");
        Option optionA = patternBuilder.create();
        Option optionB = patternBuilder.create();
        Option optionC = patternBuilder.create();
        Option optionD = patternBuilder.create();
        Option optionE = patternBuilder.create();

        assertEquals(Arrays.asList(optionA, optionB, optionC, optionD, optionE), patternBuilder.createAllOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidPattern() {
        patternBuilder.withPattern("a:!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidPatternWithUnsupportedCharacters() {
        patternBuilder.withPattern("a:$");
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateOptionWithMissingRequiredArgument() {
        patternBuilder.withPattern("a:");
        patternBuilder.create();
        patternBuilder.create(); // Should throw an exception
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateOptionWithMissingArgument() {
        patternBuilder.withPattern("a:b:");
        Option option = patternBuilder.create();
        patternBuilder.create(); // Should throw an exception
    }
}