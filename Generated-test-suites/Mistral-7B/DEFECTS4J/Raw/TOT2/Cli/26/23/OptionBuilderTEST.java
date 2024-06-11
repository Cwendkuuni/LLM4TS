package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "long opt description");

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("arg")
                .required(false)
                .hasArg(true)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option expectedOption = Option.builder("arg")
                .required(true)
                .hasArg(true)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = new Option("argName", "Argument name description")
                .argName("argName");

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("arg")
                .required(true)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("arg")
                .argName("arg")
                .valueSeparator(':');

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withValueSeparator();

        Option expectedOption = new Option("longOpt", "long opt description")
                .required(true)
                .hasArg(true)
                .argName("arg")
                .valueSeparator(':');

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreate_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withValueSeparator();

        Option actualOption = optionBuilder.create('l');
        Option expectedOption = new Option('l', "longOpt")
                .required(true)
                .hasArg(true)
                .argName("arg")
                .valueSeparator(':');

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreate_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .hasArg()
                .withValueSeparator();

        Option actualOption = optionBuilder.create("longOpt");
        Option expectedOption = new Option("longOpt", "long opt description")
                .required(true)
                .hasArg(true)
                .argName("arg")
                .valueSeparator(':');

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("arg")
                .hasArg(Option.UNLIMITED_VALUES)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder("arg")
                .hasArg(2)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = Option.builder("arg")
                .hasArg(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = Option.builder("arg")
                .hasArg(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int_withType()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3, Integer.TYPE);
        Option expectedOption = Option.builder("arg")
                .hasArg(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .type(Integer.TYPE)
                .build();

        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }
}