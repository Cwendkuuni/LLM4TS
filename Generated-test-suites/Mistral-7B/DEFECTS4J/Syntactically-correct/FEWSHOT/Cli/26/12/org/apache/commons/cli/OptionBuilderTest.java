package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
        Option option = optionBuilder.create('o');
        assertEquals("optLong", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArg_hasArgFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Option option = optionBuilder.create('b');
        assertNull(option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create('c');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('d');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create('e');
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequired_false() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Option option = optionBuilder.create('f');
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('g');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create('h');
        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('i');
        assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('j');
        assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs_numArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create('k');
        assertEquals(2, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('l');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option option = optionBuilder.create('m');
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create('n');
    }

}