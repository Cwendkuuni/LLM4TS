package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testLongOptOption() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithDescription() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .withDescription("Foo option")
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertEquals("Foo option", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testRequiredOption() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .isRequired()
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithArg() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasArg()
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasArg()
                .withArgName("bar")
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("bar", option.getArgName());
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasArg()
                .withValueSeparator(':')
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().getValue());
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionalArgOption() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasOptionalArg()
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithValueSeparatorAndArg() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasArg()
                .withValueSeparator(':')
                .hasOptionalArg()
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionWithType() {
        Option option = OptionBuilder.withLongOpt("--foo")
                .hasArg()
                .withValueSeparator(':')
                .hasOptionalArg()
                .withType(String.class)
                .create();
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
        assertEquals(String.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
}