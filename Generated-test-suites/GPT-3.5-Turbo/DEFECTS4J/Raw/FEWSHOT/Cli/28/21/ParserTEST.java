package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Options options = new Options();
        
        Option option1 = new Option("f", "foo", false, "Optional option");
        options.addOption(option1);
        
        Option option2 = new Option("b", "bar", true, "Required option");
        option2.setRequired(true);
        options.addOption(option2);
        
        Option option3 = new Option("n", "name", true, "Optional option with argument");
        option3.setArgs(1);
        options.addOption(option3);
        
        String[] args1 = {"-f", "-b", "value"};
        CommandLine cmd1 = Parser.parse(options, args1);
        assertTrue(cmd1.hasOption("f"));
        assertTrue(cmd1.hasOption("b"));
        assertEquals("value", cmd1.getOptionValue("b"));
        
        String[] args2 = {"-f", "-b"};
        try {
            CommandLine cmd2 = Parser.parse(options, args2);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertTrue(e.getMissingOptions().contains("bar"));
        }
        
        String[] args3 = {"-f", "-b", "value", "-n", "name"};
        CommandLine cmd3 = Parser.parse(options, args3);
        assertTrue(cmd3.hasOption("f"));
        assertTrue(cmd3.hasOption("b"));
        assertEquals("value", cmd3.getOptionValue("b"));
        assertTrue(cmd3.hasOption("n"));
        assertEquals("name", cmd3.getOptionValue("n"));
        
        String[] args4 = {"-f", "-b", "value", "-n"};
        try {
            CommandLine cmd4 = Parser.parse(options, args4);
            fail("MissingArgumentException should be thrown");
        } catch (MissingArgumentException e) {
            assertTrue(e.getOption().equals(option3));
        }
    }
}