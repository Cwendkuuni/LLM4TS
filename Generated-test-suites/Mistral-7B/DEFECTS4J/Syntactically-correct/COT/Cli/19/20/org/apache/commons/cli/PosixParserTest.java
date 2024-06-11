import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();
    }

    

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "-b", "-c", "--bar=value"};
        String[] expected = {"--foo", "-b", "-c", "--bar", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "-b", "-c", "--bar=value", "arg"};
        String[] expected = {"--foo", "-b", "-c", "--bar", "value", "arg"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        String[] args = {"-a", "-b", "-c", "--optionA", "--optionB=value"};
        String token = "a";
        parser.init();
        parser.processOptionToken(token, false);
        Assert.assertNull(parser.currentOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0));

        token = "optionB";
        parser.init();
        Option option = new Option("optionB", false, false, "value");
        options.addOption(option);
        parser.processOptionToken(token, false);
        Assert.assertEquals(option, parser.currentOption);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-optionB", parser.tokens.get(1));
    }

    @Test(expected = ParseException.class)
    public void testProcessInvalidOptionToken() {
        String[] args = {"--invalidOption"};
        parser.init();
        parser.processOptionToken("invalidOption", false);
    }

    @Test(expected = ParseException.class)
    public void testProcessOptionWithLongArgument() {
        String[] args = {"--longOption=longArgument"};
        parser.init();
        parser.process(args[0]);
        Option option = new Option("longOption", true, false, "longArgument");
        options.addOption(option);
        parser.processOptionToken("longOption", false);
        Assert.assertEquals(option, parser.currentOption);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--longOption", parser.tokens.get(0));
        Assert.assertEquals("longArgument", parser.tokens.get(1));

        parser.init();
        parser.process(args[0]);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--longOption=longArgument", parser.tokens.get(0));
    }

    @Test
    public void testBurstTokenSimple() {
        String token = "--foo-bar";
        parser.init();
        parser.burstToken(token, false);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-foo", parser.tokens.get(0));
        Assert.assertEquals("-bar", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--foo-bar-arg";
        parser.init();
        parser.burstToken(token, true);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("--foo-bar", parser.tokens.get(0));
        Assert.assertEquals("arg", parser.tokens.get(2));
    }

    @Test
    public void testBurstTokenWithOption() {
        String token = "--foo-bar-option";
        parser.init();
        Option option = new Option("option", true, false, "value");
        options.addOption(option);
        parser.burstToken(token, false);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("-foo-bar-option", parser.tokens.get(0));
        Assert.assertEquals("-option", parser.tokens.get(1));
        Assert.assertEquals("value", parser.tokens.get(2));
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenWithInvalidOption() {
        String token = "--invalidOption";
        parser.init();
        parser.burstToken(token, false);
    }

    @Test
    public void testGobble() {
        String[] args = {"--foo", "--bar", "arg"};
        parser.init();
        parser.flatten(options, args, false);
        parser.gobble(Arrays.asList(args).iterator());
        Assert.assertEquals(4, parser.tokens.size());
        Assert.assertEquals("--foo", parser.tokens.get(0));
        Assert.assertEquals("--bar", parser.tokens.get(1));
        Assert.assertEquals("arg", parser.tokens.get(3));
    }

    
}