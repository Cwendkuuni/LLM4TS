package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

/**
 * The class PosixParserTest is a JUnit test case for the PosixParser class.
 */
public class PosixParserTest {

    

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        PosixParser parser = new PosixParser();
        parser.flatten(null, new String[]{}, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), null, false);
    }

    @Test
    public void testFlattenBasic() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar=123", "-x", "-y", "--z"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");
        options.addOption("z", true, "z", "Specify z");
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--bar=123", "-x", "-y", "--z"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar=123", "-x", "-y", "--z", "arg"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "--bar=123", "--", "arg"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"--", "-"};
        String[] result = parser.flatten(new Options(), args, false);
        assertArrayEquals(new String[]{"--", "-"}, result);
    }

    @Test
    public void testFlattenOptionToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar", "-x", "-y", "--z=val"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");
        options.addOption("z", true, "z", "Specify z");
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "-x", "-y", "--z=val"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenNullToken() {
        PosixParser parser = new PosixParser();
        parser.burstToken(null, false);
    }

    @Test
    public void testBurstTokenEmptyToken() {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo"};
        String result = parser.burstToken("--foo", false)[0];
        assertTrue(result.startsWith("-"));
        assertTrue(result.length() > 1);
    }

    @Test
    public void testBurstTokenOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=value"};
        String result = parser.burstToken("--foo=value", false)[0];
        assertTrue(result.startsWith("-"));
        assertTrue(result.length() > 1);
        assertTrue(result.endsWith("="));
        String arg = parser.burstToken("--foo=value", false)[1];
        assertTrue(arg.equals("value"));
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid"};
        String[] result = parser.burstToken("--invalid", false);
        assertArrayEquals(new String[]{"--", "invalid"}, result);
    }

    @Test
    public void testBurstTokenOptionWithMultipleArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=value1 value2"};
        String[] result = parser.burstToken("--foo=value1 value2", false);
        assertArrayEquals(new String[]{"--foo", "value1", "value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), new String[]{"--invalid"}, false);
    }

    
}