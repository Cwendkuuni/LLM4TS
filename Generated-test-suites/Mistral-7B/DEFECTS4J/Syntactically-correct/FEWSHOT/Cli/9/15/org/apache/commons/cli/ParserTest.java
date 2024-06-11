package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = newOptions();
        Parser parser = newDefaultParser();

        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = newOptions();
        options.addOption("o", "option", false, "An option.");
        options.addOption("a", "argument", true, "An argument.");
        Parser parser = newDefaultParser();

        String[] arguments = {"-o", "value", "-a", "argumentValue"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertFalse(option.getValue().isPresent());
        assertEquals("An option.", option.getLongOpt());
        assertEquals(false, option.isRequired());

        option = optionsList.get(1);
        assertEquals("a", option.getKey());
        assertEquals("argumentValue", option.getValue().get());
        assertEquals("An argument.", option.getLongOpt());
        assertFalse(option.isRequired());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithOnlyOptions() throws ParseException {
        Options options = newOptions();
        options.addOption("o", "option", false, "An option.");
        Parser parser = newDefaultParser();

        String[] arguments = {"-o", "-o"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertFalse(option.getValue().isPresent());
        assertEquals("An option.", option.getLongOpt());
        assertEquals(false, option.isRequired());

        option = optionsList.get(1);
        assertEquals("o", option.getKey());
        assertFalse(option.getValue().isPresent());
        assertEquals("An option.", option.getLongOpt());
        assertEquals(false, option.isRequired());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithOnlyRequiredOptions() throws ParseException {
        Options options = newOptions();
        Option option1 = options.addOption("o1", "option1", true, "Option 1.");
        Option option2 = options.addOption("o2", "option2", true, "Option 2.");
        Parser parser = newDefaultParser();

        String[] arguments = {};
        try {
            parser.parse(options, arguments);
            fail("Missing required options.");
        } catch (MissingOptionException e) {
            List<String> missing = e.getMissingOptions();
            assertEquals(2, missing.size());
            assertEquals("o1", missing.get(0));
            assertEquals("o2", missing.get(1));
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = newOptions();
        options.addOption("o", "option", false, "An option.");
        Parser parser = newDefaultParser();

        Properties properties = new Properties();
        properties.setProperty("o", "value");

        String[] arguments = {"--", "--option", "value"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertEquals("value", option.getValue().get());
        assertEquals("An option.", option.getLongOpt());
        assertEquals(false, option.isRequired());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        Options options = newOptions();
        options.addOption("o", "option", false, "An option.");
        Parser parser = newDefaultParser();

        String[] arguments = {"-o", "--", "extra"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o", option.getKey());
        assertFalse(option.getValue().isPresent());
        assertEquals("An option.", option.getLongOpt());
        assertEquals(false, option.isRequired());

        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("extra", args.get(0));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Options options = newOptions();
        Option option = options.addOption("o", "option", true, "An option.");
        option.setArgName("arg");
        Parser parser = newDefaultParser();

        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option opt = optionsList.get(0);
        assertEquals("o", opt.getKey());
        assertEquals("value", opt.getValue().get());
        assertEquals("An option.", opt.getLongOpt());
        assertEquals(false, opt.isRequired());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());

        option = options.getOption("o");
        assertNull(option.getValue());

        arguments = new String[]{"-o", "value", "argValue"};
        cmd = parser.parse(options, arguments);

        optionsList = cmd.getOptions();
        opt = optionsList.get(0);
        assertEquals("o", opt.getKey());
        assertEquals("value", opt.getValue().get());
        assertEquals("An option.", opt.getLongOpt());
        assertEquals(false, opt.isRequired());

        args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("argValue", args.get(0));
    }

    @Test
    public void testParseWithGroupAndChildOption() throws ParseException {
        Options options = newOptions();
        OptionGroup group = newOptionGroup("group", "Group.");
        group.addOption(new Option("g1", "groupOption1", false, "Group option 1."));
        group.addOption(new Option("g2", "groupOption2", false, "Group option 2."));
        Option childOption = new Option("c", "childOption", true, "Child option.");
        childOption.setOptionalArg(true);
        childOption.setLongOpt("child-option");
        childOption.setParent(group);
        options.addOptionGroup(group);
        options.addOption(childOption);

        Parser parser = newDefaultParser();

        String[] arguments = {"--group", "--child-option", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option groupOption = null;
        for (Option option : optionsList) {
            if (option.getParent() instanceof OptionGroup) {
                groupOption = option;
                break;
            }
        }
        assertNotNull(groupOption);

        assertEquals("group", groupOption.getGroup().getKey());
        assertEquals("groupOption1", groupOption.getLongOpt());
        assertEquals(false, groupOption.isRequired());

        Option childOptionObj = null;
        for (Option option : optionsList) {
            if (option.getKey().equals("c")) {
                childOptionObj = option;
                break;
            }
        }
        assertNotNull(childOptionObj);
        assertEquals("child-option", childOptionObj.getLongOpt());
        assertEquals("Child option.", childOptionObj.getDescription());
        assertEquals(false, childOptionObj.isRequired());
        assertEquals("value", childOptionObj.getValue().get());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithGroupAndMissingChildOption() throws ParseException {
        Options options = newOptions();
        OptionGroup group = newOptionGroup("group", "Group.");
        group.addOption(new Option("g1", "groupOption1", false, "Group option 1."));
        group.addOption(new Option("g2", "groupOption2", false, "Group option 2."));
        Option childOption = new Option("c", "childOption", true, "Child option.");
        childOption.setOptionalArg(true);
        childOption.setLongOpt("child-option");
        childOption.setParent(group);
        options.addOptionGroup(group);
        options.addOption(childOption);

        Parser parser = newDefaultParser();

        String[] arguments = {"--group"};
        try {
            parser.parse(options, arguments);
            fail("Missing required child option.");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: c", e.getMessage());
        }
    }

    @Test
    public void testParseWithGroupAndExtraChildOptions() throws ParseException {
        Options options = newOptions();
        OptionGroup group = newOptionGroup("group", "Group.");
        group.addOption(new Option("g1", "groupOption1", false, "Group option 1."));
        group.addOption(new Option("g2", "groupOption2", false, "Group option 2."));
        Option childOption = new Option("c", "childOption", true, "Child option.");
        childOption.setOptionalArg(true);
        childOption.setLongOpt("child-option");
        childOption.setParent(group);
        options.addOptionGroup(group);
        options.addOption(childOption);

        Parser parser = newDefaultParser();

        String[] arguments = {"--group", "--child-option", "arg1", "--child-option", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option groupOption = null;
        for (Option option : optionsList) {
            if (option.getParent() instanceof OptionGroup) {
                groupOption = option;
                break;
            }
        }
        assertNotNull(groupOption);

        assertEquals("group", groupOption.getGroup().getKey());
        assertEquals("groupOption1", groupOption.getLongOpt());
        assertEquals(false, groupOption.isRequired());

        Option childOptionObj1 = null;
        Option childOptionObj2 = null;
        for (Option option : optionsList) {
            if (option.getKey().equals("c")) {
                if (option.getValue().isPresent()) {
                    childOptionObj1 = option;
                } else {
                    childOptionObj2 = option;
                }
                break;
            }
        }
        assertNotNull(childOptionObj1);
        assertEquals("arg1", childOptionObj1.getValue().get());

        assertNotNull(childOptionObj2);
        assertNull(childOptionObj2.getValue());

        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    private Options newOptions() {
        return new Options();
    }

    private Parser newDefaultParser() {
        return new DefaultParser();
    }
}