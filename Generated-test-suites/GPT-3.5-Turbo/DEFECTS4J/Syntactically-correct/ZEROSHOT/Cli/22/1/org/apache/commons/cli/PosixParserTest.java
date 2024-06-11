import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        Assert.assertFalse(posixParser.eatTheRest);
        Assert.assertEquals(0, posixParser.tokens.size());
    }

    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--opt", "value"};

        String[] result = posixParser.flatten(options, arguments, true);

        Assert.assertEquals(2, result.length);
        Assert.assertEquals("--opt", result[0]);
        Assert.assertEquals("value", result[1]);
    }

    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        Iterator<String> iter = List.of("arg1", "arg2").iterator();
        posixParser.eatTheRest = true;

        posixParser.gobble(iter);

        Assert.assertEquals(2, posixParser.tokens.size());
        Assert.assertEquals("arg1", posixParser.tokens.get(0));
        Assert.assertEquals("arg2", posixParser.tokens.get(1));
    }

    @Test
    public void processNonOptionToken() {
        PosixParser posixParser = new PosixParser();
        posixParser.processNonOptionToken("value");

        Assert.assertTrue(posixParser.eatTheRest);
        Assert.assertEquals(2, posixParser.tokens.size());
        Assert.assertEquals("--", posixParser.tokens.get(0));
        Assert.assertEquals("value", posixParser.tokens.get(1));
    }

    @Test
    public void processOptionToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        Option option = new Option("o", "option", false, "");
        options.addOption(option);

        posixParser.options = options;
        posixParser.processOptionToken("o", false);

        Assert.assertEquals(1, posixParser.tokens.size());
        Assert.assertEquals("o", posixParser.tokens.get(0));
    }

    @Test
    public void burstToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "");
        options.addOption(option);
        posixParser.options = options;

        posixParser.burstToken("-optionValue", false);

        Assert.assertEquals(2, posixParser.tokens.size());
        Assert.assertEquals("-o", posixParser.tokens.get(0));
        Assert.assertEquals("ptionValue", posixParser.tokens.get(1));
    }
}