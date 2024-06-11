package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder builder = OptionBuilder.instance();
        String longOpt = "longOption";
        Option expectedOption = new Option(longOpt, null);

        Option actualOption = builder.withLongOpt(longOpt).create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder builder = OptionBuilder.instance();
        Option expectedOption = Option.builder("argOption").required(false).argName("arg").build();

        Option actualOption = builder.hasArg().create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder builder = OptionBuilder.instance();
        Option expectedOption = Option.builder("requiredOption").argName("arg").required(true).build();

        Option actualOption = builder.isRequired().create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder builder = OptionBuilder.instance();
        char valueSeparator = ':';
        Option expectedOption = Option.builder("valueOption").argName("arg").required(false)
                .valueSeparator(valueSeparator).build();

        Option actualOption = builder.withValueSeparator(valueSeparator).create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder builder = OptionBuilder.instance();
        Option expectedOption = Option.builder("argsOption").required(false).argName("args")
                .args(Option.UNLIMITED_VALUES).build();

        Option actualOption = builder.hasArgs().create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder builder = OptionBuilder.instance();
        int numArgs = 3;
        Option expectedOption = Option.builder("argsOption").required(false).argName("args")
                .args(numArgs).build();

        Option actualOption = builder.hasArgs(numArgs).create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder builder = OptionBuilder.instance();
        Option expectedOption = Option.builder("optionalArgOption").argName("arg")
                .args(Option.UNINITIALIZED).optional(true).build();

        Option actualOption = builder.hasOptionalArg().create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder builder = OptionBuilder.instance();
        int numArgs = 3;
        Option expectedOption = Option.builder("optionalArgsOption").argName("args")
                .args(numArgs).optional(true).build();

        Option actualOption = builder.hasOptionalArgs(numArgs).create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder builder = OptionBuilder.instance();
        Class<Integer> intType = Integer.class;
        Option expectedOption = Option.builder("typeOption").argName("arg")
                .args(Option.UNINITIALIZED).type(intType).build();

        Option actualOption = builder.withType(intType).create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder builder = OptionBuilder.instance();
        String description = "description";
        Option expectedOption = Option.builder("descriptionOption").argName("arg")
                .args(Option.UNINITIALIZED).required(false)
                .longOpt("longopt").description(description).build();

        Option actualOption = builder.withLongOpt("longopt").withDescription(description).create();

        assertEquals(expectedOption, actualOption);
    }
}