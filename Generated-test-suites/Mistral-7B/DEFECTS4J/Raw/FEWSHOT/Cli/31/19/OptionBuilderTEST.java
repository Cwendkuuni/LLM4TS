package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(false, OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.required);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder.withDescription("Option description");
        assertEquals("Option description", OptionBuilder.description);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testOptionCreation() {
        OptionBuilder.withLongOpt("longOpt")
                     .isRequired()
                     .hasArg();
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithNullLongOpt() {
        OptionBuilder.withLongOpt(null);
        OptionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithUnsetLongOpt() {
        OptionBuilder.create('o');
    }
}