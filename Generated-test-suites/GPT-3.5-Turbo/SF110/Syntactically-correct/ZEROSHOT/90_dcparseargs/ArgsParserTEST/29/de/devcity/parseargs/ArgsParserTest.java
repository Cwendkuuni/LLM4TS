package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;
import de.devcity.parseargs.arguments.*;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsLeftCount();
        assertEquals(3, count);
        
        parser.parseSwitchArgument("arg1");
        count = parser.getArgsLeftCount();
        assertEquals(2, count);
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        SwitchArgument arg1 = parser.parseSwitchArgument("arg1");
        assertTrue(arg1.isSet());
        assertEquals(0, arg1.getIndex());
        
        SwitchArgument arg2 = parser.parseSwitchArgument("arg2");
        assertTrue(arg2.isSet());
        assertEquals(1, arg2.getIndex());
        
        SwitchArgument arg3 = parser.parseSwitchArgument("arg3");
        assertTrue(arg3.isSet());
        assertEquals(2, arg3.getIndex());
        
        SwitchArgument arg4 = parser.parseSwitchArgument("arg4");
        assertFalse(arg4.isSet());
        assertEquals(-1, arg4.getIndex());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "value1", "-arg2", "value2", "--arg3", "value3"});
        ParameterArgument arg1 = parser.parseParameterArgument("arg1");
        assertNotNull(arg1);
        assertEquals(0, arg1.getIndex());
        assertEquals("value1", arg1.getValue());
        
        ParameterArgument arg2 = parser.parseParameterArgument("arg2");
        assertNotNull(arg2);
        assertEquals(2, arg2.getIndex());
        assertEquals("value2", arg2.getValue());
        
        ParameterArgument arg3 = parser.parseParameterArgument("arg3");
        assertNotNull(arg3);
        assertEquals(4, arg3.getIndex());
        assertEquals("value3", arg3.getValue());
        
        ParameterArgument arg4 = parser.parseParameterArgument("arg4");
        assertNull(arg4);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "--arg2", "value1", "-arg3", "value2", "--arg4"});
        List<StringArgument> args = parser.parseStringArgument();
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0).getValue());
        assertEquals("--arg2", args.get(1).getValue());
        assertEquals("-arg3", args.get(2).getValue());
    }
}