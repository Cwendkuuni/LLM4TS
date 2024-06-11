package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.ArgList;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Options requiredOptions = new Options();
        requiredOptions.addRequiredOption("option1", true, "o1", "Description1");
        requiredOptions.addRequiredOption("option2", true, "o2", "Description2");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        List<Option> actual = parser.getRequiredOptions();
        List<Option> expected = requiredOptions.getOptions();
        assertSame(expected, actual);
    }

    @Test
    public void testFlatten_EmptyArgumentArray() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = new String[0];
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1", "-o1", "--option2", "-o2", "-a", "arg1"};
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--option1", "--option2", "-a", "arg1"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1", "-o1", "--option2", "-o2", "-a", "arg1"};
        String[] actual = parser.flatten(options, arguments, true);
        String[] expected = {"--option1", "--option2", "-a"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);
        Assert.assertEquals(Collections.emptyList(), commandLine.getArgs());
        Assert.assertEquals(Collections.emptyList(), commandLine.getOptions());
    }

    @Test
    public void testParse_SingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--option1"});
        List<Option> optionsList = commandLine.getOptions();
        List<String> argsList = commandLine.getArgs();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o1", option.getLongOpt());
        assertEquals("Description1", option.getDescription());
        assertEquals(true, option.getValue().getValue());
        assertEquals(Collections.emptyList(), argsList);
    }

    @Test
    public void testParse_SingleValueOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--option1", "value"});
        List<Option> optionsList = commandLine.getOptions();
        List<String> argsList = commandLine.getArgs();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("o1", option.getLongOpt());
        assertEquals("Description1", option.getDescription());
        assertEquals("value", option.getValue().getValue());
        assertEquals(Collections.emptyList(), argsList);
    }

    @Test
    public void testParse_MultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        options.addOption("option2", true, "o2", "Description2");
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--option1", "--option2"});
        List<Option> optionsList = commandLine.getOptions();
        List<String> argsList = commandLine.getArgs();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("o1", option1.getLongOpt());
        assertEquals("Description1", option1.getDescription());
        assertEquals(true, option1.getValue().getValue());
        assertEquals("o2", option2.getLongOpt());
        assertEquals("Description2", option2.getDescription());
        assertEquals(true, option2.getValue().getValue());
        assertEquals(Collections.emptyList(), argsList);
    }

    @Test
    public void testParse_MultipleValuesOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        options.addOption("option2", true, "o2", "Description2");
        options.addOption("option3", true, "o3", "Description3", 1);
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--option1", "value1", "--option1", "value2", "--option3", "value"});
        List<Option> optionsList = commandLine.getOptions();
        List<String> argsList = commandLine.getArgs();
        assertEquals(3, optionsList.size());
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        Option option3 = optionsList.get(2);
        assertEquals("o1", option1.getLongOpt());
        assertEquals("Description1", option1.getDescription());
        ArgList option1Values = (ArgList) option1.getValue();
        assertEquals(2, option1Values.size());
        assertEquals("value1", option1Values.get(0));
        assertEquals("value2", option1Values.get(1));
        assertEquals("o2", option2.getLongOpt());
        assertEquals("Description2", option2.getDescription());
        assertEquals(true, option2.getValue().getValue());
        assertEquals("o3", option3.getLongOpt());
        assertEquals("Description3", option3.getDescription());
        assertEquals(1, option3.getValue().size());
        assertEquals("value", option3.getValue().get(0));
        assertEquals(Collections.emptyList(), argsList);
    }

    @Test
    public void testParse_NonOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--nonOption", "arg"});
        List<Option> optionsList = commandLine.getOptions();
        List<String> argsList = commandLine.getArgs();
        assertEquals(0, optionsList.size());
        assertEquals("arg", argsList.get(0));
        assertEquals("arg", argsList.get(1));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--unrecognizedOption"});
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--option2"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        options.addOption("option2", true, "o2", "Description2", 1);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--option1"});
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Options requiredOptions = new Options();
        requiredOptions.addRequiredOption("option1", true, "o1", "Description1");
        requiredOptions.addRequiredOption("option2", true, "o2", "Description2");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        parser.parse(options, new String[0]);
        assertThrows(MissingOptionException.class, () -> parser.checkRequiredOptions());
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        Options options = new Options();
        options.addOption("option1", true, "o1", "Description1");
        options.addOption("option2", true, "o2", "Description2");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0], properties);
        List<Option> optionsList = parser.cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);
        assertEquals("o1", option1.getLongOpt());
        assertEquals("Description1", option1.getDescription());
        assertEquals("value1", option1.getValue().getValue());
        assertEquals("o2", option2.getLongOpt());
        assertEquals("Description2", option2.getDescription());
        assertEquals("value2", option2.getValue().getValue());
    }
}