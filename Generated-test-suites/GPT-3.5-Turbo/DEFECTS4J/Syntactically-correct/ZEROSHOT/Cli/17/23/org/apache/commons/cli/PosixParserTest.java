import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        String[] arguments = {"-a", "arg1", "--", "-b", "arg2", "arg3"};
        
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals("--", flattenedArguments[2]);
        assertEquals("-b", flattenedArguments[3]);
        assertEquals("arg2", flattenedArguments[4]);
        assertEquals("arg3", flattenedArguments[5]);
    }
    
    @Test
    public void testFlatten_continue() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        String[] arguments = {"-a", "arg1", "--", "-b", "arg2", "arg3"};
        
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals("--", flattenedArguments[2]);
        assertEquals("-b", flattenedArguments[3]);
        assertEquals("arg2", flattenedArguments[4]);
        assertEquals("arg3", flattenedArguments[5]);
    }
    
    @Test
    public void testProcess_singleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        String[] result = parser.getTokens();
        
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testProcessOptionToken_exists() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", true);
        String[] result = parser.getTokens();
        
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testProcessOptionToken_notExists_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-d", true);
        String[] result = parser.getTokens();
        
        assertEquals("", result[0]);
    }
    
    @Test
    public void testProcessOptionToken_notExists_continue() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-d", false);
        String[] result = parser.getTokens();
        
        assertEquals("-d", result[0]);
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");
        options.addOption("c", true, "");
        
        PosixParser parser = new PosixParser();
        parser.burstToken("-abcd", false);
        String[] result = parser.getTokens();
        
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("-d", result[3]);
    }
}