package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParser() throws Exception {
        Options options = new Options();

        String[] arguments = new String[] { "-a", "1", "--opt", "2", "--", "arg1", "arg2" };

        Parser parser = new DefaultParser();

        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[] { "1" }, cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("unknown"));
        assertNull(cmd.getOptionValue("a"));
        assertSame(options.getOption("a"), cmd.getOption("a"));
        assertSame(options.getOption("opt"), cmd.getOption("opt"));
        assertEquals("2", cmd.getOptionValue("opt"));
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgsList());
    }

    @Test
    public void testParserNoArguments() throws Exception {
        Options options = new Options();

        Parser parser = new DefaultParser();

        parser.setOptions(options);

        CommandLine cmd = parser.parse(new String[0]);

        assertTrue(cmd.isEmpty());
    }

    @Test
    public void testParserStopAtNonOption() throws Exception {
        Options options = new Options();

        String[] arguments = new String[] { "-a", "--opt", "--", "nonOption" };

        Parser parser = new DefaultParser();

        parser.setOptions(options);

        parser.parse(arguments, true);

        assertTrue(cmd.getArgsList().size() > 1);
        assertTrue(cmd.getArgsList().get(cmd.getArgsList().size() - 1).equals("nonOption"));
    }

    @Test
    public void testParserWithProperties() throws Exception {
        Options options = new Options();

        Properties properties = new Properties();
        properties.setProperty("myOption", "value");

        Parser parser = new DefaultParser();

        parser.setOptions(options);

        CommandLine cmd = parser.parse(new String[0], properties);

        assertTrue(cmd.hasOption("myOption"));
        assertEquals("value", cmd.getOptionValue("myOption"));
    }

    @Test
    public void testParserWithRequiredOptions() throws Exception {
        Options options = new Options();
        options.addRequiredOption("requiredOption");

        Parser parser = new DefaultParser();

        parser.setOptions(options);

        String[] arguments = new String[0];

        try {
            parser.parse(arguments);
            fail();
        } catch (MissingOptionException e) {
            List<Option> requiredOptions = options.getOptions();
            Option requiredOption = requiredOptions.get(0);
            assertEquals("Missing required option: requiredOption", e.getMessage());
        }

        arguments = new String[] { "--requiredOption" };

        try {
            parser.parse(arguments);
            fail();
        } catch (MissingOptionException e) {
            Option requiredOption = options.getOption("requiredOption");
            assertSame(requiredOption, e.getOption());
        }

        arguments = new String[] { "--requiredOption=value" };

        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("requiredOption"));
        assertEquals("value", cmd.getOptionValue("requiredOption"));
    }

    @Test
    public void testParserWithOptionGroup() throws Exception {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group");
        optionGroup.setRequired(true);

        optionGroup.addOption(new Option("opt1", "option1"));
        optionGroup.addOption(new Option("opt2", "option2"));

        options.addOptionGroup(optionGroup);
        options.addOption(new Option("other", "other"));

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];

        try {
            parser.parse(arguments);
            fail();
        } catch (MissingOptionException e) {
            OptionGroup optionGroup = options.getOptionGroup("group");
            assertEquals("Missing required option: group", e.getMessage());
        }

        arguments = new String[] { "--opt1" };

        try {
            parser.parse(arguments);
            fail();
        } catch (MissingOptionException e) {
            Option opt1 = optionGroup.getOption("opt1");
            assertSame(opt1, e.getOption());
        }

        arguments = new String[] { "--opt2" };

        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("opt2"));
    }

    @Test
    public void testParserWithUnrecognizedOption() throws Exception {
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[] { "--unknown" };

        try {
            parser.parse(arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: unknown", e.getMessage());
        }
    }

    @Test
    public void testParserWithOptionalArgument() throws Exception {
        Options options = new Options();

        Option option = new Option("opt", "option");
        option.hasArg(true);

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(arguments);
        assertFalse(cmd.hasOption("opt"));

        arguments = new String[] { "--opt" };

        cmd = parser.parse(arguments);
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));

        arguments = new String[] { "--opt=value" };

        cmd = parser.parse(arguments);
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParserWithValueSeparatedByEqualsSign() throws Exception {
        Options options = new Options();

        Option option = new Option("opt", "option");
        option.hasArg(true);

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[] { "--opt=value1=value2" };

        CommandLine cmd = parser.parse(arguments);
        assertTrue(cmd.hasOption("opt"));
        assertArrayEquals(new String[] { "value1=value2" }, cmd.getOptionValues("opt"));
    }

    @Test
    public void testParserWithValueSeparatedByComma() throws Exception {
        Options options = new Options();

        Option option = new Option("opt", "option");
        option.hasArg(true);

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[] { "--opt=value1,value2" };

        CommandLine cmd = parser.parse(arguments);
        assertTrue(cmd.hasOption("opt"));
        assertArrayEquals(new String[] { "value1", "value2" }, cmd.getOptionValues("opt"));
    }

    @Test
    public void testParserWithValueSeparatedBySpace() throws Exception {
        Options options = new Options();

        Option option = new Option("opt", "option");
        option.hasArg(true);

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[] { "--opt=value1 value2" };

        CommandLine cmd = parser.parse(arguments);
        assertTrue(cmd.hasOption("opt"));
        assertArrayEquals(new String[] { "value1 value2" }, cmd.getOptionValues("opt"));
    }

    @Test
    public void testParserWithOptionValueMismatch() throws Exception {
        Options options = new Options();

        Option option = new Option("opt", "option");
        option.hasArg(true);

        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[] { "--opt=value" };

        try {
            parser.parse(arguments);
            fail();
        } catch (ParseException e) {
            assertEquals("The option 'opt' requires an argument.", e.getMessage());
        }
    }
}