package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithDefaults() {
        Option opt = OptionBuilder.create();
        assertNotNull(opt);
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option opt = OptionBuilder.withLongOpt("myLong opt")
                .create();
        assertEquals("myLong opt", opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithDescription() {
        Option opt = OptionBuilder.withDescription("An option with a description")
                .create();
        assertNull(opt.getLongOpt());
        assertEquals("An option with a description", opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithArgName() {
        Option opt = OptionBuilder.withArgName("argName")
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateIsRequired() {
        Option opt = OptionBuilder.isRequired()
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertTrue(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithNumberOfArgs() {
        Option opt = OptionBuilder.hasArgs(3)
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(3, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithOptionalArg() {
        Option opt = OptionBuilder.hasOptionalArg()
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertTrue(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithOptionalArgs() {
        Option opt = OptionBuilder.hasOptionalArgs()
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, opt.getNumberOfArgs());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithOptionalArgsWithNum() {
        Option opt = OptionBuilder.hasOptionalArgs(2)
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(2, opt.getNumberOfArgs());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithType() {
        Option opt = OptionBuilder.withType(Integer.class)
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertSame(Integer.class, opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithValueSeparator() {
        Option opt = OptionBuilder.withValueSeparator(':')
                .create();
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
        assertNull(opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertSame((char) ':', opt.getValueSeparator());
        assertFalse(opt.hasOptionalArg());
    }

    @Test
    public void testCreateWithLongOptArgName() {
        Option opt = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .create();
        assertEquals("longOpt", opt.getLongOpt());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptArgNameIsRequired() {
        Option opt = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .create();
        assertEquals("longOpt", opt.getLongOpt());
        assertEquals("argName", opt.getArgName());
        assertTrue(opt.isRequired());
        assertEquals(Option.UNINITIALIZED, opt.getNumberOfArgs());
        assertNull(opt.getType());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptArgNameOptional() {
        Option opt = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .hasArgs(2)
                .withArgName("argName")
                .create();
        assertEquals("longOpt", opt.getLongOpt());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, opt.getNumberOfArgs());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptArgNameOptionalAndType() {
        Option opt = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .hasArgs(2)
                .withArgName("argName")
                .withType(String[].class)
                .create();
        assertEquals("longOpt", opt.getLongOpt());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, opt.getNumberOfArgs());
        assertSame(String[].class, opt.getType());
        assertTrue(opt.hasOptionalArg());
        assertNull(opt.getValueSeparator());
    }
}