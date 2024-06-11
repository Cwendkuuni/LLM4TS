import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"-a", "--b", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "1"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");

        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"--no-option", "-a", "--b", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--no-option", "-a", "--b", "1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // ParseException should be thrown
    }

    @Test
    public void testFlattenWithLongArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"-a", "--long-argument-for-option-b", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--long-argument-for-option-b", "1"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"-a", "-b", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "1"}, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"-a", "-b", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--"}, result);
    }
}