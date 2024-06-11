package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser gnuParser;

    @Before
    public void setUp() {
        gnuParser = new GnuParser();
    }

    
    @Test
    public void testFlatten_noOptionExists() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] flattenedArguments = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(arguments, flattenedArguments);
    }

    @Test
    public void testFlatten_optionExistsForFirstChar() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"arg1", "-D", "-b"};
        String[] expectedArguments = {"arg1", "-D", "-b"};

        String[] flattenedArguments = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_optionExistsForWholeArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"arg1", "-D", "--foo=bar"};
        String[] expectedArguments = {"arg1", "-D", "--foo=bar"};

        String[] flattenedArguments = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_optionDoesNotExist() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"arg1", "-D", "-c"};
        String[] expectedArguments = {"arg1", "-D", "-c"};

        String[] flattenedArguments = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"arg1", "-D", "-c", "arg2", "arg3"};
        String[] expectedArguments = {"arg1", "-D", "-c"};

        String[] flattenedArguments = gnuParser.flatten(options, arguments, true);

        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_doubleDash() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"arg1", "--", "arg2", "arg3"};
        String[] expectedArguments = {"arg1", "--", "arg2", "arg3"};

        String[] flattenedArguments = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, flattenedArguments);
    }
}