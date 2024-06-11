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
        options.addOption("b", true, "Option b");
        
        String[] arguments = {"-a", "valueA", "-b", "valueB", "nonOption"};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        Assert.assertArrayEquals(new String[]{"-a", "-b", "valueB", "nonOption"}, result);
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"token1", "token2"}).iterator();
        parser.eatTheRest = true;
        
        parser.gobble(iter);
        
        Assert.assertTrue(parser.tokens.contains("token1"));
        Assert.assertTrue(parser.tokens.contains("token2"));
    }
    
    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        PosixParser parser = new PosixParser();
        parser.options = options;
        
        parser.process("test1");
        
        Assert.assertTrue(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.contains("--"));
        Assert.assertTrue(parser.tokens.contains("test1"));
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        
        Assert.assertTrue(parser.tokens.contains("-"));
    }
    
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        PosixParser parser = new PosixParser();
        parser.options = options;
        
        parser.processOptionToken("a", false);
        
        Assert.assertEquals("a", parser.currentOption.getOpt());
        Assert.assertTrue(parser.tokens.contains("a"));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        
        PosixParser parser = new PosixParser();
        parser.options = options;
        
        parser.burstToken("-abc", false);
        
        Assert.assertTrue(parser.tokens.contains("-a"));
        Assert.assertTrue(parser.tokens.contains("-b"));
        Assert.assertTrue(parser.tokens.contains("-c"));
    }
}