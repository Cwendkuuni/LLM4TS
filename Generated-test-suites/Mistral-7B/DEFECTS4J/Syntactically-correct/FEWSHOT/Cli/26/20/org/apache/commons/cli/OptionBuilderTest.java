package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals("Option a requires an argument", option.getDescription());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('b');
        assertEquals("Option b has an optional argument", option.getDescription());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue(0));
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('c');
        assertEquals("Option c can have unlimited arguments", option.getDescription());
        assertNull(option.getValue(0));
    }

    @Test
    public void testOptionBuilderHasArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('d');
        assertEquals("Option d can have 2 arguments", option.getDescription());
        assertEquals("arg1", option.getValue(0));
        assertEquals("arg2", option.getValue(1));
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('e');
        assertEquals("Option e accepts an Integer value", option.getDescription());
        assertEquals(Option.TYPE_INT, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option f description");
        Option option = optionBuilder.create('f');
        assertEquals("Option f description", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option.longOption");
        Option option = optionBuilder.create('g');
        assertEquals("g", option.getOpt());
        assertEquals("option.longOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('h');
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        optionBuilder.create('i');
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        Option option = optionBuilder.create('j');
        option.setValues(new String[]{"value1", "value2"});
        assertEquals(";", option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argumentName");
        Option option = optionBuilder.create('k');
        assertEquals("argumentName", option.getArgName());
    }

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName").withValueSeparator(':');
        Option option = optionBuilder.create('l');
        assertEquals("argName", option.getArgName());
        assertEquals(':', option.getValueSeparator());
        OptionBuilder.reset();
        Option newOption = OptionBuilder.create('m');
        assertNull(newOption.getArgName());
        assertNull(newOption.getValueSeparator());
    }
}