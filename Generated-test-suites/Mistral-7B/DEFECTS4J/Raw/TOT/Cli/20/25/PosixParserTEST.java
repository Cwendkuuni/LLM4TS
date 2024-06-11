package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flatten method with basic arguments.
     */
    @Test
    public void testFlattenBasic() {
        String[] args = {"--foo=bar", "-a", "-b", "--", "-c", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");
        options.addOption("b", "bool", false, "a boolean option");
        options.addOption("c", "count", true, "a count option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "bar", "--", "-a", "--", "arg1", "arg2", "-c"}, parsedArgs);
    }

    /**
     * Test flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--foo=bar", "-a", "-b", "--", "-c", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");
        options.addOption("b", "bool", false, "a boolean option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--foo=bar", "-a", "--", "-c", "arg1", "arg2"}, parsedArgs);
    }

    /**
     * Test burstToken method.
     */
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();

        // simple option
        String[] tokens1 = parser.burstToken("-o", false);
        assertArrayEquals(new String[]{"-o"}, tokens1);

        // option with argument
        String[] tokens2 = parser.burstToken("-o=foo", false);
        assertArrayEquals(new String[]{"-o", "foo"}, tokens2);

        // invalid option
        try {
            parser.burstToken("--invalid", false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        // stop at non-option
        String[] tokens3 = parser.burstToken("--", true);
        assertArrayEquals(new String[]{"--"}, tokens3);
    }

    /**
     * Test flatten method with complex arguments.
     */
    @Test
    public void testFlattenComplex() {
        String[] args = {"--foo=bar1", "--foo=bar2", "-a", "-b=arg1", "--", "-c=arg3", "arg4", "-d"};
        Options options = new Options();
        options.addRequiredOption("a", "arg", true, "an argument");
        options.addOption("b", "bool", false, "a boolean option");
        options.addOption("c", "count", true, "a count option");
        options.addOption("foo", "foo", true, "a foo option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo=bar1", "--foo=bar2", "--", "-a", "--", "-c=arg3", "arg4"}, parsedArgs);
    }
}