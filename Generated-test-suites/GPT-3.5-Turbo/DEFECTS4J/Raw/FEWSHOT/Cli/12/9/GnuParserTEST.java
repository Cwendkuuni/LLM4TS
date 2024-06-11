package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        
        Option option1 = new Option("f", "option1", false, "Option 1");
        options.addOption(option1);
        
        Option option2 = new Option("o", "option2", true, "Option 2");
        options.addOption(option2);
        
        Option option3 = new Option("D", "option3", true, "Option 3");
        options.addOption(option3);
        
        Option option4 = new Option(null, "option4", false, "Option 4");
        options.addOption(option4);
        
        Option option5 = new Option("a", "option5", false, "Option 5");
        options.addOption(option5);
        
        Option option6 = new Option("b", "option6", false, "Option 6");
        options.addOption(option6);
        
        Option option7 = new Option("c", "option7", true, "Option 7");
        options.addOption(option7);
        
        String[] arguments1 = {"-f", "-o", "value1", "argument1"};
        String[] expected1 = {"-f", "-o", "value1", "argument1"};
        
        String[] arguments2 = {"--option2=value2", "argument2"};
        String[] expected2 = {"--option2", "value2", "argument2"};
        
        String[] arguments3 = {"--option3=value3", "argument3"};
        String[] expected3 = {"-D", "option3=value3", "argument3"};
        
        String[] arguments4 = {"--option4", "argument4"};
        String[] expected4 = {"--option4", "argument4"};
        
        String[] arguments5 = {"-abc", "argument5"};
        String[] expected5 = {"-a", "-b", "-c", "argument5"};
        
        String[] arguments6 = {"--", "argument6"};
        String[] expected6 = {"--", "argument6"};
        
        GnuParser parser = new GnuParser();
        
        String[] result1 = parser.flatten(options, arguments1, false);
        assertEquals(expected1, result1);
        
        String[] result2 = parser.flatten(options, arguments2, false);
        assertEquals(expected2, result2);
        
        String[] result3 = parser.flatten(options, arguments3, false);
        assertEquals(expected3, result3);
        
        String[] result4 = parser.flatten(options, arguments4, false);
        assertEquals(expected4, result4);
        
        String[] result5 = parser.flatten(options, arguments5, false);
        assertEquals(expected5, result5);
        
        String[] result6 = parser.flatten(options, arguments6, false);
        assertEquals(expected6, result6);
    }
}