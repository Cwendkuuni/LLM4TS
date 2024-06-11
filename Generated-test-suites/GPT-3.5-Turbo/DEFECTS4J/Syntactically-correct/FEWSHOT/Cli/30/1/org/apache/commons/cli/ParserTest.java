
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", true, "Banana option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-a", "-b", "value"};
        
        CommandLine cmd = new Parser().parse(options, arguments);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        
        Option option1 = new Option("a", "apple", true, "Apple option");
        
        options.addOption(option1);
        
        String[] arguments = {};
        
        CommandLine cmd = new Parser().parse(options, arguments);
    }
}
