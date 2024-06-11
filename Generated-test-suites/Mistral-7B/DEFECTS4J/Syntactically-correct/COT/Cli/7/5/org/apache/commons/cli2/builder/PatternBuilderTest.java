package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

/**
 * JUnit tests for PatternBuilder class
 */
public class PatternBuilderTest {

    private PatternBuilder builder;

    @Test
    public void testCreatePatternBuilder() {
        builder = new PatternBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testCreateOption() {
        builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs().size());

        builder.reset();
        builder.withPattern("b:s");
        option = builder.create();
        assertNotNull(option);
        assertEquals("b", option.getLongOpt());
        assertEquals('b', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().size());
    }

    @Test
    public void testCreateMultipleOptions() {
        builder = new PatternBuilder();
        builder.withPattern("a:s b:s c:");
        Option option1 = builder.create();
        Option option2 = builder.create();
        Option option3 = builder.create();

        assertNotNull(option1);
        assertEquals("a", option1.getLongOpt());
        assertEquals('a', option1.getOpt());
        assertTrue(option1.isRequired());
        assertEquals(1, option1.getArgs().size());

        assertNotNull(option2);
        assertEquals("b", option2.getLongOpt());
        assertEquals('b', option2.getOpt());
        assertTrue(option2.isRequired());
        assertEquals(1, option2.getArgs().size());

        assertNotNull(option3);
        assertEquals("c", option3.getLongOpt());
        assertEquals('c', option3.getOpt());
        assertFalse(option3.isRequired());
        assertEquals(0, option3.getArgs().size());
    }

    @Test
    public void testCreateOptionWithValue() {
        builder = new PatternBuilder();
        builder.withPattern("a:s b:s{1}");
        Option option1 = builder.create();
        Option option2 = builder.create();

        assertNotNull(option1);
        assertEquals("a", option1.getLongOpt());
        assertEquals('a', option1.getOpt());
        assertFalse(option1.isRequired());
        assertEquals(1, option1.getArgs().size());
        assertEquals("", option1.getValue());

        assertNotNull(option2);
        assertEquals("b", option2.getLongOpt());
        assertEquals('b', option2.getOpt());
        assertTrue(option2.isRequired());
        assertEquals(1, option2.getArgs().size());
        assertNull(option2.getValue());

        builder.reset();
        builder.withPattern("a:s b:s{1}:s");
        option1 = builder.create();
        option2 = builder.create();

        assertNotNull(option1);
        assertEquals("a", option1.getLongOpt());
        assertEquals('a', option1.getOpt());
        assertFalse(option1.isRequired());
        assertEquals(1, option1.getArgs().size());
        assertEquals("", option1.getValue());

        assertNotNull(option2);
        assertEquals("b", option2.getLongOpt());
        assertEquals('b', option2.getOpt());
        assertTrue(option2.isRequired());
        assertEquals(2, option2.getArgs().size());
        assertNull(option2.getValue());

        builder.create("test");
        assertEquals("test", option2.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        builder = new PatternBuilder();
        builder.withPattern("a:s b:s{1}:s:");
        builder.create();
    }

    @Test
    public void testCreateOptionWithValidator() {
        builder = new PatternBuilder();
        builder.withPattern("number:i");
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("number", option.getLongOpt());
        assertEquals('n', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals(0, option.getArgs().size());
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);

        builder.reset();
        builder.withPattern("file:f");
        option = builder.create();
        assertNotNull(option);
        assertEquals("file", option.getLongOpt());
        assertEquals('f', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals(0, option.getArgs().size());
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
    }
}