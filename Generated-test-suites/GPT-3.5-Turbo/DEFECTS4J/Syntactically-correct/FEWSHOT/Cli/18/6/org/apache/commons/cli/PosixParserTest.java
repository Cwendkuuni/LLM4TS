package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Test option");
        options.addOption("b", true, "Test option with argument");
        
        PosixParser parser = new PosixParser();
        
        // Test case with no options or arguments
        String[] args1 = {};
        String[] expected1 = {};
        assertArrayEquals(expected1, parser.flatten(options, args1, false));
        
        // Test case with single option without argument
        String[] args2 = {"-a"};
        String[] expected2 = {"-a"};
        assertArrayEquals(expected2, parser.flatten(options, args2, false));
        
        // Test case with single option with argument
        String[] args3 = {"-b", "arg"};
        String[] expected3 = {"-b", "arg"};
        assertArrayEquals(expected3, parser.flatten(options, args3, false));
        
        // Test case with multiple options without arguments
        String[] args4 = {"-a", "-a"};
        String[] expected4 = {"-a", "-a"};
        assertArrayEquals(expected4, parser.flatten(options, args4, false));
        
        // Test case with multiple options with arguments
        String[] args5 = {"-b", "arg1", "-b", "arg2"};
        String[] expected5 = {"-b", "arg1", "-b", "arg2"};
        assertArrayEquals(expected5, parser.flatten(options, args5, false));
        
        // Test case with non-option arguments
        String[] args6 = {"arg1", "-a", "arg2"};
        String[] expected6 = {"arg1", "-a", "arg2"};
        assertArrayEquals(expected6, parser.flatten(options, args6, false));
        
        // Test case with stopAtNonOption = true
        String[] args7 = {"arg1", "-a", "arg2"};
        String[] expected7 = {"arg1"};
        assertArrayEquals(expected7, parser.flatten(options, args7, true));
    }
}