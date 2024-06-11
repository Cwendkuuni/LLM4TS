package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    private PosixParser parser;
    private Options options;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        commandLine = null;
    }

    @Test
    public void testOptionArgsSingleHyphen() throws Exception {
        options.addOption("a", false, "toggle -a");
        options.addOption("b", true, "set -b");

        String[] args = {"-a", "-b", "foo"};

        commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals(commandLine.getOptionValue("b"), "foo");

        assertNull(parser.parse(options, new String[]{"-a"}).getOptionValue("b"));
    }

    @Test
    public void testSimpleLong() throws Exception {
        Option option = new Option("a", "all", false, "select all");

        options.addOption(option);

        String[] args = new String[]{"-a"};

        commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("all"));
        assertNull(commandLine.getOptionValue("all"));
        assertNull(commandLine.getOptionObject("all"));
    }

    @Test
    public void testShortAndLongWithEqual() throws Exception {
        options.addOption("a", true, "value a");

        String[] args = new String[]{"-a=foo"};
        
        commandLine = parser.parse(options, args);
        
        assertTrue(commandLine.hasOption("a"));
        assertEquals(commandLine.getOptionValue("a"), "foo");      
    }

    @Test
    public void testLongWithoutEqualSingleDash() throws Exception {
        String[] args = new String[]{"--file", "foo"};

        Option option = new Option("f", "file", true, "file to store");

        options.addOption(option);

        commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("f"));
        assertEquals(commandLine.getOptionValue("f"), "foo");
    }

    @Test
    public void testLongWithUnexpectedArgument() throws Exception {
        commandLine = parser.parse(options, new String[]{"--file", "foo", "bar"});

        assertTrue(commandLine.hasOption("file"));
        assertEquals(commandLine.getOptionValue("file"), "foo");
    }
}