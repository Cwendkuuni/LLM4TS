package org.apache.commons.cli;
//Test for flatten method

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();

        // case 1: token starts with "--" and has "="
        String[] arguments1 = {"--a=true"};
        String[] expected1 = {"--a", "true"};
        assertEquals(expected1, parser.flatten(options, arguments1, true));

        // case 2: token starts with "--" and has no "="
        String[] arguments2 = {"--a"};
        String[] expected2 = {"--a"};
        assertEquals(expected2, parser.flatten(options, arguments2, true));

        // case 3: token is "-"
        String[] arguments3 = {"-"};
        String[] expected3 = {"-"};
        assertEquals(expected3, parser.flatten(options, arguments3, true));

        // case 4: token starts with "-"
        String[] arguments4 = {"-a"};
        String[] expected4 = {"-a"};
        assertEquals(expected4, parser.flatten(options, arguments4, true));

        // case 5: token is non-option and stopAtNonOption is true
        String[] arguments5 = {"non-option"};
        String[] expected5 = {"non-option"};
        assertEquals(expected5, parser.flatten(options, arguments5, true));

        // case 6: token is non-option and stopAtNonOption is false
        String[] arguments6 = {"non-option"};
        String[] expected6 = {"non-option"};
        assertEquals(expected6, parser.flatten(options, arguments6, false));

        // case 7: token requires bursting
        String[] arguments7 = {"-ab"};
        String[] expected7 = {"-a", "-b"};
        assertEquals(expected7, parser.flatten(options, arguments7, true));
    }
}