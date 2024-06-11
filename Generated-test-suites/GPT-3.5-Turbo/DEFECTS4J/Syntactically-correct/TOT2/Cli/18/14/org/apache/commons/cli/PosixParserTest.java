import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertNotNull(parser);
        Assert.assertEquals(parser.tokens.size(), 0);
        Assert.assertEquals(parser.eatTheRest, false);
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        option1.setArgs(1);
        options.addOption(option1);

        String[] args = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertEquals(result.length, 2);
        Assert.assertEquals(result[0], "-a");
        Assert.assertEquals(result[1], "value");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        tokens.add("abc");
        tokens.add("def");
        Iterator<String> iter = tokens.iterator();

        parser.gobble(iter);
        Assert.assertEquals(tokens.size(), 0);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        Assert.assertEquals(parser.tokens.get(0), "-");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        options.addOption(option1);

        parser.processOptionToken("-a", false);
        Assert.assertEquals(parser.currentOption, option1);
        Assert.assertEquals(parser.tokens.get(0), "-a");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        options.addOption(option1);

        parser.burstToken("-abc", false);
        Assert.assertEquals(parser.tokens.get(0), "-a");
        Assert.assertEquals(parser.tokens.get(1), "-b");
        Assert.assertEquals(parser.tokens.get(2), "-c");
    }
}