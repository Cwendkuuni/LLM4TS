import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_LongOptionWithNoValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo"};
        String[] expectedTokens = {"--foo"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo=value"};
        String[] expectedTokens = {"--foo", "value"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-"};
        String[] expectedTokens = {"-"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithNoValue_ValidOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a"};
        String[] expectedTokens = {"-a"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithNoValue_InvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-c"};
        String[] expectedTokens = {"-c"};

        String[] actualTokens = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithNoValue_InvalidOption_ContinueFlattening() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-c"};
        String[] expectedTokens = {"-c"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument_InvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expectedTokens = {"-a", "value"};

        String[] actualTokens = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument_InvalidOption_ContinueFlattening() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expectedTokens = {"-a", "value"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"nonOption"};
        String[] expectedTokens = {"--", "nonOption"};

        String[] actualTokens = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_NonOptionToken_ContinueFlattening() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("foo").desc("foo option").build());
        options.addOption(Option.builder("b").longOpt("bar").desc("bar option").hasArg().build());
        PosixParser parser = new PosixParser();

        String[] arguments = {"nonOption"};
        String[] expectedTokens = {"nonOption"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedTokens, actualTokens);
    }
}