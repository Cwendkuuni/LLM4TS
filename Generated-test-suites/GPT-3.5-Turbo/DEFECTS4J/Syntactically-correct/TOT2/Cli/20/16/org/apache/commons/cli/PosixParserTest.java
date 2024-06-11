import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testInit() {
        parser.init();
        Assert.assertFalse(parser.isEatTheRest());
        Assert.assertTrue(parser.getTokens().isEmpty());
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "value", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertEquals("Incorrect flattened arguments length", 3, result.length);
        Assert.assertEquals("Incorrect argument value", "-a", result[0]);
        Assert.assertEquals("Incorrect argument value", "value", result[1]);
        Assert.assertEquals("Incorrect argument value", "-b", result[2]);
    }

    @Test
    public void testProcessOptionToken() {
        parser.setOptions(options);
        parser.processOptionToken("-a", false);

        Assert.assertNotNull("Current option should not be null", parser.getCurrentOption());
        Assert.assertEquals("Incorrect current option", "-a", parser.getCurrentOption().getOpt());
    }

    @Test
    public void testProcess() {
        parser.process("value");

        Assert.assertTrue(parser.isEatTheRest());
        Assert.assertEquals("Incorrect token value", "--", parser.getTokens().get(0));
        Assert.assertEquals("Incorrect token value", "value", parser.getTokens().get(1));
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        parser.burstToken(token, false);

        Assert.assertEquals("Incorrect number of tokens", 2, parser.getTokens().size());
        Assert.assertEquals("Incorrect token value", "-a", parser.getTokens().get(0));
        Assert.assertEquals("Incorrect token value", "-bc", parser.getTokens().get(1));
    }
}