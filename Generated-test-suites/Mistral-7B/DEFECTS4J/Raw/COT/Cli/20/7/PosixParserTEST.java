import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArg() {
        Options options = new Options();
        String[] arguments = {"arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 value");
        Option option2 = new Option("o2", "option2", false, "Option2 no value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "value1", "--o2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"o1", "value1", "o2"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option longOption = new Option("long-option", "Long option", false, "Long option description");
        options.addOption(longOption);
        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Option longOption = new Option("long-option", true, "l", "Long option with value");
        options.addOption(longOption);
        String[] arguments = {"--long-option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"long-option", "value"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        String[] arguments = {"arg1", "-o", "value", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "-o", "value"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        String[] arguments = {"-x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testBurstToken_EmptyToken() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("", false);
        assertEquals("", result);
    }

    @Test
    public void testBurstToken_SingleCharacter() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("-a", false);
        assertEquals("-a", result);
    }

    @Test
    public void testBurstToken_SingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        String result = parser.burstToken("-o", false);
        assertEquals("-o", result);
        result = parser.burstToken("-o", true);
        assertArrayEquals(new String[]{"o"}, result);
    }

    @Test
    public void testBurstToken_LongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option", false, "l", "Long option");
        options.addOption(longOption);
        String result = parser.burstToken("--long-option", false);
        assertEquals("--long-option", result);
        result = parser.burstToken("--long-option", true);
        assertEquals("long-option", result);
    }

    @Test
    public void testBurstToken_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        String result = parser.burstToken("-o=value", false);
        assertEquals("-o=value", result);
        result = parser.burstToken("-o=value", true);
        assertArrayEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testBurstToken_InvalidToken() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("--invalid", false);
        assertNull(result);
    }
}