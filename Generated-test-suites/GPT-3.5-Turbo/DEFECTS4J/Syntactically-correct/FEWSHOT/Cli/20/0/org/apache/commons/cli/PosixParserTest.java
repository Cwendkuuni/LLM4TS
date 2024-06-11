package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "value1", "-c=value2", "--", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(7, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value1", flattenedArguments[2]);
        assertEquals("-c", flattenedArguments[3]);
        assertEquals("value2", flattenedArguments[4]);
        assertEquals("--", flattenedArguments[5]);
        assertEquals("arg1", flattenedArguments[6]);
    }
}