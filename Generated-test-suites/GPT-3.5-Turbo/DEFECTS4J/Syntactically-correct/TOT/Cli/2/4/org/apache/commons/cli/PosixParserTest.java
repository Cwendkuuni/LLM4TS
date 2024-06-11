import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_WithStopAtNonOption_True() {
        String[] arguments = {"-a", "-b", "value1", "-c", "value2", "-d"};

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");
        Option option3 = new Option("c", true, "option c");
        Option option4 = new Option("d", false, "option d");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        String[] expected = {"-a", "-b", "value1", "-c", "value2", "-d"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithStopAtNonOption_False() {
        String[] arguments = {"-a", "-b", "value1", "-c", "value2", "-d"};

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");
        Option option3 = new Option("c", true, "option c");
        Option option4 = new Option("d", false, "option d");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        String[] expected = {"-a", "-b", "value1", "-c", "value2", "-d"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithInvalidArgumentBeforeValidOption() {
        String[] arguments = {"-a", "invalid", "-b", "value"};

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");

        options.addOption(option1);
        options.addOption(option2);

        String[] expected = {"-a", "invalid", "-b", "value"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithValidOptionsContainingEqualSign() {
        String[] arguments = {"--option1=value1", "--option2=value2"};

        Option option1 = Option.builder().longOpt("option1").hasArg(true).build();
        Option option2 = Option.builder().longOpt("option2").hasArg(true).build();

        options.addOption(option1);
        options.addOption(option2);

        String[] expected = {"--option1", "value1", "--option2", "value2"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithSingleHyphen() {
        String[] arguments = {"-"};

        String[] expected = {"-"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithValidShortOption() {
        String[] arguments = {"-a"};

        Option option = new Option("a", false, "option a");
        options.addOption(option);

        String[] expected = {"-a"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithInvalidShortOption() {
        String[] arguments = {"-z"};

        Option option = new Option("a", false, "option a");
        options.addOption(option);

        String[] expected = {"-z"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithBurstingRequired() {
        String[] arguments = {"-abc"};

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", false, "option b");
        Option option3 = new Option("c", false, "option c");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] expected = {"-a", "-b", "-c"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithBurstingAndInvalidOption() {
        String[] arguments = {"-zxy"};

        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", false, "option b");
        Option option3 = new Option("c", false, "option c");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] expected = {"-z", "-x", "-y"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_WithRemainingTokens() {
        String[] arguments = {"-a", "value1", "value2"};

        Option option = new Option("a", true, "option a");
        options.addOption(option);

        String[] expected = {"-a", "value1", "value2"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

}