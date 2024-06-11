import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "--", "non-option"};
        String[] expected = {"-a", "value1", "--", "non-option"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_DashToken() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        
        Option option = new Option("a", false, "option a");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOption() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        
        Option option = new Option("a", false, "option a");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--option=value"};
        String[] expected = {"--option", "value"};
        
        Option option = new Option("option", true, "option with value");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionWithoutValue() {
        String[] arguments = {"--option"};
        String[] expected = {"--option"};
        
        Option option = new Option("option", false, "option without value");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_InvalidOption_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "-b", "value2", "--", "non-option"};
        String[] expected = {"-a", "value1", "-b", "value2", "--", "non-option"};
        
        Option option = new Option("a", true, "option a");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_InvalidOption_Continue() {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        
        Option option = new Option("a", true, "option a");
        options.addOption(option);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        String[] arguments = {"-abc", "value"};
        String[] expected = {"-a", "-b", "-c", "value"};
        
        Option optionA = new Option("a", false, "option a");
        Option optionB = new Option("b", false, "option b");
        Option optionC = new Option("c", true, "option c");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_Continue() {
        String[] arguments = {"-abc", "value"};
        String[] expected = {"-a", "-b", "-c", "value"};
        
        Option optionA = new Option("a", false, "option a");
        Option optionB = new Option("b", false, "option b");
        Option optionC = new Option("c", true, "option c");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
}