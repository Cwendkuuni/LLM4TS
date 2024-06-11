package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Parser parser;

    @Test
    public void testParseSimple() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--optionB", "valueB"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals("optionB", commandLine.getOptionValue("optionB"));
        assertNull(commandLine.getOptionValue("optionC"));
        assertTrue(commandLine.hasOption("optionA"));
        assertTrue(commandLine.hasOption("optionB"));
        assertFalse(commandLine.hasOption("optionC"));
        assertArrayEquals(new String[]{"valueB"}, commandLine.getOptionValues("optionB"));
        assertEquals(0, commandLine.getArgs().length);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--optionB=valueB", "--optionC=valueC"};
        Properties properties = new Properties();
        properties.setProperty("optionB", "otherValueB");
        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertEquals("otherValueB", commandLine.getOptionValue("optionB"));
        assertEquals("valueC", commandLine.getOptionValue("optionC"));
        assertEquals("valueB", commandLine.getOptionValue("optionB", "defaultValue"));
        assertEquals(0, commandLine.getArgs().length);
        assertEquals(3, commandLine.getOptions().size());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--optionB"};
        List<Option> requiredOptions = Arrays.asList(new Option("optionC", true, false, "Option C is required"));
        options.addOptions(requiredOptions);
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: optionC", e.getMessage());
        }

        parser = new DefaultParser();
        arguments = new String[]{"-a", "--optionB", "--optionC"};
        commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("optionC"));
        assertFalse(commandLine.isOptionMissing("optionC"));
    }

    @Test
    public void testParseWithOptionalArgs() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--optionB=valueB", "--optionC=valueC", "arg1", "arg2"};
        Option option = new Option("optionD", true, true, "Option D with optional argument");
        option.setArgName("arg");
        option.setValueSeparator(';');
        options.addOption(option);
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getArgs().length);
        assertEquals("arg1", commandLine.getArgs()[0]);
        assertEquals("arg2", commandLine.getArgs()[1]);
        assertEquals("valueB", commandLine.getOptionValue("optionB"));
        assertEquals("valueC", commandLine.getOptionValue("optionC"));
        List<String> optionDValues = commandLine.getOptionValues("optionD");
        assertEquals(2, optionDValues.size());
        assertEquals("arg1", optionDValues.get(0));
        assertEquals("arg2", optionDValues.get(1));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group of options");
        Option optionA = new Option("optionA", true, false, "Option A");
        Option optionB = new Option("optionB", true, false, "Option B");
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOptionGroup(group);
        String[] arguments = new String[]{"--group", "--optionA"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("optionA"));
        assertTrue(commandLine.hasOption("optionB"));
        assertTrue(commandLine.isOptionSelected("group"));
        assertFalse(commandLine.isOptionSelected("group2"));
    }

    @Test
    public void testParseWithMixedArgsAndOptions() throws ParseException {
        parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--optionB=valueB", "-x", "arg1", "--optionC", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals("arg1", commandLine.getArgs()[0]);
        assertEquals("arg2", commandLine.getArgs()[1]);
        assertEquals("valueB", commandLine.getOptionValue("optionB"));
        assertTrue(commandLine.hasOption("optionC"));
        assertNull(commandLine.getOptionValue("optionC", "defaultValue"));
        assertEquals("x", commandLine.getArgs()[2]);
    }
}