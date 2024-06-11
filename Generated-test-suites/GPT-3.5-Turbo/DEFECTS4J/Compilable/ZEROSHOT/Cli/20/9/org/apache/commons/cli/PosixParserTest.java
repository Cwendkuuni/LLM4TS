package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        
        String[] arguments = {"-a", "-b", "value", "-c", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-a", "non-option", "-b", "value"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "non-option", "-b", "value"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_longOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"--b=value"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--b", "value"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_shortOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_invalidOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-c"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-c"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "-b", "c"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_noOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"non-option"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"non-option"};
        Assert.assertArrayEquals(expected, result);
    }

}