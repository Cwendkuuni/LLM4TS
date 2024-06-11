package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultOption() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithDescription() {
        Option option = OptionBuilder.withDescription("An option with a description")
                .create();
        assertNull(option.getLongOpt());
        assertEquals("An option with a description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = OptionBuilder.isRequired()
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertTrue(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionHasArg() {
        Option option = OptionBuilder.hasArg()
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithType() {
        Option option = OptionBuilder.withType(Integer.class)
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getType());
    }

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withDescription("An option with a longopt and description")
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt and description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameAndDescription() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequired() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequiredAndHasArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .hasArg()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequiredAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .hasOptionalArg()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequiredAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .hasArgs()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequiredAndHasType() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .withType(Double.class)
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(Double.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsRequiredAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .isRequired()
                .withValueSeparator(',')
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(',', option.getValueSeparator());
        assertNull(option.getType());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptional() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptionalAndHasArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .hasArg()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptionalAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .hasOptionalArgs()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptionalAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .hasArgs()
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptionalAndHasType() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .withType(String.class)
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithLongOptArgNameDescriptionIsOptionalAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .withArgName("argName")
                .withDescription("An option with a longopt, argname, and description")
                .hasOptionalArg()
                .withValueSeparator(':')
                .create();
        assertEquals("myOption", option.getLongOpt());
        assertEquals("An option with a longopt, argname, and description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getType());
    }
}