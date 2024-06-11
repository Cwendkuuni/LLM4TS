import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SingleArg() {
        String[] args = {"arg1"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        String[] args = {"--arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen_With_Argument() {
        String[] args = {"--arg=value"};
        Options options = new Options();
        Options argOption = new Options();
        argOption.addOption("arg", true, "description");
        options.addOptions(argOption);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--arg", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.fail("Expected ParseException but got " + Arrays.toString(result));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"arg1", "--arg", "arg3"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--arg", "arg3"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOption_With_Argument() {
        String[] args = {"--long-option=value"};
        Options options = new Options();
        Options longOption = new Options();
        longOption.addOption("longOption", true, "description");
        options.addOptions(longOption);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }
}