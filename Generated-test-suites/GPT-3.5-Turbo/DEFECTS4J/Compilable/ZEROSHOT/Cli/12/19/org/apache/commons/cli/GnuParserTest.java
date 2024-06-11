package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        // add options to test
        options.addOption("h", false, "Print help");
        options.addOption("v", false, "Enable verbose mode");
        options.addOption("f", true, "Specify file");
        
        // test case with no arguments
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
        
        // test case with only options
        arguments = new String[] {"-h", "-v"};
        result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
        assertEquals("-v", result[1]);

        // test case with option and non-option
        arguments = new String[] {"-h", "file.txt"};
        result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
        assertEquals("file.txt", result[1]);
        
        // test case with stopAtNonOption set to true
        arguments = new String[] {"-h", "file.txt"};
        result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("-h", result[0]);

        // test case with -- argument
        arguments = new String[] {"--", "-h", "file.txt"};
        result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-h", result[1]);
        assertEquals("file.txt", result[2]);
    }
}