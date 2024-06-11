package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "-a", "-b", "--bar"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f"));
        options.addOption(OptionBuilder.withShortOpt('b').create("bar"));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--foo", "-a", "--bar");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithArg() {
        String[] args = {"--foo=abc", "-a", "-b", "--bar=def"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar").hasArgs(2));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--foo=abc", "-a", "--bar=def");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"--long-option-name", "--foo=abc", "-a", "--bar=def"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("long-option-name").create("l").hasArg());
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar").hasArgs(2));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--long-option-name=long-option-name", "--foo=abc", "-a", "--bar=def");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "-a", "-b", "--bar=def", "nonOption"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f"));
        options.addOption(OptionBuilder.withShortOpt('b').create("bar").hasArgs(2));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List expected = Arrays.asList("--foo", "-a", "--bar=def");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithDashAsOption() {
        String[] args = {"--", "-a", "--foo=abc", "-b", "--bar=def", "--"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar").hasArgs(2));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--", "--foo=abc", "-a", "--bar=def");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithDashBeforeOption() {
        String[] args = {"--", "-a", "--foo=abc", "-b--", "--bar=def", "--"};
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar").hasArgs(2));

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--", "--foo=abc", "-b--", "--bar=def");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }

    @Test
    public void testBurstTokenSimple() {
        String token = "--foo";
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, false);
        List expected = Arrays.asList("--foo");
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenShortOption() {
        String token = "-foo";
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, false);
        List expected = Arrays.asList("-foo");
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOption() {
        String token = "--foo";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f"));

        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        List expected = Arrays.asList("--foo");
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithArg() {
        String token = "--foo=abc";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());

        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        List expected = Arrays.asList("--foo=abc");
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithArgAndNextOption() {
        String token = "--foo=abc-b";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar"));

        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        List expected = Arrays.asList("--foo=abc", "-b");
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--foo=abc-b";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create("f").hasArg());
        options.addOption(OptionBuilder.withShortOpt('b').create("bar"));

        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, true);
        List expected = Arrays.asList("--foo=abc");
        assertEquals(expected, result);
    }

}