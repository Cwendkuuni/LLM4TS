import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.isEmpty());
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "-c"}, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        parser.options = options;

        parser.processOptionToken("-a", false);
        Assert.assertEquals("-a", parser.tokens.get(0));
        Assert.assertEquals(options.getOption("a"), parser.currentOption);

        parser.processOptionToken("-d", true);
        Assert.assertTrue(parser.eatTheRest);

    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        parser.options = options;

        parser.burstToken("-ab", false);
        Assert.assertEquals(options.getOption("a"), parser.currentOption);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0));
        Assert.assertEquals("-b", parser.tokens.get(1));

        parser.init();
        parser.burstToken("-ac", true);
        Assert.assertTrue(parser.eatTheRest);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-d", false);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-d", parser.tokens.get(0));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        parser.options = options;

        parser.currentOption = options.getOption("b");
        parser.process("value");
        Assert.assertEquals("value", parser.tokens.get(0));
        Assert.assertNull(parser.currentOption);

        parser.currentOption = options.getOption("c");
        parser.process("value");
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("value", parser.tokens.get(1));

        parser.currentOption = null;
        parser.process("value");
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iterator = Arrays.asList("value1", "value2").iterator();
        parser.eatTheRest = true;
        parser.gobble(iterator);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("value1", parser.tokens.get(0));
        Assert.assertEquals("value2", parser.tokens.get(1));

        iterator = Arrays.asList("value1", "value2").iterator();
        parser.eatTheRest = false;
        parser.gobble(iterator);
        Assert.assertTrue(parser.tokens.isEmpty());
    }
}