package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", null, "arg3", null};
        parser.setArgs(args);
        
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--foo", "-b", "arg1", "--bar"};
        parser.setArgs(args);
        
        SwitchArgument notFoundSwitch = parser.parseSwitchArgument("c");
        assertFalse(notFoundSwitch.isPresent());
        
        SwitchArgument shortSwitch = parser.parseSwitchArgument("a");
        assertTrue(shortSwitch.isPresent());
        assertEquals(0, shortSwitch.getIndex());
        assertEquals("a", shortSwitch.getKey());
        
        SwitchArgument longSwitch = parser.parseSwitchArgument("foo");
        assertTrue(longSwitch.isPresent());
        assertEquals(1, longSwitch.getIndex());
        assertEquals("foo", longSwitch.getKey());
        
        SwitchArgument noValueSwitch = parser.parseSwitchArgument("b");
        assertTrue(noValueSwitch.isPresent());
        assertEquals(2, noValueSwitch.getIndex());
        assertEquals("b", noValueSwitch.getKey());
        
        SwitchArgument valueSwitch = parser.parseSwitchArgument("bar");
        assertTrue(valueSwitch.isPresent());
        assertEquals(4, valueSwitch.getIndex());
        assertEquals("bar", valueSwitch.getKey());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--foo", "-b", "arg1", "--bar"};
        parser.setArgs(args);
        
        ParameterArgument notFoundParam = parser.parseParameterArgument("c");
        assertNull(notFoundParam);
        
        ParameterArgument shortParam = parser.parseParameterArgument("a");
        assertNull(shortParam);
        
        ParameterArgument longParam = parser.parseParameterArgument("foo");
        assertNull(longParam);
        
        ParameterArgument noValueParam = parser.parseParameterArgument("b");
        assertNull(noValueParam);
        
        ParameterArgument valueParam = parser.parseParameterArgument("bar");
        assertNull(valueParam);
        
        String[] args2 = {"-c", "--foo", "-d", "value1", "--baz", "value2", "arg1"};
        parser.setArgs(args2);
        
        ParameterArgument notFoundParam2 = parser.parseParameterArgument("e");
        assertNull(notFoundParam2);
        
        ParameterArgument shortParam2 = parser.parseParameterArgument("c");
        assertNotNull(shortParam2);
        assertEquals(0, shortParam2.getIndex());
        assertEquals("c", shortParam2.getKey());
        assertEquals("arg1", shortParam2.getValue());
        
        ParameterArgument longParam2 = parser.parseParameterArgument("foo");
        assertNull(longParam2);
        
        ParameterArgument noValueParam2 = parser.parseParameterArgument("d");
        assertNull(noValueParam2);
        
        ParameterArgument valueParam2 = parser.parseParameterArgument("baz");
        assertNotNull(valueParam2);
        assertEquals(4, valueParam2.getIndex());
        assertEquals("baz", valueParam2.getKey());
        assertEquals("value2", valueParam2.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", null, "arg3", null};
        parser.setArgs(args);
        
        List<StringArgument> stringArgs = parser.parseStringArgument();
        
        assertEquals(3, stringArgs.size());
        assertEquals(0, stringArgs.get(0).getIndex());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals(1, stringArgs.get(1).getIndex());
        assertEquals("arg2", stringArgs.get(1).getValue());
        assertEquals(3, stringArgs.get(2).getIndex());
        assertEquals("arg3", stringArgs.get(2).getValue());
    }
}