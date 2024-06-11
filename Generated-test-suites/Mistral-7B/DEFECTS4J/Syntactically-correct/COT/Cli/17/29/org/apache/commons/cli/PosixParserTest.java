import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenWithSingleValueOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] args = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("a", true, "a help message");
        Option option2 = new Option("b", false, "a help message");
        options.addOption(option1);
        options.addOption(option2);
        String[] args = {"-a", "--b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "--b"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithTwoWordsOption() {
        Options options = new Options();
        String[] args = {"--two-words"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] args = {"-a", "this is a long option value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "this is a long option value"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithMultipleArguments() {
        Options options = new Options();
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] args = {"--", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--", "-a"}, result);
    }

}