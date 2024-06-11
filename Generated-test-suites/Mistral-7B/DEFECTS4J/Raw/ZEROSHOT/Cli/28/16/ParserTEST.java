package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.TestUtils;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @org.junit.Test
    public void testParser() throws Exception
    {
        // Test with no options
        String[] argsNoOptions = new String[0];
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), argsNoOptions, true);
        assertNull(cmd.getOptions());
        assertNull(cmd.getArgs());

        // Test with simple options
        String[] argsSimpleOptions = new String[]{"-a", "1", "--b", "2"};
        Options optionsSimpleOptions = new Options();
        optionsSimpleOptions.addOption("a", true, "option a");
        optionsSimpleOptions.addOption("b", true, "option b");
        cmd = parser.parse(optionsSimpleOptions, argsSimpleOptions, true);
        List<Option> options = cmd.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.get(0).isSet());
        assertEquals("1", options.get(0).getValue());
        assertTrue(options.get(1).isSet());
        assertEquals("2", options.get(1).getValue());
        assertNull(cmd.getArgs());

        // Test with required options
        String[] argsRequiredOptions = new String[]{"--a", "1", "--b", "2"};
        optionsRequiredOptions = new Options();
        optionsRequiredOptions.addOption("a", true, "option a");
        optionsRequiredOptions.addOption("b", true, "option b").required();
        cmd = parser.parse(optionsRequiredOptions, argsRequiredOptions, true);
        options = cmd.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.get(0).isSet());
        assertEquals("1", options.get(0).getValue());
        assertTrue(options.get(1).isSet());
        assertEquals("2", options.get(1).getValue());
        assertEquals(0, cmd.getArgs().length);

        // Test with optional options with values
        String[] argsOptionalValues = new String[]{"-a", "1", "--b=2", "--b=3"};
        optionsOptionalValues = new Options();
        optionsOptionalValues.addOption("a", true, "option a");
        optionsOptionalValues.addOption("b", true, "option b");
        cmd = parser.parse(optionsOptionalValues, argsOptionalValues, true);
        options = cmd.getOptions();
        assertEquals(1, options.size());
        Option optionB = options.get(0);
        assertTrue(optionB.isSet());
        List<String> valuesB = optionB.getValues();
        assertEquals(2, valuesB.size());
        assertEquals("2", valuesB.get(0));
        assertEquals("3", valuesB.get(1));
        assertNull(cmd.getArgs());

        // Test with long options
        String[] argsLongOptions = new String[]{"--long-option", "value"};
        optionsLongOptions = new Options();
        optionsLongOptions.addOption("longOption", true, "long option");
        cmd = parser.parse(optionsLongOptions, argsLongOptions, true);
        options = cmd.getOptions();
        assertEquals(1, options.size());
        Option optionLong = options.get(0);
        assertTrue(optionLong.isSet());
        assertEquals("value", optionLong.getValue());
        assertNull(cmd.getArgs());

        // Test with short and long options
        String[] argsShortLongOptions = new String[]{"-a", "--b", "3"};
        optionsShortLongOptions = new Options();
        optionsShortLongOptions.addOption("a", true, "option a");
        optionsShortLongOptions.addOption("b", false, "option b");
        cmd = parser.parse(optionsShortLongOptions, argsShortLongOptions, true);
        options = cmd.getOptions();
        assertEquals(2, options.size());
        Option optionA = options.get(0);
        assertTrue(optionA.isSet());
        assertTrue(optionA.getValue().isEmpty());
        Option optionB = options.get(1);
        assertFalse(optionB.isSet());
        assertNull(optionB.getValue());
        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertEquals("3", args[0]);

        // Test with properties
        String[] argsProperties = new String[]{"--prop=value1", "--prop2=value2"};
        Properties properties = new Properties();
        properties.setProperty("prop", "value1");
        properties.setProperty("prop2", "value2");
        cmd = parser.parse(new Options(), argsProperties, properties, true);
        options = cmd.getOptions();
        assertEquals(0, options.size());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("value1", cmd.getArgs()[0]);
        assertEquals("value2", cmd.getArgs()[1]);

        // Test with required options not present
        String[] argsRequiredOptionsMissing = new String[]{"--a"};
        try
        {
            cmd = parser.parse(optionsRequiredOptions, argsRequiredOptionsMissing, true);
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            // Expected
        }
        assertFalse(cmd.getOptions().isEmpty());
        assertEquals(0, cmd.getArgs().length);

        // Test with multiple required options not present
        String[] argsRequiredOptionsMissingMultiple = new String[]{"--a", "--b"};
        try
        {
            cmd = parser.parse(optionsRequiredOptions, argsRequiredOptionsMissingMultiple, true);
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            // Expected
        }
        assertFalse(cmd.getOptions().isEmpty());
        assertEquals(0, cmd.getArgs().length);

        // Test with unrecognized option
        String[] argsUnrecognizedOption = new String[]{"--x", "1"};
        optionsUnrecognizedOption = new Options();
        try
        {
            cmd = parser.parse(optionsUnrecognizedOption, argsUnrecognizedOption, true);
            fail("Expected UnrecognizedOptionException");
        }
        catch (UnrecognizedOptionException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getArgs());

        // Test with invalid option value
        String[] argsInvalidOptionValue = new String[]{"-a=notANumber"};
        try
        {
            cmd = parser.parse(optionsSimpleOptions, argsInvalidOptionValue, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getArgs());

        // Test with invalid long option value
        String[] argsInvalidLongOptionValue = new String[]{"--longOption=notANumber"};
        try
        {
            cmd = parser.parse(optionsLongOptions, argsInvalidLongOptionValue, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getArgs());

        // Test with invalid property value
        Properties propertiesInvalid = new Properties();
        propertiesInvalid.setProperty("prop", "notANumber");
        try
        {
            cmd = parser.parse(new Options(), new String[0], propertiesInvalid, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getArgs());

        // Test with invalid combination of short and long options
        String[] argsInvalidOptions = new String[]{"-a", "--b"};
        try
        {
            cmd = parser.parse(optionsSimpleOptions, argsInvalidOptions, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getArgs());

        // Test with option groups
        String[] argsOptionGroups = new String[]{"--group1=value1", "--group2=value2", "--group1=value3"};
        Options optionsOptionGroups = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("g", "group1", false, "group1 option", "value1"));
        group1.addOption(new Option("g", "group1", false, "group1 option", "value2"));
        optionsOptionGroups.addOptionGroup(group1);
        optionsOptionGroups.addOption(new Option("g", "group2", false, "group2 option", "value2"));
        try
        {
            cmd = parser.parse(optionsOptionGroups, argsOptionGroups, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(1, cmd.getOptions().size());
        Option optionGroup1 = cmd.getOption("group1");
        Option group1Value1 = optionGroup1.getValues().get(0);
        Option group1Value2 = optionGroup1.getValues().get(1);
        assertEquals(2, optionGroup1.getValues().size());
        assertEquals("value1", group1Value1.getValue());
        assertEquals("value2", group1Value2.getValue());
        assertNull(cmd.getArgs());

        // Test with multiple option groups
        String[] argsOptionGroupsMultiple = new String[]{"--group1=value1", "--group2=value2", "--group1=value3", "--group3=value3"};
        Options optionsOptionGroupsMultiple = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("g", "group1", false, "group1 option", "value1"));
        group1.addOption(new Option("g", "group1", false, "group1 option", "value2"));
        optionsOptionGroupsMultiple.addOptionGroup(group1);
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.addOption(new Option("g", "group2", false, "group2 option", "value2"));
        optionsOptionGroupsMultiple.addOptionGroup(group2);
        OptionGroup group3 = new OptionGroup("group3", "Group 3");
        group3.addOption(new Option("g", "group3", false, "group3 option", "value3"));
        optionsOptionGroupsMultiple.addOptionGroup(group3);
        try
        {
            cmd = parser.parse(optionsOptionGroupsMultiple, argsOptionGroupsMultiple, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(1, cmd.getOptions().size());
        Option optionGroup1 = cmd.getOption("group1");
        Option group1Value1 = optionGroup1.getValues().get(0);
        Option group1Value2 = optionGroup1.getValues().get(1);
        Option optionGroup2 = cmd.getOption("group2");
        Option optionGroup3 = cmd.getOption("group3");
        assertEquals(3, cmd.getOptions().size());
        assertEquals(2, optionGroup1.getValues().size());
        assertEquals("value1", group1Value1.getValue());
        assertEquals("value2", group1Value2.getValue());
        assertEquals("value2", optionGroup2.getValue());
        assertSame(group3, optionGroup3);
        assertNull(cmd.getArgs());

        // Test with mixed options and arguments
        String[] argsMixed = new String[]{"-a", "-b", "c"};
        try
        {
            cmd = parser.parse(optionsSimpleOptions, argsMixed, true);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
        assertEquals(0, cmd.getOptions().size());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("c", cmd.getArgs()[0]);
        assertEquals("", cmd.getArgs()[1]);
    }
}