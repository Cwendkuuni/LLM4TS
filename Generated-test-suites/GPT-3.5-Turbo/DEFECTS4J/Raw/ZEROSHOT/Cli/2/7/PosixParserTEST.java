import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser posixParser;
    private Options options;
    
    @Before
    public void setup() {
        posixParser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_stopAtNonOptionFalse_shouldReturnFlattenedArguments() {
        String[] arguments = {"-a", "--option1=value1", "-b", "-c"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--option1=value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("", result[5]);
    }
    
    @Test
    public void testFlatten_stopAtNonOptionTrue_shouldReturnFlattenedArgumentsUntilNonOption() {
        String[] arguments = {"-a", "--option1=value1", "-b", "-c", "non-option"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--option1=value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("non-option", result[4]);
    }
    
    @Test
    public void testFlatten_optionWithNoArg_shouldReturnFlattenedArgumentsWithEmptyString() {
        String[] arguments = {"-a", "--option1=value1"};
        options.addOption("a", false, "Option A");
        options.addOption("option1", false, "Option 1");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("", result[1]);
        assertEquals("--option1=value1", result[2]);
        assertEquals("", result[3]);
    }
    
    @Test
    public void testFlatten_multipleOptions_shouldReturnFlattenedArguments() {
        String[] arguments = {"-a", "--option1=value1", "-b=value2", "-c=value3", "non-option"};
        options.addOption("a", false, "Option A");
        options.addOption("option1", false, "Option 1");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("", result[1]);
        assertEquals("--option1=value1", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("value2", result[4]);
        assertEquals("-c", result[5]);
        assertEquals("value3", result[6]);
    }
    
    @Test
    public void testFlatten_singleHyphen_shouldReturnFlattenedArguments() {
        String[] arguments = {"-"};
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_optionDoesNotExistAndStopAtNonOptionTrue_shouldReturnFlattenedArguments() {
        String[] arguments = {"-d"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] result = posixParser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
    }
    
    @Test
    public void testFlatten_optionDoesNotExistAndStopAtNonOptionFalse_shouldReturnFlattenedArguments() {
        String[] arguments = {"-d"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] result = posixParser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-d", result[0]);
        assertEquals("", result[1]);
    }
    
    @Test
    public void testBurstToken_optionExists_shouldAddOptionToTokens() {
        String token = "-a";
        boolean stopAtNonOption = true;
        options.addOption("a", false, "Option A");
        
        posixParser.burstToken(token, stopAtNonOption);
        
        String[] result = (String[]) tokens.toArray(new String[] {  });
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testBurstToken_optionExistsAndHasArg_shouldAddOptionAndArgumentToTokens() {
        String token = "-barg";
        boolean stopAtNonOption = false;
        options.addOption("b", true, "Option B");
        
        posixParser.burstToken(token, stopAtNonOption);
        
        String[] result = (String[]) tokens.toArray(new String[] {  });
        
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("arg", result[1]);
    }
    
    @Test
    public void testBurstToken_optionDoesNotExistAndStopAtNonOptionTrue_shouldAddSpecialTokenAndRemainingCharactersToTokens() {
        String token = "-darg";
        boolean stopAtNonOption = true;
        
        posixParser.burstToken(token, stopAtNonOption);
        
        String[] result = (String[]) tokens.toArray(new String[] {  });
        
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-darg", result[1]);
    }
    
    @Test
    public void testBurstToken_optionDoesNotExistAndStopAtNonOptionFalse_shouldAddOptionAndRemainingCharactersToTokens() {
        String token = "-darg";
        boolean stopAtNonOption = false;
        
        posixParser.burstToken(token, stopAtNonOption);
        
        String[] result = (String[]) tokens.toArray(new String[] {  });
        
        assertEquals(1, result.length);
        assertEquals("-d", result[0]);
    }
}