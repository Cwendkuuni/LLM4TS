package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addRequiredOption("r", true, "Required Option");
        String[] arguments = {"-a", "--b=2", "--c=5", "-r"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("r"));
        assertFalse(cmd.hasOption("d"));
        assertEquals(2, cmd.getOptionValue("b").length());
        assertEquals(5, cmd.getOptionValue("c").length());
        assertNull(cmd.getOptionValue("d"));
    }

    @Test
    public void testParseWithValidArgumentsAndProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addRequiredOption("r", true, "Required Option");
        Properties properties = new Properties();
        properties.setProperty("a", "Value A");
        properties.setProperty("b", "Value B");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNull(cmd);
        cmd = parser.parse(options, new String[]{"-a", "--b=2", "--c=5", "-r"}, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("r"));
        assertFalse(cmd.hasOption("d"));
        assertEquals("Value A", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("5", cmd.getOptionValue("c"));
        assertNull(cmd.getOptionValue("d"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addRequiredOption("r", true, "Required Option");
        String[] arguments = {"-a", "--b=2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("r"));
        throw new AssertionError("Expected MissingArgumentException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addRequiredOption("r", true, "Required Option");
        String[] arguments = {"-a", "--b=2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.hasOption("r"));
        throw new AssertionError("Expected MissingOptionException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] arguments = {"-d", "--a", "--b=2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.hasOption("d"));
        throw new AssertionError("Expected UnrecognizedOptionException");
    }
}