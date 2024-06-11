package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateDefaultOption() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertEquals("arg", option.getArgName());
        assertEquals(0, option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .create();
        assertEquals("myOption", option.getLongOpt());
    }

    @Test
    public void testCreateOptionWithDescription() {
        Option option = OptionBuilder.withDescription("My Option Description")
                .create();
        assertEquals("My Option Description", option.getDescription());
    }

    @Test
    public void testCreateOptionHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getArgs().getValue());
    }

    @Test
    public void testCreateOptionHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
    }

    @Test
    public void testCreateOptionIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testCreateOptionHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals(1, option.getArgs().getValue());
        assertEquals(true, option.isOptionalArg());
    }

    @Test
    public void testCreateOptionWithType() {
        Option option = OptionBuilder.withType(String.class)
                .create();
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testCreateOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(',')
                .create();
        assertEquals(',', option.getValueSeparator());
    }
}