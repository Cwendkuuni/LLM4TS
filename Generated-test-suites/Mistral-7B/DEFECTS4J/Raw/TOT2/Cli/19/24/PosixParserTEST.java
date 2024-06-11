import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"--option1", "-o", "value1", "--option2", "-v"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option1", "-o", "value1", "--option2", "-v");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenNoOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "lO");
        String[] arguments = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("longOption", false, "lO");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", false, "o2");
        String[] arguments = {"-o1", "value1", "--o2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-o1", "value1", "--o2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] arguments = {"--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] arguments = {"--option=value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option", "value1", "value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "lO");
        String[] arguments = {"--longOption=value,arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption", "value,arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidLongOption() {
        Options options = new Options();
        String[] arguments = {"--invalidLongOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOption() {
        Options options = new Options();
        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingValueForOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] arguments = {"--option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}