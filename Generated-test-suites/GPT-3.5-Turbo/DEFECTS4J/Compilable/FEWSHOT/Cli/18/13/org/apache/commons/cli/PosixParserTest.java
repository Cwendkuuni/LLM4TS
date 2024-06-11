package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", true, "option 2");
        Option option3 = new Option("c", false, "option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] args1 = {"-a", "--", "-b", "value1", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "--", "-b", "value1", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args1, false));

        String[] args2 = {"-a", "-b", "value1", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-b", "value1", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args2, true));

        String[] args3 = {"-a", "-bvalue1", "-c", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-bvalue1", "-c", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args3, true));

        String[] args4 = {"-a", "-b", "-c", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-b", "-c", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args4, true));

        String[] args5 = {"-a", "-bvalue1", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-bvalue1", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args5, false));

        String[] args6 = {"-a", "-b=value1", "--", "-c", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-b=value1", "--", "-c", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args6, false));
        
        String[] args7 = {"-a", "-cd", "arg1", "arg2"};
        assertArrayEquals(new String[] {"-a", "-cd", "arg1", "arg2"}, 
                new PosixParser().flatten(options, args7, true));
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}