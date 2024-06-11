package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithBasicOptions() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .create('l');

        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
    }

    @Test
    public void testOptionBuilderWithOptionalArgument() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .isRequired(false)
                .hasArg(true)
                .withArgName("argName")
                .create('l');

        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
    }

    @Test
    public void testOptionBuilderWithMultipleArguments() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .isRequired(false)
                .hasArgs()
                .withArgName("argName")
                .create('l');

        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .isRequired(false)
                .hasArg(true)
                .withArgName("argName")
                .withValueSeparator(':')
                .create('l');

        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertArrayEquals(new String[]{"argValue1", "argValue2"}, option.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create('l');
    }
}