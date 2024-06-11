import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer value");
        options.addOption("c", 2, "Option C with a string value");
        options.addOption("d", Option.builder("d").longOpt("double-option")
                .valueSeparator('=')
                .hasArg()
                .desc("An option with a long name and an argument")
                .build());

        String[] arguments = {"-a", "--double-option=value1", "-b", "2", "-c", "test", "--", "arg1", "arg2"};
        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--double-option=value1",
                "-b",
                "2",
                "-c",
                "test",
                "--",
                "arg1",
                "arg2"
        );
        PosixParser parser = new PosixParser();
        String[] resultTokens = parser.flatten(options, arguments, false);
        List<String> resultList = Arrays.asList(resultTokens);
        Assert.assertEquals(expectedTokens, resultList);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-z", "--"}; // unknown option 'z'
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with no argument");
        options.addOption("b", 1, "Option B with an integer argument");
        String[] arguments = {"-a", "arg1", "--b=5"};
        List<String> expectedTokens = Arrays.asList(
                "-a",
                "arg1",
                "--b",
                "5"
        );
        PosixParser parser = new PosixParser();
        String[] resultTokens = parser.flatten(options, arguments, false);
        List<String> resultList = Arrays.asList(resultTokens);
        Assert.assertEquals(expectedTokens, resultList);
    }

    @Test
    public void testFlattenWithLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with no argument");
        options.addOption("d", Option.builder("d")
                .longOpt("double-option")
                .valueSeparator('=')
                .hasArg()
                .desc("An option with a long name and an argument")
                .build());
        String[] arguments = {"--double-option=value1"};
        List<String> expectedTokens = Arrays.asList(
                "--double-option=value1"
        );
        PosixParser parser = new PosixParser();
        String[] resultTokens = parser.flatten(options, arguments, false);
        List<String> resultList = Arrays.asList(resultTokens);
        Assert.assertEquals(expectedTokens, resultList);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with no argument");
        String[] arguments = {"--", "arg1", "arg2"};
        List<String> expectedTokens = Arrays.asList(
                "--",
                "arg1",
                "arg2"
        );
        PosixParser parser = new PosixParser();
        String[] resultTokens = parser.flatten(options, arguments, true);
        List<String> resultList = Arrays.asList(resultTokens);
        Assert.assertEquals(expectedTokens, resultList);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with no argument");
        String[] arguments = {"--invalidOption"}; // invalid long option
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}