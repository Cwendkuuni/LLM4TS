package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(Option.DEFAULT_VALUE_SEPARATOR, OptionBuilder.valuesep);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption");
        assertEquals("myOption", OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgWithName() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsWithLimit() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withDescription("myDescription");
        assertEquals("myDescription", OptionBuilder.description);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testCreateOptionWithoutLongOpt() {
        OptionBuilder.reset();
        try {
            OptionBuilder.create((char) 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateOption() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .hasArg()
                .isRequired();
        Option option = OptionBuilder.create('o');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getArgType());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateOptionWithValue() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .hasArg()
                .isRequired()
                .withValue("defaultValue");
        Option option = OptionBuilder.create('o');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals("defaultValue", option.getValue());
    }

    @Test
    public void testCreateOptionWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .hasArgs(2)
                .withValueSeparator(':');
        Option option = OptionBuilder.create('o');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(Option.VALUE_MULTIVALUED, option.getArgType());
        assertEquals(2, option.getMaxArgValues());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateOptionWithOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .hasOptionalArgs(3);
        Option option = OptionBuilder.create('o');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(Option.VALUE_OPTIONAL, option.getArgType());
        assertNull(option.getValue());
    }

}