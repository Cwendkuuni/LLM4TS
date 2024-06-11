import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        assertArrayEquals(new String[0], parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testSingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testDoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};
        String[] expected = {"--"};
        assertArrayEquals(expected, parser.flatten(new Options(), arguments, false));
    }

    @Test
    public void testSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Show options");
        String[] arguments = {"-o"};
        String[] expected = {"-o"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option value");
        String[] arguments = {"-o", "value"};
        String[] expected = {"-o", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option1", false, "Option1");
        options.addOption("p", "option2", true, "Option2 value");
        String[] arguments = {"-o", "-p", "value"};
        String[] expected = {"-o", "-p", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-x"};
        parser.flatten(options, arguments, false);
        fail("Expected ParseException");
    }

    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Show options");
        String[] arguments = {"arg1", "-o", "arg2"};
        String[] expected = {"arg1", "-o"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testBursting() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Show options");
        options.addOption("p", "param", true, "Parameter value");
        String[] arguments = {"-op", "value1", "-oparam", "value2"};
        String[] expected = {"-o", "-p", "value1", "-o", "param", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Show options");
        options.addOption("p", "param", true, "Parameter value");
        String[] arguments = {"arg1", "-op", "value1", "-oparam", "value2"};
        String[] expected = {"arg1", "-o", "p", "value1"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testBurstingWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("l", "longoption", true, "Long option value");
        String[] arguments = {"-l", "longoptionvalue"};
        String[] expected = {"-l", "longoptionvalue"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testBurstingWithLongOptionAndValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("l", "longoption", true, "Long option value");
        String[] arguments = {"-l", "longoption", "value"};
        String[] expected = {"-l", "longoption", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }
}