package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder builder;

    @Before
    public void setup() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = new Option();

        builder.options.add(option);
        Option result = builder.create();

        assertEquals(option, result);
        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testReset() {
        builder.create();

        builder.reset();

        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testCreateOption() {
        char type = ' ';
        boolean required = false;
        char opt = ' ';

        Argument argument = new Argument();
        builder.abuilder.argument = argument;

        Option option = new Option();
        builder.obuilder.option = option;

        builder.createOption(type, required, opt);

        assertEquals(argument, option.getArgument());
        assertEquals(String.valueOf(opt), option.getShortName());
        assertEquals(required, option.isRequired());
        assertTrue(builder.options.contains(option));
    }

    @Test
    public void testWithPattern() {
        String pattern = "abc";

        builder.withPattern(pattern);

        Option option = builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertTrue(builder.options.contains(option));
    }

    @Test
    public void testValidator() {
        char c = '@';
        assertTrue(builder.validator(c) instanceof ClassValidator);

        c = '+';
        assertTrue(builder.validator(c) instanceof ClassValidator);

        c = ':';
        assertTrue(builder.validator(c) == null);

        c = '%';
        assertTrue(builder.validator(c) instanceof NumberValidator);

        c = '#';
        assertTrue(builder.validator(c) instanceof DateValidator);

        c = '<';
        assertTrue(builder.validator(c) instanceof FileValidator);

        c = '>';
        assertTrue(builder.validator(c) instanceof FileValidator);

        c = '*';
        assertTrue(builder.validator(c) instanceof FileValidator);

        c = '/';
        assertTrue(builder.validator(c) instanceof UrlValidator);

        c = ' ';
        assertTrue(builder.validator(c) == null);
    }
}