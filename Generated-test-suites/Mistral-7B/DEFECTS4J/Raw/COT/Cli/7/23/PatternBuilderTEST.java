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
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder pb = new PatternBuilder();
        assertNotNull(pb);
    }

    @Test
    public void testCreateOptionWithNoArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("c:");
        Option option = pb.create();
        assertNotNull(option);
        assertNull(option.getArgument());
    }

    @Test
    public void testCreateOptionWithOneArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option = pb.create();
        Argument argument = option.getArgument();
        assertNotNull(option);
        assertNotNull(argument);
    }

    @Test
    public void testCreateOptionWithMultipleArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:ss");
        Option option = pb.create();
        Argument[] arguments = option.getArguments();
        assertNotNull(option);
        assertNotNull(arguments);
        assertNotNull(arguments[0]);
        assertNotNull(arguments[1]);
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithInvalidPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("x:");
        Option option = pb.create();
        // the test should fail as there is no validator for 'x' character
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("C:<java.lang.String>");
        Option option = pb.create();
        ClassValidator classValidator = (ClassValidator) option.getValidator();
        assertNotNull(classValidator);
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        NumberValidator numberValidator = (NumberValidator) option.getValidator();
        assertNotNull(numberValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#{yyyy-MM-dd}");
        Option option = pb.create();
        DateValidator dateValidator = (DateValidator) option.getValidator();
        assertNotNull(dateValidator);
    }

    @Test
    public void testCreateOptionWithFileValidatorExistingFile() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<file>");
        Option option = pb.create();
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.setExisting(true);
        fileValidator.validate();
        assertNull(fileValidator.getErrorMessage());
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithFileValidatorNonExistingFile() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<nonexistentfile>");
        Option option = pb.create();
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.setExisting(false);
        fileValidator.validate();
        // the test should fail as the file does not exist
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/url");
        Option option = pb.create();
        UrlValidator urlValidator = (UrlValidator) option.getValidator();
        assertNotNull(urlValidator);
    }

    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option = pb.create();
        pb.reset();
        Option resetOption = pb.create();
        assertNull(resetOption);
    }

}