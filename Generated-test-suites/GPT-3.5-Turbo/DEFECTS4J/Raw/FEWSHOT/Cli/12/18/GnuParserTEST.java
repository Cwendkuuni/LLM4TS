package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        // Test case 1: Both arguments are options
        Options options1 = new Options();
        options1.addOption(new Option("a", "Option A"));
        options1.addOption(new Option("b", "Option B"));
        String[] arguments1 = {"-a", "-b"};
        String[] expected1 = {"-a", "-b"};
        assertArrayEquals(expected1, GnuParser.flatten(options1, arguments1, false));

        // Test case 2: One argument is an option, other is not
        Options options2 = new Options();
        options2.addOption(new Option("c", "Option C"));
        String[] arguments2 = {"-c", "value"};
        String[] expected2 = {"-c", "value"};
        assertArrayEquals(expected2, GnuParser.flatten(options2, arguments2, false));

        // Test case 3: Argument is an option with value
        Options options3 = new Options();
        options3.addOption(new Option("d", "Option D", true, "Value for Option D"));
        String[] arguments3 = {"-d=value"};
        String[] expected3 = {"-d=value"};
        assertArrayEquals(expected3, GnuParser.flatten(options3, arguments3, false));

        // Test case 4: Argument is a special properties option
        Options options4 = new Options();
        options4.addOption(new Option("e", "Option E", true, "Value for Option E"));
        String[] arguments4 = {"-De=value"};
        String[] expected4 = {"-De", "value"};
        assertArrayEquals(expected4, GnuParser.flatten(options4, arguments4, false));

        // Test case 5: Argument is a non-option
        Options options5 = new Options();
        String[] arguments5 = {"non-option"};
        String[] expected5 = {"non-option"};
        assertArrayEquals(expected5, GnuParser.flatten(options5, arguments5, false));

        // Test case 6: Stop flattening at non-option
        Options options6 = new Options();
        String[] arguments6 = {"-a", "non-option", "-b"};
        String[] expected6 = {"-a", "non-option", "-b"};
        assertArrayEquals(expected6, GnuParser.flatten(options6, arguments6, true));
    }
}