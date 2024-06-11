import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with an argument");
        options.addOption("c", 3, "Option C with an argument");
    }

    @Test
    public void testFlatten_EmptyArguments() throws ParseException {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleDash() throws ParseException {
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_DoubleDash() throws ParseException {
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_SingleOption() throws ParseException {
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument() throws ParseException {
        String[] arguments = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments() throws ParseException {
        String[] arguments = {"-b", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-b", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments_MultipleOccurrences() throws ParseException {
        String[] arguments = {"-c", "arg1", "--c", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-c", "arg1", "-c", "arg2"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() throws ParseException {
        String[] arguments = {"-a", "-b", "--c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "--c"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "-b", "--", "nonOption", "--c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List expected = Arrays.asList("--", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() throws ParseException {
        String[] arguments = {"-z"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstToken_SingleCharacterOption() throws ParseException {
        String token = "-a";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List expected = Arrays.asList("-a");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstToken_MultiCharacterOption() throws ParseException {
        String token = "-abc";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List expected = Arrays.asList("-a", "-b", "-c");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstToken_MultiCharacterOptionWithArgument() throws ParseException {
        String token = "-ab";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List expected = Arrays.asList("-a", "-b");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstToken_InvalidOption() throws ParseException {
        String token = "-z";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        Assert.fail();
    }

    private List getTokens() {
        return ((PosixParser) this.parser).getTokens();
    }
}