package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertFalse(OptionBuilder.required);
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertEquals(Option.NO_VALUE, option.getValue());
    }

    @Test
    public void testSimpleOption() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .withDescription("option description")
                .hasType(Integer.TYPE);
        Option option = builder.create();
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertEquals(Integer.TYPE, option.getType());
        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionalOption() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt")
                .isRequired(false)
                .hasArg(false)
                .hasOptionalArgs(2)
                .withDescription("option description")
                .hasType(String.class);
        Option option = builder.create();
        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertEquals(String.class, option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testValueSeparatedOption() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .withDescription("option description")
                .hasType(int[].class)
                .withValueSeparator(';');
        Option option = builder.create();
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertEquals(int[].class, option.getType());
        assertEquals(new int[]{1, 2}, option.getValue());
    }
}