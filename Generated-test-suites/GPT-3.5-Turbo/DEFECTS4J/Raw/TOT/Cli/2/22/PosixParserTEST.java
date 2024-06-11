import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setup() {
        posixParser = new PosixParser();
        options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testFlatten_WhenNoOptions_ReturnsEmptyArray() {
        String[] arguments = new String[]{};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenNoArguments_ReturnsEmptyArray() {
        String[] arguments = new String[]{};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenOptionExists_AddsOptionToFlattenedArguments() {
        String[] arguments = new String[]{"-a"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenOptionHasArgument_AddsOptionAndArgumentToFlattenedArguments() {
        String[] arguments = new String[]{"-b", "value"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-b", "value"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenStopAtNonOptionSet_AddsRemainingArgumentsToFlattenedArguments() {
        String[] arguments = new String[]{"-a", "value", "nonOption"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "value", "nonOption"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenHyphenPassed_AddsHyphenToFlattenedArguments() {
        String[] arguments = new String[]{"-"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenTokenIsSpecialToken_AddsTokenToFlattenedArguments() {
        String[] arguments = new String[]{"--", "token"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--", "token"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenTokenStartsWithHyphen_BurstsTokenIntoOptionsAndArguments() {
        String[] arguments = new String[]{"-abc", "value"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "value"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenOptionDoesNotExist_BurstsTokenAndAddsIndividualOptions() {
        String[] arguments = new String[]{"-d", "-e"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-d", "-e"}, flattenedArguments);
    }

    @Test
    public void testFlatten_WhenOptionDoesNotExistAndStopAtNonOptionSet_AddsRemainingArguments() {
        String[] arguments = new String[]{"-d", "-e", "nonOption"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-d", "-e", "nonOption"}, flattenedArguments);
    }
}