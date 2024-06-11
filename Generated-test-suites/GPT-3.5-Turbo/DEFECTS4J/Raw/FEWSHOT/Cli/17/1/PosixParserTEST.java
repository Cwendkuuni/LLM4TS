import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten_withStopAtNonOption_true() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }
    
    @Test
    public void testFlatten_withStopAtNonOption_false() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
        assertEquals(null, result[4]);
    }
    
    @Test
    public void testFlatten_withOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        String[] arguments = {"-a", "-b", "value", "-c", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("nonOption", result[4]);
        assertEquals(null, result[5]);
        assertEquals(null, result[6]);
    }
    
    @Test
    public void testFlatten_withOptionsAndArguments_withInvalidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-b", "-c", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-b", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("nonOption", result[2]);
        assertEquals(null, result[3]);
    }
    
    @Test
    public void testFlatten_withOptionsAndArguments_withBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("bc", true, "Option BC");
        String[] arguments = {"-abcvalue", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
        assertEquals("nonOption", result[4]);
        assertEquals(null, result[5]);
    }
    
    @Test
    public void testFlatten_withOptionsAndArguments_withBurstTokenAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("bc", true, "Option BC");
        String[] arguments = {"-abcvalue", "nonOption"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-abcvalue", result[0]);
        assertEquals("nonOption", result[1]);
    }
}