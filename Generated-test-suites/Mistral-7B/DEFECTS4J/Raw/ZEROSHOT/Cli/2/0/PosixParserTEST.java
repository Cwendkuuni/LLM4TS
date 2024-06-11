import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo"};
        Parser parser = new PosixParser();
        String[] expected = {"--foo"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-f", "bar"};
        Parser parser = new PosixParser();
        String[] expected = {"-f", "bar"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithNoValue() {
        Options options = new Options();
        options.addOption("foo");
        String[] args = {"-f"};
        Parser parser = new PosixParser();
        String[] expected = {"-f"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("foo");
        options.addOption("bar");
        String[] args = {"-f", "-b"};
        Parser parser = new PosixParser();
        String[] expected = {"-f", "-b"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithValues() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] args = {"-f", "value1", "-b", "value2"};
        Parser parser = new PosixParser();
        String[] expected = {"-f", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOptionName", true, null);
        String[] args = {"--longOptionName", "value"};
        Parser parser = new PosixParser();
        String[] expected = {"--longOptionName", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOptionName", true, null);
        String[] args = {"--longOptionName=value"};
        Parser parser = new PosixParser();
        String[] expected = {"--longOptionName", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleWords() {
        Options options = new Options();
        options.addOption("longOptionName", true, null);
        String[] args = {"--longOptionName=multiple words"};
        Parser parser = new PosixParser();
        String[] expected = {"--longOptionName", "multiple words"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] args = {"-"};
        Parser parser = new PosixParser();
        String[] expected = {"-"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] args = {"--"};
        Parser parser = new PosixParser();
        String[] expected = {"--"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid"};
        Parser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenInvalidOptionWithValue() {
        Options options = new Options();
        String[] args = {"--invalid=value"};
        Parser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
        } catch (IllegalArgumentException e) {
            List expected = Arrays.asList("--invalid", "value");
            List result = Arrays.asList(parser.tokens.toArray(new String[0]));
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo", "value", "-a", "value2"};
        Parser parser = new PosixParser();
        String[] expected = {"--foo", "value"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithLongOption() {
        Options options = new Options();
        options.addOption("longOptionName", true, null);
        String[] args = {"--longOptionName=value", "--option2", "value2"};
        Parser parser = new PosixParser();
        String[] expected = {"--longOptionName", "value"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] args = {"-foobar"};
        Parser parser = new PosixParser();
        String[] expected = {"-f", "o", "o", "b", "a", "r"};
        parser.burstToken("-foobar", false);
        String[] result = (String[]) parser.tokens.toArray(new String[0]);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-foobar"};
        Parser parser = new PosixParser();
        String[] expected = {"--foo", "-f", "o", "o", "b", "a", "r"};
        parser.burstToken("-foobar", true);
        String[] result = (String[]) parser.tokens.toArray(new String[0]);
        Assert.assertArrayEquals(expected, result);
    }
}