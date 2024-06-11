import org.apache.commons.cli. Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyOptionsAndArguments() {
        String[] arguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
        parser.init();
    }

    @Test
    public void testFlattenWithOptionsAndArguments() {
        String[] arguments = {"-a", "--optionB", "valueB", "-c=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c=valueC"}, result);
        parser.init();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-a", "--optionB", "valueB", "-c=valueC", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List expected = Arrays.asList(new String[]{"-a", "--optionB", "valueB", "-c=valueC", "nonOption"});
        Assert.assertArrayEquals((String[]) expected.toArray(new String[expected.size()]), result);
        parser.init();
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = {"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithDoubleDash() {
        String[] arguments = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstTokenWithOption() {
        String token = "-ab";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List expected = Arrays.asList(new String[]{"-a", "-b"});
        Assert.assertEquals(expected, parser.tokens);
        parser.init();
    }

    @Test
    public void testBurstTokenWithNonOption() {
        String token = "nonOption";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List expected = Arrays.asList(new String[]{"--nonOption"});
        Assert.assertEquals(expected, parser.tokens);
        parser.init();
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-abcd";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);
        List expected = Arrays.asList(new String[]{"--abcd"});
        Assert.assertEquals(expected, parser.tokens);
        parser.init();
    }
}