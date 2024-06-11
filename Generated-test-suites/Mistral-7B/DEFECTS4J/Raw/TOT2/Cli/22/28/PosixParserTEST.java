import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlattenSingleValueOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option1", "--option2", "--option3"};
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", true, "Option2 description");
        options.addOption("option3", true, "Option3 description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2", "--option3"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalid"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.fail(); // Test should fail with ParseException
    }

    @Test
    public void testFlattenWithArg() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        options.addOption("option2", true, "Option2 description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value1", "--option2", "value2"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option=value with spaces"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value with spaces"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option", "value1", "--nonOption", "value2"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--option", "value1"}, result);
    }

    @Test
    public void testFlattenWithBurstedOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOption", "longValue", "--longOptionWithArg", "argValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        options.addOption("longOptionWithArg", true, "Long option with argument description");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "longValue", "--longOptionWithArg", "argValue"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonOptionToken() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option", "value1", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, true);
        Assert.fail(); // Test should fail with ParseException
    }
}