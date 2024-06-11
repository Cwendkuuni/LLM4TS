package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB", "--optionC=valueC", "valueD"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");
        options.addOption("c", true, "Option C with value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(3, cmd.getArgs().length);
        assertEquals("valueD", cmd.getArgs()[0]);
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("valueC", cmd.getOptionValue("c"));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("d"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--optionA=valueA"};

        Options options = new Options();
        Option optionA = new Option("a", true, "Option A with value");
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, new Properties());

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB", "--optionC=valueC", "valueD", "nonOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");
        options.addOption("c", true, "Option C with value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false, true);

        assertEquals(4, cmd.getArgs().length);
        assertEquals("valueD", cmd.getArgs()[0]);
        assertEquals("nonOption", cmd.getArgs()[1]);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = new String[]{"--optionA=valueA"};

        Options options = new Options();
        Option optionA = new Option("a", true, "Option A with value").isRequired(true);

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));

        options.addOption(optionA);
        cmd = parser.parse(options, args, new Properties());

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        String[] args = new String[]{"--groupA", "--optionB=valueB", "--groupA", "--optionC=valueC"};

        Options options = new Options();
        OptionGroup groupA = new OptionGroup("groupA", "Group A");

        Option optionB = new Option("b", true, "Option B with value");
        groupA.addOption(optionB);

        Option optionC = new Option("c", true, "Option C with value");
        groupA.addOption(optionC);

        options.addOptionGroup(groupA);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertSame(groupA, cmd.getOptionGroup("groupA"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = new String[]{"--invalidOption"};

        Options options = new Options();

        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = new String[]{"-a", "--optionB"};

        Options options = new Options();
        Option optionB = new Option("b", true, "Option B with value").hasArg(true);

        Parser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, false));
    }
}