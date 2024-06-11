
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b with argument");
        options.addOption("c", true, "option c with argument");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments1 = new String[]{"-a", "-b", "arg1", "arg2", "-c", "arg3"};
        String[] result1 = parser.flatten(options, arguments1, false);
        String[] expected1 = new String[]{"-a", "-b", "arg1", "arg2", "-c", "arg3"};
        assertArrayEquals(expected1, result1);
        
        String[] arguments2 = new String[]{"-a", "-b"};
        String[] result2 = parser.flatten(options, arguments2, false);
        String[] expected2 = new String[]{"-a", "-b"};
        assertArrayEquals(expected2, result2);
        
        String[] arguments3 = new String[]{"arg1", "-a", "-b", "arg2"};
        String[] result3 = parser.flatten(options, arguments3, true);
        String[] expected3 = new String[]{"--", "arg1", "-a", "-b", "arg2"};
        assertArrayEquals(expected3, result3);
    }
    
    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
