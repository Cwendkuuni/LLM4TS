import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenWithNoOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        String[] arguments = {"-b"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithOptionArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "");
        String[] arguments = {"-a", "arg"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithOptionArgumentSeparated() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "");
        String[] arguments = {"-a", "arg"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithNonOptionStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "");
        String[] arguments = {"-a", "arg", "non-option"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "arg", "--", "non-option"}, result);
    }

    @Test
    public void testFlattenWithNonOptionNotStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "");
        String[] arguments = {"-a", "arg", "non-option"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "arg", "non-option"}, result);
    }

    @Test
    public void testFlattenWithShortOptionBurst() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        String[] arguments = {"-ab"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlattenWithInvalidShortOptionBurstStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        String[] arguments = {"-ab", "non-option"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "--", "non-option"}, result);
    }

    @Test
    public void testFlattenWithInvalidShortOptionBurstNotStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        String[] arguments = {"-ab", "non-option"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "non-option"}, result);
    }
}