package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_withStopAtNonOptionTrue() {
        Options options = new Options();
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("nonOption", flattened[2]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionFalse() {
        Options options = new Options();
        Option option1 = new Option("a", "first option");
        Option option2 = new Option("b", "second option");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value2", flattened[3]);
        assertEquals("nonOption", flattened[4]);
    }
}