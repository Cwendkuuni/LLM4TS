import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

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
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, flattenedArguments);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"arg1", "arg2"}).iterator();
        parser.gobble(iter);
        // No assertions as it only adds elements to tokens list
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "Option a", false, "Option a value");
        parser.process("value");
        Assert.assertEquals("value", parser.tokens.get(0));

        parser.currentOption = null;
        parser.process("value");
        Assert.assertTrue(parser.eatTheRest);
        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        PosixParser parser = new PosixParser();

        parser.processOptionToken("-a", false);
        Assert.assertEquals("a", parser.currentOption.getOpt());
        Assert.assertEquals("-a", parser.tokens.get(0));

        parser.processOptionToken("-b", true); // -b option does not exist
        Assert.assertTrue(parser.eatTheRest);
        Assert.assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        PosixParser parser = new PosixParser();

        parser.burstToken("-ab", false);
        Assert.assertEquals("-a", parser.tokens.get(0));
        Assert.assertEquals("-b", parser.tokens.get(1));

        parser.burstToken("-ac", true); // -c option does not exist
        Assert.assertTrue(parser.eatTheRest);
        Assert.assertEquals("--", parser.tokens.get(2));
        Assert.assertEquals("c", parser.tokens.get(3));
    }
}