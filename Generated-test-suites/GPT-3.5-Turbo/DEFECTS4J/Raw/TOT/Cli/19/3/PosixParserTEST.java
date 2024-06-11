import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private PosixParser posParser;
    private Options testOptions;
    
    @Before
    public void setup() {
        posParser = new PosixParser();
        testOptions = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "value1", "value2", "-b"};
        testOptions.addOption(new Option("a", true, "Option a"));
        testOptions.addOption(new Option("b", false, "Option b"));
        
        String[] result = posParser.flatten(testOptions, args, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("-b", result[3]);
    }
    
    @Test
    public void testFlatten_ContinueBursting() {
        String[] args = {"-abcde"};
        testOptions.addOption(new Option("a", false, "Option a"));
        testOptions.addOption(new Option("b", false, "Option b"));
        testOptions.addOption(new Option("c", false, "Option c"));
        testOptions.addOption(new Option("d", false, "Option d"));
        testOptions.addOption(new Option("e", false, "Option e"));
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("-d", result[3]);
        assertEquals("-e", result[4]);
    }
    
    @Test
    public void testFlatten_NoOptionExists_StopAtNonOption() {
        String[] args = {"-xyz", "value"};
        
        String[] result = posParser.flatten(testOptions, args, true);
        
        assertEquals(2, result.length);
        assertEquals("-xyz", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_NoOptionExists_ContinueBursting() {
        String[] args = {"-xyz"};
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(1, result.length);
        assertEquals("-xyz", result[0]);
    }
    
    @Test
    public void testFlatten_HasArgValue_StopAtNonOption() {
        String[] args = {"-opt", "value"};
        testOptions.addOption(new Option("opt", true, "Option with argument"));
        
        String[] result = posParser.flatten(testOptions, args, true);
        
        assertEquals(2, result.length);
        assertEquals("-opt", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_HasArgValue_ContinueBursting() {
        String[] args = {"-opt", "value"};
        testOptions.addOption(new Option("opt", true, "Option with argument"));
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(2, result.length);
        assertEquals("-opt", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_HasArgsValues() {
        String[] args = {"-opt", "value1", "value2"};
        testOptions.addOption(new Option("opt", true, "Option with argument"));
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(3, result.length);
        assertEquals("-opt", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }
    
    @Test
    public void testFlatten_DoubleHyphen() {
        String[] args = {"--", "-opt"};
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-opt", result[1]);
    }
    
    @Test
    public void testFlatten_SpecialTokenWithValue() {
        String[] args = {"--opt=value"};
        
        String[] result = posParser.flatten(testOptions, args, false);
        
        assertEquals(2, result.length);
        assertEquals("--opt", result[0]);
        assertEquals("value", result[1]);
    }

}