package de.devcity.parseargs;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArgsParserTest {
    ArgsParser argsParser = new ArgsParser(new String[]{"-switch", "--param", "value", "string"});
    
    @Test
    public void testGetArgsCount() {
        assertEquals(4, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[]{"-switch", "--param", "value", "string"}, argsParser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        argsParser.setArgs(new String[]{"-new"});
        assertEquals(1, argsParser.getArgsCount());
        assertArrayEquals(new String[]{"-new"}, argsParser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        assertEquals(4, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        SwitchArgument switchArg = argsParser.parseSwitchArgument("switch");
        assertEquals(0, switchArg.getIndex());
        assertTrue(switchArg.isFound());
        
        SwitchArgument nonExistSwitch = argsParser.parseSwitchArgument("nonExist");
        assertEquals(-1, nonExistSwitch.getIndex());
        assertFalse(nonExistSwitch.isFound());
    }
    
    @Test
    public void testParseParameterArgument() {
        ParameterArgument paramArg = argsParser.parseParameterArgument("param");
        assertEquals(1, paramArg.getIndex());
        assertEquals("value", paramArg.getValue());
        
        ParameterArgument nonExistParam = argsParser.parseParameterArgument("nonExist");
        assertNull(nonExistParam);
    }
    
    @Test
    public void testParseStringArgument() {
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        assertEquals(1, stringArgs.size());
        assertEquals("string", stringArgs.get(0).getValue());
    }
}