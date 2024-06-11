package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test for {@link Parser} class using JUnit 4.
 */
public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[] {"--help"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // Check if help is displayed
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        String help = new String(System.out.getBytes());
        assertTrue("Help text was not displayed", help.contains("test"));

        // Check if help is displayed with full option list
        formatter.printHelp("test", options, true);
        help = new String(System.out.getBytes());
        assertTrue("Help text with full option list was not displayed",
                help.contains("--help"));

        // Test parsing with valid options
        options.addOption("a", "testA", false, "Test option A");
        options.addOption("b", "testB", true, "Test option B");
        args = new String[] {"-a", "--testB", "valueB"};
        cmd = parser.parse(options, args, null, false);
        assertTrue("Test option A was not set", cmd.hasOption("a"));
        assertFalse("Test option B was not set", cmd.hasOption("b"));
        assertNull("Test option B value was not null", cmd.getOptionValue("b"));
        assertSame("Test option B value was not equal to 'valueB'", "valueB",
                cmd.getOptionValue("b"));
        assertSame("Test option A value was not equal to null", null,
                cmd.getOptionValue("a"));

        // Test parsing with invalid options
        args = new String[] {"--invalidOption"};
        try {
            cmd = parser.parse(options, args, null, false);
            fail("MissingOptionException was not thrown");
        } catch (MissingOptionException e) {
            // Expected
        }
        args = new String[] {"-invalidOption"};
        try {
            cmd = parser.parse(options, args, null, false);
            fail("UnrecognizedOptionException was not thrown");
        } catch (UnrecognizedOptionException e) {
            // Expected
        }

        // Test parsing with required options
        options.addOption("c", "testC", true, "Test option C");
        args = new String[] {"--testC"};
        try {
            cmd = parser.parse(options, args, null, false);
            fail("MissingOptionException was not thrown");
        } catch (MissingOptionException e) {
            // Expected
        }
        args = new String[] {"--testC", "valueC"};
        cmd = parser.parse(options, args, null, false);
        assertTrue("Test option C was not set", cmd.hasOption("c"));
        assertSame("Test option C value was not equal to 'valueC'", "valueC",
                cmd.getOptionValue("c"));

        // Test parsing with optional arguments
        options.addOption("d", "testD", true, "Test option D with optional "
                + "argument");
        args = new String[] {"-d"};
        cmd = parser.parse(options, args, null, false);
        assertFalse("Test option D value was not null", cmd.getOptionValue("d")
                == null);
        assertNull("Test option D value was not null", cmd.getOptionValue("d"));
        args = new String[] {"-d", "valueD"};
        cmd = parser.parse(options, args, null, false);
        assertSame("Test option D value was not equal to 'valueD'", "valueD",
                cmd.getOptionValue("d"));

        // Test parsing with multiple values for an option
        options.addOption("e", "testE", true, "Test option E with multiple "
                + "values");
        args = new String[] {"-e", "valueE1", "-e", "valueE2"};
        cmd = parser.parse(options, args, null, false);
        List<String> values = Arrays.asList(cmd.getOptionValues("e"));
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("valueE1");
        expectedValues.add("valueE2");
        assertTrue("Test option E values were not equals to expected values",
                values.equals(expectedValues));

        // Test parsing with arguments
        options.addOption("f", "testF", true, "Test option F with argument");
        args = new String[] {"--testF", "valueF"};
        cmd = parser.parse(options, args, null, false);
        assertTrue("Test option F value was not set", cmd.hasOption("f"));
        assertSame("Test option F value was not equal to 'valueF'", "valueF",
                cmd.getOptionValue("f"));
        List<String> argsList = Arrays.asList(cmd.getArgs());
        List<String> expectedArgs = Arrays.asList("valueF");
        assertTrue("Test option F argument was not equals to expected argument",
                argsList.equals(expectedArgs));

        // Test parsing with multiple arguments
        args = new String[] {"--testF", "valueF1", "valueF2"};
        cmd = parser.parse(options, args, null, false);
        argsList = Arrays.asList(cmd.getArgs());
        expectedArgs = Arrays.asList("valueF1", "valueF2");
        assertTrue("Test option F arguments were not equals to expected arguments",
                argsList.equals(expectedArgs));

        // Test parsing with multiple values for an option and arguments
        options.addOption("g", "testG", true, "Test option G with multiple "
                + "values and argument");
        args = new String[] {"-g", "valueG1", "-g", "valueG2", "argG"};
        cmd = parser.parse(options, args, null, false);
        values = Arrays.asList(cmd.getOptionValues("g"));
        expectedValues = new ArrayList<>();
        expectedValues.add("valueG1");
        expectedValues.add("valueG2");
        assertTrue("Test option G values were not equals to expected values",
                values.equals(expectedValues));
        argsList = Arrays.asList(cmd.getArgs());
        expectedArgs = Arrays.asList("argG");
        assertTrue("Test option G arguments were not equals to expected arguments",
                argsList.equals(expectedArgs));

        // Test parsing with multiple options
        options.addOption("h", "testH", false, "Test option H");
        args = new String[] {"-h", "--testH"};
        cmd = parser.parse(options, args, null, false);
        assertTrue("Test option H was not set", cmd.hasOption("h"));
        assertTrue("Test option H was not set", cmd.hasOption("testH"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[] {"--help"};
        Properties properties = new Properties();
        properties.setProperty("testA", "valueA");
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        // Check if help is displayed
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        String help = new String(System.out.getBytes());
        assertTrue("Help text was not displayed", help.contains("test"));

        // Test parsing with valid options from properties
        options.addOption("a", "testA", false, "Test option A");
        args = new String[0];
        cmd = parser.parse(options, args, properties, false);
        assertTrue("Test option A was not set", cmd.hasOption("a"));
        assertSame("Test option A value was not equal to 'valueA'", "valueA",
                cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] args = new String[] {"--testMissingOption"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, null, false);
        fail("MissingOptionException was not thrown");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = new String[] {"--invalidOption"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, null, false);
        fail("UnrecognizedOptionException was not thrown");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = new String[] {"--testC"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, null, false);
        fail("MissingArgumentException was not thrown");
    }
}