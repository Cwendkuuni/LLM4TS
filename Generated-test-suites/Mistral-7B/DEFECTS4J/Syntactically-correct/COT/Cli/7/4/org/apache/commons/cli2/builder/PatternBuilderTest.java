package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Test
    public void testPatternBuilderConstruction() {
        patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOptionWithValidPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateOptionWithValidPatternAndArgument() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
    }

    @Test
    public void testCreateOptionWithMultipleValidPatterns() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:s");
        Option option1 = patternBuilder.create();

        patternBuilder.reset();
        patternBuilder.withPattern("b:i");
        Option option2 = patternBuilder.create();

        assertEquals(2, patternBuilder.options.size());
        assertEquals(option1, patternBuilder.options.iterator().next());
        assertEquals(option2, patternBuilder.options.iterator().next());
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithEmptyPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("");
        patternBuilder.create();
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithInvalidPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@#%&*()");
        patternBuilder.create();
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        assertFalse(option.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("a:s:");
        option = patternBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithValidators() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d:");
        Option option = patternBuilder.create();
        assertEquals(NumberValidator.class, option.getValidator().getClass());

        patternBuilder.reset();
        patternBuilder.withPattern("/:");
        option = patternBuilder.create();
        assertEquals(UrlValidator.class, option.getValidator().getClass());

        patternBuilder.reset();
        patternBuilder.withPattern("@:");
        option = patternBuilder.create();
        assertEquals(ClassValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithCustomValidator() {
        patternBuilder = new PatternBuilder();
        MyValidator validator = new MyValidator();
        patternBuilder.withPattern("m:v", validator);
        Option option = patternBuilder.create();
        assertEquals(validator.getClass(), option.getValidator().getClass());
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithInvalidValidator() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("m:v", new MyInvalidValidator());
        patternBuilder.create();
    }

    private static class MyValidator implements Validator {
        public void validate(String name, String value) {
            // validation logic
        }
    }

    private static class MyInvalidValidator implements Validator {
        public void validate(String name, String value) {
            throw new UnsupportedOperationException();
        }
    }
}