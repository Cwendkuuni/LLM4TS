package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for {@link OptionBuilder} class.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                      .create();

        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                      .create();

        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArgWithName()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .hasArg()
                                      .create();

        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                      .create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                      .create('D');

        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                      .create('D');

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                      .create('D');

        assertEquals(1, option.getArgs().length);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                                      .create('D');

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs(2)
                                      .create('D');

        assertEquals(2, option.getArgs().length);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.withType(Integer.class)
                                      .create('D');

        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.withDescription("description")
                                      .create('D');

        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreate()
    {
        Option option = OptionBuilder.create('D');

        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
    }

    @Test
    public void testCreateWithArgName()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .create('D');

        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateWithArgNameAndLongOpt()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndType()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescription()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .withDescription("description")
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequired()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .withDescription("description")
                                      .isRequired()
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequiredAndHasArg()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .withDescription("description")
                                      .isRequired()
                                      .hasArg()
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequiredAndHasArgAndValueSeparator()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .withDescription("description")
                                      .isRequired()
                                      .hasArg()
                                      .withValueSeparator(',')
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequiredAndHasArgAndValueSeparatorAndOptionalArg()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String.class)
                                      .withDescription("description")
                                      .isRequired()
                                      .hasArg()
                                      .withValueSeparator(',')
                                      .hasOptionalArg()
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String.class, option.getType());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequiredAndHasArgAndValueSeparatorAndOptionalArgAndHasArgs()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String[].class)
                                      .withDescription("description")
                                      .isRequired()
                                      .hasArg()
                                      .withValueSeparator(',')
                                      .hasOptionalArg()
                                      .hasArgs()
                                      .create('D');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(String[].class, option.getType());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testCreateWithArgNameAndLongOptAndTypeAndDescriptionAndIsRequiredAndHasArgAndValueSeparatorAndOptionalArgAndHasArgsAndCreateOption()
    {
        Option option = OptionBuilder.withArgName("argName")
                                      .withLongOpt("longOpt")
                                      .withType(String[].class)
                                      .withDescription("description")
                                      .isRequired()
                                      .hasArg()
                                      .withValueSeparator(',')
                                      .hasOptionalArg()
                                      .hasArgs()
                                      .create('A')
                                      .create('D');

        Option optionA = option.findOption("A");
        Option optionD = option.findOption("D");

        assertEquals("longOpt", optionD.getLongOpt());
        assertEquals("argName", optionD.getArgName());
        assertEquals(String[].class, optionD.getType());
        assertEquals("description", optionD.getDescription());
        assertTrue(optionD.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, optionD.getArgs().length);
        assertTrue(optionD.optionalArg);

        assertEquals("A", optionA.getLongOpt());
        assertEquals("argName", optionA.getArgName());
        assertEquals(null, optionA.getType());
        assertEquals("description for A", optionA.getDescription());
        assertFalse(optionA.isRequired());
        assertEquals(Option.UNINITIALIZED, optionA.getArgs().length);
    }
}