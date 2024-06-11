package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNull(option);

        pb.reset();
        pb.withPattern("a:");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
        assertNull(option.getShortName());
    }

    @Test
    public void testCreateWithArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("a:s");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertNull(argument.getValidator());
        assertNull(argument.getName());
    }

    @Test
    public void testCreateWithNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("%d");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertTrue(option.isRequired());
        NumberValidator validator = (NumberValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate("123");
    }

    @Test
    public void testCreateWithDateValidator() throws ParseException {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("#d");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertTrue(option.isRequired());
        DateValidator validator = (DateValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate("2022-12-31");
    }

    @Test
    public void testCreateWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("@java.lang.String");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertTrue(option.isRequired());
        ClassValidator validator = (ClassValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate(String.class);
    }

    @Test
    public void testCreateWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("<file>");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertTrue(option.isRequired());
        FileValidator validator = (FileValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate("test.txt");
    }

    @Test
    public void testCreateWithFileValidatorExisting() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern(">file");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertFalse(option.isRequired());
        FileValidator validator = (FileValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate("test.txt");
    }

    @Test
    public void testCreateWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("/url");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
        assertNull(option.getShortName());
        assertFalse(option.isRequired());
        UrlValidator validator = (UrlValidator) option.getValidator();
        assertNotNull(validator);
        validator.validate("https://www.example.com");
    }

    @Test
    public void testCreateWithMultiplePatterns() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("a:s");
        pb.withPattern("b:i");
        Option option1 = pb.create();
        assertNotNull(option1);
        Argument argument1 = option1.getArgument();
        assertNotNull(argument1);
        assertNull(argument1.getValidator());
        assertNull(argument1.getName());

        Option option2 = pb.create();
        assertNotNull(option2);
        Argument argument2 = option2.getArgument();
        assertNotNull(argument2);
        NumberValidator validator = (NumberValidator) argument2.getValidator();
        assertNotNull(validator);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithMismatchedPatterns() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        pb.withPattern("a:s");
        pb.withPattern("%d");
        Option option = pb.create();
    }
}