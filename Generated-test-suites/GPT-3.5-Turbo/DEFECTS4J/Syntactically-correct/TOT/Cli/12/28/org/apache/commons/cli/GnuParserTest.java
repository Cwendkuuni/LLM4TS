import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class GnuParserTest {
    
    private GnuParser parser;
    private Options options;
    private String[] arguments;
    
    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        arguments = new String[0];
    }
    
    @Test
    public void testFlatten_OnlyOptions_ReturnsFlattenedArguments() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        arguments = new String[]{"-a", "-b"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_OnlyNonOptions_ReturnsFlattenedArguments() {
        arguments = new String[]{"arg1", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_BothOptionsAndNonOptions_ReturnsFlattenedArguments() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        arguments = new String[]{"-a", "arg1", "-b", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "arg1", "-b", "arg2"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_ReturnsFlattenedArgumentsUntilNonOption() {
        options.addOption(Option.builder("a").build());
        arguments = new String[]{"-a", "arg1", "nonOption", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "arg1", "nonOption", "arg2"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_NoFlattenedArgumentsAfterNonOption() {
        options.addOption(Option.builder("a").build());
        arguments = new String[]{"-a", "arg1", "nonOption", "-b", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "arg1", "nonOption"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_WithDoubleDash_ReturnsFlattenedArgumentsWithDoubleDash() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        arguments = new String[]{"-a", "--", "-b", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "--", "-b", "arg2"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
    @Test
    public void testFlatten_WithSingleDash_ReturnsFlattenedArgumentsWithSingleDash() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        arguments = new String[]{"-a", "-", "-b", "arg2"};
        
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "-", "-b", "arg2"};
        assertArrayEquals(expected, flattenedArguments);
    }
    
}