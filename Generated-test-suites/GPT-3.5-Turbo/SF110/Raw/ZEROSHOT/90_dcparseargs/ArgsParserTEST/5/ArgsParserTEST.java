package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    // Test case for ArgsParser default constructor
    @Test
    public void testArgsParser() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }
    
    // Test case for ArgsParser constructor with args parameter
    @Test
    public void testArgsParserWithArgs() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test case for getting args count
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(4, parser.getArgsCount());
    }
    
    // Test case for getting args
    @Test
    public void testGetArgs() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test case for setting args
    @Test
    public void testSetArgs() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test case for getting args left count
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(4, parser.getArgsLeftCount());
    }
    
    // Test case for parsing switch argument with long key
    @Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("b");
        assertTrue(result.isFound());
        assertEquals(1, result.getIndex());
        assertEquals("b", result.getKey());
        assertTrue(result.isLongKey());
    }
    
    // Test case for parsing switch argument with short key
    @Test
    public void testParseSwitchArgumentShortKey() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertTrue(result.isFound());
        assertEquals(0, result.getIndex());
        assertEquals("a", result.getKey());
        assertFalse(result.isLongKey());
    }
    
    // Test case for parsing switch argument when not found
    @Test
    public void testParseSwitchArgumentNotFound() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("c");
        assertFalse(result.isFound());
        assertEquals(-1, result.getIndex());
        assertEquals("c", result.getKey());
        assertFalse(result.isLongKey());
    }
    
    // Test case for parsing parameter argument with long key
    @Test
    public void testParseParameterArgumentLongKey() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("b");
        assertNotNull(result);
        assertEquals(1, result.getIndex());
        assertEquals("b", result.getKey());
        assertEquals("value1", result.getValue());
    }
    
    // Test case for parsing parameter argument with short key
    @Test
    public void testParseParameterArgumentShortKey() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("a");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("a", result.getKey());
        assertEquals("value1", result.getValue());
    }
    
    // Test case for parsing parameter argument when not found
    @Test
    public void testParseParameterArgumentNotFound() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("c");
        assertNull(result);
    }
    
    // Test case for parsing string argument
    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "--b", "value1", "value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertNotNull(result);
        assertEquals(4, result.size());
    }
    
}