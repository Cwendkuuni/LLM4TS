import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--help", "-a", "-b", "-c=d"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--help", "-a", "-b", "-c=d"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--help", "-a", "-b", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--help", "-a", "-b", "--", "arg1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid", "-a", "-b"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--invalid", "-a", "-b", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);
    }

    @Test
    public void testBurstTokenSimple() {
        Options options = new Options();
        String[] args = {"--foo=bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testBurstTokenMultipleArgs() {
        Options options = new Options();
        String[] args = {"--foo=bar1,bar2,bar3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "bar1", "bar2", "bar3"}, result);
    }

    @Test
    public void testBurstTokenNoOption() {
        Options options = new Options();
        String[] args = {"--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }
}