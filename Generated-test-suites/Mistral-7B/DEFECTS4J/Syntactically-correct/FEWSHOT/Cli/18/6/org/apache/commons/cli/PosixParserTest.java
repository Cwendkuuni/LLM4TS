package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", false, "Option B");
        Option optionC = new Option("c", "c", true, "Option C");
        Option optionD = new Option("d", "d", true, "Option D");
        Option optionE = new Option("e", "e", false, "Option E");
        Option optionF = new Option("f", "f", false, "Option F");
        Option optionG = new Option("g", "g", true, "Option G", "Optional value");
        Option optionH = new Option("h", "h", true, "Option H");
        Option optionI = new Option("i", "i", false, "Option I");
        Option optionJ = new Option("j", "j", true, "Option J", "Optional value");
        Option optionK = new Option("k", "k", false, "Option K");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);
        options.addOption(optionE);
        options.addOption(optionF);
        options.addOption(optionG);
        options.addOption(optionH);
        options.addOption(optionI);
        options.addOption(optionJ);
        options.addOption(optionK);

        PosixParser parser = new PosixParser();
        String[] arguments = new String[] {"-a", "--d=value", "-c", "--f", "-g", "--h", "-h", "-i", "value", "--j=otherValue", "-k"};
        String[] expected = new String[] {"-a", "--d", "value", "-c", "--f", "--g", "--h", "--h", "-i", "value", "--j", "otherValue", "-k"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", false, "Option B");
        Option optionC = new Option("c", "c", true, "Option C");
        Option optionD = new Option("d", "d", true, "Option D");
        Option optionE = new Option("e", "e", false, "Option E");
        Option optionF = new Option("f", "f", false, "Option F");
        Option optionG = new Option("g", "g", true, "Option G", "Optional value");
        Option optionH = new Option("h", "h", true, "Option H");
        Option optionI = new Option("i", "i", false, "Option I");
        Option optionJ = new Option("j", "j", true, "Option J", "Optional value");
        Option optionK = new Option("k", "k", false, "Option K");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        options.addOption(optionD);
        options.addOption(optionE);
        options.addOption(optionF);
        options.addOption(optionG);
        options.addOption(optionH);
        options.addOption(optionI);
        options.addOption(optionJ);
        options.addOption(optionK);

        PosixParser parser = new PosixParser();
        String[] arguments = new String[] {"--", "-a", "--d=value", "-c", "--f", "-g", "--h", "-h", "-i", "value", "--j=otherValue", "-k", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = new String[] {"--", "-a", "--d", "value", "-c", "--f", "--g", "--h", "--h", "value", "--j", "otherValue", "-k", "nonOption"};

        assertArrayEquals(expected, result);

        assertEquals("nonOption", result[result.length - 1]);
    }

    @Test
    public void testEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, new String[0], false);

        assertEquals(new String[0], result);
    }

    @Test
    public void testNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, null, false);

        assertEquals(new String[0], result);
    }

    @Test
    public void testNullOptions() {
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(null, new String[0], false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullArgumentsAndOptions() {
        try {
            new PosixParser().flatten(null, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertArrayEquals(expected, actual, "expected and actual arrays are not equal");
    }

    private void assertArrayEquals(Object expected, Object actual, String errorMessage) {
        if (expected.getClass() != actual.getClass()) {
            throw new AssertionError(errorMessage + ": expected [" + expected.getClass().getName() + "] and actual ["
                    + actual.getClass().getName() + "]");
        }

        if (expected.length != actual.length) {
            throw new AssertionError(errorMessage + ": expected [" + expected.length + "] and actual [" + actual.length + "]");
        }

        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError(errorMessage + ": expected [" + expected[i] + "] but was [" + actual[i] + "]");
            }
        }
    }
}