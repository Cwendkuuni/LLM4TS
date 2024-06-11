package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Test Option 1");
        Option option2 = new Option("o", true, "Test Option 2");
        options.addOption(option1);
        options.addOption(option2);

        PosixParser parser = new PosixParser();

        String[] arguments1 = {"-f", "test"};
        String[] result1 = parser.flatten(options, arguments1, false);
        assertEquals("-f", result1[0]);
        assertEquals("test", result1[1]);

        String[] arguments2 = {"-o", "test"};
        String[] result2 = parser.flatten(options, arguments2, false);
        assertEquals("-o", result2[0]);
        assertEquals("test", result2[1]);

        String[] arguments3 = {"-fo", "test"};
        String[] result3 = parser.flatten(options, arguments3, false);
        assertEquals("-f", result3[0]);
        assertEquals("-o", result3[1]);
        assertEquals("test", result3[2]);

        String[] arguments4 = {"-of", "test"};
        String[] result4 = parser.flatten(options, arguments4, false);
        assertEquals("-o", result4[0]);
        assertEquals("f", result4[1]);
        assertEquals("test", result4[2]);

        String[] arguments5 = {"-fx", "test"};
        String[] result5 = parser.flatten(options, arguments5, true);
        assertEquals("-fx", result5[0]);
        assertEquals("test", result5[1]);

        String[] arguments6 = {"-f", "-o", "test"};
        String[] result6 = parser.flatten(options, arguments6, true);
        assertEquals("-f", result6[0]);
        assertEquals("-o", result6[1]);
        assertEquals("test", result6[2]);

        String[] arguments7 = {"-f", "test", "-o"};
        String[] result7 = parser.flatten(options, arguments7, true);
        assertEquals("-f", result7[0]);
        assertEquals("test", result7[1]);
        assertEquals("-o", result7[2]);
    }
}