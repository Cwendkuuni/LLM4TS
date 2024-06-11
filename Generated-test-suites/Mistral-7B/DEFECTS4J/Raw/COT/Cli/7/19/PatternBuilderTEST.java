package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ValidationException;
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

    private PatternBuilder patternBuilder;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateDefaultOption() {
        option = patternBuilder.create();
        assertSame(Option.builder(), option.getBuilder());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateOptionWithArgument() {
        patternBuilder.withPattern("a:s");
        option = patternBuilder.create();
        argument = (Argument) option.getArgument();
        assertSame(Argument.builder(), argument.getBuilder());
        assertFalse(option.isRequired());
        assertEquals("s", argument.getName());
    }

    @Test
    public void testCreateRequiredOptionWithArgument() {
        patternBuilder.withPattern("a:s");
        patternBuilder.createOption('a', true, 'a');
        option = patternBuilder.create();
        argument = (Argument) option.getArgument();
        assertSame(Argument.builder(), argument.getBuilder());
        assertTrue(option.isRequired());
        assertEquals("s", argument.getName());
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithoutArgument() {
        patternBuilder.withPattern("a:");
        patternBuilder.createOption('a', false, 'a');
        patternBuilder.create();
    }

    @Test
    public void testCreateOptionWithValidator() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithMultipleValidators() {
        patternBuilder.withPattern("a:@class com.test.MyClass");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertEquals("com.test.MyClass", option.getValidator().getClass().getName());
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithInvalidPattern() {
        patternBuilder.withPattern("a:#");
        patternBuilder.create();
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithMissingArgument() {
        patternBuilder.withPattern("a:");
        patternBuilder.createOption('a', false, 'a');
        patternBuilder.create();
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithMultipleArguments() {
        patternBuilder.withPattern("a:s b:s");
        patternBuilder.create();
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithMismatchedArgument() {
        patternBuilder.withPattern("a:i b:s");
        patternBuilder.createOption('a', false, 'a');
        patternBuilder.create();
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a:s");
        patternBuilder.create();
        patternBuilder.reset();
        assertNull(patternBuilder.create());
    }

    @Test(expected = ValidationException.class)
    public void testCreateOptionWithInvalidCharacter() {
        patternBuilder.withPattern("a:x");
        patternBuilder.create();
    }

    @Test
    public void testCreateFileOption() {
        patternBuilder.withPattern("<f>");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getValidator()).isFile());
        assertFalse(((FileValidator) option.getValidator()).isExisting());
    }

    @Test
    public void testCreateExistingFileOption() {
        patternBuilder.withPattern("<ef>");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getValidator()).isFile());
        assertTrue(((FileValidator) option.getValidator()).isExisting());
    }

    @Test(expected = ValidationException.class)
    public void testCreateUrlOption() {
        patternBuilder.withPattern("/u");
        patternBuilder.create();
    }

    @Test
    public void testCreateClassOption() {
        patternBuilder.withPattern("@c com.test.TestClass");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertEquals("com.test.TestClass", option.getValidator().getInstance().getClass().getName());
    }

    @Test
    public void testCreateNumberOption() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateOption() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        assertTrue(option.getValidator() instanceof DateValidator);
        ((DateValidator) option.getValidator()).validate(null);
    }

    private void assertValidOption(Option option) {
        option.getValidator().validate(null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateInvalidDateOption() {
        patternBuilder.withPattern("%d");
        patternBuilder.create();
        Option option = patternBuilder.create();
        try {
            assertValidOption(option);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        fail("Expected ValidationException");
    }

    @Test(expected = ValidationException.class)
    public void testCreateInvalidClassOption() {
        patternBuilder.withPattern("@c com.test.TestClass");
        patternBuilder.create();
        Option option = patternBuilder.create();
        try {
            assertValidOption(option);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        fail("Expected ValidationException");
    }

    @Test(expected = ValidationException.class)
    public void testCreateInvalidFileOption() {
        patternBuilder.withPattern("<f>");
        option = patternBuilder.create();
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.validate(new HashSet<String>(){
            {
                add("non-existent-file.txt");
            }
        });
        fail("Expected ValidationException");
    }

    @Test
    public void testCreateValidFileOption() {
        patternBuilder.withPattern("<ef>");
        option = patternBuilder.create();
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.validate(new HashSet<String>(){
            {
                add("test.txt");
            }
        });
    }

    @Test(expected = ValidationException.class)
    public void testCreateInvalidUrlOption() {
        patternBuilder.withPattern("/u");
        option = patternBuilder.create();
        UrlValidator urlValidator = (UrlValidator) option.getValidator();
        urlValidator.validate("invalid_url");
        fail("Expected ValidationException");
    }

    @Test
    public void testCreateValidUrlOption() {
        patternBuilder.withPattern("/u");
        option = patternBuilder.create();
        UrlValidator urlValidator = (UrlValidator) option.getValidator();
        urlValidator.validate("https://www.example.com");
    }

    @Test
    public void testCreateValidNumberOption() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        NumberValidator numberValidator = (NumberValidator) option.getValidator();
        numberValidator.validate(new Integer(123));
    }

    @Test(expected = ValidationException.class)
    public void testCreateInvalidNumberOption() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        NumberValidator numberValidator = (NumberValidator) option.getValidator();
        numberValidator.validate(new Double(123.5));
        fail("Expected ValidationException");
    }

    @Test
    public void testCreateValidDateOption() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        DateValidator dateValidator = (DateValidator) option.getValidator();
        dateValidator.validate("2023-03-31");
    }

    @Test(expected = ParseException.class)
    public void testCreateInvalidDateOptionWithInvalidFormat() {
        patternBuilder.withPattern("%d");
        option = patternBuilder.create();
        DateValidator dateValidator = (DateValidator) option.getValidator();
        try {
            dateValidator.validate("2023-33-31");
        } catch (ParseException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        fail("Expected ParseException");
    }
}