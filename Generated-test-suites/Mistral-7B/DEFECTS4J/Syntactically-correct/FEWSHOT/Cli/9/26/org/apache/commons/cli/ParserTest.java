package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOption("a") != null);
    }

    @Test
    public void testParseSingleValueOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a", "value"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOption("a").getValue().equals("value"));
    }

    @Test
    public void testParseMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Description A");
        options.addOption("b", true, "Description B");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a", "-b"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 2);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseMultipleValuesOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");
        options.getOption("a").setArgName("arg");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a", "value1", "-a", "value2"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 1);
        List<String> values = (List<String>) cmd.getOption("a").getValue();
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }

    @Test
    public void testParseMultipleGroups() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        group1.addOption("a", true, "Description A");

        OptionGroup group2 = new OptionGroup("group2");
        group2.addOption("b", true, "Description B");

        options.addOption("c", true, "Description C");
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-c", "-a", "--group1", "-b"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 3);
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(group1.isSelected());
        assertTrue(group2.isSelected());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");
        options.getOption("a").setRequired(true);

        Parser parser = new DefaultParser();

        String[] arguments = new String[0];

        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--test"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Description");
        options.getOption("a").setArgName("arg");
        options.getOption("a").setRequiredArg(true);

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        String[] arguments = new String[0];

        parser.parse(options, arguments);
    }

    @Test
    public void testParseProperties() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Properties properties = new Properties();
        properties.put("a", "value");

        Parser parser = new DefaultParser();

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments, properties);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOption("a").getValue().equals("value"));
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a", "value", "non-option"};

        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().size() == 1);
        assertTrue(cmd.getArgs().get(0).equals("non-option"));
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOption("a").getValue().equals("value"));
    }

    @Test
    public void testParseStopAtNonOptionWithEmptyArgument() {
        Options options = new Options();
        options.addOption("a", true, "Description");

        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-a", "", "non-option"};

        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().size() == 1);
        assertTrue(cmd.getArgs().get(0).equals("non-option"));
        assertTrue(cmd.getOptions().size() == 1);
        assertTrue(cmd.getOption("a").getValue().isEmpty());
    }
}