package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithDefaults() {
        Option option = OptionBuilder.create('N');
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertEquals(Option.NO_VALUE_SEPARATOR, option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("no-logging")
                .create('N');
        assertEquals("no-logging", option.getLongOpt());
    }

    @Test
    public void testCreateIsRequired() {
        Option option = OptionBuilder.isRequired().create('N');
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testCreateHasArg() {
        Option option = OptionBuilder.hasArg().create('N');
        assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs()[0]);
    }

    @Test
    public void testCreateHasArgs() {
        Option option = OptionBuilder.hasArgs().create('N');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testCreateHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create('N');
        assertEquals(Option.OPTIONAL_ARGUMENT, option.getArgs()[0]);
    }

    @Test
    public void testCreateHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create('N');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testCreateHasOptionalArgsWithLimit() {
        Option option = OptionBuilder.hasOptionalArgs(3).create('N');
        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testCreateWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create('N');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithArgName() {
        Option option = OptionBuilder.withArgName("filename").create('N');
        assertEquals("filename", option.getArgName());
    }

    @Test
    public void testCreateWithType() {
        Option option = OptionBuilder.withType(Integer.class).create('N');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreateWithDescription() {
        Option option = OptionBuilder.withDescription("No logging").create('N');
        assertEquals("No logging", option.getDescription());
    }
}