package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("p", "path", true, "input path");
        Option option3 = new Option("d", "debug", false, "enable debug mode");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOption(option3);
        options.addOptionGroup(group);
    }
    
    @After
    public void tearDown() {
        parser = null;
        options = null;
    }
    
    @Test
    public void testParseWithValidOptions() throws ParseException {
        String[] args = {"-f", "test.txt", "-d"};
        CommandLine cmd = parser.parse(options, args);
        
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("f"));
        assertNotNull(cmd.getOptionValue("file"));
        assertEquals("test.txt", cmd.getOptionValue("file"));
        assertEquals(1, cmd.getOptions().length);
    }
    
    @Test
    public void testParseWithInvalidOptions() throws ParseException {
        String[] args = {"-f", "test.txt", "-w"};
        
        try {
            parser.parse(options, args);
            fail("Should throw UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -w", e.getMessage());
        }
    }
    
    @Test
    public void testParseWithMissingRequiredOptions() throws ParseException {
        String[] args = {"-d"};
        
        try {
            parser.parse(options, args);
            fail("Should throw MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: f", e.getMessage());
        }
    }
    
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"-f", "test.txt", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);
        
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertNull(cmd.getOptionValue("f"));
        assertNotNull(cmd.getOptionValue("file"));
        assertEquals("test.txt", cmd.getOptionValue("file"));
        assertEquals("nonOption", cmd.getArgList().get(0));
        assertEquals(1, cmd.getOptions().length);
    }
}