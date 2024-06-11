import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 3, "Option C with argument");

        String[] args = {"-a", "-b", "--c", "3"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-a", "-b", "--c", "3");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptions() {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");
        options.addOption("long-option-b", false, "Long Option B");

        String[] args = {"--long-option-a", "--long-option-b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--long-option-a", "--long-option-b");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_ShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-a", "-b");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 3, "Option B with argument");

        String[] args = {"-a", "--b", "3"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-a", "--b", "3");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "non-option"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList("-a", "non-option");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        Options options = new Options();

        String[] args = {"--", "--option", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--", "--option", "value");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        Options options = new Options();
        options.addOption("long-option", true, "Long Option");
        options.addOption("long-option-with-argument", true, "Long Option with argument");

        String[] args = {"--long-option", "--long-option-with-argument=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--long-option", "--long-option-with-argument=value");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("long-option", true, "Long Option");
        options.addOption("long-option-with-multiple-arguments", 2, "Long Option with multiple arguments");

        String[] args = {"--long-option", "--long-option-with-multiple-arguments=arg1 arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--long-option", "--long-option-with-multiple-arguments=arg1 arg2");
        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }
}