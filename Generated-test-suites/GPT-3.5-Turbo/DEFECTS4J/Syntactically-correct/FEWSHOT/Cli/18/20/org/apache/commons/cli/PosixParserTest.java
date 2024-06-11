package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        
        Option option1 = new Option("a", false, "option a");
        options.addOption(option1);
        
        Option option2 = new Option("b", true, "option b");
        options.addOption(option2);
        
        Option option3 = new Option("c", false, "option c");
        options.addOption(option3);
        
        String[] arguments1 = new String[]{"-a"};
        String[] expectedOutput1 = new String[]{"-a"};
        assertArrayEquals(expectedOutput1, PosixParser.flatten(options, arguments1, false));
        assertArrayEquals(expectedOutput1, PosixParser.flatten(options, arguments1, true));
        
        String[] arguments2 = new String[]{"-b", "value"};
        String[] expectedOutput2 = new String[]{"-b", "value"};
        assertArrayEquals(expectedOutput2, PosixParser.flatten(options, arguments2, false));
        assertArrayEquals(expectedOutput2, PosixParser.flatten(options, arguments2, true));
        
        String[] arguments3 = new String[]{"-c", "-a"};
        String[] expectedOutput3 = new String[]{"-c", "-a"};
        assertArrayEquals(expectedOutput3, PosixParser.flatten(options, arguments3, false));
        assertArrayEquals(expectedOutput3, PosixParser.flatten(options, arguments3, true));
        
        String[] arguments4 = new String[]{"-d"};
        String[] expectedOutput4 = new String[]{"-d"};
        assertArrayEquals(expectedOutput4, PosixParser.flatten(options, arguments4, false));
        assertArrayEquals(expectedOutput4, PosixParser.flatten(options, arguments4, true));
        
        String[] arguments5 = new String[]{"-abc", "-d"};
        String[] expectedOutput5 = new String[]{"-a", "-b", "-c", "-d"};
        assertArrayEquals(expectedOutput5, PosixParser.flatten(options, arguments5, false));
        assertArrayEquals(expectedOutput5, PosixParser.flatten(options, arguments5, true));
    }
    
    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}