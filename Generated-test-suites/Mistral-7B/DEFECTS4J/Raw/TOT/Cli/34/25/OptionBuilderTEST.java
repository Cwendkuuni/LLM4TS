package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Testing the OptionBuilder class from Apache Commons CLI package.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionWithSingleValue()
    {
        Option option = OptionBuilder.hasArg()
                                     .withLongOpt("output")
                                     .withDescription("output file")
                                     .create('o');

        assertEquals("output", option.getLongOpt());
        assertEquals("output file", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        Option newOption = OptionBuilder.hasArg()
                                         .withLongOpt("output")
                                         .withDescription("output file")
                                         .isRequired()
                                         .create('o');

        assertEquals("output", newOption.getLongOpt());
        assertEquals("output file", newOption.getDescription());
        assertTrue(newOption.isRequired());
        assertNull(newOption.getArgs());

        Option optionWithValue = OptionBuilder.withArgName("file")
                                             .hasArg()
                                             .withLongOpt("inputFile")
                                             .withDescription("input file")
                                             .create('i');

        assertEquals("inputFile", optionWithValue.getLongOpt());
        assertEquals("input file", optionWithValue.getDescription());
        assertFalse(optionWithValue.isRequired());
        assertArrayEquals(new String[]{"file"}, optionWithValue.getArgs());
    }

    @Test
    public void testOptionWithMultipleValues()
    {
        Option option = OptionBuilder.hasArgs()
                                     .withLongOpt("files")
                                     .withDescription("input files")
                                     .create('f');

        assertEquals("files", option.getLongOpt());
        assertEquals("input files", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getArgs());

        Option optionWithValues = OptionBuilder.hasArgs(2)
                                              .withLongOpt("files")
                                              .withDescription("input files")
                                              .create('f');

        assertEquals("files", optionWithValues.getLongOpt());
        assertEquals("input files", optionWithValues.getDescription());
        assertTrue(optionWithValues.isRequired());
        assertArrayEquals(new String[2], optionWithValues.getArgs());

        Option optionWithValuesSeparator = OptionBuilder.hasArgs(2)
                                                      .withValueSeparator(' ')
                                                      .withLongOpt("files")
                                                      .withDescription("input files")
                                                      .create('f');

        assertEquals("files", optionWithValuesSeparator.getLongOpt());
        assertEquals("input files", optionWithValuesSeparator.getDescription());
        assertTrue(optionWithValuesSeparator.isRequired());
        assertArrayEquals(new String[]{"file1", "file2"}, optionWithValuesSeparator.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt()
    {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOptAndDescription()
    {
        OptionBuilder.withDescription("")
                    .create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOptAndDescriptionAndArgName()
    {
        OptionBuilder.withArgName("")
                    .create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOptAndDescriptionAndArgNameAndArg()
    {
        OptionBuilder.hasArg()
                    .create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOptAndDescriptionAndArgNameAndArgAndType()
    {
        OptionBuilder.hasArg()
                    .withType(String.class)
                    .create();
    }

    @Test
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder.withLongOpt("---")
                    .create();
    }
}