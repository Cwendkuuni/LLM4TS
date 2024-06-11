package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        options.addRequiredOption("a", "a", false, "an option");
        options.addOption("b", "b", true, "an optional option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a", "--b"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertNull(commandLine.getOptionValue("b"));
        assertEquals(Arrays.asList("an option"), commandLine.getOptionValues("a"));
    }

    @Test
    public void testParseWithValues() throws Exception {
        Options options = new Options();
        options.addOption("a", "a", true, "an option with value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a", "value"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", "a", true, "an option with value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a"};
        parser.parse(args);

        // should throw an exception
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", "a", false, "an option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-x"};
        parser.parse(args);

        // should throw an exception
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", "a", true, "an option with value");

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {};
        CommandLine commandLine = parser.parse(args, properties);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception {
        Options options = new Options();
        options.addRequiredOption("a", "a", false, "an option");
        options.addRequiredOption("b", "b", false, "another required option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {};
        try {
            parser.parse(args);
            fail();
        } catch (MissingOptionException e) {
            // expected
            List<String> requiredOptions = parser.getOptions().getRequiredOptions();
            assertEquals(2, requiredOptions.size());
            assertTrue(requiredOptions.contains("a"));
            assertTrue(requiredOptions.contains("b"));
        }
    }

    @Test
    public void testParseWithOptionalGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", "a", false, "an option in group");
        group.addOption("b", "b", false, "another option in group");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--group", "-a"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testParseWithValuesInGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", "a", true, "an option with value in group");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--group", "-a", "value"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithValuesAndGroups() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", "a", true, "an option with value in group");
        options.addOptionGroup(group);
        options.addOption("b", "b", true, "an option with value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--group", "-a", "value1", "-b", "value2"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithValuesAndGroupsAndProperties() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", "a", true, "an option with value in group");
        options.addOptionGroup(group);
        options.addOption("b", "b", true, "an option with value");

        Properties properties = new Properties();
        properties.setProperty("b", "valueB");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--group", "-a", "value1"};
        CommandLine commandLine = parser.parse(args);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("valueB", commandLine.getOptionValue("b"));
    }
}