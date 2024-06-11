import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {
    
    ArgsParser parser = new ArgsParser();
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "--arg2", "-arg3"};
        parser.setArgs(args);
        int expectedCount = 3;
        
        int actualCount = parser.getArgsCount();
        
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testGetArgs_NullArgsArray() {
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "--arg2", "-arg3"};
        parser.setArgs(args);
        int expectedCount = 3;
        
        int actualCount = parser.getArgsLeftCount();
        
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        String[] args = {"-arg1", "--arg2"};
        parser.setArgs(args);
        String key = "arg2";
        SwitchArgument expectedArgument = new SwitchArgument(1, key, true);
        
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyNotExists() {
        String[] args = {"-arg1"};
        parser.setArgs(args);
        String key = "arg2";
        SwitchArgument expectedArgument = new SwitchArgument(-1, key, false);
        
        SwitchArgument actualArgument = parser.parseSwitchArgument(key);
        
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyExistsWithNextValue() {
        String[] args = {"-arg1", "--arg2", "value"};
        parser.setArgs(args);
        String key = "arg2";
        ParameterArgument expectedArgument = new ParameterArgument(1, key, "value");
        
        ParameterArgument actualArgument = parser.parseParameterArgument(key);
        
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyExistsWithoutNextValue() {
        String[] args = {"-arg1", "--arg2"};
        parser.setArgs(args);
        String key = "arg2";
        ParameterArgument expectedArgument = null;
        
        ParameterArgument actualArgument = parser.parseParameterArgument(key);
        
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyNotExists() {
        String[] args = {"-arg1"};
        parser.setArgs(args);
        String key = "arg2";
        ParameterArgument expectedArgument = null;
        
        ParameterArgument actualArgument = parser.parseParameterArgument(key);
        
        assertEquals(expectedArgument, actualArgument);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "--arg2", "-arg3"};
        parser.setArgs(args);
        StringArgument expectedArgument1 = new StringArgument(0, "arg1");
        StringArgument expectedArgument2 = new StringArgument(1, "--arg2");
        StringArgument expectedArgument3 = new StringArgument(2, "-arg3");
        
        List<StringArgument> actualArguments = parser.parseStringArgument();
        
        assertEquals(expectedArgument1, actualArguments.get(0));
        assertEquals(expectedArgument2, actualArguments.get(1));
        assertEquals(expectedArgument3, actualArguments.get(2));
    }
}