package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionWithLongOptAndNoArgs() {
        Option opt = OptionBuilder.withLongOpt("longopt")
                                   .create();
        assertNotNull(opt);
        assertEquals("longopt", opt.getLongOpt());
        assertEquals(null, opt.getArgName());
        assertEquals(Option.NO_ARGUMENTS, opt.getArgs());
        assertNull(opt.getType());
        assertNull(opt.getValueSeparator());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
    }

    @Test
    public void testOptionWithLongOptAndRequiredArg() {
        Option opt = OptionBuilder.withLongOpt("longopt")
                                   .hasArg()
                                   .create();
        assertNotNull(opt);
        assertEquals("longopt", opt.getLongOpt());
        assertEquals("arg", opt.getArgName());
        assertEquals(Option.ONE_ARGUMENT, opt.getArgs());
        assertNull(opt.getType());
        assertNull(opt.getValueSeparator());
        assertTrue(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
    }

    @Test
    public void testOptionWithLongOptAndOptionalArg() {
        Option opt = OptionBuilder.withLongOpt("longopt")
                                   .hasOptionalArg()
                                   .create();
        assertNotNull(opt);
        assertEquals("longopt", opt.getLongOpt());
        assertEquals("arg", opt.getArgName());
        assertEquals(Option.ONE_ARGUMENT, opt.getArgs());
        assertNull(opt.getType());
        assertNull(opt.getValueSeparator());
        assertFalse(opt.isRequired());
        assertTrue(opt.hasOptionalArg());
    }

    @Test
    public void testOptionWithLongOptAndValueSeparator() {
        Option opt = OptionBuilder.withLongOpt("longopt")
                                   .withValueSeparator(',')
                                   .create();
        assertNotNull(opt);
        assertEquals("longopt", opt.getLongOpt());
        assertEquals(null, opt.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, opt.getArgs());
        assertNull(opt.getType());
        assertEquals(',', opt.getValueSeparator());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
    }

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option opt = OptionBuilder.withLongOpt("longopt")
                                   .withDescription("A long option with description")
                                   .create();
        assertNotNull(opt);
        assertEquals("longopt", opt.getLongOpt());
        assertEquals("A long option with description", opt.getDescription());
        assertEquals(null, opt.getArgName());
        assertEquals(Option.NO_ARGUMENTS, opt.getArgs());
        assertNull(opt.getType());
        assertNull(opt.getValueSeparator());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt() {
        OptionBuilder.reset();
        Option opt = OptionBuilder.create();
        assertNull(opt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidLongOpt() {
        OptionBuilder.reset();
        Option opt = OptionBuilder.withLongOpt("")
                                   .create();
        assertNull(opt);
    }

    @Test
    public void testOptionWithShortOpt() {
        Option opt = OptionBuilder.create('s')
                                   .create();
        assertNotNull(opt);
        assertEquals('s', opt.getValue());
        assertEquals(null, opt.getLongOpt());
        assertEquals(null, opt.getDescription());
        assertEquals(null, opt.getArgName());
        assertEquals(Option.NO_ARGUMENTS, opt.getArgs());
        assertNull(opt.getType());
        assertNull(opt.getValueSeparator());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
    }
}