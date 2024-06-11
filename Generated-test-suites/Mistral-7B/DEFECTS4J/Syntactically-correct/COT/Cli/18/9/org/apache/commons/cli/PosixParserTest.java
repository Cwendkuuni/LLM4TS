import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();

        String[] expected = new String[0];
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SimpleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, null, "Option 1");
        options.addOption("option2", true, "value", "Option 2 with value");

        String[] expected = {"option1", "option2"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--long-option-with-value=value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "value", "Long option with value");

        String[] expected = {"longOptionWithValue", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ShortOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "--option1", "-v", "--value"};
        Options options = new Options();
        options.addOption("o", false, null, "Option 1");
        options.addOption("v", true, null, "Value");

        String[] expected = {"o", "option1", "v", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ShortWithLongOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "--option1", "--longOption=value"};
        Options options = new Options();
        options.addOption("o", false, null, "Option 1");
        options.addOption("longOption", true, null, "Long option");

        String[] expected = {"o", "option1", "longOption", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        Options options = new Options();

        String[] expected = {"--"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalidOption"};
        Options options = new Options();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--option1", "-a", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, null, "Option 1");

        String[] expected = {"option1", "-a"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionWithValue=value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "value", "Long option with value");

        String[] expected = {"longOptionWithValue", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionWithValue1=value1", "--longOptionWithValue2=value2"};
        Options options = new Options();
        options.addOption("longOptionWithValue", List.of("value1", "value2"), true, "Long option with multiple values");

        String[] expected = {"longOptionWithValue", "value1", "value2"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-v", "value"};
        Options options = new Options();
        options.addOption("v", true, null, "Value");

        String[] expected = {"v", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ShortOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-v", "value1", "value2"};
        Options options = new Options();
        options.addOption("v", List.of("value1", "value2"), true, "Value");

        String[] expected = {"v", "value1", "value2"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionWithValue=value", "-a"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "value", "Long option with value");

        String[] expected = {"longOptionWithValue", "value", "-a"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShortOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "--option1", "-a"};
        Options options = new Options();
        options.addOption("o", false, null, "Option 1");

        String[] expected = {"o", "option1", "-a"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionWithValue1=value1", "--longOptionWithValue2=value2", "-a"};
        Options options = new Options();
        options.addOption("longOptionWithValue", List.of("value1", "value2"), true, "Long option with multiple values");

        String[] expected = {"longOptionWithValue", "value1", "value2", "-a"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShortOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-v", "value", "--option1"};
        Options options = new Options();
        options.addOption("v", true, null, "Value");

        String[] expected = {"v", "value", "option1"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }
}