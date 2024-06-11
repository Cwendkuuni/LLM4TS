package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B with argument"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "argument"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("argument", result[2]);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B with argument"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "argument", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B with argument"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"--a", "--b=argument"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--a", result[0]);
        assertEquals("--b", result[1]);
    }
    
    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B with argument"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlattenUnknownOption() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-b"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }
    
    @Test
    public void testFlattenBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }
    
    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));

        PosixParser parser = new PosixParser();
        String[] arguments = {"-abc", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }
}