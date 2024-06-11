package de.devcity.parseargs;

import org.junit.Test;
import org.junit.Assert;

import java.util.List;

import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        Assert.assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] expected = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expected, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] expected = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expected, parser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        Assert.assertEquals(2, parser.getArgsLeftCount());
        
        parser.parseSwitchArgument("arg1");
        Assert.assertEquals(1, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-a", "--b"});
        
        SwitchArgument argument1 = parser.parseSwitchArgument("a");
        Assert.assertEquals(0, argument1.getIndex());
        Assert.assertEquals("a", argument1.getKey());
        Assert.assertTrue(argument1.isLongKey());
        
        SwitchArgument argument2 = parser.parseSwitchArgument("b");
        Assert.assertEquals(1, argument2.getIndex());
        Assert.assertEquals("b", argument2.getKey());
        Assert.assertTrue(argument2.isLongKey());
        
        SwitchArgument argument3 = parser.parseSwitchArgument("c");
        Assert.assertEquals(-1, argument3.getIndex());
        Assert.assertEquals("c", argument3.getKey());
        Assert.assertFalse(argument3.isLongKey());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--param1", "value1", "-param2", "value2"});
        
        ParameterArgument argument1 = parser.parseParameterArgument("param1");
        Assert.assertEquals(0, argument1.getIndex());
        Assert.assertEquals("param1", argument1.getKey());
        Assert.assertEquals("value1", argument1.getValue());
        
        ParameterArgument argument2 = parser.parseParameterArgument("param2");
        Assert.assertEquals(2, argument2.getIndex());
        Assert.assertEquals("param2", argument2.getKey());
        Assert.assertEquals("value2", argument2.getValue());
        
        ParameterArgument argument3 = parser.parseParameterArgument("param3");
        Assert.assertNull(argument3);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        
        List<StringArgument> arguments = parser.parseStringArgument();
        Assert.assertEquals(3, arguments.size());
        
        StringArgument argument1 = arguments.get(0);
        Assert.assertEquals(0, argument1.getIndex());
        Assert.assertEquals("arg1", argument1.getValue());
        
        StringArgument argument2 = arguments.get(1);
        Assert.assertEquals(1, argument2.getIndex());
        Assert.assertEquals("arg2", argument2.getValue());
        
        StringArgument argument3 = arguments.get(2);
        Assert.assertEquals(2, argument3.getIndex());
        Assert.assertEquals("arg3", argument3.getValue());
    }

}