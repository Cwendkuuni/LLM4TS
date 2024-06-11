import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertNull(parser.currentOption);
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.isEmpty());
    }
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "valueA", "-b"};
        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertEquals(3, flattened.length);
        Assert.assertEquals("-a", flattened[0]);
        Assert.assertEquals("valueA", flattened[1]);
        Assert.assertEquals("-b", flattened[2]);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-cd", false);

        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-c", parser.tokens.get(0));
        Assert.assertEquals("-d", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("e", true, "Option E");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.processOptionToken("-e", false);

        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-e", parser.tokens.get(0));
        Assert.assertEquals("e", parser.currentOption.getOpt());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        
        parser.process("value");

        Assert.assertTrue(parser.eatTheRest);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("value", parser.tokens.get(1));
    }

}