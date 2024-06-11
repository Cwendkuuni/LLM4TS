package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();

        assertEquals(null, OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("option");

        assertEquals("option", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();

        assertEquals(1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasArgWithOptionalArg() {
        OptionBuilder.hasArg(true);

        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
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
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');

        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasArgsWithOptionalArg() {
        OptionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasArgsWithFixedNumberOfArgs() {
        OptionBuilder.hasArgs(3);

        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);

        assertSame(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");

        assertEquals("description", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("option");
        Option option = OptionBuilder.create('o');

        assertEquals("option", option.getLongOpt());
        assertEquals(OptionBuilder.description, option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(OptionBuilder.required == option.isRequired());
        assertEquals(OptionBuilder.numberOfArgs, option.getArgs().length);
        assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
        assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
        assertEquals(OptionBuilder.argName, option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndValue() {
        OptionBuilder.withLongOpt("option");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.withArgName("value");

        Option option = OptionBuilder.create('o');
        option.parseArgument("--option:value");

        assertEquals("value", option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndMultipleValues() {
        OptionBuilder.withLongOpt("option");
        OptionBuilder.hasArgs();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(',');
        OptionBuilder.withArgName("value");

        Option option = OptionBuilder.create('o');
        option.parseArgument("--option value1,value2");

        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }
}