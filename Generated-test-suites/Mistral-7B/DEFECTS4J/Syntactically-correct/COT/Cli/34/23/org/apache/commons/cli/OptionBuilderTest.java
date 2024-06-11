package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        assertEquals(expectedOptionBuilder.longopt, optionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test(expected = AssertionError.class)
    public void testHasArgWithoutHasArgPrevious()
    {
        OptionBuilder.hasArg();
        OptionBuilder.hasArg(false);
    }

    @Test
    public void testHasArgWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNINITIALIZED;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.argName = "argName";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = ':';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testCreateOption() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "longOpt", true, "argName", "Description", Option.NO_ARG, null);
        assertEquals(expectedOption, option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create('o');
    }

    @Test
    public void testCreateWithEmptyLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("")
                .withDescription("Description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "", true, "argName", "Description", Option.NO_ARG, null);
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Description")
                .hasOptionalArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "longOpt", false, "argName", "Description", Option.HAS_ARG, null);
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreateWithUnlimitedArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Description")
                .hasArgs()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "longOpt", true, "argName", "Description", Option.UNLIMITED_VALUES, null);
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreateWithSpecifiedNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Description")
                .hasArgs(3)
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':');
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "longOpt", true, "argName", "Description", 3, null);
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .withType(String.class);
        Option option = optionBuilder.create('o');

        Option expectedOption = new Option('o', "longOpt", true, "argName", "Description", Option.NO_ARG, String.class);
        assertEquals(expectedOption, option);
    }
}