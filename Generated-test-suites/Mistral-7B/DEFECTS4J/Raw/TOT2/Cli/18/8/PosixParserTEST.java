import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test case for flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"appName", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with one option.
     */
    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("option1", true, "o")
                .desc("Option description");
        String[] arguments = new String[]{"appName", "--option1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "-o");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with option having an argument.
     */
    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("option1", true, "o")
                .desc("Option description")
                .longOpt("longOption1")
                .valueSeparator('=')
                .requiredArg();
        String[] arguments = new String[]{"appName", "--option1=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "-o", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .desc("Option 1 description")
                .longOpt("longOption1");
        options.addOption("option2", true, "o2")
                .desc("Option 2 description")
                .longOpt("longOption2");
        String[] arguments = new String[]{"appName", "--option1", "--longOption1", "--option2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "-o1", "--longOption1", "-o2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with a double hyphen and an option.
     */
    @Test
    public void testFlattenDoubleHyphenAndOption() {
        Options options = new Options();
        options.addOption("option1", true, "o")
                .desc("Option description");
        String[] arguments = new String[]{"appName", "--", "--option1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "--", "-o");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with an option followed by
     * a double hyphen and another option.
     */
    @Test
    public void testFlattenOptionDoubleHyphenOption() {
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .desc("Option 1 description")
                .longOpt("longOption1");
        options.addOption("option2", true, "o2")
                .desc("Option 2 description")
                .longOpt("longOption2");
        String[] arguments = new String[]{"appName", "--option1", "--", "--option2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "-o1", "--", "-o2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with an option having multiple arguments.
     */
    @Test
    public void testFlattenOptionMultipleArguments() {
        Options options = new Options();
        options.addOption("option1", true, "o")
                .desc("Option description")
                .valueSeparator(' ')
                .requiredArg();
        String[] arguments = new String[]{"appName", "--option1=arg1 arg2 arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("appName", "-o", "arg1", "arg2", "arg3");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test case for flatten method with an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = new String[]{"appName", "--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test case for flatten method with an empty argument list.
     */
    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }
}