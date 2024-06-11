import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSimpleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean flag");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean flag");
        options.addOption("b", true, "An option with a boolean flag");
        options.addOption("c", 1, "An option with an integer value");
        String[] arguments = {"-a", "--b", "-c=3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "--b", "-c=3");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean flag");
        options.addOption("b", true, "An option with a boolean flag");
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "-b");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option with a boolean flag");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option with a boolean flag");
        options.addOption("shortOption", true, "A short option with a boolean flag");
        String[] arguments = {"--longOption", "--shortOption=true", "someArgument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption", "--shortOption=true", "someArgument");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSpecialTokens() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--someOption", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--someOption", "--");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean flag");
        String[] arguments = {"-a", "-b", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("-a", "-b", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}