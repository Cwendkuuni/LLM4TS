package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithOptString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create('t');
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreateWithDescription() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

    @Test
    public void testCreateWithRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasKey() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testReset() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(0, option.getValueSeparator());
    }
}