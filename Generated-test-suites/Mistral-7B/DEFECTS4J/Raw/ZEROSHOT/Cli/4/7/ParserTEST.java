package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @org.junit.Test
    public void testParse() throws Exception {
        String[] args = new String[] {"-a", "--b=c", "1", "2", "--", "-d", "e"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("d", true, "D");

        CommandLine commandLine = parser.parse(options, args, false);

        assertSame(args.length, commandLine.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertSame(args[i], commandLine.getArgs().get(i));
        }

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("d"));
        assertSame("c", commandLine.getOptionValue("b"));
        assertNull(commandLine.getOptionValue("e"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingOption() throws Exception {
        String[] args = new String[] {"--e=f"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        parser.parse(options, args, false);
    }

    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        String[] args = new String[] {"--x=y"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        parser.parse(options, args, false);
    }

    @org.junit.Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[] {"--a=1", "--b=2", "--c=3"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("c", true, "C");

        Properties properties = new Properties();
        properties.setProperty("a", "4");
        properties.setProperty("d", "5");

        CommandLine commandLine = parser.parse(options, args, properties, false);

        assertSame(args.length, commandLine.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertSame(args[i], commandLine.getArgs().get(i));
        }

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertSame("4", commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue("d"));
    }

    @org.junit.Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = new String[] {"--a", "--b=c", "d", "--", "e", "f"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        CommandLine commandLine = parser.parse(options, args, true);

        List<String> argsList = Arrays.asList(args);
        List<String> parsedArgsList = Arrays.asList(commandLine.getArgs());

        assertTrue(argsList.subList(0, commandLine.getArgs().size()).equals(commandLine.getArgs()));
        for (int i = commandLine.getArgs().size(); i < args.length; i++) {
            assertTrue(parsedArgsList.get(parsedArgsList.size() - 1).equals(args[i]));
            parsedArgsList.remove(parsedArgsList.size() - 1);
        }

        assertTrue(parsedArgsList.isEmpty());
    }

    @org.junit.Test
    public void testParseWithRequiredOptions() throws Exception {
        String[] args = new String[] {"--a"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addRequiredOption("b", false, "B");

        try {
            parser.parse(options, args, false);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseWithMixedOptions() throws Exception {
        String[] args = new String[] {"--a", "-b", "--c=d"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("c", true, "C");

        CommandLine commandLine = parser.parse(options, args, false);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertSame("d", commandLine.getOptionValue("c"));
    }

    @org.junit.Test
    public void testParseWithOptionGroup() throws Exception {
        String[] args = new String[] {"--foo", "--bar"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.addOption(new Option("a", true, "A"));
        group.addOption(new Option("b", true, "B"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, false);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
    }

    @org.junit.Test
    public void testParseWithValueSeparator() throws Exception {
        String[] args = new String[] {"--separator", "-s", "1,2,3"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("separator", true, "separator");
        options.addOption("s", true, "s");

        CommandLine commandLine = parser.parse(options, args, false);

        String[] parsedValues = commandLine.getOptionValues("separator");
        assertArrayEquals(new String[] {"1", "2", "3"}, parsedValues);
    }

    @org.junit.Test
    public void testParseWithLongOption() throws Exception {
        String[] args = new String[] {"--long-option", "value"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longOption", true, "longOption");

        CommandLine commandLine = parser.parse(options, args, false);

        assertTrue(commandLine.hasOption("longOption"));
        assertSame("value", commandLine.getOptionValue("longOption"));
    }
}