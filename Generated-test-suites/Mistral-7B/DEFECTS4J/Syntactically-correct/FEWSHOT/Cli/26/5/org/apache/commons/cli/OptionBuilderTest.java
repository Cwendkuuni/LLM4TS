package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = optionBuilder.create('L');
        assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        assertEquals(1, option.getNumArgs());
        assertNotNull(option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');
        assertEquals(1, option.getNumArgs());
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('G');
        assertEquals(Option.UNLIMITED_VALUES, option.getNumArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create('N');
        assertEquals(3, option.getNumArgs());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('V');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
        Option option = optionBuilder.create('D');
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('R');
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('I');
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionBuilderCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create('X');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("X");
        optionBuilder.create('X');
    }
}