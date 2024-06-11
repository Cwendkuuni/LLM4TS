package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a double argument");
        options.addOption("d", "default", "Option D with a default value");
        options.addOption("e", null, "Option E with no argument");
        options.addOption("f", false, "Option F without a flag");

        String[] arguments = {"-a", "-b", "1", "-c", "3.14", "-d", "--", "-e"};
        Parser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(8, commandLine.getArgs().size());
        assertEquals("1", commandLine.getOptionValue("b"));
        assertEquals("3.14", commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue("f"));
        assertEquals("default", commandLine.getOptionValue("d"));
        assertNull(commandLine.getOptionValue("g"));

        List<Option> optionsList = commandLine.getOptions();
        assertEquals(6, optionsList.size());
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);
        Option optionC = optionsList.get(2);
        Option optionD = optionsList.get(3);
        Option optionE = optionsList.get(4);
        Option optionF = optionsList.get(5);

        assertEquals("a", optionA.getKey());
        assertEquals(true, optionA.isRequired());
        assertEquals("Option A", optionA.getLongOpt());
        assertEquals("Option A", optionA.getDescription());

        assertEquals("b", optionB.getKey());
        assertEquals(1, optionB.getArgPosition());
        assertEquals("Option B with an integer argument", optionB.getLongOpt());
        assertEquals("Option B", optionB.getDescription());

        assertEquals("c", optionC.getKey());
        assertEquals(2, optionC.getArgPosition());
        assertEquals("Option C with a double argument", optionC.getLongOpt());
        assertEquals("Option C", optionC.getDescription());

        assertEquals("d", optionD.getKey());
        assertEquals("default", optionD.getValue());
        assertEquals(null, optionD.getArgPosition());
        assertEquals("Option D with a default value", optionD.getLongOpt());
        assertEquals("Option D", optionD.getDescription());

        assertEquals("e", optionE.getKey());
        assertEquals(null, optionE.getValue());
        assertEquals(0, optionE.getArgPosition());
        assertEquals("Option E with no argument", optionE.getLongOpt());
        assertEquals("Option E", optionE.getDescription());

        assertEquals("f", optionF.getKey());
        assertEquals(false, optionF.isRequired());
        assertEquals(null, optionF.getLongOpt());
        assertEquals("Option F without a flag", optionF.getDescription());
    }

    @Test
    public void testParseWithNoOptions() {
        String[] arguments = {};
        Parser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(new Options(), arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--"};
        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithMissingOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");

        String[] arguments = {"-a", "-b"};
        Parser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithExtraArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1", "arg2"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties properties = new Properties();
        properties.setProperty("a", "valueA");

        String[] arguments = {"--"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, arguments, properties);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals("valueA", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithMixedArgumentsAndOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--", "-a", "arg1", "--"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals(true, commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithOptionGroups() {
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1");
        group1.addOption("a", true, "Option A in group1");
        group1.addOption("b", true, "Option B in group1");

        OptionGroup group2 = new OptionGroup("group2");
        group2.addOption("c", true, "Option C in group2");

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        String[] arguments = {"--group1", "-a", "-b", "--group2", "-c"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(commandLine);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(true, commandLine.hasOption("a"));
        assertEquals(true, commandLine.hasOption("b"));
        assertEquals(true, commandLine.hasOption("c"));
        assertEquals(group1, commandLine.getOptionGroup("group1"));
        assertEquals(group2, commandLine.getOptionGroup("group2"));
    }

    @Test
    public void testParseWithAmbiguousOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option A2");

        String[] arguments = {"--a"};
        Parser parser = new DefaultParser();

        assertThrows(AmbiguousOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }
}