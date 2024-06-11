package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());

        Option optionWithArg = OptionBuilder.hasArg(true)
                .create();
        assertEquals(1, optionWithArg.getArgs());
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                .create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(',')
                .create();
        assertEquals(',', option.getValueSeparator());

        Option optionNoSeparator = OptionBuilder.withValueSeparator()
                .create();
        assertEquals('=', optionNoSeparator.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());

        Option optionWithNumArgs = OptionBuilder.hasArgs(3)
                .create();
        assertEquals(3, optionWithNumArgs.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertTrue(option.isOptional());

        Option optionWithArgs = OptionBuilder.hasOptionalArgs(2)
                .create();
        assertEquals(2, optionWithArgs.getArgs());
        assertTrue(optionWithArgs.isOptional());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        String testString = "test";
        Class<String> testClass = String.class;

        Option option = OptionBuilder.withType(testClass)
                .create();
        assertEquals(testClass, option.getType());

        Option optionWithString = OptionBuilder.withType(String.class)
                .create();
        assertEquals(String.class, optionWithString.getType());

        try
        {
            Option optionWithInvalidType = OptionBuilder.withType(testString.getClass())
                    .create();
            fail();
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("test description")
                .create();
        assertEquals("test description", option.getDescription());

        Option optionNoDescription = OptionBuilder.create();
        assertNull(optionNoDescription.getDescription());
    }

    @Test
    public void testOptionBuilderCreate()
    {
        Option option = OptionBuilder.create('t');
        assertEquals('t', option.getValue());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());

        Option optionWithLongOpt = OptionBuilder.withLongOpt("testOption")
                .create('t');
        assertEquals("testOption", optionWithLongOpt.getLongOpt());
        assertEquals('t', optionWithLongOpt.getValue());
        assertNull(optionWithLongOpt.getDescription());
        assertNull(optionWithLongOpt.getArgName());
        assertNull(optionWithLongOpt.isRequired());
        assertNull(optionWithLongOpt.getArgs());
        assertNull(optionWithLongOpt.getType());
        assertNull(optionWithLongOpt.getValueSeparator());
    }
}