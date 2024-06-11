package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseNoArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg(true).build());
        String[] arguments = new String[]{"arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").build());
        options.addOption(Option.builder("a").argName("arg").hasArg(true).build());
        String[] arguments = new String[]{"-o", "option", "-a", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option option = optionsList.get(0);
        assertSame(Option.builder("o").argName("option").build(), option);
        assertNull(option.getValue());

        Option argOption = optionsList.get(1);
        assertSame(Option.builder("a").argName("arg").hasArg(true).build(), argOption);
        assertArrayEquals(new String[]{"arg1"}, argOption.getValues());
        assertArrayEquals(new String[]{"arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithOptionsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option1").build());
        options.addOption(Option.builder("o").argName("option2").build());
        options.addOption(Option.builder("a").argName("arg").hasArg(true).build());

        Properties properties = new Properties();
        properties.put("o", "option3");

        String[] arguments = new String[]{"-a", "arg1", "--o", "option1", "--o", "option2", "--o", "option3"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(4, optionsList.size());

        Option option1 = optionsList.get(0);
        assertSame(Option.builder("o").argName("option1").build(), option1);
        assertNull(option1.getValue());

        Option option2 = optionsList.get(1);
        assertSame(Option.builder("o").argName("option2").build(), option2);
        assertNull(option2.getValue());

        Option argOption = optionsList.get(2);
        assertSame(Option.builder("a").argName("arg").hasArg(true).build(), argOption);
        assertArrayEquals(new String[]{"arg1"}, argOption.getValues());
        assertArrayEquals(new String[]{"option3"}, cmd.getOptionValues("o"));
        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").isRequired(true).build());
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").isRequired(true).build());
        String[] arguments = new String[]{"arg1", "arg2"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").build());
        String[] arguments = new String[]{"-x", "arg"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptionWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").build());
        String[] arguments = new String[]{"-x", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);
    }

    @Test
    public void testParseWithGroups() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(Option.builder("g").argName("group-option").build());

        options.addOptionGroup(group);
        options.addOption(Option.builder("o").argName("option").build());
        options.addOption(Option.builder("a").argName("arg").hasArg(true).build());

        String[] arguments = new String[]{"--group", "--g", "group-option", "--o", "option"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option groupOption = optionsList.get(0);
        assertSame(Option.builder("g").argName("group-option").build(), groupOption);
        assertNull(groupOption.getValue());

        Option option = optionsList.get(1);
        assertSame(Option.builder("o").argName("option").build(), option);
        assertNull(option.getValue());

        assertTrue(group.isSelected());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingGroupOption() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(Option.builder("g").argName("group-option").build());

        options.addOptionGroup(group);
        options.addOption(Option.builder("o").argName("option").build());
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);
    }

    @Test
    public void testParseWithEmptyOptionValues() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").hasArg(true).build());
        String[] arguments = new String[]{"-o", "", "-o", ""};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());

        Option option = optionsList.get(0);
        assertSame(Option.builder("o").argName("option").hasArg(true).build(), option);
        assertArrayEquals(new String[0], option.getValues());
        assertArrayEquals(new String[0], cmd.getArgs());
    }
}