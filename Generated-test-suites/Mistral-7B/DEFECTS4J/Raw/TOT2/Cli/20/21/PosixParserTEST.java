import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", false, null, "A long option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenWithSingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-o"};
        Options options = new Options();
        options.addOption("o", true, null, "Output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenWithSingleCharacterOptionAndValue() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-o", "output.txt"};
        Options options = new Options();
        options.addOption("o", true, null, "Output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-o", "output.txt", "--longOption", "value"};
        Options options = new Options();
        options.addOption("o", true, null, "Output file");
        options.addOption("longOption", false, null, "A long option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt", "--longOption", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-x"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        // This test should throw ParseException
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--", "arg1", "--option", "value"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--", "arg1"}, result);
    }

    @Test
    public void testFlattenWithBurstedOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-co", "value"};
        Options options = new Options();
        options.addOption("c", true, null, "Compression level");
        options.addOption("o", true, null, "Output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-c", "value", "-o"}, result);
    }

    @Test
    public void testFlattenWithBurstedOptionAndValue() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-co", "value", "output.txt"};
        Options options = new Options();
        options.addOption("c", true, null, "Compression level");
        options.addOption("o", true, null, "Output file");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-c", "value", "-o", "output.txt"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndBurstedOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--", "-co", "value", "output.txt"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--", "-c"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndBurstedOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--", "-co", "value", "output.txt"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }
}