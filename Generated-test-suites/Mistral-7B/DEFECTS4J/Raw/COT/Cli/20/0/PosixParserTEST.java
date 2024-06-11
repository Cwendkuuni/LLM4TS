package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * The PosixParser instance to be tested.
     */
    private PosixParser parser;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "arg1", "-d=arg2", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");

        List tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "baz", "-a", "-c", "arg1", "-d", "arg2", "--"}, (String[]) tokens.toArray());
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "arg1", "-d=arg2", "--", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");

        List tokens = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "baz", "--", "arg3", "arg4"}, (String[]) tokens.toArray());
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)}.
     */
    @Test
    public void testProcessOptionToken() throws ParseException {
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "--foo", "-d=arg2", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");

        parser.init();
        parser.currentOption = null;

        String token = "--foo";
        parser.processOptionToken(token, false);

        Assert.assertEquals("Option FOO", parser.currentOption);

        token = "--bar=baz";
        parser.processOptionToken(token, false);

        Assert.assertEquals("Option BAR", parser.currentOption);
        Assert.assertEquals("baz", parser.currentOption.getValue());
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)}.
     */
    @Test
    public void testBurstToken() throws ParseException {
        String[] args = {"--foo-bar", "--foo=baz", "--", "-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");

        parser.init();
        parser.currentOption = null;

        String token = "--foo-bar";
        parser.burstToken(token, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-", "bar"}, (String[]) parser.tokens.toArray());

        token = "--foo=baz";
        parser.init();
        parser.tokens.clear();
        parser.currentOption = null;
        parser.burstToken(token, false);

        Assert.assertArrayEquals(new String[]{"--foo", "baz"}, (String[]) parser.tokens.toArray());
    }
}