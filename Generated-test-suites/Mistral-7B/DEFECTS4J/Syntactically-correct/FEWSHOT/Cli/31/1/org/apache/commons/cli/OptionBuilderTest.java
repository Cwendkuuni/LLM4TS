package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.reset();

        assertEquals(null, OptionBuilder.longopt);
        assertEquals(0, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.required);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withLongOpt("longOpt");

        assertEquals("longOpt", builder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasArg();

        assertEquals(1, builder.numberOfArgs);
    }

    @Test
    public void testHasArg_false() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasArg(false);

        assertEquals(Option.UNINITIALIZED, builder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withArgName("argName");

        assertEquals("argName", builder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.isRequired();

        assertTrue(builder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withValueSeparator(':');

        assertEquals(':', builder.valuesep);
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withValueSeparator();

        assertEquals('=', builder.valuesep);
    }

    @Test
    public void testIsRequired_false() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.isRequired(false);

        assertFalse(builder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
    }

    @Test
    public void testHasArgs_num() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasArgs(3);

        assertEquals(3, builder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasOptionalArg();

        assertEquals(1, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_num() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.hasOptionalArgs(2);

        assertEquals(2, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withType(String.class);

        assertEquals(String.class, builder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withDescription("description");

        assertEquals("description", builder.description);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreate_noLongOpt() {
        OptionBuilder.instance().create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noType() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withType(Integer.class);
        builder.reset();

        try {
            builder.create('o');
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid option character: 'o'", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreate_longOpt() {
        OptionBuilder builder = OptionBuilder.instance();
        builder.withLongOpt("longOpt");
        builder.hasArg();
        builder.withArgName("argName");
        builder.isRequired();
        builder.withValueSeparator(':');
        builder.withType(Integer.class);

        Option option = builder.create('o');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals(Option.VALUE_REQUIRED, option.getOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }
}