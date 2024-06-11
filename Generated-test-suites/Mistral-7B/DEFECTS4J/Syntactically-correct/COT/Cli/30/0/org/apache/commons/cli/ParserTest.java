package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        

        // Test empty arguments
        Options options = new Options();
        String[] arguments = {};
        CommandLine parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }
        }.parse(options, arguments);
        assertNotNull(parser);
        assertNull(parser.getArgs());
        assertEquals(0, parser.getOptions().size());

        // Test arguments with options
        options = new Options();
        arguments = new String[]{"--option1", "--option2", "arg1", "arg2"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }
        }.parse(options, arguments);
        assertNotNull(parser);
        assertNull(parser.getArgs());
        assertEquals(2, parser.getOptions().size());
        assertTrue(parser.getOptions().hasOption("option1"));
        assertTrue(parser.getOptions().hasOption("option2"));
        assertTrue(parser.getOptions().getOptionValue("option1").isEmpty());
        assertTrue(parser.getOptions().getOptionValue("option2").isEmpty());

        // Test arguments with values for options
        options = new Options();
        Option option1 = new Option("o", "option1", true, "Value for option1");
        Option option2 = new Option("a", "option2", true, "Value for option2");
        option1.setRequired(false);
        option2.setRequired(false);
        options.addOption(option1);
        options.addOption(option2);
        arguments = new String[]{"--option1=value1", "--option2=value2", "arg1", "arg2"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }
        }.parse(options, arguments);
        assertNotNull(parser);
        assertNotNull(parser.getArgs());
        assertEquals(2, parser.getArgs().size());
        assertEquals("arg1", parser.getArgs().get(0));
        assertEquals("arg2", parser.getArgs().get(1));
        assertEquals(2, parser.getOptions().size());
        assertTrue(parser.getOptions().hasOption("option1"));
        assertTrue(parser.getOptions().hasOption("option2"));
        assertEquals("value1", parser.getOptions().getOptionValue("option1"));
        assertEquals("value2", parser.getOptions().getOptionValue("option2"));

        // Test required arguments
        options = new Options();
        Option option = new Option("r", "requiredOption", true, "Required option value");
        option.setRequired(true);
        options.addOption(option);
        arguments = new String[]{"--option1=value1", "--option2=value2"};
        try
        {
            new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
                {
                    return args;
                }
            }.parse(options, arguments);
            fail();
        }
        catch (MissingArgumentException e)
        {
            // expected
        }

        // Test invalid option
        options = new Options();
        arguments = new String[]{"--invalidOption", "arg1", "arg2"};
        try
        {
            new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
                {
                    return args;
                }
            }.parse(options, arguments);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
        }

        // Test long option with value
        options = new Options();
        Option longOption = new Option("longOption", "long option", false, "Value for long option");
        options.addOption(longOption);
        arguments = new String[]{"--longOption=value"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }
        }.parse(options, arguments);
        assertNotNull(parser);
        assertNotNull(parser.getArgs());
        assertEquals(0, parser.getArgs().size());
        assertEquals(1, parser.getOptions().size());
        assertFalse(parser.getOptions().getOptionValue("longOption").isEmpty());
        assertEquals("value", parser.getOptions().getOptionValue("longOption"));

        // Test multiple long options with values
        options = new Options();
        longOption = new Option("longOption", "long option", false, "Value for long option");
        options.addOption(longOption);
        arguments = new String[]{"--longOption1=value1", "--longOption2=value2"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }
        }.parse(options, arguments);
        assertNotNull(parser);
        assertNotNull(parser.getArgs());
        assertEquals(0, parser.getArgs().size());
        assertEquals(2, parser.getOptions().size());
        assertFalse(parser.getOptions().getOptionValue("longOption1").isEmpty());
        assertEquals("value1", parser.getOptions().getOptionValue("longOption1"));
        assertFalse(parser.getOptions().getOptionValue("longOption2").isEmpty());
        assertEquals("value2", parser.getOptions().getOptionValue("longOption2"));

        // Test properties
        Properties properties = new Properties();
        properties.setProperty("option.property1", "Value for option property1");
        properties.setProperty("option.property2.subOption", "Value for sub option");
        options = new Options();
        Option optionProperty1 = new Option("p", "property1", false, "Property option 1");
        optionProperty1.setLongOpt("property");
        Option optionProperty2 = new Option("p", "property2", false, "Property option 2");
        optionProperty2.setLongOpt("property2");
        optionProperty2.setValues(new String[]{"subOption"});
        Option optionProperty3 = new Option("p", "property3", false, "Property option 3");
        optionProperty3.setLongOpt("property3");
        options.addOption(optionProperty1);
        options.addOption(optionProperty2);
        options.addOption(optionProperty3);
        arguments = new String[]{"--property1", "--property2=subOption", "--property3"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] args, boolean stopAtNonOption) throws ParseException
            {
                return args;
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException
            {
                return super.parse(options, arguments, properties, stopAtNonOption);
            }
        }.parse(options, arguments, properties, false);
        assertNotNull(parser);
        assertNotNull(parser.getArgs());
        assertEquals(0, parser.getArgs().size());
        assertEquals(3, parser.getOptions().size());
        assertTrue(parser.getOptions().hasOption("property1"));
        assertTrue(parser.getOptions().hasOption("property2"));
        assertTrue(parser.getOptions().hasOption("property3"));
        assertEquals("Value for option property1", parser.getOptions().getOptionValue("property1"));
        List<String> values = parser.getOptions().getOptionValues("property2");
        assertEquals(1, values.size());
        assertEquals("subOption", values.get(0));
        assertEquals("Value for property option 3", parser.getOptions().getOptionValue("property3"));

        
    }
}