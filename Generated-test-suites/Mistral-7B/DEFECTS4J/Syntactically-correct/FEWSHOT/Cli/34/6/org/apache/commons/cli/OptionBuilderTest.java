package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();

        assertEquals(null, OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        assertNull(OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("OPT_LONG");

        assertEquals("OPT_LONG", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();

        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_hasArgFalse() {
        OptionBuilder.hasArg(false);

        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("ARG_NAME");

        assertEquals("ARG_NAME", OptionBuilder.argName);
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
    public void testWithValueSeparator_noArg() {
        OptionBuilder.withValueSeparator();

        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_hasArg() {
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();

        assertTrue(OptionBuilder.required);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_noArg() {
        OptionBuilder.isRequired();

        OptionBuilder.reset();

        throw new IllegalArgumentException("must specify longopt");
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_numArgs() {
        OptionBuilder.hasArgs(5);

        assertEquals(5, OptionBuilder.numberOfArgs);
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
    public void testHasOptionalArgs_numArgs() {
        OptionBuilder.hasOptionalArgs(3);

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
        OptionBuilder.withDescription("Option Description");

        assertEquals("Option Description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws Exception {
        OptionBuilder.withLongOpt("OPT_LONG");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("ARG_NAME");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.hasArgs();
        OptionBuilder.withType(Integer.class);
        OptionBuilder.withDescription("Option Description");

        Option option = OptionBuilder.create('L');

        assertEquals("OPT_LONG", option.getLongOpt());
        assertEquals("Option Description", option.getDescription());
        assertEquals("ARG_NAME", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() throws Exception {
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("ARG_NAME");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.hasArgs();
        OptionBuilder.withType(Integer.class);
        OptionBuilder.withDescription("Option Description");

        OptionBuilder.reset();

        throw new IllegalArgumentException("must specify longopt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_invalidChar() throws Exception {
        OptionBuilder.withLongOpt("OPT_LONG");

        OptionBuilder.reset();

        throw new IllegalArgumentException("Invalid option character L");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_longOptExists() throws Exception {
        OptionBuilder.withLongOpt("OPT_LONG");
        OptionBuilder.create('L');

        OptionBuilder.reset();

        throw new IllegalArgumentException("Option definition for option L already exists");
    }
}