package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "");
        options.addOption("o", true, "");
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", true, "");

        String[] args1 = {"-f", "-o", "file.txt"};
        String[] expected1 = {"-f", "-o", "file.txt"};
        assertEquals(expected1, PosixParser.flatten(options, args1, false));

        String[] args2 = {"-f", "-abc", "-o", "file.txt"};
        String[] expected2 = {"-f", "-a", "-b", "-c", "-o", "file.txt"};
        assertEquals(expected2, PosixParser.flatten(options, args2, false));

        String[] args3 = {"-f", "-o", "file.txt", "arg1", "arg2"};
        String[] expected3 = {"-f", "-o", "file.txt", "arg1", "arg2"};
        assertEquals(expected3, PosixParser.flatten(options, args3, false));

        String[] args4 = {"-f", "-a", "-o", "file.txt", "arg1", "arg2"};
        String[] expected4 = {"-f", "-a", "-o", "file.txt", "arg1", "arg2"};
        assertEquals(expected4, PosixParser.flatten(options, args4, false));

        String[] args5 = {"-f", "-a", "-b", "-c", "value"};
        String[] expected5 = {"-f", "-a", "-b", "-c", "value"};
        assertEquals(expected5, PosixParser.flatten(options, args5, false));

        String[] args6 = {"-f", "-a", "-o"};
        String[] expected6 = {"-f", "-a", "-o"};
        assertEquals(expected6, PosixParser.flatten(options, args6, false));

        String[] args7 = {"-f", "-o", "file.txt", "-a", "--", "arg1", "arg2"};
        String[] expected7 = {"-f", "-o", "file.txt", "-a", "--", "arg1", "arg2"};
        assertEquals(expected7, PosixParser.flatten(options, args7, true));

        String[] args8 = {"-f", "-o", "file.txt", "-a", "arg1", "arg2"};
        String[] expected8 = {"-f", "-o", "file.txt", "-a", "arg1", "arg2"};
        assertEquals(expected8, PosixParser.flatten(options, args8, true));

        String[] args9 = {"-f", "-a", "-b", "-c"};
        String[] expected9 = {"-f", "-a", "-b", "-c"};
        assertEquals(expected9, PosixParser.flatten(options, args9, true));

        String[] args10 = {"-f", "arg1", "-o"};
        String[] expected10 = {"-f", "arg1", "-o"};
        assertEquals(expected10, PosixParser.flatten(options, args10, true));

        String[] args11 = {"-f", "arg1", "arg2"};
        String[] expected11 = {"-f", "arg1", "arg2"};
        assertEquals(expected11, PosixParser.flatten(options, args11, true));
    }
}