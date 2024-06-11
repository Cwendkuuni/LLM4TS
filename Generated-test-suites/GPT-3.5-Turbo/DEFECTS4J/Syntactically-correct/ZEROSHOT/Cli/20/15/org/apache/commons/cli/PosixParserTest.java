import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-a", "value1", "-b", "value2", "non-option"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("non-option", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-a", "value1", "-b", "value2", "non-option"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("non-option", result[4]);
    }
    
    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        options.addOption("c", false, "option C");
        
        String[] arguments = {"--optionA", "--optionB=value", "--optionC"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("--optionA", result[0]);
        assertEquals("--optionB=value", result[1]);
        assertEquals("--optionC", result[2]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-a"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testFlatten_SingleOptionNoArg() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-b"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }
    
    @Test
    public void testFlatten_BurstToken() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-ab", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
    }
    
    @Test
    public void testFlatten_UnknownOption_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-c", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_UnknownOption_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", true, "option B");
        
        String[] arguments = {"-c", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_True_NoOptions() {
        Options options = new Options();
        
        String[] arguments = {"-c", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_False_NoOptions() {
        Options options = new Options();
        
        String[] arguments = {"-c", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        
        String[] arguments = {"-c", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        
        String[] arguments = {};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_NullOptions_NullArguments() {
        Options options = null;
        
        String[] arguments = null;
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNull(result);
    }
}