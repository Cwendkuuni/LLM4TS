import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleCase() {
        String[] args = {"-a", "--optionB", "123", "--optionC=value", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", false, "Option B");
        options.addOption("optionC", true, "Option C with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--optionB", "-a", "--optionC=value", "123", "--");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_ThrowsException() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_LongOption_Value_Case() {
        String[] args = {"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--longOption=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption_Case() {
        String[] args = {"--optionA", "-b", "--optionB", "-c", "--"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList("--optionA", "-b", "--optionB", "--");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_BurstedOption_Case() {
        String[] args = {"--optionX=value1=value2"};
        Options options = new Options();
        options.addOption("optionX", true, "Option X with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--optionX=value1", "--optionX=value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_DoubleDash_ThrowsException() {
        String[] args = {"--", "--optionA"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_EmptyOption_Value_Case() {
        String[] args = {"--optionX="};
        Options options = new Options();
        options.addOption("optionX", true, "Option X with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--optionX");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithArg_Case() {
        String[] args = {"--optionX", "value"};
        Options options = new Options();
        options.addOption("optionX", true, "Option X with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--optionX=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithArgs_Case() {
        String[] args = {"--optionX", "value1", "value2"};
        Options options = new Options();
        options.addOption("optionX", null, "Option X with multiple values");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--optionX=value1", "--optionX=value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}