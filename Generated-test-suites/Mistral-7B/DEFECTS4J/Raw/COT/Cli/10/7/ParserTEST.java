package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertNotNull(parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        options.addRequiredOption("a", "optionA", false, "Option A");
        parser.setOptions(options);
        List<Option> requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals("optionA", requiredOptions.get(0).getLongOpt());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullOptions() {
        Parser parser = new DefaultParser();
        parser.parse(null, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
        assertNull(parser.cmd);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"invalidOption", "argument"};
        parser.parse(options, arguments);
        assertNull(parser.cmd);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--unrecognizedOption"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addRequiredOption("a", "optionA", false, "Option A");
        String[] arguments = {"--optionB"};
        parser.parse(options, arguments);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseNoSuchOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("a", "optionA", false, "Option A");
        String[] arguments = {"--optionB"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseValidArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("a", "optionA", false, "Option A");
        String[] arguments = {"--optionA"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertEquals(1, commandLine.getArgs().length);
        assertEquals("optionA", commandLine.getOptionValue("optionA"));
    }

    @Test
    public void testParseArgumentsWithValues() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        String[] arguments = {"--optionA", "valueA", "--optionB", "valueB"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertEquals(2, commandLine.getArgs().length);
        assertEquals("valueA", commandLine.getOptionValue("optionA"));
        assertEquals("valueB", commandLine.getOptionValue("optionB"));
    }

    @Test
    public void testParseProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        parser.parse(options, null, properties);
        assertEquals(1, parser.cmd.getOptions().size());
        Option optionA = parser.cmd.getOption("optionA");
        assertNotNull(optionA);
        assertEquals("valueA", optionA.getValue());
    }

    @Test
    public void testParseMultipleProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");
        parser.parse(options, null, properties);
        assertEquals(2, parser.cmd.getOptions().size());
        Option optionA = parser.cmd.getOption("optionA");
        assertNotNull(optionA);
        assertEquals("valueA", optionA.getValue());
        Option optionB = parser.cmd.getOption("optionB");
        assertNotNull(optionB);
        assertEquals("valueB", optionB.getValue());
    }

    @Test
    public void testParseMultiplePropertiesWithValues() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");
        options.addOption("c", "optionC", true, "Option C");
        parser.parse(options, null, properties);
        assertEquals(3, parser.cmd.getOptions().size());
        Option optionA = parser.cmd.getOption("optionA");
        assertNotNull(optionA);
        assertEquals("valueA", optionA.getValue());
        Option optionB = parser.cmd.getOption("optionB");
        assertNotNull(optionB);
        assertEquals("valueB", optionB.getValue());
        Option optionC = parser.cmd.getOption("optionC");
        assertNotNull(optionC);
        assertEquals(null, optionC.getValue());
    }
}