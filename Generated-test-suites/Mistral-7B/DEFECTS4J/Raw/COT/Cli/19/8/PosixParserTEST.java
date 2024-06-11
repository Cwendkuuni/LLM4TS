import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c=value", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList("--foo", "--bar", "value", "-a", "-b", "-c", "value", "--", "arg1", "arg2");
        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c=value", "--", "arg1", "arg2", "nonOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", 1, "b");

        PosixParser parser = new PosixParser();
        String[] args = {"-f", "--foo", "-b", "1", "-b", "2", "--bar", "value", "--"};
        parser.flatten(options, args, false);

        Assert.assertEquals("foo", parser.currentOption.getLongOpt());
        Assert.assertTrue(parser.currentOption.isSet());
    }

    @Test
    public void testBurstTokenWithValidOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", 1, "b");

        PosixParser parser = new PosixParser();
        String[] args = {"-f-b", "1", "-f-b", "2"};
        parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList("-f--foo", "-b", "1", "-f--foo", "-b", "2");
        List<String> parsedArgs = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        Assert.assertEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-x-y", "-z"};
        parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList("--", "-x--y", "-z");
        List<String> parsedArgs = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        Assert.assertEquals(expectedArgs, parsedArgs);
    }
}