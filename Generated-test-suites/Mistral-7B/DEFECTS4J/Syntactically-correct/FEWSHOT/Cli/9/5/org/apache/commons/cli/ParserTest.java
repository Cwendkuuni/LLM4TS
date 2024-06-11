package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsAndArguments() {
        Parser parser = new GnuParser();
        Options options = new Options();

        CommandLine cmd = parser.parse(options, new String[0]);

        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        Parser parser = new GnuParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "arg", true, "Argument");
        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[] { "-o", "value1", "-a", "value2" };

        CommandLine cmd = parser.parse(options, args);

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        List<String> expectedArgs = Arrays.asList("value1", "value2");

        assertEquals(expectedOptions, cmd.getOptions());
        assertEquals(expectedArgs, cmd.getArgs());
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() {
        Parser parser = new GnuParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "arg", true, "Argument");
        options.addOption(option1);
        Option option3 = new Option("h", "help", false, "Print this help message.");
        options.addOption(option2);

        String[] args = new String[] { "--option1", "value1", "arg1", "--help" };

        CommandLine cmd = parser.parse(options, args);

        List<Option> expectedOptions = Arrays.asList(option1, option3);
        List<String> expectedArgs = Arrays.asList("value1", "arg1");

        assertEquals(expectedOptions, cmd.getOptions());
        assertEquals(expectedArgs, cmd.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Parser parser = new GnuParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("a", "arg", true, "Argument");
        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[] { "-a", "arg1", "arg2", "-o", "value1" };

        CommandLine cmd = parser.parse(options, args, true);

        List<Option> expectedOptions = Collections.emptyList();
        List<String> expectedArgs = Arrays.asList("arg1", "arg2");

        assertEquals(expectedOptions, cmd.getOptions());
        assertEquals(expectedArgs, cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() {
        Parser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        options.addOption(option1);

        Properties properties = new Properties();
        properties.setProperty("option1", "value1");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        List<Option> expectedOptions = Arrays.asList(option1);
        List<String> expectedArgs = Collections.emptyList();

        assertEquals(expectedOptions, cmd.getOptions());
        assertEquals(expectedArgs, cmd.getArgs());
        assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() {
        Parser parser = new GnuParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("r", "requiredOption", true, "Required Option");
        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[] { "-o", "value1" };

        CommandLine cmd = parser.parse(options, args);

        List<Option> expectedOptions = Arrays.asList(option1, option2);
        List<String> expectedArgs = Collections.emptyList();

        assertFalse(cmd.getOptions().equals(expectedOptions));
        assertArrayEquals(new String[0], cmd.getArgs().toArray(new String[0]));

        try {
            parser.parse(options, args);
            fail();
        } catch (MissingOptionException e) {
            // Expected
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() {
        Parser parser = new GnuParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        options.addOption(option1);

        String[] args = new String[] { "--unrecognizedOption" };

        parser.parse(options, args);

        List<Option> expectedOptions = Collections.emptyList();
        List<String> expectedArgs = Collections.emptyList();

        assertFalse(cmd.getOptions().equals(expectedOptions));
        assertArrayEquals(new String[0], cmd.getArgs().toArray(new String[0]));

        try {
            parser.parse(options, args);
            fail();
        } catch (UnrecognizedOptionException e) {
            // Expected
        }
    }
}