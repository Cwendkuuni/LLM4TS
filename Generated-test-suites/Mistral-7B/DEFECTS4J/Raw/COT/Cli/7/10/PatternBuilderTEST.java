package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

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
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();

        // Test creating a single Option with no arguments
        builder.withPattern("a:");
        Option option = builder.create();
        assertNotNull(option);

        // Test creating an Option with a required argument
        builder.reset();
        builder.withPattern("a:s");
        option = builder.create();
        assertInstanceOf(Option.class, option);
        Argument argument = ((Option) option).getArgument();
        assertNotNull(argument);

        // Test creating an Option with a non-required argument
        builder.reset();
        builder.withPattern("a:*s");
        option = builder.create();
        assertInstanceOf(Option.class, option);
        argument = ((Option) option).getArgument();
        assertNull(argument);

        // Test creating an Option with a validator
        builder.reset();
        builder.withPattern("a:%d");
        option = builder.create();
        assertInstanceOf(Option.class, option);
        argument = ((Option) option).getArgument();
        assertNull(argument);
        Validator validator = ((Option) option).getValidator();
        assertInstanceOf(NumberValidator.class, validator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:x"); // invalid character 'x'
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();

        // Test creating multiple Options
        builder.withPattern("a:s b:s c:d");
        Option optionA = builder.create();
        Option optionB = builder.create();
        Option optionC = builder.create();

        assertInstanceOf(Option.class, optionA);
        assertInstanceOf(Option.class, optionB);
        assertInstanceOf(Option.class, optionC);

        Argument argumentA = ((Option) optionA).getArgument();
        Argument argumentB = ((Option) optionB).getArgument();
        Argument argumentC = ((Option) optionC).getArgument();

        assertNotNull(argumentA);
        assertNotNull(argumentB);
        assertNull(argumentC);
    }

    @Test
    public void testCreateGroup() {
        PatternBuilder builder = new PatternBuilder();

        // Test creating a group with multiple Options
        builder.withPattern("g{a:s b:s}");
        Option groupOption = builder.create();
        assertInstanceOf(Option.class, groupOption);
        GroupBuilder groupBuilder = ((Option) groupOption).getGroup();
        assertNotNull(groupBuilder);

        Option optionA = groupBuilder.getOption("a");
        Option optionB = groupBuilder.getOption("b");

        assertInstanceOf(Option.class, optionA);
        assertInstanceOf(Option.class, optionB);

        Argument argumentA = ((Option) optionA).getArgument();
        Argument argumentB = ((Option) optionB).getArgument();

        assertNotNull(argumentA);
        assertNotNull(argumentB);
    }

    @Test
    public void testReset() {
        PatternBuilder builder = new PatternBuilder();

        // Test resetting the builder
        builder.withPattern("a:s");
        Option optionA = builder.create();

        builder.reset();

        Option optionB = builder.create();

        assertInstanceOf(Option.class, optionB);
        assertNotNull(optionB);
        assertInstanceOf(Option.class, optionA);
        assertNotNull(optionA);
        assertFalse(optionB.equals(optionA));
    }
}