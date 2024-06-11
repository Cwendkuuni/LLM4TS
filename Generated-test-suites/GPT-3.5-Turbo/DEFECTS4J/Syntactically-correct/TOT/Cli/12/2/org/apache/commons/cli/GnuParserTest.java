import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        options.addOption("a", false, "Enable option a");
    }
    
    @Test
    public void testFlatten_NoOptions_ReturnsSameArgumentArray() {
        String[] arguments = {"param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        assertArrayEquals(arguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsOption_NotInOptions_AddsArgumentToArray() {
        String[] arguments = {"-b", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-b", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsOptionInOptions_AddsOptionToArray() {
        String[] arguments = {"-a", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-a", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_FlagSet_StopsFlattening() {
        String[] arguments = {"-a", "-b", "--", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        String[] expectedArguments = {"-a", "-b", "--"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_FlagNotSet_ContinuesFlattening() {
        String[] arguments = {"-a", "-b", "--", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-a", "-b", "--", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StartsWithHyphenHyphen_AddsHyphenHyphenToArray() {
        String[] arguments = {"--", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"--", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_StartsWithHyphen_AddsHyphenToArray() {
        String[] arguments = {"-", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsTwoCharOptionInOptions_AddsOptionToArray() {
        options.addOption("ab", false, "Enable option ab");
        
        String[] arguments = {"-ab", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-ab", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsTwoCharOptionNotInOptions_AddsSeparatedOptionToArray() {
        String[] arguments = {"-ab", "param1", "param2"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-a", "b", "param1", "param2"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsNonOptionToken_AfterStopAtNonOption_FlagSet_IgnoresToken() {
        String[] arguments = {"-a", "-b", "--", "param1", "param2", "nonOption"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        String[] expectedArguments = {"-a", "-b", "--"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
    
    @Test
    public void testFlatten_ContainsNonOptionToken_AfterStopAtNonOption_FlagNotSet_KeepsToken() {
        String[] arguments = {"-a", "-b", "--", "param1", "param2", "nonOption"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        String[] expectedArguments = {"-a", "-b", "--", "param1", "param2", "nonOption"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
}