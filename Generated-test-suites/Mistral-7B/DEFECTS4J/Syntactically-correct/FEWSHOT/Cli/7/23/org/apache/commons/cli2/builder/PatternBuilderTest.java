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

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPatternSingleCharacterOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a");
        Option option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testWithPatternRequiredOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!a");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testWithPatternOptionalOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a");
        Option option = pb.create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testWithPatternStringOptionWithArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%s");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("s", argument.getName());
    }

    @Test
    public void testWithPatternStringOptionWithNoArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern(":s");
        Option option = pb.create();
        assertNull(option.getArg());
        assertEquals("s", option.getShortName());
    }

    @Test
    public void testWithPatternCharacterArgWithNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("d", argument.getName());
        assertTrue(argument.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testWithPatternCharacterArgWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%D");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("D", argument.getName());
        assertTrue(argument.getValidator() instanceof DateValidator);
    }

    @Test
    public void testWithPatternCharacterArgWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@s");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("s", argument.getName());
        assertTrue(argument.getValidator() instanceof ClassValidator);
    }

    @Test
    public void testWithPatternCharacterArgWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<s");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("s", argument.getName());
        assertTrue(argument.getValidator() instanceof FileValidator);
    }

    @Test
    public void testWithPatternCharacterArgWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/s");
        Option option = pb.create();
        Argument argument = option.getArg();
        assertNotNull(argument);
        assertEquals("s", argument.getName());
        assertTrue(argument.getValidator() instanceof UrlValidator);
    }

    @Test(expected = AssertionError.class)
    public void testWithPatternInvalidOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!@");
        Option option = pb.create();
        assertNull(option);
    }
}