package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\u0000', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("optLong");
        assertEquals("optLong", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_false() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_false() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_num() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_num() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withDescription("Option description");
        assertEquals("Option description", OptionBuilder.description);
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("optLong")
                .withDescription("Option description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = builder.create('o');
        assertNotNull(option);
        assertEquals("optLong", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.create('o');
    }
}