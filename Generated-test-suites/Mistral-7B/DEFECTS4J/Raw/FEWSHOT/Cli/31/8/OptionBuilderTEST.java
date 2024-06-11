package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testCreateDefaultOption() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithOptString() {
        Option option = OptionBuilder.withOptString("shortOption")
                .create();
        assertEquals("", option.getLongOpt());
        assertEquals("shortOption", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithDescription() {
        Option option = OptionBuilder.withDescription("Option description")
                .create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testCreateOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateOptionWithHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithArg() {
        Option option = OptionBuilder.withArgName("argName")
                .hasArg().create();
        assertEquals("", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithArgAndType() {
        Option option = OptionBuilder.withArgName("argName")
                .hasArg().withType(String.class).create();
        assertEquals("", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertEquals(String.class, option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithHasOptionalArgsAndType() {
        Option option = OptionBuilder.hasOptionalArgs(3)
                .withType(String[].class).create();
        assertEquals("", option.getLongOpt());
        assertEquals("", option.getOptString());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertEquals(String[].class, option.getType());
        assertFalse(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasArg()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasOptionalArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasOptionalArg()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasArgAndArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasArg()
                .withArg("argValue")
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertEquals("argValue", option.getValue());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasArgAndArgAndType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasArg()
                .withArg("argValue")
                .withType(String.class)
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(new int[]{Option.VALUE_INDEX}, option.getArgPosition().length);
        assertEquals("argValue", option.getValue());
        assertEquals(String.class, option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasOptionalArgs()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasOptionalArgsAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasOptionalArgs()
                .withValueSeparator(' ')
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(' ', option.getValueSeparator());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasOptionalArgsAndValueSeparatorAndType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasOptionalArgs()
                .withValueSeparator(' ')
                .withType(String[].class)
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertEquals(' ', option.getValueSeparator());
        assertEquals(String[].class, option.getType());
        assertFalse(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isUnbounded());
    }

    @Test
    public void testCreateOptionWithLongOptAndDescriptionAndArgNameAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option description")
                .withArgName("argName")
                .hasArgs()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getOptString());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().length);
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.isUnbounded());
    }
}