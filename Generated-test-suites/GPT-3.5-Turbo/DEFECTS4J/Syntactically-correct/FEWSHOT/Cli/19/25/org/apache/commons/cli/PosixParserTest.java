package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        String[] args = new String[] { "-a", "value", "-b", "arg", "non-option" };
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("non-option", result[2]);
    }
    
    @Test
    public void testFlatten_noStopAtNonOption() {
        String[] args = new String[] { "-a", "value", "-b", "arg", "non-option" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg", result[3]);
        assertEquals("non-option", result[4]);
    }
    
    @Test
    public void testFlatten_multipleOptions() {
        String[] args = new String[] { "-a", "-b", "arg" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg", result[2]);
    }
    
    @Test
    public void testFlatten_noOptionValue() {
        String[] args = new String[] { "-a", "-b" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlatten_specialToken() {
        String[] args = new String[] { "--foo", "--bar=baz" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar=baz", result[1]);
    }
    
    @Test
    public void testFlatten_singleHyphen() {
        String[] args = new String[] { "-" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_invalidOption() {
        String[] args = new String[] { "-c", "value" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_noOption() {
        String[] args = new String[] { "value" };
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
}