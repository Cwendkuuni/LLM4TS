import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expectedArgs = {};
        assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_SingleArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expectedArgs = {"arg1"};
        assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_SpecialTokens() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar=value", "--baz=value1,value2"};
        String[] expectedArgs = {"--foo", "--bar", "value", "--baz", "value1", "value2"};
        assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"--", "-a", "arg1", "-b", "--", "-c"};
        String[] expectedArgs = {"--", "-a", "arg1", "--", "-c"};
        assertArrayEquals(expectedArgs, parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        String[] expectedArgs = {"--invalidOption"};
        assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testFlatten_InvalidOptionWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        parser.flatten(options, args, true);
    }

    @Test
    public void testBurstToken_ValidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";
        String[] expectedArgs = {"-a"};
        assertArrayEquals(expectedArgs, parser.burstToken(token, false));
    }

    @Test
    public void testBurstToken_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "-i";
        String[] expectedArgs = {"-i"};
        assertArrayEquals(expectedArgs, parser.burstToken(token, false));
    }

    @Test
    public void testBurstToken_OptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";
        String arg = "arg";
        String[] expectedArgs = {"-a", "arg"};
        assertArrayEquals(expectedArgs, parser.burstToken(token + arg, false));
    }

    @Test
    public void testBurstToken_InvalidOptionWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-i";
        parser.burstToken(token, true);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        Assert.assertArrayEquals(new String[]{}, expected, actual);
    }
}