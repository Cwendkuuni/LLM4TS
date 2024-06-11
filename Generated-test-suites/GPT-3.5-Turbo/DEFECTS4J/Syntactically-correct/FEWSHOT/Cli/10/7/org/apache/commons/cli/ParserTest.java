
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = { "-f", "file.txt", "--output", "results.txt" };
        boolean stopAtNonOption = false;
        Parser parser = new DefaultParser();
        CommandLine cmd = new CommandLine();
        cmd.addArg("-f");
        cmd.addArg("file.txt");
        cmd.addArg("--output");
        cmd.addArg("results.txt");
        assertEquals(cmd.getArgs(), Arrays.asList(parser.flatten(options, arguments, stopAtNonOption)));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "file option");
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");
        String[] arguments = { "--input", "input.txt" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("input"));
        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "file option");
        String[] arguments = { "--input", "input.txt" };
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "file option");
        options.addOption("o", true, "output option");
        options.addOption("a", false, "additional option");
        String[] arguments = { "-f", "file.txt", "nonOption", "-o", "output.txt" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertFalse(cmd.hasOption("nonOption"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("additional"));
    }

    @Test
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("f", true, "file option");
        String[] arguments = { "-f" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getOptionValue("f"));
    }
}
