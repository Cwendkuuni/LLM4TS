package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(Arrays.toString(args), Arrays.toString(argsParser.getArgs()));
    }
    
    @Test
    public void testSetArgs() {
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(Arrays.toString(args), Arrays.toString(argsParser.getArgs()));
    }
    
    @Test
    public void testSetArgs_NullArgs() {
        try {
            argsParser.setArgs(null);
        } catch (NullPointerException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[] {"arg1", null, "arg3"});
        assertEquals(2, argsParser.getArgsLeftCount());
        
        argsParser.setArgs(new String[] {});
        assertEquals(0, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyExists() {
        argsParser.setArgs(new String[] {"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key1");
        assertEquals("key1", switchArgument.getKey());
        assertEquals(true, switchArgument.exists());
        assertEquals(0, switchArgument.getIndex());
        
        switchArgument = argsParser.parseSwitchArgument("key2");
        assertEquals("key2", switchArgument.getKey());
        assertEquals(true, switchArgument.exists());
        assertEquals(1, switchArgument.getIndex());
    }
    
    @Test
    public void testParseSwitchArgument_LongKeyDoesNotExist() {
        argsParser.setArgs(new String[] {"--key1", "--key2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("key3");
        assertEquals("key3", switchArgument.getKey());
        assertEquals(false, switchArgument.exists());
        assertEquals(-1, switchArgument.getIndex());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyExists() {
        argsParser.setArgs(new String[] {"-k1", "-k2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k1");
        assertEquals("k1", switchArgument.getKey());
        assertEquals(true, switchArgument.exists());
        assertEquals(0, switchArgument.getIndex());
        
        switchArgument = argsParser.parseSwitchArgument("k2");
        assertEquals("k2", switchArgument.getKey());
        assertEquals(true, switchArgument.exists());
        assertEquals(1, switchArgument.getIndex());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKeyDoesNotExist() {
        argsParser.setArgs(new String[] {"-k1", "-k2"});
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("k3");
        assertEquals("k3", switchArgument.getKey());
        assertEquals(false, switchArgument.exists());
        assertEquals(-1, switchArgument.getIndex());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithNoValue() {
        argsParser.setArgs(new String[] {"--key1", "value1"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithValue() {
        argsParser.setArgs(new String[] {"--key1", "value1"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertEquals("key1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithNoValue_MultipleKeys() {
        argsParser.setArgs(new String[] {"--key1", "value1", "--key2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key2");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgument_LongKeyWithValue_MultipleKeys() {
        argsParser.setArgs(new String[] {"--key1", "value1", "--key2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("key1");
        assertEquals("key1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithNoValue() {
        argsParser.setArgs(new String[] {"-k1", "value1"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k1");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithValue() {
        argsParser.setArgs(new String[] {"-k1", "value1"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k1");
        assertEquals("k1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithNoValue_MultipleKeys() {
        argsParser.setArgs(new String[] {"-k1", "value1", "-k2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k2");
        assertNull(parameterArgument);
    }
    
    @Test
    public void testParseParameterArgument_ShortKeyWithValue_MultipleKeys() {
        argsParser.setArgs(new String[] {"-k1", "value1", "-k2"});
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k1");
        assertEquals("k1", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }
    
    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        List<StringArgument> expectedStringArguments = new ArrayList<StringArgument>();
        expectedStringArguments.add(new StringArgument(0, "arg1"));
        expectedStringArguments.add(new StringArgument(1, "arg2"));
        expectedStringArguments.add(new StringArgument(2, "arg3"));
        assertEquals(expectedStringArguments, stringArguments);
    }
}