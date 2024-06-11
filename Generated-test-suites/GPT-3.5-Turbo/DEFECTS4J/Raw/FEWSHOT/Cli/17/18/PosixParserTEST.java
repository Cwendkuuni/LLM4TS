import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        
        assertEquals(false, posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("f", false, ""));
        options.addOption(new Option("a", true, ""));
        
        String[] arguments = {"-f", "--", "value1", "value2"};
        
        PosixParser posixParser = new PosixParser();
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }
    
    @Test
    public void testFlatten_noStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("f", false, ""));
        options.addOption(new Option("a", true, ""));
        
        String[] arguments = {"-f", "--", "value1", "value2"};
        
        PosixParser posixParser = new PosixParser();
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("value2", result[4]);
    }
    
    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        posixParser.currentOption = new Option("a", true, "");
        posixParser.currentOption.setArgs(1);
        
        posixParser.process("value");
        
        assertEquals(1, posixParser.tokens.size());
        assertEquals("value", posixParser.tokens.get(0));
        assertNull(posixParser.currentOption);
        
        posixParser.currentOption = new Option("f", false, "");
        
        posixParser.process("value");
        
        assertEquals(3, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(0));
        assertEquals("value", posixParser.tokens.get(1));
        assertEquals("value", posixParser.tokens.get(2));
        assertNull(posixParser.currentOption);
    }
    
    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        
        posixParser.processSingleHyphen("-");
        
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-", posixParser.tokens.get(0));
    }
    
    @Test
    public void testProcessOptionToken_validOption() {
        Options options = new Options();
        options.addOption(new Option("f", false, ""));
        
        PosixParser posixParser = new PosixParser();
        posixParser.options = options;
        
        posixParser.processOptionToken("-f", true);
        
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-f", posixParser.tokens.get(0));
    }
    
    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        
        Iterator<String> iterator = Arrays.asList("value1", "value2").iterator();
        
        posixParser.eatTheRest = true;
        posixParser.tokens.clear();
        
        posixParser.gobble(iterator);
        
        assertEquals(2, posixParser.tokens.size());
        assertEquals("value1", posixParser.tokens.get(0));
        assertEquals("value2", posixParser.tokens.get(1));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, ""));
        
        PosixParser posixParser = new PosixParser();
        posixParser.options = options;
        
        posixParser.burstToken("-ab", true);
        
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
    }
}