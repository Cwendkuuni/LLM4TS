import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @BeforeEach
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_NoArguments_StopAtNonOptionFalse() {
        String[] arguments = new String[]{};
        String[] expected = new String[]{};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NoArguments_StopAtNonOptionTrue() {
        String[] arguments = new String[]{};
        String[] expected = new String[]{};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOption_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-a"};
        String[] expected = new String[]{"-a"};
        
        options.addOption("a", false, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOption_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-a"};
        String[] expected = new String[]{"-a"};
        
        options.addOption("a", false, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOptionWithArgument_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-a", "value"};
        String[] expected = new String[]{"-a", "value"};
        
        options.addOption("a", true, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOptionWithArgument_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-a", "value"};
        String[] expected = new String[]{"-a", "value"};
        
        options.addOption("a", true, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_MultipleOptions_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-a", "-b", "-c"};
        String[] expected = new String[]{"-a", "-b", "-c"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", false, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_MultipleOptions_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-a", "-b", "-c"};
        String[] expected = new String[]{"-a", "-b", "-c"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", false, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphenTwoCharacters_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-ab"};
        String[] expected = new String[]{"-a", "-b"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphenTwoCharacters_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-ab"};
        String[] expected = new String[]{"-a", "-b"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphenMultipleCharacters_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-abc"};
        String[] expected = new String[]{"-a", "-b", "-c"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", false, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphenMultipleCharacters_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-abc"};
        String[] expected = new String[]{"-a", "-b", "-c"};
        
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", false, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] arguments = new String[]{"-a", "--", "value"};
        String[] expected = new String[]{"-a", "--", "value"};
        
        options.addOption("a", false, "");
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assertions.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        String[] arguments = new String[]{"-a", "--", "value"};
        String[] expected = new String[]{"-a"};
        
        options.addOption("a", false, "");
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assertions.assertArrayEquals(expected, result);
    }
}