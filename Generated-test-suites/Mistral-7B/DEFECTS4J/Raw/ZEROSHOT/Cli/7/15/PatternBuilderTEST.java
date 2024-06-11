package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ValidationException;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder.
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test creating an option with no pattern.
     */
    @Test
    public void testCreateOptionNoPattern() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    /**
     * Test creating an option with an empty pattern.
     */
    @Test
    public void testCreateOptionEmptyPattern() {
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    /**
     * Test creating an option with a valid pattern.
     */
    @Test
    public void testCreateOptionValidPattern() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertFalse(optionBuilder.isRequired());
        assertNull(optionBuilder.getArgument());
    }

    /**
     * Test creating an option with a required argument.
     */
    @Test
    public void testCreateOptionRequiredArgument() {
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertTrue(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        assertTrue(argument instanceof Argument);
        assertNull(argument.getValidator());
    }

    /**
     * Test creating an option with a validator.
     */
    @Test
    public void testCreateOptionValidator() {
        patternBuilder.withPattern("n:i");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'n');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        NumberValidator validator = (NumberValidator) argument.getValidator();
        assertNull(argument.getValue());
        try {
            validator.validate(null);
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            // expected
        }
    }

    /**
     * Test creating an option with multiple validators.
     */
    @Test
    public void testCreateOptionMultipleValidators() {
        patternBuilder.withPattern("s:s@com.example.MyClass");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 's');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        assertNull(argument.getValue());
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertTrue(classValidator.getInstance() == MyClass.class);
    }

    /**
     * Test creating an option with a validator that expects an instance.
     */
    @Test
    public void testCreateOptionValidatorInstance() {
        patternBuilder.withPattern("i:+java.util.List");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'i');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        assertTrue(argument.getValue() == null);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertTrue(classValidator.isInstance(new ArrayList<>()));
    }

    /**
     * Test creating an option with a date validator.
     */
    @Test
    public void testCreateOptionDateValidator() {
        patternBuilder.withPattern("d:");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'd');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        DateValidator dateValidator = (DateValidator) argument.getValidator();
        LocalDate date = LocalDate.of(2022, 1, 1);
        dateValidator.validate(date);
    }

    /**
     * Test creating an option with a file validator.
     */
    @Test
    public void testCreateOptionFileValidator() {
        patternBuilder.withPattern("f:");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'f');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        FileValidator fileValidator = (FileValidator) argument.getValidator();
        fileValidator.validate("test.txt");
    }

    /**
     * Test creating an option with a url validator.
     */
    @Test
    public void testCreateOptionUrlValidator() {
        patternBuilder.withPattern("u:");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'u');
        assertFalse(optionBuilder.isRequired());
        Argument argument = optionBuilder.getArgument();
        UrlValidator urlValidator = (UrlValidator) argument.getValidator();
        urlValidator.validate("http://example.com");
    }

    /**
     * Test creating an option with multiple arguments.
     */
    @Test
    public void testCreateOptionMultipleArguments() {
        patternBuilder.withPattern("aa:s,i");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertFalse(optionBuilder.isRequired());
        Set<Argument> arguments = optionBuilder.getArguments();
        assertEquals(2, arguments.size());
        Argument arg1 = arguments.iterator().next();
        assertTrue(arg1 instanceof Argument);
        assertNull(arg1.getValue());
        assertEquals("s", arg1.getValidator().getClass().getSimpleName());
        Argument arg2 = arguments.iterator().next();
        assertTrue(arg2 instanceof Argument);
        assertNull(arg2.getValue());
        assertEquals("i", arg2.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a required argument.
     */
    @Test
    public void testCreateOptionRequiredArgumentWithValue() {
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertFalse(optionBuilder.isRequired());
        argumentWithValue(optionBuilder, "value");
    }

    /**
     * Test creating an option with a required argument and a default value.
     */
    @Test
    public void testCreateOptionRequiredArgumentWithDefaultValue() {
        patternBuilder.withPattern("a:s{value='default'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertFalse(optionBuilder.isRequired());
        argumentWithValue(optionBuilder, "default");
    }

    /**
     * Test creating an option with an optional argument.
     */
    @Test
    public void testCreateOptionOptionalArgumentWithValue() {
        patternBuilder.withPattern("a:s{metavar='VALUE'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertTrue(optionBuilder.getShortName() == 'a');
        assertTrue(optionBuilder.isRequired());
        assertNull(optionBuilder.getValue());
        optionBuilder.setValue("value");
        assertEquals("value", optionBuilder.getValue());
    }

    /**
     * Test creating an option with a long name.
     */
    @Test
    public void testCreateOptionLongName() {
        patternBuilder.withPattern("myLongOption:s");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myLongOption", optionBuilder.getLongOpt());
        assertTrue(optionBuilder.getShortName() == 0);
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a metavar.
     */
    @Test
    public void testCreateOptionMetavar() {
        patternBuilder.withPattern("myOption:s{metavar='VALUE'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myOption", optionBuilder.getLongOpt());
        assertEquals("VALUE", optionBuilder.getMetavar());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a description.
     */
    @Test
    public void testCreateOptionDescription() {
        patternBuilder.withPattern("myOption:s{description='This is an option.'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myOption", optionBuilder.getLongOpt());
        assertEquals("This is an option.", optionBuilder.getDescription());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a description and a metavar.
     */
    @Test
    public void testCreateOptionDescriptionAndMetavar() {
        patternBuilder.withPattern("myOption:s{description='This is an option.', metavar='VALUE'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myOption", optionBuilder.getLongOpt());
        assertEquals("This is an option.", optionBuilder.getDescription());
        assertEquals("VALUE", optionBuilder.getMetavar());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a long name and a short name.
     */
    @Test
    public void testCreateOptionLongAndShortName() {
        patternBuilder.withPattern("myLongOption:s{short='s'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myLongOption", optionBuilder.getLongOpt());
        assertTrue(optionBuilder.getShortName() == 's');
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a long name and a short name and a description.
     */
    @Test
    public void testCreateOptionLongShortNameAndDescription() {
        patternBuilder.withPattern("myLongOption:s{short='s', description='This is an option.'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myLongOption", optionBuilder.getLongOpt());
        assertTrue(optionBuilder.getShortName() == 's');
        assertEquals("This is an option.", optionBuilder.getDescription());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a long name and a short name and a metavar.
     */
    @Test
    public void testCreateOptionLongShortNameAndMetavar() {
        patternBuilder.withPattern("myLongOption:s{short='s', metavar='VALUE'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myLongOption", optionBuilder.getLongOpt());
        assertTrue(optionBuilder.getShortName() == 's');
        assertEquals("VALUE", optionBuilder.getMetavar());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Test creating an option with a long name and a short name and a description and a metavar.
     */
    @Test
    public void testCreateOptionLongShortNameDescriptionAndMetavar() {
        patternBuilder.withPattern("myLongOption:s{short='s', description='This is an option.', metavar='VALUE'}");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        OptionBuilder optionBuilder = (OptionBuilder) option;
        assertEquals("myLongOption", optionBuilder.getLongOpt());
        assertTrue(optionBuilder.getShortName() == 's');
        assertEquals("This is an option.", optionBuilder.getDescription());
        assertEquals("VALUE", optionBuilder.getMetavar());
        assertEquals("s", optionBuilder.getValidator().getClass().getSimpleName());
    }

    /**
     * Helper method to set the value of an option.
     *
     * @param optionBuilder the OptionBuilder to set the value for
     * @param value the value to set
     */
    private void argumentWithValue(final OptionBuilder optionBuilder, final String value) {
        optionBuilder.setValue(value);
        assertEquals(value, optionBuilder.getValue());
    }

    private static class MyClass {
        // empty class for testing
    }
}