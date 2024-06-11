package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithEmptySettings() {
        Option option = OptionBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateWithDefaultSettings() {
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithCustomSettings() {
        OptionBuilder builder = OptionBuilder.withLongOpt("myLongOpt")
                .withDescription("This is a custom option")
                .isRequired()
                .hasArg()
                .withArgName("myArgName")
                .withType(Integer.class)
                .withValueSeparator(':');

        Option option = builder.create('a');

        assertNotNull(option);
        assertEquals("myLongOpt", option.getLongOpt());
        assertEquals("This is a custom option", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.Value.INTEGER_CLASS, option.getArgs().getValue());
        assertEquals(Integer.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("myArgName", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMissingLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}