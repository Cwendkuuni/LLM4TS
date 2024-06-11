package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_NoOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(0, commandLine.getArgs().size());
        assertNull(commandLine.getOptions());
    }

    @Test
    public void testParse_SimpleOptions() throws ParseException {
        String[] arguments = new String[] {"-a", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", null, "Long option b");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        List<Option> optionsList = commandLine.getOptions();
        assertEquals(2, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());

        Option optionB = optionsList.get(1);
        assertNull(optionB.getValue());
    }

    @Test
    public void testParse_OptionsWithValues() throws ParseException {
        String[] arguments = new String[] {"-a", "valueA", "-b=valueB"};

        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", true, "Long option b");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        List<Option> optionsList = commandLine.getOptions();
        assertEquals(2, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());
        assertEquals("valueA", optionA.getValue());

        Option optionB = optionsList.get(1);
        assertTrue(optionB.isSet());
        assertEquals("valueB", optionB.getValue());
    }

    @Test
    public void testParse_OptionsWithGroup() throws ParseException {
        String[] arguments = new String[] {"--group", "-g", "-optionA", "valueA"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("a", true, "Short option A"));

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        List<Option> optionsList = commandLine.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("group", option.getLongOpt());
        assertTrue(option.isSet());
        List<Option> subOptionsList = option.getSubOptions();
        assertEquals(1, subOptionsList.size());
        Option subOption = subOptionsList.get(0);
        assertTrue(subOption.isSet());
        assertEquals("optionA", subOption.getLongOpt());
        assertEquals("valueA", subOption.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        String[] arguments = new String[] {"--unknownOption"};

        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
        fail();
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgument() throws ParseException {
        String[] arguments = new String[] {"-a"};

        Options options = new Options();
        Option option = new Option("a", true, "Short option a");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
        fail();
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] arguments = new String[] {"-a", "-b", "valueB", "--"};

        Options options = new Options();
        Option optionA = new Option("a", true, "Short option a");
        Option optionB = new Option("b", true, "Short option b");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true, false);

        List<String> argsList = commandLine.getArgs();
        assertEquals(1, argsList.size());
        assertEquals("valueB", argsList.get(0));
        assertNull(commandLine.getOptions());
    }

    @Test
    public void testParse_Properties() throws ParseException {
        String[] arguments = new String[] {"--property1=foo", "--property2=bar"};

        Options options = new Options();
        options.addOption("property1", true, "Property 1");
        options.addOption("property2", true, "Property 2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        Properties properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertEquals("foo", properties.getProperty("property1"));
        assertEquals("bar", properties.getProperty("property2"));
    }

    @Test
    public void testParse_RequiredOptions() throws ParseException {
        String[] arguments = new String[] {"--optionA", "--optionB"};

        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        optionA.setRequired(true);
        Option optionB = new Option("b", true, "Option B");
        optionB.setRequired(true);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(0, commandLine.getArgs().size());
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).isSet());
        assertTrue(optionsList.get(1).isSet());
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOptions() throws ParseException {
        String[] arguments = new String[] {"--optionA"};

        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        optionA.setRequired(true);
        Option optionB = new Option("b", true, "Option B");
        optionB.setRequired(true);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }
}