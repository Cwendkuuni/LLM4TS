package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOptions() {
        Option option = OptionBuilder.create('A');
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("foo");
        Option option = OptionBuilder.create('A');
        assertEquals("foo", option.getLongOpt());
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('A');
        assertEquals(1, option.getArgs().getMinimumNumberOfValues());
        assertFalse(OptionBuilder.hasArg(false).hasArg());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create('A');
        assertTrue(option.isRequired());
        OptionBuilder.isRequired(false);
        Option option1 = OptionBuilder.create('A');
        assertFalse(option1.isRequired());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('A');
        assertTrue(option.hasOptionalArg());
        OptionBuilder.hasOptionalArg(false);
        Option option1 = OptionBuilder.create('A');
        assertFalse(option1.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('A');
        assertEquals(':', option.getValueSeparator());
        OptionBuilder.withValueSeparator();
        Option option1 = OptionBuilder.create('A');
        assertEquals('=', option1.getValueSeparator());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("foo");
        Option option = OptionBuilder.create('A');
        assertEquals("foo", option.getDescription());
        OptionBuilder.withDescription(null);
        Option option1 = OptionBuilder.create('A');
        assertNull(option1.getDescription());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("bar");
        Option option = OptionBuilder.create('A');
        assertEquals("bar", option.getArgName());
        OptionBuilder.withArgName(null);
        Option option1 = OptionBuilder.create('A');
        assertNull(option1.getArgName());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create('A');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getMaximumNumberOfValues());
        OptionBuilder.hasArgs(3);
        Option option1 = OptionBuilder.create('A');
        assertEquals(3, option1.getArgs().getMaximumNumberOfValues());
    }

    @Test
    public void testHasArgsWithType() {
        OptionBuilder.hasArgs(String.class);
        Option option = OptionBuilder.create('A');
        assertEquals(String.class, option.getType());
        OptionBuilder.hasArgs(Integer.class);
        Option option1 = OptionBuilder.create('A');
        assertEquals(Integer.class, option1.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}