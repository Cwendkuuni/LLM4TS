package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        final Options options = new Options();
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.emptyList(), cmd.getOptionMap().keySet());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        final Options options = new Options();
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Arrays.asList("arg"), cmd.getArgs());
        assertEquals(Collections.emptyList(), cmd.getOptionMap().keySet());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.singletonMap("o", option), cmd.getOptionMap());
    }

    @Test
    public void testParseWithSingleOptionAndValue() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.singletonMap("o", option), cmd.getOptionMap());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o1", "opt1", true, "Option 1");
        final Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o1", "arg1", "-o2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Arrays.asList(option1, option2), cmd.getOptionMap().values());
    }

    @Test
    public void testParseWithMultipleOptionsAndValues() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o1", "opt1", true, "Option 1");
        final Option option2 = new Option("o2", "opt2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o1", "arg1", "-o2", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Arrays.asList(option1, option2), cmd.getOptionMap().values());
        assertEquals("arg1", option1.getValue());
        assertEquals("arg2", option2.getValue());
    }

    @Test
    public void testParseWithMultipleOptionsWithSameLetter() throws ParseException {
        final Options options = new Options();
        final Option option1 = new Option("o", "opt1", true, "Option 1");
        final Option option2 = new Option("o", "opt2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        assertThrows(ParseException.class, () -> parser.parse(options, new String[]{"-o", "arg"}));
    }

    @Test
    public void testParseWithRequiredOptionMissing() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "opt", true, "Description");
        option.setRequired(true);
        options.addOption(option);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"arg1", "arg2"};

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("opt", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.singletonMap("o", option), cmd.getOptionMap());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        final Options options = new Options();
        final Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"arg1", "-o", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(Arrays.asList("arg1", "-o"), cmd.getArgs());
        assertEquals(Collections.singletonMap("o", option), cmd.getOptionMap());
        assertEquals("arg2", option.getValue());
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        final Options options = new Options();
        final OptionGroup group = new OptionGroup("g", "group");
        final Option option1 = new Option("o1", "opt1", true, "Option 1");
        final Option option2 = new Option("o2", "opt2", true, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        final Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--g", "-o1", "arg1", "-o2", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Arrays.asList(option1, option2), cmd.getOptionMap().values());
        assertEquals("arg1", option1.getValue());
        assertEquals("arg2", option2.getValue());
        assertSame(group, option1.getParent());
        assertSame(group, option2.getParent());
        assertTrue(group.isSelected());
    }
}