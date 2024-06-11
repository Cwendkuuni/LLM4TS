package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class OptionBuilderTest
{
    @org.junit.Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
        Option expectedOption = new Option("opt", "optLong", false, "Optional long option");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", true, "required argument");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = new Option("argName", true, "required argument");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("opt", "optional", false, "Optional option, but required");
        Option actualOption = optionBuilder.create();
        assertTrue(actualOption.isRequired());
        assertEquals("Optional option, but required", actualOption.getDescription());
    }

    @org.junit.Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("opt", "optLong", false, "Option with value separator :");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getValueSeparator());
        assertEquals(':', actualOption.getValueSeparator());
    }

    @org.junit.Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("opt", "optLong", false, "Option with multiple values");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getArgs());
        assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs().length);
    }

    @org.junit.Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option("opt", "optLong", true, "Optional option with multiple values");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getArgs());
        assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs().length);
        assertTrue(actualOption.isOptional());
    }

    @org.junit.Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("opt", "optLong", true, "Optional option with one value");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getArgs());
        assertEquals(1, actualOption.getArgs().length);
        assertTrue(actualOption.isOptional());
    }

    @org.junit.Test
    public void testHasArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option expectedOption = new Option("opt", "optLong", false, "Option with 3 values");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getArgs());
        assertEquals(3, actualOption.getArgs().length);
    }

    @org.junit.Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option("opt", "optLong", false, "Option with String value");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
        assertNotNull(actualOption.getType());
        assertEquals(String.class, actualOption.getType());
    }

    @org.junit.Test
    public void testCreateWithNoSettings()
    {
        try
        {
            OptionBuilder.create();
            fail("IllegalArgumentException expected");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateWithNoDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
        try
        {
            Option actualOption = optionBuilder.create();
            fail("IllegalArgumentException expected");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("must specify description", e.getMessage());
        }
    }
}