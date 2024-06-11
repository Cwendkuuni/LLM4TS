import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten_StopAtNonOption_EmptyArgs_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_NonOptionsExist_ReturnsNonOptionsOnly() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", false));
        options.addOption(new Option("c", false));
        String[] arguments = {"-a", "nonOption1", "-b", "nonOption2", "-c"};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("nonOption1", result[0]);
        assertEquals("nonOption2", result[1]);
        assertEquals("-c", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_InvalidOption_ReturnsRemainingArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        String[] arguments = {"-a", "value1", "-b", "value2"};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value1", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_ValidOptionsExist_ReturnsOptionsAndNonOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", false));
        options.addOption(new Option("c", false));
        String[] arguments = {"-a", "value1", "-b", "value2", "-c", "value3"};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("value3", result[5]);
    }
    
    @Test
    public void testFlatten_DoNotStopAtNonOption_EmptyArgs_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_DoNotStopAtNonOption_NonOptionsExist_ReturnsAllArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", false));
        options.addOption(new Option("c", false));
        String[] arguments = {"-a", "nonOption1", "-b", "nonOption2", "-c"};
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("nonOption2", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("--", result[5]);
        assertEquals("", result[6]);
    }
    
    @Test
    public void testFlatten_DoNotStopAtNonOption_InvalidOption_ReturnsRemainingArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        String[] arguments = {"-a", "value1", "-b", "value2"};
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }
    
    @Test
    public void testFlatten_DoNotStopAtNonOption_ValidOptionsExist_ReturnsAllArguments() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", false));
        options.addOption(new Option("c", false));
        String[] arguments = {"-a", "value1", "-b", "value2", "-c", "value3"};
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("value3", result[5]);
    }
}