package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        String[] arguments = {"--a", "-b", "--c=foo", "-d", "--e=bar", "--foo=bar"};
        String[] expected = {"--a", "-b", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar"};
        assertArrayEquals(expected, PosixParser.parse(options, arguments, false));

        String[] arguments2 = {"--a", "-b", "--c=foo", "-d", "--e=bar", "--foo=bar", "-"};
        String[] expected2 = {"--a", "-b", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar", "--"};
        assertArrayEquals(expected2, PosixParser.parse(options, arguments2, false));

        String[] arguments3 = {"--a", "-b", "--c=foo", "-d", "--e=bar", "--foo=bar", "--"};
        String[] expected3 = {"--a", "-b", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar", "--"};
        assertArrayEquals(expected3, PosixParser.parse(options, arguments3, true));

        String[] arguments4 = {"--a", "-b", "--c=foo", "-d", "--e=bar", "--foo=bar", "-d", "--e=baz"};
        String[] expected4 = {"--a", "-b", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar", "--d", "--e", "baz"};
        assertArrayEquals(expected4, PosixParser.parse(options, arguments4, false));
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--a", "-x", "--c=foo", "-d", "--e=bar", "--foo=bar"};
        String[] expected = {"--a", "-x", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar"};
        assertArrayEquals(expected, PosixParser.parse(options, arguments, false));

        String[] arguments2 = {"--a", "-x", "--c=foo", "-d", "--e=bar", "--foo=bar", "--x"};
        String[] expected2 = {"--a", "-x", "--c", "--", "foo", "-d", "--e", "bar", "--foo", "bar", "--x"};
        assertArrayEquals(expected2, PosixParser.parse(options, arguments2, false));
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--a", "--value=long-value"};
        String[] expected = {"--a", "--value", "long-value"};
        assertArrayEquals(expected, PosixParser.parse(options, arguments, false));
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, PosixParser.parse(null, arguments, false));
    }

    @Test
    public void testFlattenWithNoArguments() {
        Options options = new Options();
        String[] arguments = {};
        assertArrayEquals(new String[0], PosixParser.parse(options, arguments, false));
    }

    @Test
    public void testFlattenWithEmptyString() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};
        assertArrayEquals(expected, PosixParser.parse(options, arguments, false));
    }

    @Test
    public void testFlattenWithEmptyStringAndOtherArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2", "arg3"};
        String[] expected = {"--", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected, PosixParser.parse(options, arguments, false));
    }

    @Test
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        assertArrayEquals(new String[0], PosixParser.parse(options, arguments, false));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        String token = "-abc";
        List<String> expected = Arrays.asList("-a", "-c", "--");
        assertArrayEquals(expected.toArray(new String[0]), PosixParser.burstToken(token, false));

        List<String> expected2 = Arrays.asList("-a", "-c", "--", "b");
        assertArrayEquals(expected2.toArray(new String[0]), PosixParser.burstToken(token, true));
    }

    @Test
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String token = "-axc";
        List<String> expected = Arrays.asList("-a", "-x", "-c");
        assertArrayEquals(expected.toArray(new String[0]), PosixParser.burstToken(token, false));

        List<String> expected2 = Arrays.asList("-a", "-x", "-c", "--");
        assertArrayEquals(expected2.toArray(new String[0]), PosixParser.burstToken(token, true));
    }

    @Test
    public void testBurstTokenWithNoOptions() {
        String token = "-ab";
        List<String> expected = Collections.singletonList("-");
        assertArrayEquals(expected.toArray(new String[0]), PosixParser.burstToken(token, false));

        List<String> expected2 = Arrays.asList("-", "--");
        assertArrayEquals(expected2.toArray(new String[0]), PosixParser.burstToken(token, true));
    }
}