import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

  private Options options;

  public PosixParserTest() {
        options = new Options();
        options.addOption(Option.builder("a").hasArg().desc("Option a").build());
        options.addOption(Option.builder("b").desc("Option b").build());
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "value1", "-b", "value2", "nonOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ContinueAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "value1", "-b", "value2", "nonOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "value1", "-b"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option=value"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--option", "value"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--option"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-c"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-c"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "-b"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-ab", "value"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "-b", "value"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_ContinueAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-ab", "value"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "-b", "value"};
        Assert.assertArrayEquals(expected, result);
    }

}