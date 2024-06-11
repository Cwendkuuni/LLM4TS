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
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] arguments = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_SingleOptionValue() {
        String[] arguments = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "Option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option", "value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_SingleDash() {
        String[] arguments = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] arguments = {"--option1", "--option2", "--option3"};
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", false, "Option2 description");
        options.addOption("option3", true, "Option3 description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option1", "--option2", "--option3");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption", "value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_ShortOption() {
        String[] arguments = {"-o"};
        Options options = new Options();
        options.addOption("o", true, "Short option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-o");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        String[] arguments = {"-o", "value"};
        Options options = new Options();
        options.addOption("o", true, "Short option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-o", "value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_ShortOptionBeforeLongOption() {
        String[] arguments = {"-o", "--longOption"};
        Options options = new Options();
        options.addOption("o", true, "Short option description");
        options.addOption("longOption", true, "Long option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-o", "--longOption");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_ShortOptionWithValueBeforeLongOption() {
        String[] arguments = {"-o", "value", "--longOption"};
        Options options = new Options();
        options.addOption("o", true, "Short option description");
        options.addOption("longOption", true, "Long option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-o", "value", "--longOption");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_OptionsWithArguments() {
        String[] arguments = {"--option1", "arg1", "--option2", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", 1, "Option2 description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option1", "arg1", "--option2", "arg2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlatten_OptionsWithMultipleArguments() {
        String[] arguments = {"--option", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", 2, "Option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--option", "arg1", "arg2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidShortOption() {
        String[] arguments = {"-x"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidShortOptionWithValue() {
        String[] arguments = {"-x", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"--option", "arg1", "-", "nonOption", "--option2"};
        Options options = new Options();
        options.addOption("option", true, "Option description");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("--option", "arg1", "--option2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }
}