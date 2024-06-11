import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        Options options = new Options();
        String[] args = new String[0];

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList();

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testSingleArg() {
        Options options = new Options();
        String[] args = {"arg"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("arg");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testDoubleHyphen() {
        Options options = new Options();
        String[] args = {"--"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] args = {"-o", "value"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-o", "value");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testSingleOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        String[] args = {"-o", "value1", "value2"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-o", "value1", "value2");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("o", "option1", true, "Option1 description");
        options.addOption("a", "option2", true, "Option2 description");
        String[] args = {"-o", "val1", "-a", "val2"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-o", "val1", "-a", "val2");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", "Long option description", false, null);
        String[] args = {"--longOption"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", "Long option description", true, "Long option value description");
        String[] args = {"--longOption=value"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption", "value");

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        Options options = new Options();
        String[] args = {"-x"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testInvalidLongOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.fail("Expected ParseException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOptions() {
        String[] args = {"arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(null, args, false);

        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, null, false);

        Assert.fail("Expected IllegalArgumentException");
    }
}