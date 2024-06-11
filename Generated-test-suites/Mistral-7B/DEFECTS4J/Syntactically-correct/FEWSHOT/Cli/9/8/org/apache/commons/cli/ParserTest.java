package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        final Options options = new Options();
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "option", true, "a help option");
        option.setRequired(false);
        options.addOption(option);

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o", "value"});
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isPresent("option"));
        assertTrue(cmd.getOptionValue("option").equals("value"));
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        final Options options = new Options();

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg"});
        assertTrue(cmd.getArgs().size() == 1);
        assertTrue(cmd.getArgs().get(0).equals("arg"));
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o1", "option1", true, "Option 1");
        final Option option2 = new Option("o2", "option2", false, "Option 2");

        option1.setRequired(false);
        option2.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o1", "value1", "-o2"});
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isPresent("option1"));
        assertTrue(cmd.getOptionValue("option1").equals("value1"));
        assertTrue(cmd.getOptions().isPresent("option2"));
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        final Options options = new Options();

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        List<String> args = Arrays.asList(cmd.getArgs());
        assertTrue(args.size() == 2);
        assertTrue(args.get(0).equals("arg1"));
        assertTrue(args.get(1).equals("arg2"));
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseMixedOptionsAndArguments() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o1", "option1", true, "Option 1");
        final Option option2 = new Option("o2", "option2", false, "Option 2");

        option1.setRequired(false);
        option2.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o1", "value1", "-o2", "arg3", "arg4"});
        List<String> args = Arrays.asList(cmd.getArgs());
        assertTrue(args.size() == 3);
        assertTrue(args.get(0).equals("arg3"));
        assertTrue(args.get(1).equals("arg4"));
        assertTrue(cmd.getOptions().isPresent("option1"));
        assertTrue(cmd.getOptionValue("option1").equals("value1"));
        assertTrue(cmd.getOptions().isPresent("option2"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "option", true, "A help option");
        option.setRequired(false);
        options.addOption(option);

        final Properties properties = new Properties();
        properties.setProperty("option", "value");

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isPresent("option"));
        assertTrue(cmd.getOptionValue("option").equals("value"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o1", "option1", true, "Option 1");
        final Option option2 = new Option("o2", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));

        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"-o1"}));
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"-o2"}));
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        final Options options = new Options();

        final OptionGroup group = new OptionGroup("group", "Options group");
        final Option groupOption = new Option("g", "group", false, "Group option");
        group.addOption(groupOption);

        final Option option1 = new Option("o1", "option1", true, "Option 1");
        final Option option2 = new Option("o2", "option2", false, "Option 2");

        options.addOption(group);
        options.addOption(option1);
        options.addOption(option2);

        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-g", "-o1", "value1"});
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isPresent("group"));
        assertTrue(cmd.getOptions().isPresent("option1"));
        assertTrue(cmd.getOptionValue("option1").equals("value1"));
        assertNull(cmd.getOptionValue("option2"));
    }
}