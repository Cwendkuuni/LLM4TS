package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals("arg1", parser.getArgs()[0]);
        assertEquals("arg2", parser.getArgs()[1]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals("arg1", parser.getArgs()[0]);
        assertEquals("arg2", parser.getArgs()[1]);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, null, "arg4", null});
        assertEquals(2, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value"});
        SwitchArgument argument = parser.parseSwitchArgument("key1");
        assertEquals(0, argument.getIndex());
        assertEquals("key1", argument.getKey());
        assertEquals(true, argument.isFlag());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value"});
        SwitchArgument argument = parser.parseSwitchArgument("key2");
        assertEquals(-1, argument.getIndex());
        assertEquals("key2", argument.getKey());
        assertEquals(false, argument.isFlag());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyExists() {
        ArgsParser parser = new ArgsParser(new String[]{"-k1", "value"});
        SwitchArgument argument = parser.parseSwitchArgument("k");
        assertEquals(0, argument.getIndex());
        assertEquals("k", argument.getKey());
        assertEquals(true, argument.isFlag());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"-k1", "value"});
        SwitchArgument argument = parser.parseSwitchArgument("s");
        assertEquals(-1, argument.getIndex());
        assertEquals("s", argument.getKey());
        assertEquals(false, argument.isFlag());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistsWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value"});
        ParameterArgument argument = parser.parseParameterArgument("key1");
        assertEquals(0, argument.getIndex());
        assertEquals("key1", argument.getKey());
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistsWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1"});
        ParameterArgument argument = parser.parseParameterArgument("key1");
        assertNull(argument);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value"});
        ParameterArgument argument = parser.parseParameterArgument("key2");
        assertNull(argument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistsWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-k1", "value"});
        ParameterArgument argument = parser.parseParameterArgument("k");
        assertEquals(0, argument.getIndex());
        assertEquals("k", argument.getKey());
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistsWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-k1"});
        ParameterArgument argument = parser.parseParameterArgument("k");
        assertNull(argument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser(new String[]{"-k1", "value"});
        ParameterArgument argument = parser.parseParameterArgument("s");
        assertNull(argument);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg3", "arg4", null});
        List<StringArgument> arguments = parser.parseStringArgument();
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0).getValue());
        assertEquals("arg3", arguments.get(1).getValue());
    }
}