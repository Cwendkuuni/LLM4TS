
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.getArgsCount());
        
        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2"};
        argsParser.setArgs(args);
        
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        String[] args = new String[]{"arg1", "arg2"};
        argsParser.setArgs(args);
        
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        argsParser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[]{"arg1", null});
        assertEquals(1, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[]{null, null});
        assertEquals(0, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"-v", "--verbose"});
        
        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("v");
        assertTrue(switchArgument1.isSwitchSet());
        assertEquals(0, switchArgument1.getIndex());
        assertEquals("v", switchArgument1.getKey());
        
        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("verbose");
        assertTrue(switchArgument2.isSwitchSet());
        assertEquals(1, switchArgument2.getIndex());
        assertEquals("verbose", switchArgument2.getKey());
        
        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("x");
        assertFalse(switchArgument3.isSwitchSet());
        assertEquals(-1, switchArgument3.getIndex());
        assertEquals("x", switchArgument3.getKey());
    }
    
    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--name", "John", "-a", "29"});
        
        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("name");
        assertNotNull(parameterArgument1);
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("name", parameterArgument1.getKey());
        assertEquals("John", parameterArgument1.getValue());
        
        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("a");
        assertNotNull(parameterArgument2);
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("a", parameterArgument2.getKey());
        assertEquals("29", parameterArgument2.getValue());
        
        ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("x");
        assertNull(parameterArgument3);
    }
    
    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        
        assertEquals(2, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}
