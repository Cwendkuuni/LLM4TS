package de.devcity.parseargs;

import org.apache.commons.cli.Util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "-ghi"});
        assertEquals(3, parser.getArgsCount());
        
        parser.setArgs(new String[]{"-abc", "--def"});
        assertEquals(2, parser.getArgsCount());
        
        parser.setArgs(new String[]{});
        assertEquals(0, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "-ghi"});
        assertEquals(new String[]{"-abc", "--def", "-ghi"}, parser.getArgs());
        
        parser.setArgs(new String[]{"-abc", "--def"});
        assertEquals(new String[]{"-abc", "--def"}, parser.getArgs());
        
        parser.setArgs(new String[]{});
        assertEquals(new String[]{}, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-abc", "--def", "-ghi"});
        assertEquals(new String[]{"-abc", "--def", "-ghi"}, parser.getArgs());
        
        parser.setArgs(new String[]{"-abc", "--def"});
        assertEquals(new String[]{"-abc", "--def"}, parser.getArgs());
        
        parser.setArgs(new String[]{});
        assertEquals(new String[]{}, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "-ghi"});
        assertEquals(3, parser.getArgsLeftCount());
        
        parser.parseSwitchArgument("abc");
        assertEquals(2, parser.getArgsLeftCount());
        
        parser.parseSwitchArgument("def");
        assertEquals(1, parser.getArgsLeftCount());
        
        parser.parseSwitchArgument("ghi");
        assertEquals(0, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "-ghi"});
        
        SwitchArgument switch1 = parser.parseSwitchArgument("abc");
        assertEquals(0, switch1.getIndex());
        assertEquals("abc", switch1.getKey());
        assertEquals(true, switch1.getValue());
        
        SwitchArgument switch2 = parser.parseSwitchArgument("def");
        assertEquals(1, switch2.getIndex());
        assertEquals("def", switch2.getKey());
        assertEquals(true, switch2.getValue());
        
        SwitchArgument switch3 = parser.parseSwitchArgument("ghi");
        assertEquals(2, switch3.getIndex());
        assertEquals("ghi", switch3.getKey());
        assertEquals(true, switch3.getValue());
        
        SwitchArgument switch4 = parser.parseSwitchArgument("xyz");
        assertEquals(-1, switch4.getIndex());
        assertEquals("xyz", switch4.getKey());
        assertEquals(false, switch4.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--abc", "--def", "--ghi"});
        
        SwitchArgument switch1 = parser.parseSwitchArgument("abc");
        assertEquals(0, switch1.getIndex());
        assertEquals("abc", switch1.getKey());
        assertEquals(true, switch1.getValue());
        
        SwitchArgument switch2 = parser.parseSwitchArgument("def");
        assertEquals(1, switch2.getIndex());
        assertEquals("def", switch2.getKey());
        assertEquals(true, switch2.getValue());
        
        SwitchArgument switch3 = parser.parseSwitchArgument("ghi");
        assertEquals(2, switch3.getIndex());
        assertEquals("ghi", switch3.getKey());
        assertEquals(true, switch3.getValue());
        
        SwitchArgument switch4 = parser.parseSwitchArgument("xyz");
        assertEquals(-1, switch4.getIndex());
        assertEquals("xyz", switch4.getKey());
        assertEquals(false, switch4.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentNoValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "-ghi"});
        
        SwitchArgument switch1 = parser.parseSwitchArgument("abc");
        assertEquals(0, switch1.getIndex());
        assertEquals("abc", switch1.getKey());
        assertEquals(true, switch1.getValue());
        
        SwitchArgument switch2 = parser.parseSwitchArgument("def");
        assertEquals(1, switch2.getIndex());
        assertEquals("def", switch2.getKey());
        assertEquals(true, switch2.getValue());
        
        SwitchArgument switch3 = parser.parseSwitchArgument("ghi");
        assertEquals(2, switch3.getIndex());
        assertEquals("ghi", switch3.getKey());
        assertEquals(true, switch3.getValue());
        
        parser.parseSwitchArgument("abc");
        assertEquals(false, switch1.getValue());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def=123", "-ghi"});
        
        ParameterArgument param1 = parser.parseParameterArgument("abc");
        assertEquals(0, param1.getIndex());
        assertEquals("abc", param1.getKey());
        assertEquals("", param1.getValue());
        
        ParameterArgument param2 = parser.parseParameterArgument("def");
        assertEquals(1, param2.getIndex());
        assertEquals("def", param2.getKey());
        assertEquals("123", param2.getValue());
        
        ParameterArgument param3 = parser.parseParameterArgument("ghi");
        assertEquals(2, param3.getIndex());
        assertEquals("ghi", param3.getKey());
        assertEquals("", param3.getValue());
        
        ParameterArgument param4 = parser.parseParameterArgument("xyz");
        assertEquals(null, param4);
    }
    
    @Test
    public void testParseParameterArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def=123", "-ghi"});
        
        ParameterArgument param1 = parser.parseParameterArgument("abc");
        assertEquals(0, param1.getIndex());
        assertEquals("abc", param1.getKey());
        assertEquals("", param1.getValue());
        
        ParameterArgument param2 = parser.parseParameterArgument("def");
        assertEquals(1, param2.getIndex());
        assertEquals("def", param2.getKey());
        assertEquals("123", param2.getValue());
        
        ParameterArgument param3 = parser.parseParameterArgument("ghi");
        assertEquals(2, param3.getIndex());
        assertEquals("ghi", param3.getKey());
        assertEquals("", param3.getValue());
        
        ParameterArgument param4 = parser.parseParameterArgument("xyz");
        assertEquals(null, param4);
    }
    
    @Test
    public void testParseParameterArgumentNoValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def=123", "-ghi"});
        
        ParameterArgument param1 = parser.parseParameterArgument("abc");
        assertEquals(0, param1.getIndex());
        assertEquals("abc", param1.getKey());
        assertEquals("", param1.getValue());
        
        ParameterArgument param2 = parser.parseParameterArgument("def");
        assertEquals(1, param2.getIndex());
        assertEquals("def", param2.getKey());
        assertEquals("123", param2.getValue());
        
        parser.parseParameterArgument("def");
        assertEquals("", param2.getValue());
        
        ParameterArgument param3 = parser.parseParameterArgument("ghi");
        assertEquals(2, param3.getIndex());
        assertEquals("ghi", param3.getKey());
        assertEquals("", param3.getValue());
        
        ParameterArgument param4 = parser.parseParameterArgument("xyz");
        assertEquals(null, param4);
    }
    
    @Test
    public void testParseParameterArgumentNextValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "--def", "123", "-ghi"});
        
        ParameterArgument param1 = parser.parseParameterArgument("abc");
        assertEquals(0, param1.getIndex());
        assertEquals("abc", param1.getKey());
        assertEquals("", param1.getValue());
        
        ParameterArgument param2 = parser.parseParameterArgument("def");
        assertEquals(null, param2);
        
        ParameterArgument param3 = parser.parseParameterArgument("ghi");
        assertEquals(2, param3.getIndex());
        assertEquals("ghi", param3.getKey());
        assertEquals("", param3.getValue());
        
        ParameterArgument param4 = parser.parseParameterArgument("xyz");
        assertEquals(null, param4);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-abc", "def", "-ghi"});
        
        List<StringArgument> args = parser.parseStringArgument();
        assertEquals(2, args.size());
        
        assertEquals(0, args.get(0).getIndex());
        assertEquals("-abc", args.get(0).getValue());
        
        assertEquals(1, args.get(1).getIndex());
        assertEquals("def", args.get(1).getValue());
    }
}