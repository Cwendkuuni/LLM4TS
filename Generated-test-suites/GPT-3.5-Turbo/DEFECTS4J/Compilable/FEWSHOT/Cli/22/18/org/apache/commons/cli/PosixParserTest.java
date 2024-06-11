package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlattenNoOptions() {
        String[] args = {"arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(args, result);
    }

    @Test
    public void testFlattenWithOptions() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = {"-a", "arg1", "-b", "value"};

        String[] expected = {"-a", "arg1", "-b", "value"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionStop() {
        options.addOption("a", false, "Option a");

        String[] args = {"-a", "arg1", "arg2"};

        String[] expected = {"-a", "arg1", "--", "arg2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionNoStop() {
        options.addOption("a", false, "Option a");

        String[] args = {"-a", "arg1", "arg2"};

        String[] expected = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        options.addOption("a", false, "Option a");

        String[] args = {"-a", "-b"};

        String[] expected = {"-a", "-b"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(expected, result);
    }
}