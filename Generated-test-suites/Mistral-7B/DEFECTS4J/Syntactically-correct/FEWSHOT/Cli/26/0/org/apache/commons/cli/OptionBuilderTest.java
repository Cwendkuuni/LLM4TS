package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongOpt");
        assertEquals("optionLongOpt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithName() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
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
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
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
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        Object type = new String("stringType");
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        assertEquals(type, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        String description = "description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        assertEquals(description, OptionBuilder.description);
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        String optionLongOpt = "longOpt";
        Option option = OptionBuilder.withLongOpt(optionLongOpt)
                .hasArg()
                .create('o');
        assertEquals(optionLongOpt, option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithValue() {
        OptionBuilder.reset();
        String optionLongOpt = "longOpt";
        String argName = "argName";
        Option option = OptionBuilder.withLongOpt(optionLongOpt)
                .withArgName(argName)
                .hasArg("value")
                .create('o');
        assertEquals(optionLongOpt, option.getLongOpt());
        assertEquals(argName, option.getArgName());
        assertFalse(option.isRequired());
        assertEquals("value", option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.create('o');
        throw new AssertionError("IllegalArgumentException not thrown");
    }
}