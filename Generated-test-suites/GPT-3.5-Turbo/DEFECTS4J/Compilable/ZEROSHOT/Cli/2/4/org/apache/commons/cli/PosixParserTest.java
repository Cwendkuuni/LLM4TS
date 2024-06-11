package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        PosixParser parser = new PosixParser();
        
        // Test case 1: empty arguments
        String[] arguments1 = new String[0];
        String[] expected1 = new String[0];
        assertArrayEquals(expected1, parser.flatten(options, arguments1, false));
        
        // Test case 2: arguments without options
        String[] arguments2 = new String[]{"arg1", "arg2"};
        String[] expected2 = new String[]{"arg1", "arg2"};
        assertArrayEquals(expected2, parser.flatten(options, arguments2, false));
        
        // Test case 3: arguments with single hyphen
        String[] arguments3 = new String[]{"-"};
        String[] expected3 = new String[]{"-"};
        assertArrayEquals(expected3, parser.flatten(options, arguments3, false));
        
        // Test case 4: argument "--"
        String[] arguments4 = new String[]{"--"};
        String[] expected4 = new String[]{"--"};
        assertArrayEquals(expected4, parser.flatten(options, arguments4, false));
        
        // Test case 5: single character option without value
        String[] arguments5 = new String[]{"-a"};
        String[] expected5 = new String[]{"-a"};
        assertArrayEquals(expected5, parser.flatten(options, arguments5, false));
        
        // Test case 6: single character option with value
        String[] arguments6 = new String[]{"-b", "value"};
        String[] expected6 = new String[]{"-b", "value"};
        assertArrayEquals(expected6, parser.flatten(options, arguments6, false));
        
        // Test case 7: single character option with value in equals format
        String[] arguments7 = new String[]{"-b=value"};
        String[] expected7 = new String[]{"-b", "value"};
        assertArrayEquals(expected7, parser.flatten(options, arguments7, false));
        
        // Test case 8: single character option with multiple values
        String[] arguments8 = new String[]{"-c", "value1", "value2"};
        String[] expected8 = new String[]{"-c", "value1", "value2"};
        assertArrayEquals(expected8, parser.flatten(options, arguments8, false));
        
        // Test case 9: single character option with multiple values in equals format
        String[] arguments9 = new String[]{"-c=value1,value2"};
        String[] expected9 = new String[]{"-c", "value1,value2"};
        assertArrayEquals(expected9, parser.flatten(options, arguments9, false));
        
        // Test case 10: stopAtNonOption is true, option followed by non-option
        String[] arguments10 = new String[]{"-a", "nonoption"};
        String[] expected10 = new String[]{"-a", "nonoption"};
        assertArrayEquals(expected10, parser.flatten(options, arguments10, true));
        
        // Test case 11: stopAtNonOption is true, non-option followed by option
        String[] arguments11 = new String[]{"nonoption", "-a"};
        String[] expected11 = new String[]{"--", "nonoption", "-a"};
        assertArrayEquals(expected11, parser.flatten(options, arguments11, true));
        
        // Test case 12: non-option followed by option, stopAtNonOption is false
        String[] arguments12 = new String[]{"nonoption", "-a"};
        String[] expected12 = new String[]{"nonoption", "-a"};
        assertArrayEquals(expected12, parser.flatten(options, arguments12, false));
    }
}