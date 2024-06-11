package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    
    // Test the parse method with empty arguments
    @Test
    public void testParseEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{});
        
        assertNotNull(result);
    }
    
    // Test the parse method with options and arguments
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("g").hasArg().build());
        
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{"-f", "-g", "value"});
        
        assertTrue(result.hasOption("f"));
        assertTrue(result.hasOption("g"));
        assertEquals("value", result.getOptionValue("g"));
    }
    
    // Test the parse method with multiple options with same key
    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("f").hasArg().build());
        
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{"-f", "-f value"});
        
        assertTrue(result.hasOption("f"));
        assertNull(result.getOptionValue("f"));
        assertEquals("value", result.getOptionValues("f")[0]);
    }
    
    // Test the parse method with required options
    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption(Option.builder("f").build());
        
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{"-f value"});
        
        assertTrue(result.hasOption("f"));
        assertEquals("value", result.getOptionValue("f"));
    }
    
    // Test the parse method with missing required options
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption(Option.builder("f").build());
        
        Parser parser = new DefaultParser();
        
        parser.parse(options, new String[]{});
    }
    
    // Test the parse method with option groups
    @Test
    public void testParseWithOptionGroups() throws ParseException {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder("f").build());
        group.addOption(Option.builder("g").build());
        
        options.addOptionGroup(group);
        
        Parser parser = new DefaultParser();
        
        CommandLine result1 = parser.parse(options, new String[]{"-f"});
        
        assertTrue(result1.hasOption("f"));
        assertFalse(result1.hasOption("g"));
        
        CommandLine result2 = parser.parse(options, new String[]{"-g"});
        
        assertTrue(result2.hasOption("g"));
        assertFalse(result2.hasOption("f"));
    }
    
    // Test the parse method with properties
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        
        Properties properties = new Properties();
        properties.setProperty("f", "value");
        
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{}, properties);
        
        assertTrue(result.hasOption("f"));
        assertEquals("value", result.getOptionValue("f"));
    }
    
    // Test the parse method with stopAtNonOption
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        
        Parser parser = new DefaultParser();
        
        CommandLine result = parser.parse(options, new String[]{"-f", "value", "nonOption"}, true);
        
        assertTrue(result.hasOption("f"));
        assertEquals("value", result.getOptionValue("f"));
        assertEquals("nonOption", result.getArgs()[0]);
    }
    
    // Test the parse method with missing argument
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        
        Parser parser = new DefaultParser();
        
        parser.parse(options, new String[]{"-f"});
    }

}