package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 1, "A test option");

        String[] args = new String[]{"-a", "--b", "1", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithNullOptions() {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"arg1", "arg2"};

        assertThrows(NullPointerException.class, () -> {
            parser.parse(new Options(), args);
        });
    }

    @Test
    public void testParseWithEmptyOptions() {
        Options options = new Options();

        String[] args = new String[]{"arg1", "arg2"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertEquals(2, cmd.getArgs().size());
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithNoOptions() {
        Options options = new Options();

        String[] args = new String[]{"arg1", "arg2"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertEquals(2, cmd.getArgs().size());
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithRequiredOptions() {
        Options options = new Options();
        options.addOption("a", true, "A test option", Collections.singletonList("required"));

        String[] args = new String[]{"arg1"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(args);
        });

        assertEquals(Collections.singletonList("a"), options.getRequiredOptions());
    }

    @Test
    public void testParseWithOptionalOptions() {
        Options options = new Options();
        options.addOption("a", false, "A test option");

        String[] args = new String[]{"--a"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(null, cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithValueOptions() {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 1, "A test option");

        String[] args = new String[]{"-a", "--b", "1"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithLongOptions() {
        Options options = new Options();
        options.addOption(" long-option", false, "A test option");

        String[] args = new String[]{"--long-option"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("long-option"));
        assertEquals(null, cmd.getOptionValue("long-option"));
    }

    @Test
    public void testParseWithGroupOptions() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", false, "A test option");
        group.addOption("b", false, "Another test option");

        options.addOptionGroup(group);

        String[] args = new String[]{"--group", "--a"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(null, cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));

        assertEquals("group", group.getLongOpt());
    }

    @Test
    public void testParseWithGroupOptionsRequired() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption("a", false, "A test option");
        group.addOption("b", false, "Another test option");

        options.addOptionGroup(group);

        String[] args = new String[]{"--group"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(args);
        });
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "A test option");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] args = new String[0];

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "A test option");

        String[] args = new String[]{"--a", "nonOption"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, null, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("nonOption", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOptionAndNoOptions() {
        String[] args = new String[]{"nonOption1", "nonOption2"};

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(args, null, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("nonOption1", cmd.getArgs().get(0));
        assertEquals("nonOption2", cmd.getArgs().get(1));
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithGroupAndProperties() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", false, "A test option");
        group.addOption("b", false, "Another test option");

        options.addOptionGroup(group);

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] args = new String[0];

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithGroupAndPropertiesAndStopAtNonOption() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", false, "A test option");
        group.addOption("b", false, "Another test option");

        options.addOptionGroup(group);

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] args = new String[]{"nonOption"};

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, properties, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("nonOption", cmd.getArgs().get(0));
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = new String[]{"--a", "--b", "--", "arg1", "--a", "arg2"};

        Parser parser = new DefaultParser();
        List<String> result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a", "--b", "--", "arg1", "--a", "arg2");

        assertEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = new String[]{"--a", "--b", "nonOption"};

        Parser parser = new DefaultParser();
        List<String> result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--a", "--b", "nonOption");

        assertEquals(expected, result);
    }
}