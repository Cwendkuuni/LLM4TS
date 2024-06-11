package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("opt").create();
        assertEquals("opt", OptionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg().create();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true).create();
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArg(false).create();
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg").create();
        assertEquals("arg", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired().create();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg().create();
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs().create();
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2).create();
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder.withValueSeparator().create();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true).create();
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false).create();
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2).create();
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class).create();
        assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("desc").create();
        assertEquals("desc", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
    }
}