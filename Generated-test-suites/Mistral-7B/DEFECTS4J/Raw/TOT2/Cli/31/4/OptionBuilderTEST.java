package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;

/**
 * Test cases for OptionBuilder.
 */
public class OptionBuilderTest
{
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("option");
        Option option = ob.create();
        assertEquals("option", option.getLongOpt());
    }

    // Test for hasArg method
    @Test
    public void testHasArg()
    {
        OptionBuilder ob = OptionBuilder.hasArg();
        Option option = ob.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(Option.TYPE_NORMAL, option.getType());
        try
        {
            option.parse("--option value");
            fail("Expected ArgumentException for required option with no argument provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        OptionBuilder obArg = OptionBuilder.hasArg("argName");
        Option optionArg = obArg.create();
        assertEquals("argName", optionArg.getArgName());
        try
        {
            optionArg.parse("--option value");
            fail("Expected ArgumentException for required option with no argument provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        OptionBuilder obValue = OptionBuilder.hasArg().withValueSeparator(':');
        Option optionValue = obValue.create();
        assertEquals(Option.TYPE_VALUE_WITH_SEPARATOR, optionValue.getType());
        try
        {
            optionValue.parse("--option value1:value2");
            Option[] options = optionValue.getValues();
            assertEquals(2, options.length);
            assertEquals("value1", options[0]);
            assertEquals("value2", options[1]);
        }
        catch (ParseException e)
        {
            fail("Expected valid parsing for option with value separator.");
        }

        OptionBuilder obValueOptional = OptionBuilder.hasOptionalArg().withValueSeparator(':');
        Option optionValueOptional = obValueOptional.create();
        assertEquals(Option.TYPE_VALUE_WITH_SEPARATOR, optionValueOptional.getType());
        try
        {
            optionValueOptional.parse("--option value1:value2");
            Option[] options = optionValueOptional.getValues();
            assertEquals(2, options.length);
            assertEquals("value1", options[0]);
            assertEquals("value2", options[1]);
        }
        catch (ParseException e)
        {
            try
            {
                optionValueOptional.parse("--option");
            }
            catch (ParseException pe)
            {
                fail("Expected no error for optional option with no value.");
            }
        }
    }

    // Test for isRequired method
    @Test
    public void testIsRequired()
    {
        OptionBuilder ob = OptionBuilder.isRequired();
        Option option = ob.create();
        assertEquals(Option.TYPE_NORMAL, option.getType());
        try
        {
            option.parse("--option");
            fail("Expected ArgumentException for required option with no argument provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        OptionBuilder obOptional = OptionBuilder.isRequired(false);
        Option optionOptional = obOptional.create();
        assertEquals(Option.TYPE_NORMAL, optionOptional.getType());
        try
        {
            optionOptional.parse("--option");
        }
        catch (ParseException e)
        {
            fail("Expected no error for optional option with no value.");
        }
    }

    // Test for hasArgs method
    @Test
    public void testHasArgs()
    {
        OptionBuilder ob = OptionBuilder.hasArgs();
        Option option = ob.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        try
        {
            option.parse("--option value1 value2 value3");
            Option[] options = option.getValues();
            assertEquals(3, options.length);
            assertEquals("value1", options[0]);
            assertEquals("value2", options[1]);
            assertEquals("value3", options[2]);
        }
        catch (ParseException e)
        {
            fail("Expected valid parsing for option with multiple values.");
        }

        OptionBuilder obOptional = OptionBuilder.hasArgs(2);
        Option optionOptional = obOptional.create();
        assertEquals(2, optionOptional.getArgs().length);
        try
        {
            optionOptional.parse("--option value1 value2 value3");
            fail("Expected ArgumentException for option with too many values.");
        }
        catch (ParseException e)
        {
            // expected
        }

        OptionBuilder obOptionalWithValue = OptionBuilder.hasOptionalArgs().hasArgs(1);
        Option optionOptionalWithValue = obOptionalWithValue.create();
        assertEquals(Option.UNLIMITED_VALUES, optionOptionalWithValue.getArgs().length);
        try
        {
            optionOptionalWithValue.parse("--option value");
            Option[] options = optionOptionalWithValue.getValues();
            assertEquals(1, options.length);
            assertEquals("value", options[0]);
        }
        catch (ParseException e)
        {
            try
            {
                optionOptionalWithValue.parse("--option");
            }
            catch (ParseException pe)
            {
                fail("Expected no error for optional option with no value.");
            }
        }
    }

    // Test for hasOptionalArgs method
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder ob = OptionBuilder.hasOptionalArgs();
        Option option = ob.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        try
        {
            option.parse("--option value1 value2");
            Option[] options = option.getValues();
            assertEquals(2, options.length);
            assertEquals("value1", options[0]);
            assertEquals("value2", options[1]);
        }
        catch (ParseException e)
        {
            try
            {
                option.parse("--option");
            }
            catch (ParseException pe)
            {
                fail("Expected no error for optional option with no value.");
            }
        }
    }

    // Test for hasOptionalArgs method with limit
    @Test
    public void testHasOptionalArgsWithLimit()
    {
        OptionBuilder ob = OptionBuilder.hasOptionalArgs(3);
        Option option = ob.create();
        assertEquals(3, option.getArgs().length);
        try
        {
            option.parse("--option value1 value2 value3 value4");
            fail("Expected ArgumentException for option with too many values.");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            option.parse("--option value1 value2");
            Option[] options = option.getValues();
            assertEquals(2, options.length);
            assertEquals("value1", options[0]);
            assertEquals("value2", options[1]);
        }
        catch (ParseException e)
        {
            fail("Expected valid parsing for option with optional values.");
        }

        try
        {
            option.parse("--option value1");
            Option[] options = option.getValues();
            assertEquals(1, options.length);
            assertEquals("value1", options[0]);
        }
        catch (ParseException e)
        {
            fail("Expected valid parsing for option with optional value.");
        }
    }

    // Test for withType method
    @Test
    public void testWithType()
    {
        OptionBuilder ob = OptionBuilder.withType(Boolean.TYPE);
        Option option = ob.create();
        assertEquals(Boolean.TYPE, option.getType());

        OptionBuilder obString = OptionBuilder.withType(String.class);
        Option optionString = obString.create();
        assertEquals(String.class, optionString.getType());
    }

    // Test for withDescription method
    @Test
    public void testWithDescription()
    {
        OptionBuilder ob = OptionBuilder.withDescription("Test option");
        Option option = ob.create();
        assertEquals("Test option", option.getDescription());
    }
}