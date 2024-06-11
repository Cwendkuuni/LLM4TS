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
    public void testParse() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 2, "A test option that takes an integer argument");
        options.addOption("c", 3, "A test option that takes a string argument");
        options.addOption("d", null, "A test option with no description");
        options.addOption("e", null, true, "A test option with no description and is required");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--a", "2", "--c", "'foobar'" };
        CommandLine cmd = parser.parse(args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e")); // required option, should not be present

        List<Option> optionsList = cmd.getOptions();
        assertEquals(4, optionsList.size());

        Option optionA = optionsList.get(0);
        assertEquals("a", optionA.getKey());
        assertTrue(optionA.isSet());
        assertNull(optionA.getValue());

        Option optionB = optionsList.get(1);
        assertEquals("b", optionB.getKey());
        assertTrue(optionB.isSet());
        assertEquals("2", optionB.getValue()[0]);

        Option optionC = optionsList.get(2);
        assertEquals("c", optionC.getKey());
        assertTrue(optionC.isSet());
        assertEquals("foobar", optionC.getValue()[0]);

        Option optionD = optionsList.get(3);
        assertEquals("d", optionD.getKey());
        assertTrue(optionD.isSet());
        assertNull(optionD.getValue());
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 2, "A test option that takes an integer argument");

        Properties props = new Properties();
        props.put("b", "3");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {};
        CommandLine cmd = parser.parse(args, props);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("3", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("e", true, "A test option that is required");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {};
        parser.parse(args);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--x" };
        parser.parse(args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 2, "A test option that takes an integer argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--a" };
        parser.parse(args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--x" };
        parser.parse(args);
    }

    @Test
    public void testParseWithOptionGroups() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A test group");
        group.addOption("g", true, "A test option in the group");

        options.addOption("a", true, "A test option");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--g" };
        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("g"));
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithVarargs() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        options.addOption("b", 1, "A test option that takes an integer argument");
        options.addOption("c", 2, "A test option that takes a varargs argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = { "--a", "--b", "1", "--c", "1", "2" };
        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals(2, cmd.getValues("c").length);
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    private void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    private void assertEquals(List<Option> expected, List<Option> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("Expected: " + expected.size() + ", but was: " + actual.size());
        }

        for (int i = 0; i < expected.size(); i++) {
            Option expectedOption = expected.get(i);
            Option actualOption = actual.get(i);

            if (!expectedOption.equals(actualOption)) {
                throw new AssertionError("Expected: " + expectedOption + ", but was: " + actualOption);
            }
        }
    }
}