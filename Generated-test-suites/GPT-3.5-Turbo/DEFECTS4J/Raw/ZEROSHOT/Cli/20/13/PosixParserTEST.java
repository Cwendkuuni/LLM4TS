import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        Assert.assertFalse(parser.eatTheRest);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--foo", "-bar", "-baz", "value"};
        String[] flattened = parser.flatten(options, arguments, false);

        Assert.assertEquals(5, flattened.length);
        Assert.assertEquals("--foo", flattened[0]);
        Assert.assertEquals("-bar", flattened[1]);
        Assert.assertEquals("-baz", flattened[2]);
        Assert.assertEquals("value", flattened[3]);
        Assert.assertEquals("--", flattened[4]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.eatTheRest = true;
        List<String> tokens = new ArrayList<String>();
        tokens.add("token1");
        tokens.add("token2");

        Iterator iter = tokens.iterator();
        parser.gobble(iter);

        Assert.assertEquals(4, parser.tokens.size());
        Assert.assertEquals("token1", parser.tokens.get(0));
        Assert.assertEquals("token2", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", false, "Option A");
        parser.currentOption.setArgs(1);

        parser.process("value");

        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        parser.options = options;
        parser.processOptionToken("a", false);

        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("a", parser.tokens.get(0));
        Assert.assertNotNull(parser.currentOption);
        Assert.assertEquals("Option A", parser.currentOption.getDescription());
    }

    @Test
    public void testProcessOptionToken_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        parser.options = options;
        parser.processOptionToken("c", true);

        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNotNull(parser.currentOption);
        Assert.assertNull(parser.currentOption.getDescription());
        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        parser.options = options;
        parser.currentOption = null;
        parser.tokens.clear();

        parser.burstToken("-foo", false);

        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-foo", parser.tokens.get(0));
    }
}