package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option option = optionBuilder.create();
        assertEquals("option1", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        String[] args = {"testArg"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            assertEquals("testArg", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
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
        assertEquals(1, option.getArgs().length);

        String[] args = {"testArg"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            assertEquals("testArg", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        OptionBuilder optionBuilder2 = OptionBuilder.hasArg(false);
        Option option2 = optionBuilder2.create();
        assertNull(option2.getValue());
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
        assertEquals(true, option.isRequired());

        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            assertNull(cmd.getOptionValue("option1"));
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
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

        String[] args = {"option1", "value1:value2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            assertEquals("value1:value2", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
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
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);

        String[] args = {"option1", "value1", "value2", "value3"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            String[] values = cmd.getOptionValues("option1");
            assertEquals(3, values.length);
            assertEquals("value1", values[0]);
            assertEquals("value2", values[1]);
            assertEquals("value3", values[2]);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
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
        assertEquals(2, option.getArgs().length);

        String[] args = {"option1", "value1", "value2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            String[] values = cmd.getOptionValues("option1");
            assertEquals(2, values.length);
            assertEquals("value1", values[0]);
            assertEquals("value2", values[1]);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String[] args2 = {"option1"};
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine cmd = parser2.parse(args2, OptionBuilder.buildParser().addOption(option));
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
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
        assertEquals(1, option.getArgs().length);
        assertEquals(Option.UNINITIALIZED, option.getArgs()[0]);

        String[] args = {"option1", "value1"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            assertEquals("value1", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String[] args2 = {"option1"};
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine cmd = parser2.parse(args2, OptionBuilder.buildParser().addOption(option));
            assertNull(cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
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
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);

        String[] args = {"option1", "value1", "value2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            String[] values = cmd.getOptionValues("option1");
            assertEquals(2, values.length);
            assertEquals("value1", values[0]);
            assertEquals("value2", values[1]);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String[] args2 = {"option1", "value1", "value2", "value3"};
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine cmd = parser2.parse(args2, OptionBuilder.buildParser().addOption(option));
            String[] values = cmd.getOptionValues("option1");
            assertEquals(3, values.length);
            assertEquals("value1", values[0]);
            assertEquals("value2", values[1]);
            assertEquals("value3", values[2]);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
        Option option = optionBuilder.create();
        assertEquals(Integer.TYPE, option.getType());

        String[] args = {"option1", "1", "2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(args, OptionBuilder.buildParser().addOption(option));
            Object value = cmd.getOptionValue("option1");
            assertEquals(2, ((Integer) value).intValue());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String[] args2 = {"option1", "hello"};
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine cmd = parser2.parse(args2, OptionBuilder.buildParser().addOption(option));
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for option1");
        Option option = optionBuilder.create();
        assertEquals("Description for option1", option.getDescription());
    }
}