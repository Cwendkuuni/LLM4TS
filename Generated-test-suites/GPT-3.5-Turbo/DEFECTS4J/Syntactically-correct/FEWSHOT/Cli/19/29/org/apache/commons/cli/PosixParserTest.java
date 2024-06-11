package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Create test options
        Options options = new Options();
        Option option1 = Option.builder("f").longOpt("file").argName("file").hasArg().desc("Input file").build();
        Option option2 = Option.builder("x").longOpt("execute").argName("command").hasArg().desc("Execute command").build();
        options.addOption(option1);
        options.addOption(option2);

        // Test with single option and argument
        String[] arguments1 = {"-f", "input.txt"};
        String[] expected1 = {"-f", "input.txt"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));

        // Test with multiple options and arguments
        String[] arguments2 = {"-f", "input.txt", "-x", "command"};
        String[] expected2 = {"-f", "input.txt", "-x", "command"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, false));

        // Test with stopAtNonOption set to true
        String[] arguments3 = {"-f", "input.txt", "nonoption"};
        String[] expected3 = {"-f", "input.txt", "nonoption"};
        assertArrayEquals(expected3, new PosixParser().flatten(options, arguments3, true));

        // Test with stopAtNonOption set to true and invalid option
        String[] arguments4 = {"-f", "input.txt", "-x", "command", "nonoption"};
        String[] expected4 = {"-f", "input.txt", "-x", "command", "nonoption"};
        assertArrayEquals(expected4, new PosixParser().flatten(options, arguments4, true));

        // Test with invalid option and remaining tokens
        String[] arguments5 = {"invalid", "-f", "input.txt"};
        String[] expected5 = {"invalid", "-f", "input.txt"};
        assertArrayEquals(expected5, new PosixParser().flatten(options, arguments5, false));

        // Test with special tokens "--" and "="
        String[] arguments6 = {"--file=input.txt"};
        String[] expected6 = {"--file", "input.txt"};
        assertArrayEquals(expected6, new PosixParser().flatten(options, arguments6, false));

        // Test with special token "-"
        String[] arguments7 = {"-"};
        String[] expected7 = {"-"};
        assertArrayEquals(expected7, new PosixParser().flatten(options, arguments7, false));
    }
}