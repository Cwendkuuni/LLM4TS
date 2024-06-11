package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        pb.withPattern("b:");
        Option optionA = pb.create();
        Option optionB = pb.create();
        assertTrue(optionA.getShortName().equals("a"));
        assertTrue(optionB.getShortName().equals("b"));
    }

    @Test
    public void testCreateOptionWithArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option = pb.create();
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getMetavar().equals("ARG"));
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%:f");
        Option option = pb.create();
        Validator validator = option.getValidator();
        assertNotNull(validator);
        assertTrue(validator instanceof NumberValidator);
    }

    @Test
    public void testCreatePatternWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@:java.lang.String");
        Option option = pb.create();
        Validator validator = option.getValidator();
        assertNotNull(validator);
        assertTrue(validator instanceof ClassValidator);
    }

    @Test
    public void testCreatePatternWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#:yyyy-MM-dd");
        Option option = pb.create();
        Validator validator = option.getValidator();
        assertNotNull(validator);
        assertTrue(validator instanceof DateValidator);
    }

    @Test
    public void testCreatePatternWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<:file");
        Option option = pb.create();
        Validator validator = option.getValidator();
        assertNotNull(validator);
        assertTrue(validator instanceof FileValidator);
    }

    @Test
    public void testCreatePatternWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/:url");
        Option option = pb.create();
        Validator validator = option.getValidator();
        assertNotNull(validator);
        assertTrue(validator instanceof UrlValidator);
    }
}