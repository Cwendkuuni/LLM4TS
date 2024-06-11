package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--", "-c"}, result);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--", "-c", "arg"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--long-option", "-c"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "--long-option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--long-option", "value", "-c"}, result);
    }

    @Test
    public void testFlattenWithShortOption() {
        Options options = new Options();
        options.addOption("f", true, "f");
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--f", "-c"}, result);
    }

    @Test
    public void testFlattenWithShortOptionValue() {
        Options options = new Options();
        options.addOption("f", true, "f");
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "--f=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--f", "value", "-c"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", true, "f");
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "-f", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--f", "arg"}, result);
    }

    @Test
    public void testFlattenWithGroupedOptions() {
        Options options = new Options();
        options.addOptionGroup(new OptionGroup("mygroup", "My group"));
        Option option1 = new Option("o1", true, "o");
        Option option2 = new Option("o2", true, "o");
        option1.setLongOpt("longopt1");
        option2.setLongOpt("longopt2");
        option1.setOptionGroup(options.getOptionGroup("mygroup"));
        option2.setOptionGroup(options.getOptionGroup("mygroup"));
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--mygroup", "--longopt1", "--longopt2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--mygroup", "--longopt1", "--longopt2"}, result);
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        String[] result = new PosixParser().burstToken(token, false);
        assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-abc";
        String[] result = new PosixParser().burstToken(token, true);
        assertArrayEquals(new String[]{"--", "-a", "-b", "-c"}, result);
    }

    @Test
    public void testBurstTokenWithArg() {
        String token = "--foo=abc";
        String[] result = new PosixParser().burstToken(token, false);
        assertArrayEquals(new String[]{"--foo", "abc"}, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionAndArg() {
        String token = "--foo=abc";
        String[] result = new PosixParser().burstToken(token, true);
        assertArrayEquals(new String[]{"--foo", "--", "abc"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithInvalidOption() {
        String token = "-xyz";
        new PosixParser().burstToken(token, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithInvalidOptionAndStopAtNonOption() {
        String token = "-xyz";
        new PosixParser().burstToken(token, true);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "-d"};
        parser.processOptionToken("-d", true, options);
        assertSame(options, parser.getCurrentOptions());
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--d"}, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testProcessOptionTokenWithoutStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "-d"};
        parser.processOptionToken("-d", false, options);
        assertSame(options, parser.getCurrentOptions());
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "-d"}, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "--bar=baz", "-c", "arg"};
        parser.processNonOptionToken("arg", options);
        assertSame(options, parser.getCurrentOptions());
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "baz", "--", "arg"}, parser.getTokens().toArray(new String[0]));
    }
}