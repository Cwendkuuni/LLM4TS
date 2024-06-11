package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing PosixParser class from Commons CLI.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "-h", "-a", "-b", "--bar=baz", "arg1", "-x", "--", "-y", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");

        DefaultParser parser = new DefaultParser();
        String[] flattened = parser.parse(options, args, true);
        List<String> expected = Arrays.asList(
                "--foo",
                "-h",
                "-a",
                "-b",
                "--bar",
                "baz",
                "arg1",
                "--",
                "-y",
                "arg2"
        );
        assertEquals(expected.toArray(new String[0]), flattened);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "-h", "-a", "-b", "--bar=baz", "arg1", "-x", "--", "-y", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");

        DefaultParser parser = new DefaultParser();
        String[] flattened = parser.parse(options, args, true);

        String[] argsForStopAtNonOption = {"--foo", "-h", "-a", "-b", "--bar=baz", "arg1", "-x", "--", "-y", "arg2"};
        String[] stopAtNonOptionFlattened = parser.parse(options, argsForStopAtNonOption, false).toArray(new String[0]);

        List<String> expected = Arrays.asList(
                "--foo",
                "-h",
                "-a",
                "-b",
                "--bar=baz",
                "--",
                "-x",
                "-y",
                "arg2"
        );
        assertEquals(expected.toArray(new String[0]), stopAtNonOptionFlattened);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"--foo-bar", "qux", "-z"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");

        DefaultParser parser = new DefaultParser();
        String[] flattened = parser.parse(options, args, true).toArray(new String[0]);

        List<String> expected = Arrays.asList(
                "--foo",
                "-bar",
                "qux"
        );
        assertEquals(expected.toArray(new String[0]), flattened);
    }

    @Test(expected = ParseException.class)
    public void testBurstingWithNonOption() throws ParseException {
        String[] args = {"--foo-bar", "qux", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testNoOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        DefaultParser parser = new DefaultParser();

        String[] flattened = parser.parse(options, args, false).toArray(new String[0]);
        assertEquals(new String[0], flattened);
    }
}