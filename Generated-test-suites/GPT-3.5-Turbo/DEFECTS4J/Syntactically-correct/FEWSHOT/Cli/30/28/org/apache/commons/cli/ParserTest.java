package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("foo").argName("val").hasArg().build());
        
        Parser parser = new DefaultParser();
        
        // Test case where no options are provided
        String[] args = {};
        CommandLine result = parser.parse(options, args);
        assertNull(result.getOptionValue("f"));
        assertNull(result.getOptionValue("foo"));
        
        // Test case where only short option is provided
        args = new String[] {"-f"};
        result = parser.parse(options, args);
        assertEquals("", result.getOptionValue("f"));
        assertNull(result.getOptionValue("foo"));
        
        // Test case where only long option is provided
        args = new String[] {"--foo", "bar"};
        result = parser.parse(options, args);
        assertNull(result.getOptionValue("f"));
        assertEquals("bar", result.getOptionValue("foo"));
        
        // Test case where both short and long options are provided
        args = new String[] {"-f", "--foo", "bar"};
        result = parser.parse(options, args);
        assertEquals("", result.getOptionValue("f"));
        assertEquals("bar", result.getOptionValue("foo"));
        
        // Test case where option value is not provided
        args = new String[] {"--foo"};
        try {
            parser.parse(options, args);
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: --foo", e.getMessage());
        }
        
        // Test case with properties
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        args = new String[] {};
        result = parser.parse(options, args, properties);
        assertNull(result.getOptionValue("f"));
        assertEquals("bar", result.getOptionValue("foo"));
    }

}