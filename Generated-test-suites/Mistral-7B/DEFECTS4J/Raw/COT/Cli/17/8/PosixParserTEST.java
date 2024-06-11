import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] args = {"arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"-o"};
        Options options = new Options();
        options.addOption("o", true, "output");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        String[] args = {"-o", "output.txt"};
        Options options = new Options();
        options.addOption("o", true, "output");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-o", "output.txt", "-i", "input.txt"};
        Options options = new Options();
        options.addOption("o", true, "output");
        options.addOption("i", true, "input");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt", "-i", "input.txt"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-z"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_MultipleInvalidOptions() {
        String[] args = {"-z", "-x"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_DoubleDashWithArgument() {
        String[] args = {"--", "arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-o", "output.txt", "arg"};
        Options options = new Options();
        options.addOption("o", true, "output");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_MultipleInvalidOptions() {
        String[] args = {"-z", "-x"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOption() {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithValue() {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[0], result);
    }
}