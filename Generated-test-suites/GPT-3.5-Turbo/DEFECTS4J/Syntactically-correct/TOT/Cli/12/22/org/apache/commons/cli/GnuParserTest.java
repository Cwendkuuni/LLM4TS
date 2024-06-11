import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten_WithOptionsAndArguments_ReturnsFlattenedArguments() {
        Options options = new Options();
        String[] arguments = {"-abc", "value1", "--option", "value2", "non-option"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"-abc", "value1", "--option", "value2", "non-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithNoOptionsAndArguments_ReturnsOriginalArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithStopAtNonOptionTrue_ReturnsFlattenedArgumentsUntilNonOptionEncountered() {
        Options options = new Options();
        options.addOption("option");

        String[] arguments = {"-abc", "value1", "--option", "value2", "non-option", "arg1"};
        boolean stopAtNonOption = true;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"-abc", "value1", "--option", "value2", "non-option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithArgumentsContainingHyphens_ReturnsCorrectlyFlattenedArguments() {
        Options options = new Options();
        options.addOption("abc");

        String[] arguments = {"-abc", "-def", "--option", "-ghi", "arg1"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"-abc", "-def", "--option", "-ghi", "arg1"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithArgumentsContainingDoubleHyphens_ReturnsCorrectlyFlattenedArguments() {
        Options options = new Options();
        options.addOption("option");

        String[] arguments = {"--abc", "value1", "--option", "value2", "--", "arg1"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"--abc", "value1", "--option", "value2", "--", "arg1"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionsAndArguments_ReturnsFlattenedArgumentsUntilDoubleHyphensEncountered() {
        Options options = new Options();
        options.addOption("option");

        String[] arguments = {"-abc", "value1", "--option", "value2", "--", "arg1", "arg2"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"-abc", "value1", "--option", "value2", "--", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithArgumentsContainingIncompleteOption_ReturnsFlattenedArgumentsWithIncompleteOption() {
        Options options = new Options();
        options.addOption("abc");

        String[] arguments = {"-abc=value", "-d", "arg1"};
        boolean stopAtNonOption = false;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"-abc=value", "-d", "arg1"};
        Assert.assertArrayEquals(expected, result);
    }

}