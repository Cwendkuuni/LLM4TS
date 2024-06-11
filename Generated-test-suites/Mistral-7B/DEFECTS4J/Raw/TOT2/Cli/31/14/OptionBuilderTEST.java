package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
        Option option = optionBuilder.create();
        assertEquals("longOption", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue());

        String[] args = new String[]{"test"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals("test", line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs());

        String[] args = new String[]{"test"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals("test", line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }

        String[] argsWithValue = new String[]{"test", "value"};
        CommandLine line = null;
        try
        {
            line = parser.parse(argsWithValue, optionBuilder.build());
            assertEquals("value", line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }

        assertNull(option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());

        String[] args = new String[]{"test:", "test2:"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals("test:", line.getOptionValue("longOption"));
            assertEquals("test2:", line.getOptionValue("longOption2"));
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create();
        assertEquals('=', option.getValueSeparator());

        String[] args = new String[]{"test=value"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals("value", line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Option option = optionBuilder.create();
        assertFalse(option.isRequired());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());

        String[] args = new String[]{"test1", "test2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals(2, line.getArgList().size());
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs());

        String[] args = new String[]{"test1", "test2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals(2, line.getArgList().size());
        }
        catch (ParseException e)
        {
            if (args.length > 2)
            {
                fail();
            }
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
        }
        catch (ParseException e)
        {
            // expected
        }

        String[] argsWithValue = new String[]{"test"};
        CommandLine line = null;
        try
        {
            line = parser.parse(argsWithValue, optionBuilder.build());
            assertNotNull(line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
        }
        catch (ParseException e)
        {
            // expected
        }

        String[] argsWithValues = new String[]{"test1", "test2"};
        CommandLine line = null;
        try
        {
            line = parser.parse(argsWithValues, optionBuilder.build());
            assertEquals(2, line.getOptionValues("longOption").length);
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        assertEquals(String.class, option.getType());

        String[] args = new String[]{"test"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args, optionBuilder.build());
            assertEquals("test", line.getOptionValue("longOption"));
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Option option = optionBuilder.create();
        assertEquals("test description", option.getLongOpt());
    }
}