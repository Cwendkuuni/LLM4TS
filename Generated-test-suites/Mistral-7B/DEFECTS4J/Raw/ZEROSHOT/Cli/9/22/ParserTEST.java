package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParseWithInputAndOutput() throws Exception {
        String[] args = {"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertFalse(cmd.hasOption("h"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        String[] args = {"-o", "output.txt"};
        parser.parse(options, args); // should throw MissingOptionException
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnknownOption() throws Exception {
        String[] args = {"-x", "unknownOption"};
        parser.parse(options, args); // should throw UnrecognizedOptionException
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionalArgument() throws Exception {
        String[] args = {"-o"};
        parser.parse(options, args); // should throw MissingArgumentException
    }

    @Test
    public void testParseProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");

        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = {"-i", "input.txt", "-", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals(args.length - 2, cmd.getArgs().size());
        assertNotNull(cmd.getArgs().get(0));
        assertNotNull(cmd.getArgs().get(1));
    }
}