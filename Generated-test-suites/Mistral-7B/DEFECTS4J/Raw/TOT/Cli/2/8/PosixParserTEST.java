import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, parsedArgs);
    }

    @Test
    public void testSimpleMultiValueOption() {
        String[] args = {"--option", "value1", "--option", "value2"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option", "value1", "--option", "value2"}, parsedArgs);
    }

    @Test
    public void testLongOption() {
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "This is a long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, parsedArgs);
    }

    @Test
    public void testHiddenOption() {
        String[] args = {"--no-hide-options", "--secret", "value"};
        Options options = new Options();
        options.addOption("secret", true, "This is a hidden option");
        options.option("secret", false, "Do not print this option in help");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--no-hide-options", "--secret", "value"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.fail("Expected ParseException, but no exception thrown");
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"--option", "value", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, parsedArgs);
        Assert.assertArrayEquals(Arrays.asList("nonOption").toArray(new String[0]), parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testBurstedOption() {
        String[] args = {"--long-option-with-value", "value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "This is a long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-value", "value"}, parsedArgs);
    }

    @Test
    public void testBurstedOptionWithArgument() {
        String[] args = {"--long-option-with-value-and-arg", "argValue"};
        Options options = new Options();
        options.addOption("longOptionWithValueAndArg", true, "This is a long option");
        options.addOption("longOptionWithValueAndArg", true, "Another description for the option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-value", "argValue"}, parsedArgs);
    }

    @Test
    public void testBurstedOptionWithArgs() {
        String[] args = {"--long-option-with-multiple-args", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOptionWithMultipleArgs", true, "This is a long option with multiple arguments");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-multiple-args", "arg1", "arg2"}, parsedArgs);
    }

    @Test
    public void testBurstedInvalidOption() {
        String[] args = {"--invalidLongOption"};
        Options options = new Options();
        options.addOption("validOption", true, "This is a valid option");

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail("Expected ParseException, but no exception thrown");
        } catch (ParseException e) {
            // expected
        }
    }
}