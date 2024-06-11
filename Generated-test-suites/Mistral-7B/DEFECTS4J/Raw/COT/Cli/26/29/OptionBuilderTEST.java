package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        Option option = optionBuilder.create();
        assertEquals(type, option.getValue());
    }

    @Test
    public void testWithDescription() {
        String description = "description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        assertEquals(description, option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        Option option = OptionBuilder.create();
        fail("must throw IllegalArgumentException");
    }

    @Test
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        Option option = optionBuilder.create();
        assertEquals("", option.getLongOpt());
    }

    @Test
    public void testCreateWithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        try {
            Option option = optionBuilder.create();
            fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateWithEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("");
        Option option = optionBuilder.create();
        assertEquals("", option.getDescription());
    }

    @Test
    public void testCreateWithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(null);
        try {
            Option option = optionBuilder.create();
            fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}