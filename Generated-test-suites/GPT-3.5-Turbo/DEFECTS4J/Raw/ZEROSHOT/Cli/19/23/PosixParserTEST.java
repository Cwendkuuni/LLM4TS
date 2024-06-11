import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "--option1=value1", "-c", "value2", "argument1", "argument2"};
        String[] expected = {"-a", "-b", "--option1", "value1", "-c", "value2", "argument1", "argument2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "-b", "--option1=value1", "-c", "value2", "argument1", "argument2"};
        String[] expected = {"-a", "-b", "--option1=value1", "-c", "value2", "argument1", "argument2"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"argument1", "argument2"};
        String[] expected = {"argument1", "argument2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenNoOptionsStopAtNonOption() {
        String[] arguments = {"argument1", "argument2"};
        String[] expected = {"argument1", "argument2"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenSingleHyphenStopAtNonOption() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenInvalidOption() {
        String[] arguments = {"-d"};
        String[] expected = {"-d"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenInvalidOptionStopAtNonOption() {
        String[] arguments = {"-d"};
        String[] expected = {"-d"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstToken() {
        String[] arguments = {"-abcd"};
        String[] expected = {"-a", "-b", "-c", "-d"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        String[] arguments = {"-abcd"};
        String[] expected = {"-a", "-b", "-c", "-d"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstTokenWithArg() {
        String[] arguments = {"-abc", "value"};
        String[] expected = {"-a", "-b", "-c", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstTokenWithArgStopAtNonOption() {
        String[] arguments = {"-abc", "value"};
        String[] expected = {"-a", "-b", "-c", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
}