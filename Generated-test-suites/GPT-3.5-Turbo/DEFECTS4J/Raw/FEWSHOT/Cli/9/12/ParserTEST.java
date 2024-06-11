package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Test case 1: No options or arguments
        String[] args1 = {};
        Options options1 = new Options();
        CommandLine expectedOutput1 = new CommandLine();
        try {
            Parser parser = new DefaultParser();
            CommandLine actualOutput1 = parser.parse(options1, args1);
            assertEquals(expectedOutput1, actualOutput1);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: One option with one argument
        String[] args2 = {"-a", "value"};
        Options options2 = new Options();
        Option option2 = new Option("a", true, "option a");
        options2.addOption(option2);
        CommandLine expectedOutput2 = new CommandLine();
        expectedOutput2.addOption(option2);
        expectedOutput2.getOptionValue("a", "value");
        try {
            Parser parser = new DefaultParser();
            CommandLine actualOutput2 = parser.parse(options2, args2);
            assertEquals(expectedOutput2, actualOutput2);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 3: One option without an argument
        String[] args3 = {"-b"};
        Options options3 = new Options();
        Option option3 = new Option("b", false, "option b");
        options3.addOption(option3);
        CommandLine expectedOutput3 = new CommandLine();
        expectedOutput3.addOption(option3);
        try {
            Parser parser = new DefaultParser();
            CommandLine actualOutput3 = parser.parse(options3, args3);
            assertEquals(expectedOutput3, actualOutput3);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 4: Multiple options with arguments
        String[] args4 = {"-a", "value1", "-b", "value2"};
        Options options4 = new Options();
        Option option4a = new Option("a", true, "option a");
        Option option4b = new Option("b", true, "option b");
        options4.addOption(option4a);
        options4.addOption(option4b);
        CommandLine expectedOutput4 = new CommandLine();
        expectedOutput4.addOption(option4a);
        expectedOutput4.addOption(option4b);
        expectedOutput4.getOptionValue("a", "value1");
        expectedOutput4.getOptionValue("b", "value2");
        try {
            Parser parser = new DefaultParser();
            CommandLine actualOutput4 = parser.parse(options4, args4);
            assertEquals(expectedOutput4, actualOutput4);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}