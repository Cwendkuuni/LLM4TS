package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                      .create();
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                      .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());

        option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArg_WithArgName()
    {
        Option option = OptionBuilder.hasArg().withArgName("argName")
                                          .create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired().create();
        assertFalse(option.isRequired());

        Option optionRequired = OptionBuilder.isRequired().create();
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                      .create();
        assertEquals(':', option.getValueSeparator());

        Option optionNoValueSeparator = OptionBuilder.create();
        assertNull(optionNoValueSeparator.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderHasArgsWithNegativeNumber()
    {
        OptionBuilder.hasArgs(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderHasOptionalArgsWithNegativeNumber()
    {
        OptionBuilder.hasOptionalArgs(-1);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithNumber()
    {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(String.class)
                                      .create();
        assertEquals(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidType()
    {
        OptionBuilder.withType(int[].class);
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("description")
                                      .create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateOptionWithNoneSpecified()
    {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create());
    }

    @Test
    public void testCreateOptionWithAllSpecified()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                      .hasArg()
                                      .withArgName("argName")
                                      .isRequired()
                                      .hasArgs(2)
                                      .hasOptionalArg()
                                      .withDescription("description")
                                      .withValueSeparator(':')
                                      .create();

        assertEquals("longOpt", option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(2, option.getArgs());
        assertFalse(option.isOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValue(0));

        option.setValue("value");
        assertEquals("value", option.getValue());
        assertEquals("value", option.getValue(0));
        assertEquals(1, option.getValues().length);
        assertEquals("value", option.getValues()[0]);
    }
}