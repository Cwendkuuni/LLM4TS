import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1"}, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o", "value1"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "value1"}, result);
    }

    @Test
    public void testFlattenOptionWithLongName() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        String[] arguments = {"-l", "value1"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"longOption1", "value1"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "i");
        String[] arguments = {"-o", "-i"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "option2"}, result);
    }

    @Test
    public void testFlattenOptionWithValueLongName() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        String[] arguments = {"-l", "value1"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"longOption1", "value1"}, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithValues() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "i");
        String[] arguments = {"-o", "value1", "-i", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"option1", "value1", "option2", "value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-z"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.fail();
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o", "-i", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        List expected = Arrays.asList("o", "i", "arg1");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}