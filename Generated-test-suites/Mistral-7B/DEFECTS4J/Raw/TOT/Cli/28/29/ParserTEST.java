package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Parser} class.
 */
public class ParserTest
{
    @Test
    public void testParseSimple() throws Exception
    {
        String[] args = {"-a", "valueA", "-b=valueB"};
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(new Options(), args);

        assertNotNull(cmd);
        assertNotNull(cmd.getOptions());
        assertEquals(2, cmd.getOptions().size());
        assertEquals("-a", cmd.getOptions().get(0).getKey());
        assertEquals("valueA", cmd.getOptions().get(0).getValue());
        assertEquals("-b=valueB", cmd.getOptions().get(1).getKey());
        assertEquals("valueB", cmd.getOptions().get(1).getValue());
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception
    {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addRequiredOption("option1", false, "o1");
        options.addRequiredOption("option2", false, "o2");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, args);
            fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            assertEquals(Arrays.asList("option1", "option2"), e.getMissingOptions());
        }
    }

    @Test
    public void testParseWithOptionalOptionsAndArg() throws Exception
    {
        String[] args = {"--option", "value"};
        Options options = new Options();
        Option option = new Option("o", "option", true, "Opt");
        option.setArgName("value");
        options.addOption(option);

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertNotNull(cmd.getOptions());
        assertEquals(1, cmd.getOptions().size());
        assertEquals("-o", cmd.getOptions().get(0).getKey());
        assertEquals("value", cmd.getOptions().get(0).getValue());
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"--prop1=value1", "--prop2=value2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");

        Options options = new Options();

        Option option1 = new Option("p", "prop1", true, "Prop1");
        option1.setArgName("value");
        option1.setValueSeparator('=');
        option1.setLongOpt("property1");
        options.addOption(option1);

        Option option2 = new Option("p", "prop2", true, "Prop2");
        option2.setArgName("value");
        option2.setValueSeparator('=');
        option2.setLongOpt("property2");
        options.addOption(option2);

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args, properties, false);

        assertNotNull(cmd);
        assertNotNull(cmd.getOptions());
        assertEquals(2, cmd.getOptions().size());
        assertEquals("-p=prop1=value1", cmd.getOptions().get(0).getKey());
        assertEquals("value1", cmd.getOptions().get(0).getValue());
        assertEquals("-p=prop2=value2", cmd.getOptions().get(1).getKey());
        assertEquals("value2", cmd.getOptions().get(1).getValue());
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithUnrecognizedOption() throws Exception
    {
        String[] args = {"--unknown"};
        Options options = new Options();
        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, args);
            fail("UnrecognizedOptionException expected");
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("--unknown", e.getOption());
        }
    }

    @Test
    public void testParseWithMixedOptions() throws Exception
    {
        String[] args = {"--option1=value1", "-o", "option2=value2", "arg1", "--option3=value3"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option1");
        option1.setArgName("Value1");
        option1.setValueSeparator('=');
        option1.setLongOpt("option1");
        options.addOption(option1);

        Option option2 = new Option("o", "option2", true, "Option2");
        option2.setArgName("Value2");
        option2.setValueSeparator('=');
        option2.setLongOpt("option2");
        options.addOption(option2);

        Option option3 = new Option("o", "option3", true, "Option3");
        option3.setArgName("Value3");
        option3.setValueSeparator('=');
        option3.setLongOpt("option3");
        options.addOption(option3);

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertNotNull(cmd.getOptions());
        assertEquals(3, cmd.getOptions().size());
        assertEquals("-o=option1=value1", cmd.getOptions().get(0).getKey());
        assertEquals("value1", cmd.getOptions().get(0).getValue());
        assertEquals("-o", cmd.getOptions().get(1).getKey());
        assertNull(cmd.getOptions().get(1).getValue());
        assertEquals("-o=option2=value2", cmd.getOptions().get(2).getKey());
        assertEquals("value2", cmd.getOptions().get(2).getValue());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals(2, cmd.getArgs().size());
    }
}