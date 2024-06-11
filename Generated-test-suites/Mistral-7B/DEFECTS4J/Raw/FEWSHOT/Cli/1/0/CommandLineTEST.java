package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateEmptyCommandLine() {
        CommandLine cmd = new CommandLine();
        List args = cmd.getArgList();
        Map options = cmd.getOptions();
        assertTrue(args.isEmpty());
        assertTrue(options.isEmpty());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A"));
        cmd.addOption(new Option("b", "optB", true, "Option B with argument"));
        cmd.addOption(new Option("c", "optC", false, "Option C with no argument"));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A with no argument"));
        cmd.addOption(new Option("b", "optB", true, "Option B with argument"));
        cmd.addOption(new Option("c", "optC", false, "Option C with no argument"));

        cmd.addArg("firstArg");
        cmd.addArg("secondArg");
        cmd.addArg("thirdArg");

        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue('b'));

        assertEquals("firstArg", cmd.getOptionValue("b", "default"));
        assertEquals("secondArg", cmd.getOptionValue("c", "default"));
        assertEquals("thirdArg", cmd.getOptionValue("d", "default"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A with no argument"));
        cmd.addOption(new Option("b", "optB", true, "Option B with multiple arguments"));
        cmd.addOption(new Option("c", "optC", false, "Option C with no argument"));

        cmd.addArg("firstArg");
        cmd.addArg("secondArg");
        cmd.addArg("thirdArg firstArg thirdArg");

        assertArrayEquals(new String[0], cmd.getOptionValues("a"));
        assertArrayEquals(new String[]{"firstArg", "thirdArg"}, cmd.getOptionValues("b"));
        assertArrayEquals(new String[0], cmd.getOptionValues("d"));
        assertNull(cmd.getOptionValues('c'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A with no argument"));
        cmd.addOption(new Option("b", "optB", true, "Option B with argument"));
        cmd.addOption(new Option("c", "optC", false, "Option C with no argument"));

        cmd.addArg("firstArg");
        cmd.addArg("secondArg");
        cmd.addArg("thirdArg firstArg thirdArg");

        List<String> expectedArgs = new LinkedList<>();
        expectedArgs.add("firstArg");
        expectedArgs.add("secondArg");
        expectedArgs.add("thirdArg");
        expectedArgs.addAll(List.of("firstArg", "thirdArg"));

        assertArrayEquals(expectedArgs.toArray(new String[0]), cmd.getArgs());
        assertSame(cmd.getArgList(), expectedArgs);
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A with no argument"));
        cmd.addOption(new Option("b", "optB", true, "Option B with argument"));
        cmd.addOption(new Option("c", "optC", false, "Option C with no argument"));

        cmd.addArg("firstArg");
        cmd.addArg("secondArg");
        cmd.addArg("thirdArg firstArg thirdArg");

        String expected = "[ CommandLine: [ options: {optA=false, optC=false, optB=OptionB@5e0e67} ] [ args: [firstArg, secondArg, thirdArg, firstArg, thirdArg] ] ]";

        assertEquals(expected, cmd.toString());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        cmd.addOption(new Option("a", "optA", false, "Option A with no argument"));

        cmd.addArg("firstArg");
        cmd.addArg("secondArg");
        cmd.addArg("thirdArg firstArg thirdArg");

        List<String> expectedArgs = new LinkedList<>();
        expectedArgs.add("firstArg");
        expectedArgs.add("secondArg");
        expectedArgs.add("thirdArg");
        expectedArgs.addAll(List.of("firstArg", "thirdArg"));

        assertArrayEquals(expectedArgs.toArray(new String[0]), cmd.getArgs());
        assertSame(cmd.getArgList(), expectedArgs);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        Option optA = new Option("a", "optA", false, "Option A with no argument");
        Option optB = new Option("b", "optB", true, "Option B with argument");
        Option optC = new Option("c", "optC", false, "Option C with no argument");

        cmd.addOption(optA);
        cmd.addOption(optB);
        cmd.addOption(optC);

        Map<String, Option> expectedOptions = new HashMap<>();
        expectedOptions.put("optA", optA);
        expectedOptions.put("optB", optB);
        expectedOptions.put("optC", optC);

        assertEquals(expectedOptions, cmd.getOptions());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        Option optA = new Option("a", "optA", false, "Option A with no argument");
        Option optB = new Option("b", "optB", true, "Option B with argument");
        Option optC = new Option("c", "optC", false, "Option C with no argument");

        cmd.addOption(optA);
        cmd.addOption(optB);
        cmd.addOption(optC);

        List<Option> expectedOptions = new LinkedList<>();
        expectedOptions.add(optA);
        expectedOptions.add(optB);
        expectedOptions.add(optC);

        Iterator<Option> it = cmd.iterator();

        List<Option> actualOptions = new LinkedList<>();
        while (it.hasNext()) {
            actualOptions.add(it.next());
        }

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptionsArray() {
        CommandLine cmd = new CommandLine();

        Option optA = new Option("a", "optA", false, "Option A with no argument");
        Option optB = new Option("b", "optB", true, "Option B with argument");
        Option optC = new Option("c", "optC", false, "Option C with no argument");

        cmd.addOption(optA);
        cmd.addOption(optB);
        cmd.addOption(optC);

        Option[] expectedOptions = new Option[]{optA, optB, optC};

        assertArrayEquals(expectedOptions, cmd.getOptions());
    }
}