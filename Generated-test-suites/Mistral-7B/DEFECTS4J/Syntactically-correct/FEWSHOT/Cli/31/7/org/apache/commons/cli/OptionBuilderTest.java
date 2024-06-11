package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderLongOptWithDescription() {
        String longOpt = "myLongOpt";
        String description = "Description for myLongOpt";

        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(longOpt).withDescription(description);
        Option option = optionBuilder.create();

        assertEquals(longOpt, option.getLongOpt());
        assertEquals(description, option.getDescription());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasArgWithArgName() {
        String argName = "myArgName";

        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName(argName);
        Option option = optionBuilder.create();

        assertEquals(argName, option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertFalse(option.isRequired());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasArgsWithValueSeparator() {
        char valueSeparator = ':';

        OptionBuilder optionBuilder = OptionBuilder.hasArgs().withValueSeparator(valueSeparator);
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(valueSeparator, option.getValueSeparator());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();

        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        int numOptionalArgs = 2;

        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(numOptionalArgs);
        Option option = optionBuilder.create();

        assertEquals(numOptionalArgs, option.getArgs());
        assertFalse(option.isRequired());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderWithType() {
        Class<String> stringType = String.class;

        OptionBuilder optionBuilder = OptionBuilder.withType(stringType);
        Option option = optionBuilder.create();

        assertSame(String.class, option.getType());
        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("");

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(null);

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithEmptyArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    @Test
    public void testOptionBuilderCreateWithNullArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName(null);

        try {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        verifyOptionBuilderPropertiesReset();
    }

    private void verifyOptionBuilderPropertiesReset() {
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals('\0', OptionBuilder.valuesep);
    }
}