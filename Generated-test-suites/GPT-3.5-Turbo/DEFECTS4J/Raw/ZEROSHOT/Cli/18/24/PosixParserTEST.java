import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser posixParser;
    private Options options;
    
    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
    }
    
    @Test
    public void testFlatten_NotStopAtNonOption() {
        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("nonOption", result[4]);
    }
    
    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        String[] arguments = new String[]{"--option1=value1", "--option2"};
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = new String[]{"-"};
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_MultiCharacterOption() {
        options.addOption("option1", true, "Option 1");
        String[] arguments = new String[]{"-option1"};
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-option1", result[0]);
    }
    
    @Test
    public void testFlatten_BurstToken() {
        options.addOption("a", false, "Option A");
        String[] arguments = new String[]{"-ab"};
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
    }
}