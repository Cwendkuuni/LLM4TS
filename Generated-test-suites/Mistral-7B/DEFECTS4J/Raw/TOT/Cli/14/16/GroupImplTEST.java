package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroupImplTest {

    private PrintStream originalErrorStream;
    private PrintStream errorStream;

    @Before
    public void setUp() {
        originalErrorStream = System.err;
        errorStream = new PrintStream(new OutputStreamWriter(System.out));
        System.setErr(errorStream);
    }

    @Test
    public void testGroupImplicit() throws ParseException {
        final String[] args = {"testGroup", "--option1", "--option2", "--value1", "--value2"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1"),
                new Option("option2", true, "o2", "Option 2")
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        parser.parse(options, commandLine, args);

        Assert.assertEquals(2, commandLine.getValuesForOption("option1").size());
        Assert.assertEquals("value1", commandLine.getValuesForOption("option1").get(0));
        Assert.assertEquals("value2", commandLine.getValuesForOption("option1").get(1));
        Assert.assertEquals(Collections.emptyList(), commandLine.getValuesForOption("option3"));

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupExplicit() throws ParseException {
        final String[] args = {"testGroup", "--test-group", "--option1=value1", "--option2=value2"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1"),
                new Option("option2", true, "o2", "Option 2")
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        parser.parse(options, commandLine, args);

        Assert.assertEquals(2, commandLine.getValuesForOption("option1").size());
        Assert.assertEquals("value1", commandLine.getValuesForOption("option1").get(0));
        Assert.assertEquals("value2", commandLine.getValuesForOption("option1").get(1));
        Assert.assertEquals(Collections.emptyList(), commandLine.getValuesForOption("option3"));

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupMinimum() throws ParseException {
        final String[] args = {"testGroup", "--option1=value1"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1").required(true),
                new Option("option2", true, "o2", "Option 2")
        ));
        options.addOptionGroup(group);

        try {
            final WriteableCommandLine commandLine = new BasicParseResult();

            parser.parse(options, commandLine, args);

            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getOption().getIndex());
            Assert.assertEquals("option1", e.getOption().getLongOpt());
            Assert.assertEquals("The option 'option1' is required.", e.getMessage());
        }

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupMaximum() throws ParseException {
        final String[] args = {"testGroup", "--option1=value1", "--option1=value2", "--option1=value3"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1").required(false).limit(2)
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        try {
            parser.parse(options, commandLine, args);
        } catch (ParseException e) {
            Assert.fail();
        }

        Assert.assertEquals(2, commandLine.getValuesForOption("option1").size());

        System.setErr(originalErrorStream);
    }

    @Test(expected = ParseException.class)
    public void testGroupMaximumError() throws ParseException {
        final String[] args = {"testGroup", "--option1=value1", "--option1=value2", "--option1=value3", "--option1=value4"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1").required(false).limit(2)
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        parser.parse(options, commandLine, args);

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupAnonymous() throws ParseException {
        final String[] args = {"testGroup", "--value1=foobar", "--option1=value1"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1", new ValueFormatter()),
                new Argument("value1", true, "v1", "Value 1", "Value for anonymous argument")
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        parser.parse(options, commandLine, args);

        Assert.assertEquals("foobar", commandLine.getValuesForOption("value1").get(0));
        Assert.assertEquals(1, commandLine.getValuesForOption("option1").size());
        Assert.assertEquals("value1", commandLine.getValuesForOption("option1").get(0));

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupAnonymousOptional() throws ParseException {
        final String[] args = {"testGroup", "--value1=foobar", "--option1=value1"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1", new ValueFormatter()),
                new Argument("value1")
        ));
        options.addOptionGroup(group);

        final WriteableCommandLine commandLine = new BasicParseResult();

        parser.parse(options, commandLine, args);

        Assert.assertEquals(null, commandLine.getValuesForOption("value1"));

        System.setErr(originalErrorStream);
    }

    @Test
    public void testGroupAnonymousRequired() throws ParseException {
        final String[] args = {"testGroup"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1", new ValueFormatter()),
                new Argument("value1").required(true)
        ));
        options.addOptionGroup(group);

        try {
            final WriteableCommandLine commandLine = new BasicParseResult();

            parser.parse(options, commandLine, args);

            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getOption().getIndex());
            Assert.assertEquals("value1", e.getOption().getLongOpt());
            Assert.assertEquals("The value for option 'value1' is required.", e.getMessage());
        }

        System.setErr(originalErrorStream);
    }

    @Test
    public void testHelp() {
        final PrintStream out = System.out;

        final String[] args = {"--help"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1"),
                new Option("option2", true, "o2", "Option 2"),
                new Option("option3", true, "o3", "Option 3")
        ));
        options.addOptionGroup(group);

        final HelpFormatter formatter = new HelpFormatter();

        final WriteableCommandLine commandLine = new BasicParseResult();

        try {
            parser.parse(options, commandLine, args);
        } catch (ParseException e) {
            // ignore
        }

        formatter.printHelp("Test", "test [options]", true, options, 80, out);

        final String help = out.getErrorStream().toString();

        final Map<Integer, String> lines = new TreeMap<Integer, String>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        final String[] linesArray = help.split("\n");

        for (final String line : linesArray) {
            lines.put(lines.size() + 1, line);
        }

        final List<String> expectedLines = Arrays.asList(
                "",
                "Test (version)",
                "",
                "Test [options]",
                "",
                "options:",
                "  -o1 OPTION1 (isOptionRequired)",
                "  -o2 OPTION2 (isOptionRequired)",
                "  -o3 OPTION3 (isOptionRequired)",
                "",
                "Groups:",
                "  testGroup:Test Group",
                "    Option 1 (isOptionRequired)",
                "    Value 1 (isOptionRequired, takes a value)",
                "");

        for (int i = 1; i <= expectedLines.size(); i++) {
            final String expectedLine = expectedLines.get(i - 1);
            final String actualLine = lines.get(i);

            Assert.assertEquals(expectedLine, actualLine);
        }

        System.setOut(out);
    }

    @Test
    public void testHelpWithLongOption() {
        final PrintStream out = System.out;

        final String[] args = {"--test-help"};

        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        final Group group = new Group("testGroup", "Test Group", Arrays.asList(
                new Option("option1", true, "o1", "Option 1"),
                new Option("option2", true, "o2", "Option 2"),
                new Option("option3", true, "o3", "Option 3")
        ));
        options.addOptionGroup(group);

        final HelpFormatter formatter = new HelpFormatter();

        final WriteableCommandLine commandLine = new BasicParseResult();

        try {
            parser.parse(options, commandLine, args);
        } catch (ParseException e) {
            // ignore
        }

        formatter.printHelp("Test", "test [options]", true, options, 80, out);

        final String help = out.getErrorStream().toString();

        final Map<Integer, String> lines = new TreeMap<Integer, String>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        final String[] linesArray = help.split("\n");

        for (final String line : linesArray) {
            lines.put(lines.size() + 1, line);
        }

        final List<String> expectedLines = Arrays.asList(
                "",
                "Test (version)",
                "",
                "Test [options]",
                "",
                "options:",
                "  -o1 OPTION1 (isOptionRequired)",
                "  -o2 OPTION2 (isOptionRequired)",
                "  -o3 OPTION3 (isOptionRequired)",
                "",
                "Groups:",
                "  testGroup:Test Group",
                "    Option 1 (isOptionRequired)",
                "    Option 2 (isOptionRequired)",
                "    Option 3 (isOptionRequired)",
                "");

        for (int i = 1; i <= expectedLines.size(); i++) {
            final String expectedLine = expectedLines.get(i - 1);
            final String actualLine = lines.get(i);

            Assert.assertEquals(expectedLine, actualLine);
        }

        System.setOut(out);
    }
}