package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();
        char required = 'r';
        char opt = 'o';
        char type = 's';
        builder.createOption(type, false, opt);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals(Option.Builder.class, option.getClass().getSuperclass());
        assertEquals(opt, option.getOpt());
        assertFalse(option.isRequired());
        assertEquals(type, option.getLongOpt());
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass opt1:s@MyClass opt2:");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertEquals(2, builder.options.size());
        assertEquals(option1, builder.options.iterator().next());
        assertEquals(option2, builder.options.iterator().next());
    }

    @Test
    public void testCreateWithRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass r:");
        Option option = builder.create();
        assertTrue(option.isRequired());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass r:a");
        builder.create();
    }

    @Test
    public void testCreateWithValidation() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass:%d");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertEquals(Argument.Builder.class, option.getArgument().getClass().getSuperclass());
        assertEquals('d', option.getArgument().getOptionSettings().getLongOpt());
        assertTrue(option.getArgument().hasValidator());
        Validator validator = option.getArgument().getValidator();
        assertTrue(validator instanceof NumberValidator);
    }

    @Test
    public void testCreateWithFileValidation() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass:<file>");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertEquals('file', option.getArgument().getOptionSettings().getLongOpt());
        assertTrue(option.getArgument().hasValidator());
        Validator validator = option.getArgument().getValidator();
        assertTrue(validator instanceof FileValidator);
        assertTrue(((FileValidator) validator).isExisting());
        assertTrue(((FileValidator) validator).isFile());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithInvalidValidation() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass:#d");
        builder.create();
    }

    @Test
    public void testCreateWithClassValidation() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s@MyClass:@MyClass");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertEquals('', option.getArgument().getName());
        assertTrue(option.getArgument().hasValidator());
        Validator validator = option.getArgument().getValidator();
        assertTrue(validator instanceof ClassValidator);
    }

}