package de.devcity.parseargs;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {

    private ArgsParser argsParser;
    
    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
        
        argsParser.setArgs(new String[] {});
        assertEquals(0, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[] {"arg1", "arg2", "arg3"}, argsParser.getArgs());
        
        argsParser.setArgs(new String[] {});
        assertArrayEquals(new String[] {}, argsParser.getArgs());
    }
    
    @Test(expected=NullPointerException.class)
    public void testSetArgs_nullArgs() {
        argsParser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[] {"arg1", null, "arg3"});
        assertEquals(2, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[] {"arg1", null, null});
        assertEquals(1, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[] {null, null, null});
        assertEquals(0, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_longKey() {
        argsParser.setArgs(new String[] {"--key1", "-key2"});
        
        SwitchArgument result1 = argsParser.parseSwitchArgument("key1");
        assertTrue(result1.isSwitch());
        assertEquals(0, result1.getIndex());
        assertEquals("key1", result1.getKey());
        
        SwitchArgument result2 = argsParser.parseSwitchArgument("key2");
        assertTrue(result2.isSwitch());
        assertEquals(1, result2.getIndex());
        assertEquals("key2", result2.getKey());
        
        SwitchArgument result3 = argsParser.parseSwitchArgument("key3");
        assertFalse(result3.isSwitch());
        assertEquals(-1, result3.getIndex());
        assertEquals("key3", result3.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_shortKey() {
        argsParser.setArgs(new String[] {"-k1", "-k2", "-k3"});
        
        SwitchArgument result1 = argsParser.parseSwitchArgument("k1");
        assertTrue(result1.isSwitch());
        assertEquals(0, result1.getIndex());
        assertEquals("k1", result1.getKey());
        
        SwitchArgument result2 = argsParser.parseSwitchArgument("k2");
        assertTrue(result2.isSwitch());
        assertEquals(1, result2.getIndex());
        assertEquals("k2", result2.getKey());
        
        SwitchArgument result3 = argsParser.parseSwitchArgument("k3");
        assertTrue(result3.isSwitch());
        assertEquals(2, result3.getIndex());
        assertEquals("k3", result3.getKey());
        
        SwitchArgument result4 = argsParser.parseSwitchArgument("k4");
        assertFalse(result4.isSwitch());
        assertEquals(-1, result4.getIndex());
        assertEquals("k4", result4.getKey());
    }
    
    @Test
    public void testParseParameterArgument_longKey() {
        argsParser.setArgs(new String[] {"--key1", "value1", "--key2"});
        
        ParameterArgument result1 = argsParser.parseParameterArgument("key1");
        assertNotNull(result1);
        assertEquals(0, result1.getIndex());
        assertEquals("key1", result1.getKey());
        assertEquals("value1", result1.getValue());
        
        ParameterArgument result2 = argsParser.parseParameterArgument("key2");
        assertNull(result2);
    }
    
    @Test
    public void testParseParameterArgument_shortKey() {
        argsParser.setArgs(new String[] {"-k1", "value1", "-k2", "value2", "-k3"});
        
        ParameterArgument result1 = argsParser.parseParameterArgument("k1");
        assertNotNull(result1);
        assertEquals(0, result1.getIndex());
        assertEquals("k1", result1.getKey());
        assertEquals("value1", result1.getValue());
        
        ParameterArgument result2 = argsParser.parseParameterArgument("k2");
        assertNotNull(result2);
        assertEquals(2, result2.getIndex());
        assertEquals("k2", result2.getKey());
        assertEquals("value2", result2.getValue());
        
        ParameterArgument result3 = argsParser.parseParameterArgument("k3");
        assertNull(result3);
    }
    
    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals(0, result.get(0).getIndex());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals(1, result.get(1).getIndex());
        assertEquals("arg3", result.get(2).getValue());
        assertEquals(2, result.get(2).getIndex());
        
        List<StringArgument> emptyResult = argsParser.parseStringArgument();
        assertEquals(0, emptyResult.size());
    }
    
}