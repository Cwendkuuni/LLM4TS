package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {
    private static Parser parser;
    private static Option option1;
    private static Option option2;
    private static Option option3;
    
    @BeforeClass
    public static void setUp() {
        parser = new DefaultParser();
        option1 = Option.builder("a").build();
        option2 = Option.builder("b").hasArg().build();
        option3 = Option.builder("c").build();
    }
    
    @Test
    public void testParseWithOptions() throws ParseException {
        String[] args = {"-a", "-b", "value", "-c"};
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption(option1.getOpt()));
        assertTrue(cmd.hasOption(option2.getOpt()));
        assertTrue(cmd.hasOption(option3.getOpt()));
        assertNull(cmd.getOptionValue(option2.getOpt()));
    }
    
    @Test
    public void testParseWithArgumentsOnly() throws ParseException {
        String[] args = {"argument1", "argument2"};
        Options options = new Options();
        
        CommandLine cmd = parser.parse(options, args);
        
        assertFalse(cmd.hasOption(option1.getOpt()));
        assertFalse(cmd.hasOption(option2.getOpt()));
        assertFalse(cmd.hasOption(option3.getOpt()));
        assertEquals(2, cmd.getArgs().length);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"-d"};
        Options options = new Options();
        
        parser.parse(options, args);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = {"-b"};
        Options options = new Options();
        options.addOption(option2);
        
        parser.parse(options, args);
    }
}