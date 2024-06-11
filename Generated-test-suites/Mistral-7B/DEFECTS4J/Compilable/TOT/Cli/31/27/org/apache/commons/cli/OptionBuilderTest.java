package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testBasicOption()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option with long opt and no arguments")
                .create();

        assertEquals("option1", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());

        Option optionWithArg = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .withArgName("argName")
                .create();

        assertEquals("option2", optionWithArg.getLongOpt());
        assertEquals("argName", optionWithArg.getArgName());
        assertTrue(optionWithArg.isRequired());
        assertEquals(1, optionWithArg.getArgs());
        assertNull(optionWithArg.getType());
        assertFalse(optionWithArg.hasOptionalArg());
    }

    @Test
    public void testOptionWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("option3")
                .withDescription("An option with long opt and argument value")
                .withValueSeparator(':')
                .create();

        assertEquals("option3", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testRequiredOptionWithValue()
    {
        Option option = OptionBuilder.withLongOpt("option4")
                .withDescription("An option with long opt, argument and is required")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .create();

        assertEquals("option4", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());

        try
        {
            OptionBuilder.create();
            fail("It should throw an IllegalArgumentException when long opt is not set");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            OptionBuilder.create('A');
            fail("It should throw an IllegalArgumentException when an invalid char is used for option");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testOptionalOptionWithValue()
    {
        Option option = OptionBuilder.withLongOpt("option5")
                .withDescription("An option with long opt, argument and is optional")
                .hasArg()
                .withArgName("argName")
                .hasOptionalArg()
                .create();

        assertEquals("option5", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());

        Option optionNoArg = OptionBuilder.withLongOpt("option6")
                .withDescription("An option with long opt and no arguments")
                .hasOptionalArgs()
                .create();

        assertEquals("option6", optionNoArg.getLongOpt());
        assertNull(optionNoArg.getArgName());
        assertFalse(optionNoArg.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, optionNoArg.getArgs());
        assertNull(optionNoArg.getType());
        assertTrue(optionNoArg.hasOptionalArg());
    }

    @Test
    public void testOptionWithType()
    {
        Option option = OptionBuilder.withLongOpt("option7")
                .withDescription("An option with long opt and argument of type int")
                .hasArg()
                .withArgName("argName")
                .withType(Integer.TYPE)
                .create();

        assertEquals("option7", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertEquals(Integer.TYPE, option.getType());
        assertFalse(option.hasOptionalArg());
    }
}