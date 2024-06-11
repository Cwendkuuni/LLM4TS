import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_LongOption_NoArgs_Success() {
        Options options = new Options();
        options.addOption("foo", false, "option foo");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"--foo"}, true);
        
        assertEquals(1, args.length);
        assertEquals("--foo", args[0]);
    }
    
    @Test
    public void testFlatten_LongOption_WithArg_Success() {
        Options options = new Options();
        options.addOption("foo", true, "option foo");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"--foo=bar"}, true);
        
        assertEquals(2, args.length);
        assertEquals("--foo", args[0]);
        assertEquals("bar", args[1]);
    }
    
    @Test
    public void testFlatten_LongOption_UnknownOption_Failure() {
        Options options = new Options();
        options.addOption("foo", false, "option foo");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"--bar"}, true);
        
        assertEquals(2, args.length);
        assertEquals("--", args[0]);
        assertEquals("--bar", args[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen_Success() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-"}, true);
        
        assertEquals(1, args.length);
        assertEquals("-", args[0]);
    }
    
    @Test
    public void testFlatten_ShortOption_Success() {
        Options options = new Options();
        options.addOption("f", true, "option f");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-f"}, true);
        
        assertEquals(1, args.length);
        assertEquals("-f", args[0]);
    }
    
    @Test
    public void testFlatten_ShortOption_RequiresArg_Success() {
        Options options = new Options();
        options.addOption("f", true, "option f");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-fvalue"}, true);
        
        assertEquals(2, args.length);
        assertEquals("-f", args[0]);
        assertEquals("value", args[1]);
    }
    
    @Test
    public void testFlatten_ShortOption_UnknownOption_StopAtNonOption_Success() {
        Options options = new Options();
        options.addOption("f", true, "option f");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-fvalue", "other"}, true);
        
        assertEquals(3, args.length);
        assertEquals("-f", args[0]);
        assertEquals("value", args[1]);
        assertEquals("other", args[2]);
    }
    
    @Test
    public void testFlatten_ShortOption_UnknownOption_NotStopAtNonOption_Success() {
        Options options = new Options();
        options.addOption("f", true, "option f");
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-fvalue", "other"}, false);
        
        assertEquals(2, args.length);
        assertEquals("-f", args[0]);
        assertEquals("value", args[1]);
    }
    
    @Test
    public void testFlatten_NonOptionToken_Success() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"value"}, true);
        
        assertEquals(2, args.length);
        assertEquals("--", args[0]);
        assertEquals("value", args[1]);
    }
    
    @Test
    public void testBurstToken_OptionChar_ValidOption_AddsToProcessedTokens() {
        Options options = new Options();
        options.addOption("f", false, "option f");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-f", true);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
    }
    
    @Test
    public void testBurstToken_OptionChar_ValidOption_RequiresArg_AddsToProcessedTokens() {
        Options options = new Options();
        options.addOption("f", true, "option f");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-fvalue", true);
        
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
    
    @Test
    public void testBurstToken_OptionChar_UnknownOption_StopAtNonOption_AddsToProcessedTokens() {
        Options options = new Options();
        options.addOption("f", false, "option f");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-bar", true);
        
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-bar", parser.tokens.get(1));
    }
    
    @Test
    public void testBurstToken_OptionChar_UnknownOption_NotStopAtNonOption_AddsToProcessedTokens() {
        Options options = new Options();
        options.addOption("f", false, "option f");
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-bar", false);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("-bar", parser.tokens.get(0));
    }
    
    @Test
    public void testBurstToken_NonOptionChar_StopAtNonOption_AddsToProcessedTokens() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("value", true);
        
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
    
    @Test
    public void testBurstToken_NonOptionChar_NotStopAtNonOption_AddsToProcessedTokens() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("value", false);
        
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }
}