package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals('\u0000', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        assertEquals("optionLongName", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithHasArgFalse() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(OptionBuilder.type);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        assertEquals("Option description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Option description")
                .isRequired()
                .withArgName("argName")
                .hasArg()
                .withValueSeparator(':')
                .hasOptionalArg();
        Option option = optionBuilder.create('O');
        assertNotNull(option);
        assertEquals("optionLongName", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        try {
            Option option = optionBuilder.create('O');
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
            throw e;
        }
    }
}