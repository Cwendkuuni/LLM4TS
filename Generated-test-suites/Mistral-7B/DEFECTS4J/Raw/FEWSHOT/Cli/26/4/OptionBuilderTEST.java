package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaults() {
        Option option = OptionBuilder.create(' optionName');
        assertNotNull(option);
        assertEquals("optionName", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals(0, option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithCustomValues() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .hasArgs(3)
                .withType(String.class)
                .withValueSeparator(';');

        Option option = optionBuilder.create('option');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(3, option.getArgs());
        assertEquals(String.class, option.getType());
        assertEquals(';', option.getValueSeparator());
    }
}