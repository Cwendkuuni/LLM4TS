import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.*;
import java.util.*;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        // Typical Use Case: ArgsParser is initialized with an array of arguments
        String[] args = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args);
        
        assertEquals("getArgsCount should return the total number of arguments", 4, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgsCount_EmptyArgs() {
        // Variant: ArgsParser is initialized with an empty array of arguments
        String[] args = {};
        argsParser.setArgs(args);
        
        assertEquals("getArgsCount should return 0 for an empty array of arguments", 0, argsParser.getArgsCount());
    }
    
    @Test(expected=NullPointerException.class)
    public void testSetArgs_NullArgs() {
        // Error Scenario: ArgsParser is initialized with null arguments
        String[] args = null;
        
        argsParser.setArgs(args);
        
        fail("NullPointerException should be thrown when trying to set null arguments");
    }
    
    @Test
    public void testGetArgsLeftCount() {
        // Typical Use Case: ArgsParser is initialized with an array of arguments
        String[] args = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args);
        
        assertEquals("getArgsLeftCount should return the number of remaining arguments", 4, argsParser.getArgsLeftCount());
        
        argsParser.parseSwitchArgument("a"); // Parsing switch argument removes it from the innerArgs array
        
        assertEquals("getArgsLeftCount should return the updated number of remaining arguments", 3, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        // Typical Use Case: Parsing a valid switch argument
        String[] args = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args);
        
        SwitchArgument result = argsParser.parseSwitchArgument("a");
        
        assertTrue("parseSwitchArgument should return a SwitchArgument object", result instanceof SwitchArgument);
        assertEquals("The parsed switch argument should have the correct index", 0, result.getIndex());
        assertEquals("The parsed switch argument should have the correct key", "a", result.getKey());
        assertTrue("The parsed switch argument should have the correct value", result.isValue());
        
        // Edge Case: Parsing a switch argument with a long key
        String[] args2 = {"-a", "--verbose", "--test", "file.txt"};
        argsParser.setArgs(args2);
        
        SwitchArgument result2 = argsParser.parseSwitchArgument("test");
        
        assertTrue("parseSwitchArgument should return a SwitchArgument object", result2 instanceof SwitchArgument);
        assertEquals("The parsed switch argument should have the correct index", 2, result2.getIndex());
        assertEquals("The parsed switch argument should have the correct key", "test", result2.getKey());
        assertTrue("The parsed switch argument should have the correct value", result2.isValue());
        
        // Error Scenario: Parsing an invalid switch argument
        String[] args3 = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args3);
        
        SwitchArgument result3 = argsParser.parseSwitchArgument("b");
        
        assertTrue("parseSwitchArgument should return a SwitchArgument object", result3 instanceof SwitchArgument);
        assertEquals("The parsed switch argument should have the correct index", -1, result3.getIndex());
        assertEquals("The parsed switch argument should have the correct key", "b", result3.getKey());
        assertFalse("The parsed switch argument should have the correct value", result3.isValue());
    }
    
    @Test
    public void testParseParameterArgument() {
        // Typical Use Case: Parsing a valid parameter argument
        String[] args = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args);
        
        ParameterArgument result = argsParser.parseParameterArgument("f");
        
        assertTrue("parseParameterArgument should return a ParameterArgument object", result instanceof ParameterArgument);
        assertEquals("The parsed parameter argument should have the correct index", 2, result.getIndex());
        assertEquals("The parsed parameter argument should have the correct key", "f", result.getKey());
        assertEquals("The parsed parameter argument should have the correct value", "file.txt", result.getValue());
        
        // Variant: Parsing a parameter argument with a long key
        String[] args2 = {"-a", "--verbose", "--file", "file.txt"};
        argsParser.setArgs(args2);
        
        ParameterArgument result2 = argsParser.parseParameterArgument("file");
        
        assertTrue("parseParameterArgument should return a ParameterArgument object", result2 instanceof ParameterArgument);
        assertEquals("The parsed parameter argument should have the correct index", 2, result2.getIndex());
        assertEquals("The parsed parameter argument should have the correct key", "file", result2.getKey());
        assertEquals("The parsed parameter argument should have the correct value", "file.txt", result2.getValue());
        
        // Error Scenario: Parsing an invalid parameter argument
        String[] args3 = {"-a", "--verbose", "-f", "file.txt"};
        argsParser.setArgs(args3);
        
        ParameterArgument result3 = argsParser.parseParameterArgument("b");
        
        assertNull("parseParameterArgument should return null when parsing an invalid parameter argument", result3);
    }
    
    @Test
    public void testParseStringArgument() {
        // Typical Use Case: Parsing multiple valid string arguments
        String[] args = {"-a", "hello", "world", "-f", "file.txt"};
        argsParser.setArgs(args);
        
        List<StringArgument> result = argsParser.parseStringArgument();
        
        assertEquals("parseStringArgument should return a list of StringArgument objects", 2, result.size());
        assertEquals("The first parsed string argument should have the correct index", 1, result.get(0).getIndex());
        assertEquals("The first parsed string argument should have the correct value", "hello", result.get(0).getValue());
        assertEquals("The second parsed string argument should have the correct index", 2, result.get(1).getIndex());
        assertEquals("The second parsed string argument should have the correct value", "world", result.get(1).getValue());
        
        // Variant: Parsing a single valid string argument
        String[] args2 = {"hello"};
        argsParser.setArgs(args2);
        
        List<StringArgument> result2 = argsParser.parseStringArgument();
        
        assertEquals("parseStringArgument should return a list of StringArgument objects", 1, result2.size());
        assertEquals("The parsed string argument should have the correct index", 0, result2.get(0).getIndex());
        assertEquals("The parsed string argument should have the correct value", "hello", result2.get(0).getValue());
        
        // Variant: Parsing zero string arguments
        String[] args3 = {"-a", "-f", "file.txt"};
        argsParser.setArgs(args3);
        
        List<StringArgument> result3 = argsParser.parseStringArgument();
        
        assertNotNull("parseStringArgument should return an empty list when no string arguments are present", result3);
        assertEquals("parseStringArgument should return an empty list when no string arguments are present", 0, result3.size());
    }
}