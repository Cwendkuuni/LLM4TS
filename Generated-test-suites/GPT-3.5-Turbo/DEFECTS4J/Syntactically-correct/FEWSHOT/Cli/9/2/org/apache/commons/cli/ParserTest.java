import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Options options = new Options();
        
        // Set up options
        
        Option opt1 = new Option("f", "foo", false, "Option 1");
        Option opt2 = new Option("b", "bar", true, "Option 2");
        
        options.addOption(opt1);
        options.addOption(opt2);
        
        // Test case 1: Option 1 is present
        String[] args1 = {"-f"};
        CommandLine result1 = Parser.parse(options, args1);
        assertEquals(true, result1.hasOption("f"));
        assertEquals(opt1, result1.getOption("f"));
        assertNull(result1.getOptionValue("f"));
        
        // Test case 2: Option 2 is present with an argument
        String[] args2 = {"-b", "value"};
        CommandLine result2 = Parser.parse(options, args2);
        assertEquals(true, result2.hasOption("b"));
        assertEquals(opt2, result2.getOption("b"));
        assertEquals("value", result2.getOptionValue("b"));
        
        // Test case 3: Option 1 and Option 2 are present with arguments
        String[] args3 = {"-f", "-b", "value"};
        CommandLine result3 = Parser.parse(options, args3);
        assertEquals(true, result3.hasOption("f"));
        assertEquals(opt1, result3.getOption("f"));
        assertEquals(false, result3.hasOption("b"));
        assertEquals(null, result3.getOption("b"));
        assertEquals(null, result3.getOptionValue("b"));
        
        // Test case 4: Option 1 and Option 2 are not present
        String[] args4 = {};
        CommandLine result4 = Parser.parse(options, args4);
        assertEquals(false, result4.hasOption("f"));
        assertEquals(null, result4.getOption("f"));
        assertEquals(null, result4.getOptionValue("f"));
        assertEquals(false, result4.hasOption("b"));
        assertEquals(null, result4.getOption("b"));
        assertEquals(null, result4.getOptionValue("b"));
    }
}