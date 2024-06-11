package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = new String[]{"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "-c"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = new String[]{"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value"}, result);
    }

    @Test
    public void testFlattenNoOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "-c"}, result);
    }

    @Test
    public void testFlattenNoOptionsStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "-c"}, result);
    }

    @Test
    public void testFlattenSpecialTokens() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenSpecialTokensStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"--option=value"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenSingleHyphenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-d"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-d"}, result);
    }

    @Test
    public void testFlattenInvalidOptionStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"-d"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-d"}, result);
    }

    @Test
    public void testFlattenBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = new String[]{"-ab"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = new String[]{"-ab"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }
}