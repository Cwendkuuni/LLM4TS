package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Option f");
        Option option2 = new Option("o", true, "Option o");
        Option option3 = new Option("t", false, "Option t");
        Option option4 = new Option("s", true, "Option s");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        PosixParser parser = new PosixParser();

        String[] args1 = {"-f", "-o", "value", "-t"};
        String[] expected1 = {"-f", "-o", "value", "-t"};
        String[] result1 = parser.flatten(options, args1, false);
        assertEquals(expected1, result1);

        String[] args2 = {"-f", "-o", "value", "-t"};
        String[] expected2 = {"-f"};
        String[] result2 = parser.flatten(options, args2, true);
        assertEquals(expected2, result2);

        String[] args3 = {"-o", "value", "-s", "other value"};
        String[] expected3 = {"-o", "value", "-s", "other value"};
        String[] result3 = parser.flatten(options, args3, true);
        assertEquals(expected3, result3);

        String[] args4 = {"-o", "value", "-x", "other value"};
        String[] expected4 = {"-o", "value", "-x", "other value"};
        String[] result4 = parser.flatten(options, args4, false);
        assertEquals(expected4, result4);

        String[] args5 = {"--option", "value"};
        String[] expected5 = {"--option", "value"};
        String[] result5 = parser.flatten(options, args5, false);
        assertEquals(expected5, result5);

        String[] args6 = {"--option", "value", "--other", "other value"};
        String[] expected6 = {"--option", "value", "--other", "other value"};
        String[] result6 = parser.flatten(options, args6, true);
        assertEquals(expected6, result6);

        String[] args7 = {"--option=value", "--other=other value"};
        String[] expected7 = {"--option", "value", "--other", "other value"};
        String[] result7 = parser.flatten(options, args7, true);
        assertEquals(expected7, result7);

        String[] args8 = {"--option", "-x", "other value"};
        String[] expected8 = {"--option", "-x", "other value"};
        String[] result8 = parser.flatten(options, args8, false);
        assertEquals(expected8, result8);

        String[] args9 = {"--option", "value", "other value"};
        String[] expected9 = {"--option", "value", "other value"};
        String[] result9 = parser.flatten(options, args9, true);
        assertEquals(expected9, result9);

        String[] args10 = {};
        String[] expected10 = {};
        String[] result10 = parser.flatten(options, args10, false);
        assertEquals(expected10, result10);

        String[] args11 = {"-f", "--option", "-t"};
        String[] expected11 = {"-f", "--option", "-t"};
        String[] result11 = parser.flatten(options, args11, false);
        assertEquals(expected11, result11);

        String[] args12 = {"-f", "-x", "value"};
        String[] expected12 = {"-f", "-x", "value"};
        String[] result12 = parser.flatten(options, args12, true);
        assertEquals(expected12, result12);

        String[] args13 = {"-f", "-x", "value"};
        String[] expected13 = {"-f", "-x", "value"};
        String[] result13 = parser.flatten(options, args13, false);
        assertEquals(expected13, result13);

        String[] args14 = {};
        String[] expected14 = {};
        String[] result14 = parser.flatten(options, args14, true);
        assertEquals(expected14, result14);

        String[] args15 = {"-o", "-t"};
        String[] expected15 = {"-o", "-t"};
        String[] result15 = parser.flatten(options, args15, true);
        assertEquals(expected15, result15);

        String[] args16 = {"-f", "value", "-t", "other value"};
        String[] expected16 = {"-f", "value", "-t", "other value"};
        String[] result16 = parser.flatten(options, args16, false);
        assertEquals(expected16, result16);
    }
}