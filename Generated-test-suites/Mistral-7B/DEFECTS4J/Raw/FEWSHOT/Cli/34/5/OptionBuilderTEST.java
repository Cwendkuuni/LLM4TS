package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionWithLongOptAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option test");
        Option option = optionBuilder.create();
        assertEquals("longOption", option.getLongOpt());
        assertNotNull(option.getDescription());
        assertEquals("This is a long option test", option.getDescription());
    }

    @Test
    public void testOptionWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        assertNull(option.getArgName());
        OptionBuilder.withArgName("newArgName");
        option = optionBuilder.create();
        assertEquals("newArgName", option.getArgName());
    }

    @Test
    public void testOptionWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        OptionBuilder.hasArg(true);
        option = optionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionWithArgAndArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionWithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.isOptional());
        OptionBuilder.hasOptionalArg();
        option = optionBuilder.create();
        assertEquals(1, option.getArgs());
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionWithOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionWithOptionalArgsAndArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs()
                .withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.isOptional());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionWithArgNameAndArgValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName")
                .withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        assertNull(option.getType());
        OptionBuilder.withType(Integer.class);
        option = optionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('/');
        Option option = optionBuilder.create();
        assertEquals('/', option.getValueSeparator());
    }

    @Test
    public void testOptionWithRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertFalse(option.isRequired());
        OptionBuilder.isRequired();
        option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
    }
}