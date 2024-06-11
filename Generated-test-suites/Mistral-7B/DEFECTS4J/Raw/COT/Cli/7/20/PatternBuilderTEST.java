package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

/**
 * Test cases for PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testPatternBuilderWithSingleOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertNotNull(option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgument());
    }

    @Test
    public void testPatternBuilderWithOptionalSingleCharacterOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:.");
        Option option = patternBuilder.create();
        assertNotNull(option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgument());
    }

    @Test
    public void testPatternBuilderWithMandatorySingleCharacterOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:!");
        Option option = patternBuilder.create();
        assertNotNull(option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgument());
        assertNotNull(option.isRequired());
    }

    @Test
    public void testPatternBuilderWithSingleCharacterOptionWithArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertNotNull(argument.getName());
    }

    @Test
    public void testPatternBuilderWithMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:s b:i");
        Option option1 = patternBuilder.create();
        Argument argument1 = option1.getArgument();
        assertNotNull(argument1);
        assertNotNull(argument1.getName());

        Option option2 = patternBuilder.create();
        Argument argument2 = option2.getArgument();
        assertNotNull(argument2);
        assertNotNull(argument2.getName());
        assertNotNull(argument2.getValidator());
        assertInstanceOf(NumberValidator.class, argument2.getValidator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderWithIllegalPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:!a");
        patternBuilder.create();
    }

    @Test
    public void testPatternBuilderWithDateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("d:");
        Option option = patternBuilder.create();
        assertNotNull(option.getArgument());
        assertInstanceOf(DateValidator.class, option.getArgument().getValidator());
    }

    @Test
    public void testPatternBuilderWithFileOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("f:");
        Option option = patternBuilder.create();
        assertNotNull(option.getArgument());
        assertInstanceOf(FileValidator.class, option.getArgument().getValidator());
    }

    @Test
    public void testPatternBuilderWithClassOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("C:java.lang.String");
        Option option = patternBuilder.create();
        assertNotNull(option.getArgument());
        assertInstanceOf(ClassValidator.class, option.getArgument().getValidator());
        assertInstanceOf(String.class, option.getArgument().getInstance());
    }

    @Test
    public void testPatternBuilderWithUrlOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("U:");
        Option option = patternBuilder.create();
        assertNotNull(option.getArgument());
        assertInstanceOf(UrlValidator.class, option.getArgument().getValidator());
    }
}