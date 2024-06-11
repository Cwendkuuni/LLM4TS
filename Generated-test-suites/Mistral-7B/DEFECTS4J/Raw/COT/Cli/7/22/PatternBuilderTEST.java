package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreateOption() {
        // Test creating a single option with no arguments
        builder.withPattern("a:");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertSame(Option.Type.NO_ARGUMENT, option.getType());
        assertFalse(option.isRequired());

        // Test creating an option with a single required argument
        builder.reset();
        builder.withPattern("a:s");
        Option optionWithArgument = builder.create();
        assertTrue(optionWithArgument.isSettable());
        assertNull(optionWithArgument.getValue());
        assertSame(Option.Type.VALUE_REQUIRED, optionWithArgument.getType());
        assertTrue(optionWithArgument.isRequired());
    }

    @Test
    public void testCreateOptionWithArgumentValue() {
        // Test creating an option with a single value argument
        builder.reset();
        builder.withPattern("a:s");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());

        builder.withPattern("a:s argument");
        option = builder.create();
        assertTrue(option.isSettable());
        assertSame("argument", option.getValue());
    }

    @Test
    public void testCreateOptionWithMultipleArguments() {
        // Test creating an option with multiple value arguments
        builder.reset();
        builder.withPattern("a:s,s");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());

        builder.withPattern("a:s,s argument1 argument2");
        option = builder.create();
        assertTrue(option.isSettable());
        assertSame(new String[]{"argument1", "argument2"}, option.getValues());
    }

    @Test
    public void testCreateOptionWithLongPattern() {
        // Test creating an option with a long pattern
        builder.reset();
        builder.withPattern("a:s,s with_long_name_that_can_be_used_as_an_alias_for_option_a");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertSame("with_long_name_that_can_be_used_as_an_alias_for_option_a", option.getLongOpt());
    }

    @Test
    public void testCreateOptionWithShortPattern() {
        // Test creating an option with a short pattern
        builder.reset();
        builder.withPattern("a:s,s -a");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertSame("-a", option.getShortOpt());
    }

    @Test
    public void testCreateOptionWithMandatoryArgument() {
        // Test creating an option with a mandatory argument
        builder.reset();
        builder.withPattern("a:s");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());

        builder.withPattern("a:s argument");
        option = builder.create();
        assertTrue(option.isSettable());
        assertSame("argument", option.getValue());

        // Test creating an option with a mandatory argument and no pattern specified
        builder.reset();
        option = builder.create();
        assertFalse(option.isSettable());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        // Test creating an option with an optional argument
        builder.reset();
        builder.withPattern("a:s");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());

        // Test creating an option with an optional argument and a value
        builder.reset();
        builder.withPattern("a:s argument");
        option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());

        builder.withPattern("a:s argumentValue");
        option = builder.create();
        assertTrue(option.isSettable());
        assertSame("argumentValue", option.getValue());
    }

    @Test
    public void testCreateOptionWithNoArguments() {
        // Test creating an option with no arguments
        builder.reset();
        builder.withPattern("a:");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertSame(Option.Type.NO_ARGUMENT, option.getType());
        assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternCharacter() {
        // Test creating an option with an invalid pattern character
        builder.reset();
        builder.withPattern("a:x");
        builder.create();
    }

    @Test
    public void testCreateOptionWithClassArgument() {
        // Test creating an option with a class argument
        builder.reset();
        builder.withPattern("a:C");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertTrue(option.isValueRequired());
        assertSame(Option.Type.CLASS, option.getType());
    }

    @Test
    public void testCreateOptionWithNumberArgument() {
        // Test creating an option with a number argument
        builder.reset();
        builder.withPattern("a:d,d,d");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertTrue(option.isValueRequired());
        assertSame(Option.Type.NUMBER, option.getType());
    }

    @Test
    public void testCreateOptionWithDateArgument() {
        // Test creating an option with a date argument
        builder.reset();
        builder.withPattern("a:D,D,D");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertTrue(option.isValueRequired());
        assertSame(Option.Type.DATE, option.getType());
    }

    @Test
    public void testCreateOptionWithFileArgument() {
        // Test creating an option with a file argument
        builder.reset();
        builder.withPattern("a:f");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertTrue(option.isValueRequired());
        assertSame(Option.Type.FILE, option.getType());
    }

    @Test
    public void testCreateOptionWithUrlArgument() {
        // Test creating an option with a URL argument
        builder.reset();
        builder.withPattern("a:u");
        Option option = builder.create();
        assertTrue(option.isSettable());
        assertNull(option.getValue());
        assertTrue(option.isValueRequired());
        assertSame(Option.Type.URL, option.getType());
    }

    @Test
    public void testCreateMultipleOptions() {
        // Test creating multiple options
        builder.reset();
        builder.withPattern("a:s,b:s,c:s");
        Option optionA = builder.create();
        Option optionB = builder.create();
        Option optionC = builder.create();

        assertTrue(optionA.isSettable());
        assertNull(optionA.getValue());
        assertSame("a", optionA.getShortOpt());
        assertSame("a", optionA.getLongOpt());

        assertTrue(optionB.isSettable());
        assertNull(optionB.getValue());
        assertSame("b", optionB.getShortOpt());
        assertSame("b", optionB.getLongOpt());

        assertTrue(optionC.isSettable());
        assertNull(optionC.getValue());
        assertSame("c", optionC.getShortOpt());
        assertSame("c", optionC.getLongOpt());
    }

    @Test
    public void testReset() {
        // Test resetting the builder
        builder.reset();
        assertSame(new HashSet(), builder.getOptions());
    }

    @Test
    public void testGetOptions() {
        // Test getting the options
        builder.reset();
        builder.withPattern("a:s,b:s,c:s");
        Option optionA = builder.create();
        Option optionB = builder.create();
        Option optionC = builder.create();

        Set options = builder.getOptions();
        assertTrue(options.size() == 3);
        assertTrue(options.contains(optionA));
        assertTrue(options.contains(optionB));
        assertTrue(options.contains(optionC));
    }

    @Test
    public void testValidator() {
        // Test validator for different types
        builder.reset();
        builder.withPattern("a:C");
        Option option = builder.create();
        Validator validator = option.getValidator();
        assertTrue(validator instanceof ClassValidator);

        builder.reset();
        builder.withPattern("a:D");
        option = builder.create();
        validator = option.getValidator();
        assertTrue(validator instanceof DateValidator);

        builder.reset();
        builder.withPattern("a:F");
        option = builder.create();
        validator = option.getValidator();
        assertTrue(validator instanceof FileValidator);

        builder.reset();
        builder.withPattern("a:N");
        option = builder.create();
        validator = option.getValidator();
        assertTrue(validator instanceof NumberValidator);

        builder.reset();
        builder.withPattern("a:U");
        option = builder.create();
        validator = option.getValidator();
        assertTrue(validator instanceof UrlValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        // Test creating an invalid option pattern
        builder.reset();
        builder.withPattern("a:");
        builder.create();
    }
}