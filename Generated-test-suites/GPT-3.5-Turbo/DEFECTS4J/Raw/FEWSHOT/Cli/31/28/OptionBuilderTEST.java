package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);

        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertEquals(OptionBuilder.argName, option.getArgName());
        assertEquals(OptionBuilder.longopt, option.getLongOpt());
        assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
        assertEquals(OptionBuilder.required, option.isRequired());
        assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
        assertEquals(OptionBuilder.type, option.getType());
        assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
        assertEquals(OptionBuilder.description, option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertEquals(OptionBuilder.argName, option.getArgName());
        assertEquals(OptionBuilder.longopt, option.getLongOpt());
        assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
        assertEquals(OptionBuilder.required, option.isRequired());
        assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
        assertEquals(OptionBuilder.type, option.getType());
        assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
        assertEquals(OptionBuilder.description, option.getDescription());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        assertEquals(OptionBuilder.argName, option.getArgName());
        assertEquals(OptionBuilder.longopt, option.getLongOpt());
        assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
        assertEquals(OptionBuilder.required, option.isRequired());
        assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
        assertEquals(OptionBuilder.type, option.getType());
        assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
        assertEquals(OptionBuilder.description, option.getDescription());
    }
}