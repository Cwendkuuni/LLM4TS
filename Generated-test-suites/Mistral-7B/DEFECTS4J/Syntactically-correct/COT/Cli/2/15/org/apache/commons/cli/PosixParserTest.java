import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo=bar", "--bar", "-c", "-d", "-e", "-f", "-g=h", "--", "-i", "-j"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--foo", "bar", "--", "-c", "-d", "-e", "-f", "-g", "h", "--", "-i", "-j"));
        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--invalid-option", "-c", "-d", "-e", "-f", "-g=h", "--", "-i", "-j"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo=bar", "--bar", "-c", "-d", "-e", "-f", "-g=h", "--", "-i", "-j"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--foo=bar", "-c", "-d", "-e", "-f", "-g=h", "--", "-i", "-j"));
        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArguments);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String token = "-abcde";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("-a", "-b", "-c", "-d", "-e"));
        Assert.assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        String token = "-abcde";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--", "-abcde"));
        Assert.assertEquals(expected, parser.tokens);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenWithInvalidToken() {
        Options options = new Options();
        String token = "invalid-token";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        Assert.assertFalse(parser.eatTheRest);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);
    }
}