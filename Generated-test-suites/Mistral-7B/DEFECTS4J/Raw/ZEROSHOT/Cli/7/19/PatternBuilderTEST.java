package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setup() {
        builder = new PatternBuilder();
    }

    @After
    public void tearDown() {
        builder.reset();
    }

    @Test
    public void testCreateEmptyOption() {
        Option option = builder.create();
        assertNull(option);
    }

    @Test
    public void testCreateSingleOption() {
        builder.withPattern("a:");
        Option option = builder.create();
        Argument argument = option.getArgument();
        assertNotNull(option);
        assertNotNull(argument);
        assertEquals("", argument.getName());
        assertEquals(Option.UNLIMITED_ARITY, option.getArity());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateSingleRequiredOption() {
        builder.withPattern("a:!");
        Option option = builder.create();
        Argument argument = option.getArgument();
        assertNotNull(option);
        assertNotNull(argument);
        assertEquals("", argument.getName());
        assertEquals(Option.UNLIMITED_ARITY, option.getArity());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateSingleOptionWithArgument() {
        builder.withPattern("a:b");
        Option option = builder.create();
        Argument argument = option.getArgument();
        assertNotNull(option);
        assertNotNull(argument);
        assertEquals("b", argument.getName());
        assertEquals(Option.UNLIMITED_ARITY, option.getArity());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateSingleRequiredOptionWithArgument() {
        builder.withPattern("a:b!");
        Option option = builder.create();
        Argument argument = option.getArgument();
        assertNotNull(option);
        assertNotNull(argument);
        assertEquals("b", argument.getName());
        assertEquals(Option.UNLIMITED_ARITY, option.getArity());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateMultipleOptions() {
        builder.withPattern("a:b c:d e:!");
        Option option1 = builder.create();
        Argument argument1 = option1.getArgument();
        Option option2 = builder.create();
        Argument argument2 = option2.getArgument();
        Option option3 = builder.create();
        Argument argument3 = option3.getArgument();

        assertEquals(3, builder.getOptions().size());
        assertEquals("b", argument1.getName());
        assertEquals("d", argument2.getName());
        assertEquals("", argument3.getName());
        assertEquals(Option.UNLIMITED_ARITY, option1.getArity());
        assertEquals(Option.UNLIMITED_ARITY, option2.getArity());
        assertEquals(Option.UNLIMITED_ARITY, option3.getArity());
        assertFalse(option1.isRequired());
        assertFalse(option2.isRequired());
        assertTrue(option3.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        builder.withPattern("a: !");
        builder.create();
    }
}