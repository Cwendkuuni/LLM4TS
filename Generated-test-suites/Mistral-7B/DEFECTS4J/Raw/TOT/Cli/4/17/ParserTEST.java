package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-a", "arg1", "--optionB", "valueB"};
        Options options = new Options();
        options.addOption("a", true, "anOption");
        options.addOption("b", true, "anOtherOption")
                .setValueSeparator('=')
                .longOpt("optionB")
                .required(false);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("optionB"));
        assertEquals(1, cmd.getOptionValues("a").length);
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
        assertEquals("valueB", cmd.getOptionValues("optionB")[0]);
        assertNull(cmd.getOptionValues("c"));
        assertNull(cmd.getOptionValues("d"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "optionA")
                .isRequired(true);
        options.addOption("b", true, "optionB")
                .isRequired(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));

        try {
            parser.parse(options, new String[0], true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            parser.parse(options, new String[]{"--optionA"}, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            parser.parse(options, new String[]{"--optionB"}, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithOptionalAndValueArgs() throws ParseException {
        String[] args = {"-a", "value1", "--optionB=value2"};
        Options options = new Options();
        options.addOption("a", true, "anOption")
                .hasArg(true);
        options.addOption("b", true, "anOtherOption")
                .valueSeparator('=')
                .longOpt("optionB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValues("a")[0]);
        assertTrue(cmd.hasOption("optionB"));
        assertEquals("value2", cmd.getOptionValues("optionB")[0]);

        try {
            parser.parse(options, new String[]{"-a"}, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            parser.parse(options, new String[]{"--optionB"}, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            parser.parse(options, new String[]{"--optionB", "value3"}, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}