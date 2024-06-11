import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions() throws ParseException {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"test", "arg1", "arg2"};
        String[] expected = {"test", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"--optionA"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] expected = {"--optionA", "arg"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArgs() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", "arg1,arg2,arg3", "Option A with args");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2", "arg3"};
        String[] expected = {"--optionA", "arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ArgAfterOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg", "arg2"};
        String[] expected = {"--optionA", "arg", "arg2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash() throws ParseException {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        String[] expected = {"--"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() throws ParseException {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-z"};
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_OptionWithoutArg_StopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"test", "-a", "arg1", "arg2"};
        parser.flatten(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidArgType() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", "arg1,arg2,arg3", "Option A with args (incorrect type)");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2", "arg3", "arg4"};
        parser.flatten(options, args, false);
    }

}