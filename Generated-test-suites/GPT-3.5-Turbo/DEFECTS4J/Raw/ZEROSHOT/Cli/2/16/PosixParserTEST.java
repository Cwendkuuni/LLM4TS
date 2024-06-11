import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "arg1", "-b"};
        options.addOption(new Option("a", "option1", false, "Option 1"));
        options.addOption(new Option("b", "option2", false, "Option 2"));

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "value1", "arg1", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        String[] arguments = {"-a", "value1", "arg1", "-b"};
        options.addOption(new Option("a", "option1", false, "Option 1"));
        options.addOption(new Option("b", "option2", false, "Option 2"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "value1", "arg1", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialToken() {
        String[] arguments = {"--special=token", "--special-token"};
        options.addOption(new Option("special", false, "Special option"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--special", "token", "--special-token"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        options.addOption(new Option("a", "option1", false, "Option 1"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionToken() {
        String[] arguments = {"-a"};
        options.addOption(new Option("a", "option1", false, "Option 1"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionToken_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "arg1"};
        options.addOption(new Option("a", "option1", false, "Option 1"));

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "value1", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionToken() {
        String[] arguments = {"--long-option"};
        options.addOption(new Option("l", "long-option", false, "Long Option"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--long-option"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionToken_StopAtNonOption() {
        String[] arguments = {"--long-option", "value1", "arg1"};
        options.addOption(new Option("l", "long-option", false, "Long Option"));

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--long-option", "value1", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionTokenWithEqualSign() {
        String[] arguments = {"--long-option=value1"};
        options.addOption(new Option("l", "long-option", true, "Long Option"));

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--long-option", "value1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionTokenWithEqualSign_StopAtNonOption() {
        String[] arguments = {"--long-option=value1", "arg1"};
        options.addOption(new Option("l", "long-option", true, "Long Option"));

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"--long-option", "value1", "arg1"};
        assertArrayEquals(expected, result);
    }
}