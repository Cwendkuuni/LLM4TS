package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] args = {"--", "-a", "value1", "value2"};
        String[] expectedArgs = {"--", "-a", "value1", "value2"};

        String[] result = parser.flatten(options, args, true);
        
        assertArrayEquals(expectedArgs, result);
    }
    
    @Test
    public void testFlatten_handleLongOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"--a", "--b=value"};
        String[] expectedArgs = {"--a", "--b", "value"};

        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expectedArgs, result);
    }
    
    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-"};
        String[] expectedArgs = {"-"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expectedArgs, result);
    }
    
    @Test
    public void testFlatten_singleOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b=value"};
        String[] expectedArgs = {"-a", "-b", "value"};

        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expectedArgs, result);
    }
    
    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-abvalue"};
        String[] expectedArgs = {"-a", "-b", "value"};

        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expectedArgs, result);
    }
    
    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

}