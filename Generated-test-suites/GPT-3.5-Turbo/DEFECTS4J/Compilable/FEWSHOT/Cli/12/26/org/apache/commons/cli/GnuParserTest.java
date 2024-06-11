package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("D", true, "Define a system property"));
        options.addOption(new Option("h", "help", false, "Print help"));
        options.addOption(new Option("v", "version", false, "Print version"));

        String[] arguments = {"-Dproperty=value", "--foo=bar", "-v", "file.txt"};
        String[] expected = {"-Dproperty=value", "--foo=bar", "-v", "file.txt"};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("D", true, "Define a system property"));
        options.addOption(new Option("h", "help", false, "Print help"));
        options.addOption(new Option("v", "version", false, "Print version"));

        String[] arguments = {"-Dproperty=value", "--foo=bar", "-v", "file.txt"};
        String[] expected = {"-Dproperty=value", "--foo=bar"};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOptionNoOptions() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};
        String[] expected = {};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] arguments = {"-a", "-b", "-c"};
        String[] expected = {"-a", "-b", "-c"};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOptionMultipleOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] arguments = {"-a", "-b", "-c"};
        String[] expected = {};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }

    @Test
    public void testFlattenMixedOptionsAndNonOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOptionMixedOptionsAndNonOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = {};
        String[] expected = {};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOptionEmptyArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = {};
        String[] expected = {};

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }

    @Test
    public void testFlattenNullArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = null;
        String[] expected = null;

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOptionNullArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", false, "Option b"));

        String[] arguments = null;
        String[] expected = null;

        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, true));
    }
}