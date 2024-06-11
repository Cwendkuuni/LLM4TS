import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] arguments = {"--option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        String[] expected = {"--option"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleValueOption() {
        String[] arguments = {"--option", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        String[] expected = {"--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] arguments = {"--option1", "--option2=value", "--option3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        String[] expected = {"--option1", "--option2", "value", "--option3"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] arguments = {"--long-option", "--long-option=value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        String[] expected = {"--long-option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"--option", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        String[] expected = {"--option"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_BurstedOptions() {
        String[] arguments = {"-o", "--option", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        String[] expected = {"-o", "--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstedOptionsWithArgument() {
        String[] arguments = {"-o", "--option=value", "--option2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "This is an option");
        options.addOption("option2", true, "Another option");

        String[] expected = {"-o", "--option=value", "--option2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
}