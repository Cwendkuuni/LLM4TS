package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new DefaultParser();
    }
    
    @Test
    public void testParseOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        
        String[] args = {"-a", "-b", "-c"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
    }
    
    @Test
    public void testParseOptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        
        String[] args = {"-a", "value"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }
    
    @Test
    public void testParseOptionWithMultipleArgs() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());
        
        String[] args = {"-a", "value1", "value2", "value3"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValues("a")[0]);
        assertEquals("value2", cmd.getOptionValues("a")[1]);
        assertEquals("value3", cmd.getOptionValues("a")[2]);
    }
    
    @Test
    public void testParseOptionWithArgAndMultipleArgs() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().hasArgs().build());
        
        String[] args = {"-a", "value1", "value2", "value3"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValues("a")[0]);
        assertEquals("value2", cmd.getOptionValues("a")[1]);
        assertEquals("value3", cmd.getOptionValues("a")[2]);
    }
    
    @Test
    public void testParseOptionWithOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasOptionalArg().build());
        
        String[] args1 = {"-a", "value"};
        CommandLine cmd1 = parser.parse(options, args1);
        
        assertTrue(cmd1.hasOption("a"));
        assertEquals("value", cmd1.getOptionValue("a"));
        
        String[] args2 = {"-a"};
        CommandLine cmd2 = parser.parse(options, args2);
        
        assertTrue(cmd2.hasOption("a"));
        assertNull(cmd2.getOptionValue("a"));
    }
    
    @Test
    public void testParseOptionWithArgAndOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().hasOptionalArg().build());
        
        String[] args1 = {"-a", "value1", "value2"};
        CommandLine cmd1 = parser.parse(options, args1);
        
        assertTrue(cmd1.hasOption("a"));
        assertEquals("value1", cmd1.getOptionValue("a"));
        assertNull(cmd1.getOptionValues("a")[1]);
        
        String[] args2 = {"-a", "value1"};
        CommandLine cmd2 = parser.parse(options, args2);
        
        assertTrue(cmd2.hasOption("a"));
        assertEquals("value1", cmd2.getOptionValue("a"));
        assertNull(cmd2.getOptionValues("a")[1]);
        
        String[] args3 = {"-a"};
        CommandLine cmd3 = parser.parse(options, args3);
        
        assertTrue(cmd3.hasOption("a"));
        assertNull(cmd3.getOptionValue("a"));
        assertNull(cmd3.getOptionValues("a")[1]);
    }
}