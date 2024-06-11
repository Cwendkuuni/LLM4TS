package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
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
    public void testHasArg_BooleanTrue() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArg_BooleanFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertEquals(-1, option.getArgs());
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
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequired_BooleanTrue() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testIsRequired_BooleanFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Integer.MAX_VALUE, option.getArgs());
    }

    @Test
    public void testHasArgs_Int() {
        OptionBuilder.hasArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Integer.MAX_VALUE, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_Int() {
        OptionBuilder.hasOptionalArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        Option option = OptionBuilder.create();
        assertEquals(Object.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }
}