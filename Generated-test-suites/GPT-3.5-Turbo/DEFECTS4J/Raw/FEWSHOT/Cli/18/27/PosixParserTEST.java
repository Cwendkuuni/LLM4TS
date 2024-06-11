package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("v", "verbose", false, "enable verbose mode");
        Option option3 = new Option("o", "output", true, "output file");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        PosixParser parser = new PosixParser();
        
        String[] arguments1 = {"-f"};
        String[] expected1 = {"-f"};
        String[] actual1 = parser.flatten(options, arguments1, true);
        assertArrayEquals(expected1, actual1);
        
        String[] arguments2 = {"-v"};
        String[] expected2 = {"-v"};
        String[] actual2 = parser.flatten(options, arguments2, false);
        assertArrayEquals(expected2, actual2);
        
        String[] arguments3 = {"-f", "-o", "output.txt"};
        String[] expected3 = {"-f", "-o", "output.txt"};
        String[] actual3 = parser.flatten(options, arguments3, true);
        assertArrayEquals(expected3, actual3);
        
        String[] arguments4 = {"--file"};
        String[] expected4 = {"--file"};
        String[] actual4 = parser.flatten(options, arguments4, false);
        assertArrayEquals(expected4, actual4);
        
        String[] arguments5 = {"--file", "input.txt"};
        String[] expected5 = {"--file", "input.txt"};
        String[] actual5 = parser.flatten(options, arguments5, true);
        assertArrayEquals(expected5, actual5);
        
        String[] arguments6 = {"--output", "output.txt", "-v"};
        String[] expected6 = {"--output", "output.txt", "-v"};
        String[] actual6 = parser.flatten(options, arguments6, false);
        assertArrayEquals(expected6, actual6);
        
        String[] arguments7 = {"-f", "-o", "output.txt", "--verbose", "input.txt"};
        String[] expected7 = {"-f", "-o", "output.txt", "--verbose", "input.txt"};
        String[] actual7 = parser.flatten(options, arguments7, true);
        assertArrayEquals(expected7, actual7);
        
        String[] arguments8 = {"input.txt"};
        String[] expected8 = {"input.txt"};
        String[] actual8 = parser.flatten(options, arguments8, false);
        assertArrayEquals(expected8, actual8);
    }
}