package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import junit.framework.TestCase;

import java.util.Arrays;

public class PosixParserTest extends TestCase {

    public void testFlatten() throws Exception {
        String[] args = new String[]{"--foo", "-o", "bar", "--", "-a", "-b", "--quux", "--", "1", "2", "3"};

        Options options = new Options();
        options.addOption("foo", true, "A boolean flag for option foo");
        options.addOption("o", "o", true, "An option with an argument");
        options.addOption("a", true, "An option that can take multiple values");
        options.addOption("quux", false, "An option with no arguments");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "true", "--", "-o", "bar", "--", "-a", "1", "--quux", "2", "--quux", "3"}, flattenedArgs);
    }

    public void testFlattenWithStopAtNonOption() throws Exception {
        String[] args = new String[]{"--foo", "-o", "bar", "--", "-a", "-b", "--quux", "--", "1", "2", "3", "baz"};

        Options options = new Options();
        options.addOption("foo", true, "A boolean flag for option foo");
        options.addOption("o", "o", true, "An option with an argument");
        options.addOption("a", true, "An option that can take multiple values");
        options.addOption("quux", false, "An option with no arguments");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--foo", "true", "--quux", "baz"}, flattenedArgs);
    }

    public void testBurstToken() throws Exception {
        String token = "-foo-bar-baz";

        PosixParser parser = new PosixParser();
        String[] burstedTokens = parser.burstToken(token, false);

        assertArrayEquals(new String[]{"-foo", "-bar", "-baz"}, burstedTokens);
    }

    public void testBurstTokenWithStopAtNonOption() throws Exception {
        String token = "-foo-bar-baz-quux";

        PosixParser parser = new PosixParser();
        String[] burstedTokens = parser.burstToken(token, true);

        assertArrayEquals(new String[]{"--", "--quux"}, burstedTokens);
    }

    public void testFlattenError() throws Exception {
        String[] args = new String[]{"--foo=bar", "-o=baz", "--quux=quux"};

        Options options = new Options();
        options.addOption("foo", true, "A boolean flag for option foo");
        options.addOption("o", "o", true, "An option with an argument");
        options.addOption("quux", false, "An option with no arguments");

        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid option: o=", e.getMessage());
        }
    }
}