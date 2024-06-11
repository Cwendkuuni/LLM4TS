package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParser() throws Exception {
        // Test case for parse method with no options and no arguments
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValues("help"));

        // Test case for parse method with required options
        options.addRequiredOption("o", "option", true, "Option value");
        args = new String[]{"-o", "value"};
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValues("option")[0]);

        // Test case for parse method with optional options
        options.addOption("o", "option", false, "Option value");
        args = new String[]{"--option", "value"};
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValues("option")[0]);

        // Test case for parse method with long options
        options.addLongOption("long-option", true, "Long option description");
        args = new String[]{"--long-option", "value"};
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValues("long-option")[0]);

        // Test case for parse method with multiple values for an option
        options.addOption("o", "option", true, "Option value");
        args = new String[]{"-o", "value1", "-o", "value2"};
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        List<String> values = new ArrayList<>(Arrays.asList(cmd.getOptionValues("option")));
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        // Test case for parse method with properties
        options.addOption("p", "property", true, "Property value");
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        cmd = parser.parse(options, (String[]) null, properties, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValues("property")[0]);

        // Test case for parse method with stopAtNonOption = true
        options.addOption("o", "option", true, "Option value");
        args = new String[]{"--option", "value", "arg1", "arg2"};
        cmd = parser.parse(options, args, null, true);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));

        // Test case for parse method with mixed options and arguments
        options.addOption("o", "option", true, "Option value");
        args = new String[]{"-o", "value1", "arg1", "-o", "value2", "arg2"};
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
        List<String> optValues = new ArrayList<>(Arrays.asList(cmd.getOptionValues("option")));
        Assert.assertEquals(2, optValues.size());
        Assert.assertEquals("value1", optValues.get(0));
        Assert.assertEquals("value2", optValues.get(1));

        // Test case for parse method with required options missing
        options.addRequiredOption("r", "required-option", true, "Required option value");
        args = new String[]{"-o", "value"};
        try {
            cmd = parser.parse(options, args, null, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            List<String> missing = new ArrayList<>(Arrays.asList(e.getMissingOptions()));
            Assert.assertEquals(1, missing.size());
            Assert.assertEquals("r", missing.get(0));
        }

        // Test case for parse method with unrecognized option
        options.addOption("u", "invalid-option", false, "Invalid option value");
        args = new String[]{"-u", "value"};
        try {
            cmd = parser.parse(options, args, null, false);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: u", e.getMessage());
        }

        // Test case for parse method with missing required group
        OptionGroup group = new OptionGroup("group", "Group description");
        group.addOption(new Option("g", "group-option", true, "Group option value"));
        options.addOptionGroup(group);
        options.addRequiredOption("r", "required-group-option", true, "Required group option value");
        args = new String[]{"-g", "value"};
        try {
            cmd = parser.parse(options, args, null, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            List<String> missing = new ArrayList<>(Arrays.asList(e.getMissingOptions()));
            Assert.assertEquals(1, missing.size());
            Assert.assertEquals("r", missing.get(0));
        }

        // Test case for parse method with missing required group option
        group.addOption(new Option("g", "group-option", true, "Group option value"));
        options.addRequiredOption("g", "required-group-option", true, "Required group option value");
        args = new String[]{"-o", "value"};
        try {
            cmd = parser.parse(options, args, null, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            List<String> missing = new ArrayList<>(Arrays.asList(e.getMissingOptions()));
            Assert.assertEquals(1, missing.size());
            Assert.assertEquals("g", missing.get(0));
        }
    }
}