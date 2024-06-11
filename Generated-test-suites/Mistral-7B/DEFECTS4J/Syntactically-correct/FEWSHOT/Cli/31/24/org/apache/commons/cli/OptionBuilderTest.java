package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption");
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option");
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgName() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName");
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArg();
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs());
        assertEquals(false, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArgAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArg()
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArgAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArg()
                .withType(String[].class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String[].class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndRequired() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired();
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndRequiredAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired()
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndRequiredAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired()
                .withType(String.class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.isRequired());
        assertEquals(String.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArg();
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArgAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArg()
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArgAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArg()
                .withType(String[].class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String[].class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArgs(2);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(false, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArgsAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArgs(2)
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasOptionalArgsAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasOptionalArgs(2)
                .withType(String[].class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String[].class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArgs() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArgs(3);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(3, option.getArgs());
        assertEquals(false, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArgsAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArgs(3)
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(3, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndHasArgsAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .hasArgs(3)
                .withType(String[].class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(3, option.getArgs());
        assertEquals(false, option.isRequired());
        assertEquals(String[].class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndIsRequired() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired();
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndIsRequiredAndType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired()
                .withType(String.class);
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescriptionAndArgNameAndIsRequiredAndTypeAndValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOption")
                .withDescription("This is a long option")
                .withArgName("argName")
                .isRequired()
                .withType(String.class)
                .withValueSeparator(':');
        Option option = builder.create();
        assertNotNull(option);
        assertEquals("longOption", option.getLongOpt());
        assertEquals("This is a long option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
        assertEquals(String.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }
}