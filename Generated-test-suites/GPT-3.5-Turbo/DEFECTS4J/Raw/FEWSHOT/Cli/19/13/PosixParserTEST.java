package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));
        options.addOption(new Option("c", "optionC", true, "Option C"));

        String[] arguments1 = {"-a", "--optionB", "value"};
        String[] expected1 = {"-a", "--optionB", "value"};
        assertEquals(expected1, PosixParser.flatten(options, arguments1, false));

        String[] arguments2 = {"-a", "--optionB", "value", "extra"};
        String[] expected2 = {"-a", "--optionB", "value", "extra"};
        assertEquals(expected2, PosixParser.flatten(options, arguments2, false));

        String[] arguments3 = {"--optionB=value", "-c", "optionCValue"};
        String[] expected3 = {"--optionB", "value", "-c", "optionCValue"};
        assertEquals(expected3, PosixParser.flatten(options, arguments3, false));

        String[] arguments4 = {"-a", "-b", "value1", "value2"};
        String[] expected4 = {"-a", "-b", "value1", "value2"};
        assertEquals(expected4, PosixParser.flatten(options, arguments4, false));

        String[] arguments5 = {"-a", "-bvalue", "extra"};
        String[] expected5 = {"-a", "-b", "value", "extra"};
        assertEquals(expected5, PosixParser.flatten(options, arguments5, false));

        String[] arguments6 = {"-acvalue"};
        String[] expected6 = {"-a", "-c", "value"};
        assertEquals(expected6, PosixParser.flatten(options, arguments6, false));

        String[] arguments7 = {"-a", "-cvalue", "extra"};
        String[] expected7 = {"-a", "-c", "value", "extra"};
        assertEquals(expected7, PosixParser.flatten(options, arguments7, false));

        String[] arguments8 = {"-a", "-cx", "extra"};
        String[] expected8 = {"-a", "-c", "x", "extra"};
        assertEquals(expected8, PosixParser.flatten(options, arguments8, false));

        String[] arguments9 = {"-a", "-b", "extra"};
        String[] expected9 = {"-a", "-b", "extra"};
        assertEquals(expected9, PosixParser.flatten(options, arguments9, false));

        String[] arguments10 = {"-a", "-c=", "extra"};
        String[] expected10 = {"-a", "-c", "", "extra"};
        assertEquals(expected10, PosixParser.flatten(options, arguments10, false));

        String[] arguments11 = {"-acx", "extra"};
        String[] expected11 = {"-a", "-c", "x", "extra"};
        assertEquals(expected11, PosixParser.flatten(options, arguments11, false));

        String[] arguments12 = {"-a"};
        String[] expected12 = {"-a"};
        assertEquals(expected12, PosixParser.flatten(options, arguments12, false));

        String[] arguments13 = {"-a", "--", "-bvalue"};
        String[] expected13 = {"-a", "--", "-bvalue"};
        assertEquals(expected13, PosixParser.flatten(options, arguments13, false));
    }
}