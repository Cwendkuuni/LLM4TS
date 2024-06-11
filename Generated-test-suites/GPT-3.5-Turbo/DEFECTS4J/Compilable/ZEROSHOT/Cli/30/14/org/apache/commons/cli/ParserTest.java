package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
        Option option1 = new Option("h", "help", false, "print help");
        Option option2 = new Option("v", "version", false, "print version");
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testParse() {
        String[] args = {"-h", "file.txt"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertEquals("file.txt", cmd.getArgList().get(0));
    }

    @Test
    public void testParseWithProperties() {
        String[] args = {"-h"};
        Properties properties = new Properties();
        properties.setProperty("version", "true");
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, properties);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("version"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        String[] args = {"-h", "file.txt", "command"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertEquals("file.txt", cmd.getArgList().get(0));
        assertEquals("command", cmd.getArgList().get(1));
    }

    @Test
    public void testParseWithPropertiesAndStopAtNonOption() {
        String[] args = {"-h", "command"};
        Properties properties = new Properties();
        properties.setProperty("version", "true");
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, properties, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("version"));
        assertEquals("command", cmd.getArgList().get(0));
    }

    @Test
    public void testCheckRequiredOptions() {
        Option option = new Option("r", "required", true, "required option");
        option.setRequired(true);
        options.addOption(option);
        String[] args = {"-h"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: r", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return null;
        }
    }
}