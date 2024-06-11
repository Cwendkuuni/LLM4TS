package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultValues() {
        OptionBuilder builder = OptionBuilder.getInstance();

        assertNull(builder.getLongOpt());
        assertEquals("arg", builder.getArgName());
        assertFalse(builder.isRequired());
        assertEquals(Option.UNINITIALIZED, builder.getNumberOfArgs());
        assertNull(builder.getType());
        assertFalse(builder.isOptionalArg());
        assertEquals('\0', builder.getValueSeparator());
    }

    @Test
    public void testLongOptValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withLongOpt("optionName");

        assertEquals("optionName", builder.getLongOpt());
    }

    @Test
    public void testArgNameValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withArgName("argName");

        assertEquals("argName", builder.getArgName());
    }

    @Test
    public void testRequiredValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.isRequired();

        assertTrue(builder.isRequired());
    }

    @Test
    public void testNumberOfArgsValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.hasArg(true);

        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testOptionalArgValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.hasOptionalArg();

        assertTrue(builder.isOptionalArg());
    }

    @Test
    public void testValueSeparatorValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withValueSeparator(':');

        assertEquals(':', builder.getValueSeparator());
    }

    @Test
    public void testTypeValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withType(String.class);

        assertEquals(String.class, builder.getType());
    }

    @Test
    public void testDescriptionValue() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withDescription("description");

        assertEquals("description", builder.getDescription());
    }

    @Test
    public void testCreateOptionWithDefaultValues() {
        OptionBuilder builder = OptionBuilder.getInstance();

        Option option = builder.create('O');

        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.isOptionalArg());
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithSpecifiedValues() {
        OptionBuilder builder = OptionBuilder.getInstance();
        builder.withLongOpt("optionName")
                .hasArg(true)
                .withArgName("argName")
                .isRequired()
                .hasOptionalArg()
                .withValueSeparator(':')
                .withType(String.class)
                .withDescription("description");

        Option option = builder.create('O');

        assertEquals("optionName", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.isOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertEquals(String.class, option.getType());
        assertSame("description", option.getDescription());
    }
}