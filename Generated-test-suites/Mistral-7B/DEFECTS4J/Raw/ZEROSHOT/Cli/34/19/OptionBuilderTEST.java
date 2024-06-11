package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                                       .create();
        assertEquals("longOption", option.getLongOpt());
    }

    @org.junit.Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                       .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.hasArg());
    }

    @org.junit.Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                       .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @org.junit.Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                       .create();
        assertEquals(':', option.getValueSeparator());
    }

    @org.junit.Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                       .create();
        assertEquals(true, option.isRequired());
    }

    @org.junit.Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                       .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @org.junit.Test
    public void testOptionBuilderHasArgsWithNumber()
    {
        Option option = OptionBuilder.hasArgs(2)
                                       .create();
        assertEquals(2, option.getArgs());
    }

    @org.junit.Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(Integer.class)
                                       .create();
        assertEquals(Integer.class, option.getType());
    }

    @org.junit.Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("test description")
                                       .create();
        assertEquals("test description", option.getDescription());
    }

    @org.junit.Test
    public void testOptionBuilderCreateWithNoSettings()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @org.junit.Test
    public void testOptionBuilderCreateWithAllSettings()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                                       .hasArg()
                                       .withValueSeparator(':')
                                       .isRequired()
                                       .hasArgs(2)
                                       .withType(Integer.class)
                                       .withDescription("test description")
                                       .create();

        assertEquals("longOption", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());

        Option longOption = OptionBuilder.withLongOpt("longOption")
                                          .create();
        Option argOption = OptionBuilder.hasArg()
                                          .create();
        Option valueSeparatorOption = OptionBuilder.withValueSeparator(' ')
                                                    .create();
        Option requiredOption = OptionBuilder.isRequired()
                                             .create();
        Option argsOption = OptionBuilder.hasArgs(3)
                                          .create();
        Option typeOption = OptionBuilder.withType(String.class)
                                          .create();
        Option descriptionOption = OptionBuilder.withDescription("test description")
                                                .create();

        Option complexOption = new Option("complex", "This is a complex option", true,
                                           Arrays.asList(longOption, argOption,
                                                          valueSeparatorOption,
                                                          requiredOption,
                                                          argsOption, typeOption,
                                                          descriptionOption));
        Option parsedOption = Option.builder("complex").longOpt("complex").argName("complex")
                                  .hasArg().valueSeparator(' ').isRequired()
                                  .hasArgs(3).withType(String.class)
                                  .withDescription("This is a complex option")
                                  .create();

        assertEquals(complexOption, parsedOption);
    }
}