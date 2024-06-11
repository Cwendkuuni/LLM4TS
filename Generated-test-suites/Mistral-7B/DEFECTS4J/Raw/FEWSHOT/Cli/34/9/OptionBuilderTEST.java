package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultValues() {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs().length);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasArgWithOptionalArg() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs().length);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Option option = OptionBuilder.create();
        assertEquals("testArgName", option.getArgName());
        assertNull(OptionBuilder.argName);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.hasOptionalArgs());
    }

    @Test
    public void testHasArgsWithNumArgs() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs().length);
        assertFalse(option.hasOptionalArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Option option = OptionBuilder.create();
        assertSame(String.class, option.getType());
        assertNull(OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testDescription", option.getDescription());
        assertNull(OptionBuilder.description);
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.isRequired();
        OptionBuilder.hasArgs();
        OptionBuilder.withType(String.class);
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(1, option.getArgs().length);
        assertEquals("testArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertSame(String.class, option.getType());
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('D');
        assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}