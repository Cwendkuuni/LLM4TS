import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        String[] arguments = new String[]{"--optionA", "-b", "value"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--optionA", "-b", "value"}, flattened);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));

        String[] arguments = new String[]{"--optionA", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--optionA", "nonOption"}, flattened);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        String[] arguments = new String[]{"--optionA", "-b", "value", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--optionA", "-b", "value", "nonOption"}, flattened);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        String[] arguments = new String[]{"-abvalue"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value"}, flattened);
    }

    @Test
    public void testFlattenWithBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        String[] arguments = new String[]{"-abvalue", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "nonOption"}, flattened);
    }

}