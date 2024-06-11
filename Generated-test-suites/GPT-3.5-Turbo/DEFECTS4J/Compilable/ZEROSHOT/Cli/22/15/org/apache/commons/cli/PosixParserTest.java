package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case 1
        
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b", "--foo", "bar"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("--", result[2]);
        assertEquals("--foo", result[3]);
        assertEquals("bar", result[4]);
        assertEquals(null, result[5]);
        
        // Test case 2
        
        options = new Options();
        arguments = new String[]{"-c", "value"};
        stopAtNonOption = true;
        
        parser = new PosixParser();
        result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
        assertEquals(null, result[2]);
        
        // Test case 3
        
        options = new Options();
        arguments = new String[]{"--", "-d", "value"};
        stopAtNonOption = true;
        
        parser = new PosixParser();
        result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("value", result[2]);
        
        // Test case 4
        
        options = new Options();
        arguments = new String[]{"-e", "value"};
        stopAtNonOption = false;
        
        parser = new PosixParser();
        result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("-e", result[0]);
        assertEquals("value", result[1]);
        assertEquals(null, result[2]);
    }
}