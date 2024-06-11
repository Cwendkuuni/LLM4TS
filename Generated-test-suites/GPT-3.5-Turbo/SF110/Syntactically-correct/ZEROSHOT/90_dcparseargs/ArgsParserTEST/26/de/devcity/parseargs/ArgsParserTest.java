package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertEquals(2, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        assertEquals(2, argsParser.getArgsLeftCount());
        
        argsParser.parseSwitchArgument("arg1");
        assertEquals(1, argsParser.getArgsLeftCount());
        argsParser.parseSwitchArgument("arg2");
        assertEquals(0, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_LongKey() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"--arg1", "-arg2"};
        argsParser.setArgs(args);
        
        SwitchArgument result = argsParser.parseSwitchArgument("arg1");
        assertTrue(result.isSwitchPresent());
        
        result = argsParser.parseSwitchArgument("arg2");
        assertFalse(result.isSwitchPresent());
        
        result = argsParser.parseSwitchArgument("arg3");
        assertFalse(result.isSwitchPresent());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKey() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"-a1", "-a2"};
        argsParser.setArgs(args);
        
        SwitchArgument result = argsParser.parseSwitchArgument("a1");
        assertTrue(result.isSwitchPresent());
        
        result = argsParser.parseSwitchArgument("a2");
        assertTrue(result.isSwitchPresent());
        
        result = argsParser.parseSwitchArgument("a3");
        assertFalse(result.isSwitchPresent());
    }
    
    @Test
    public void testParseParameterArgument_LongKey() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"--arg1", "value1"};
        argsParser.setArgs(args);
        
        ParameterArgument result = argsParser.parseParameterArgument("arg1");
        assertNotNull(result);
        assertEquals("value1", result.getValue());
        
        result = argsParser.parseParameterArgument("arg2");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_ShortKey() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"-a1", "value1"};
        argsParser.setArgs(args);
        
        ParameterArgument result = argsParser.parseParameterArgument("a1");
        assertNotNull(result);
        assertEquals("value1", result.getValue());
        
        result = argsParser.parseParameterArgument("a2");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
    }
}