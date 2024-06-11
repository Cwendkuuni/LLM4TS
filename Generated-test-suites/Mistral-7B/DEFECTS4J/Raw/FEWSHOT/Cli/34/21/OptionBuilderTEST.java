package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionsWithDefaultValues() {
        Option option = OptionBuilder.create('O');
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionsWithCustomValues() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .hasArgs(2)
                .withType(String.class)
                .hasOptionalArg()
                .withValueSeparator(':');

        Option option = builder.create('O');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertEquals(String.class, option.getType());
        assertTrue(option.hasOptionalArg());
        assertEquals(":", option.getValueSeparator());
        assertNull(option.getDescription());
    }
}