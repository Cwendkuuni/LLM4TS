package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        assertNotNull(option);
    }

    @Test
    public void testCreateWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertNull(argument.getValidator());

        patternBuilder.reset();
        patternBuilder.withPattern("@a:#");
        option = patternBuilder.create();
        argument = option.getArgument();
        assertNotNull(argument);
        DateValidator dateValidator = (DateValidator) argument.getValidator();
        assertNotNull(dateValidator);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a:#!@");
        patternBuilder.create();
    }

    @Test
    public void testCreateWithOptionalArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("+a:");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertNull(argument.isRequired());
    }

    @Test
    public void testCreateWithRequiredArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a:");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.isRequired());
    }

    @Test
    public void testCreateWithMultipleArguments() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("++a:+b:");
        Option option = patternBuilder.create();
        Argument argumentA = option.getArgument(0);
        Argument argumentB = option.getArgument(1);
        assertNotNull(argumentA);
        assertNotNull(argumentB);
    }

    @Test
    public void testCreateWithClassValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a::Class");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertNotNull(classValidator);
    }

    @Test
    public void testCreateWithDateValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a:#");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        DateValidator dateValidator = (DateValidator) argument.getValidator();
        assertNotNull(dateValidator);
    }

    @Test
    public void testCreateWithFileValidatorExisting() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<a:>");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        FileValidator fileValidator = (FileValidator) argument.getValidator();
        fileValidator.setExisting(true);
        fileValidator.setFile(true);
        assertNotNull(fileValidator);
    }

    @Test
    public void testCreateWithFileValidatorNonExisting() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<a:>");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        FileValidator fileValidator = (FileValidator) argument.getValidator();
        fileValidator.setExisting(false);
        fileValidator.setFile(true);
        assertNotNull(fileValidator);
    }

    @Test
    public void testCreateWithFileValidatorNotFile() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a:>");
        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        UrlValidator urlValidator = (UrlValidator) argument.getValidator();
        assertNotNull(urlValidator);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithInvalidValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a:X");
        Option option = patternBuilder.create();
    }
}