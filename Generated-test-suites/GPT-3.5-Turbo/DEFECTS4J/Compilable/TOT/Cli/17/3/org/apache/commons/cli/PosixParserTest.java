package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_FlattenedArgumentsContainStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "nonOption"};
        String[] expected = {"-a", "arg1", "nonOption"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_FlattenedArgumentsOnlyContainOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "-b", "nonOption"};
        String[] expected = {"-a", "-b"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_NoOptionExists_FlattenedArgumentsContainStopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"-a", "arg1", "nonOption"};
        String[] expected = {"--", "-a", "arg1", "nonOption"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_StopAtNonOption_NoOptionExists_FlattenedArgumentsOnlyContainOptions() {
        Options options = new Options();

        String[] arguments = {"-a", "arg1", "nonOption"};
        String[] expected = {"-a", "arg1", "nonOption"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_HyphenToken_FlattenedArgumentsContainHyphenToken() {
        Options options = new Options();

        String[] arguments = {"-"};
        String[] expected = {"-"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_HyphenTokenWithStopAtNonOption_FlattenedArgumentsContainHyphenToken() {
        Options options = new Options();

        String[] arguments = {"-"};
        String[] expected = {"-"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_OptionToken_ValidOption_FlattenedArgumentsContainOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_OptionToken_ValidOptionWithArg_FlattenedArgumentsContainOptionAndArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1"};
        String[] expected = {"-a", "arg1"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_OptionToken_ValidOptionWithMultipleArgs_FlattenedArgumentsContainOptionAndArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_OptionToken_InvalidOption_StopAtNonOption_FlattenedArgumentsContainStopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"-b"};
        String[] expected = {"--", "-b"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_OptionToken_InvalidOption_DoesNotStopAtNonOption_FlattenedArgumentsContainInvalidOption() {
        Options options = new Options();

        String[] arguments = {"-b", "arg1"};
        String[] expected = {"-b", "arg1"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_BurstToken_ValidOptionsExist_FlattenedArgumentsContainBurstOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("bc", true, "Option BC");

        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_BurstToken_NoValidOptionExists_StopAtNonOption_FlattenedArgumentsContainStopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"-abc"};
        String[] expected = {"--", "-abc"};

        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_BurstToken_NoValidOptionExists_DoesNotStopAtNonOption_FlattenedArgumentsContainBurstOptions() {
        Options options = new Options();

        String[] arguments = {"-abc", "arg1"};
        String[] expected = {"-a", "-b", "-c", "arg1"};

        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }
}