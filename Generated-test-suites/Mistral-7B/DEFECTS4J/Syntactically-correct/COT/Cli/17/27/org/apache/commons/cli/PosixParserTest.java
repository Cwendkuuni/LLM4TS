import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleCase() {
        Options options = new Options();
        String[] args = {"--option1", "value1", "--option2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--option1", "value1", "--option2", "value2"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        String[] args = {"-", "--option1", "value1", "--option2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--", "-", "--option1", "value1", "--option2", "value2"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", 1, "i", "i");
        String[] args = {"--option1", "true", "--option2", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--option1", "true", "--option2", "1"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_OptionWithArgs() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", "arg1 arg2 arg3", "i");
        String[] args = {"--option1", "--option2", "arg1 arg2 arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--option1", "--option2", "arg1", "arg2", "arg3"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOptionWithValue() {
        Options options = new Options();
        String[] args = {"--invalidOption=value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--option1", "value1", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(new String[]{"--option1", "value1", "--", "nonOption"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}