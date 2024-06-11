import org.apache.commons.cli.*;
import org.junit.*;

public class PosixParserTest {

    @Test
    public void testFlatten_withValidOptions_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

    @Test
    public void testFlatten_withStopAtNonOption_returnsFlattenedArrayStoppingAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "nonOption"};
        String[] expected = {"-a", "nonOption"};
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(result, expected);
    }

    @Test
    public void testFlatten_withLongOption_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"--optionA"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

    @Test
    public void testFlatten_withLongOptionAndValue_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"--optionA=value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

    @Test
    public void testFlatten_withSingleHyphen_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

    @Test
    public void testFlatten_withInvalidOption_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();

        String[] args = {"-b"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

    @Test
    public void testFlatten_withMultipleOptions_returnsFlattenedArray() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "--optionB=value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(result, args);
    }

}