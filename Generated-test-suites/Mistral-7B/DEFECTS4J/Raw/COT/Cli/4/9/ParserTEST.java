package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseExceptionHR;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParse() throws Exception {
        // Given
        String[] args = {"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        assertNotNull(commandLine);
        assertEquals(2, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("1", commandLine.getOptionValue("a"));
        assertEquals("2", commandLine.getOptionValue("b"));
    }

    @Test(expected = ParseExceptionHR.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        // Given
        String[] args = {"--unknownOption", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("Expected ParseExceptionHR but it didn't occur");
    }

    @Test(expected = ParseExceptionHR.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        // Given
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredB");

        CommandLineParser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("Expected ParseExceptionHR but it didn't occur");
    }

    @Test(expected = ParseExceptionHR.class)
    public void testParseWithMissingOptionalOption() throws Exception {
        // Given
        String[] args = {"--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("Expected ParseExceptionHR but it didn't occur");
    }

    @Test(expected = ParseExceptionHR.class)
    public void testParseWithInvalidOptionValue() throws Exception {
        // Given
        String[] args = {"-a", "invalidValue"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("Expected ParseExceptionHR but it didn't occur");
    }

    @Test
    public void testParseWithProperties() throws Exception {
        // Given
        String[] args = {"--optionA=valueA", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.put("optionA", "valueA");
        properties.put("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, properties, false);

        // Then
        assertNotNull(commandLine);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("valueA", commandLine.getOptionValue("a"));
        assertEquals("valueB", commandLine.getOptionValue("b"));
    }

}