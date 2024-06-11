import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }
    
    // Test flatten() method
    
    @Test
    public void testFlatten_WithValidOptions_ReturnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");
        String[] arguments = {"-a", "-b", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_WithNonOptionStop_ReturnsProcessedArgumentsUntilNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "nonOption", "-b"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "nonOption", "-b"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_WithRemainingTokens_ReturnsAllProcessedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "-b", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "-b", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }
    
    // Test processOptionToken() method
    
    @Test
    public void testProcessOptionToken_WithValidOption_AddsTokenToProcessedTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.processOptionToken("-a", false);
        String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testProcessOptionToken_WithInvalidOptionAndStopAtNonOption_AddsAllRemainingTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.processOptionToken("-b", true);
        String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        String[] expected = {"-b"};
        assertArrayEquals(expected, result);
    }
    
    // Test process() method
    
    @Test
    public void testProcess_WithCurrentOptionAndHasArg_AddsValueToProcessedTokens() {
        Options options = new Options();
        Option option = new Option("b", true, "Option b with argument");
        options.addOption(option);
        parser.currentOption = option;
        parser.process("value1");
        String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        String[] expected = {"value1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testProcess_WithCurrentOptionAndHasArgs_AddsValueToProcessedTokens() {
        Options options = new Options();
        Option option = new Option("b", true, "Option b with arguments");
        option.setArgs(2);
        options.addOption(option);
        parser.currentOption = option;
        parser.process("value1");
        String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        String[] expected = {"value1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testProcess_WithoutCurrentOption_AddsSpecialTokenAndValueToProcessedTokens() {
        Options options = new Options();
        parser.process("value1");
        String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        String[] expected = {"--", "value1"};
        assertArrayEquals(expected, result);
    }
    
    // Test burstToken() method
    
    @Test
    public void testBurstToken_WithValidOptions_BurstsTokenIntoConstituentParts() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", true, "Option c with argument");
        String[] expected = {"-a", "-b", "--", "-c", "value"};
        
        parser.burstToken("-abcvalue", true);
        String[] result1 = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        assertArrayEquals(expected, result1);
        
        parser.burstToken("-ba-cvalue", true);
        String[] result2 = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
        assertArrayEquals(expected, result2);
    }
    
}