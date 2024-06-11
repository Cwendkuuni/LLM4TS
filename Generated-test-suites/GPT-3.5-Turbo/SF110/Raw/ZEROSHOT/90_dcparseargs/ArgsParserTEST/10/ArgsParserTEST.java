package de.devcity.parseargs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import java.util.List;
import java.util.ArrayList;

public class ArgsParserTest {
    private ArgsParser parser;
    
    @Before
    public void setUp() {
        this.parser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        this.parser.setArgs(args);
        assertEquals(3, this.parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        this.parser.setArgs(args);
        assertArrayEquals(args, this.parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        this.parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        this.parser.setArgs(args);
        assertEquals(3, this.parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"--switch"};
        this.parser.setArgs(args);
        SwitchArgument result = this.parser.parseSwitchArgument("switch");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("switch", result.getKey());
        assertTrue(result.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentShortKey() {
        String[] args = {"-s"};
        this.parser.setArgs(args);
        SwitchArgument result = this.parser.parseSwitchArgument("s");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentNotFound() {
        String[] args = {"--switch"};
        this.parser.setArgs(args);
        SwitchArgument result = this.parser.parseSwitchArgument("other");
        assertNotNull(result);
        assertEquals(-1, result.getIndex());
        assertEquals("other", result.getKey());
        assertFalse(result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        String[] args = {"--param", "value"};
        this.parser.setArgs(args);
        ParameterArgument result = this.parser.parseParameterArgument("param");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("param", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        String[] args = {"--param"};
        this.parser.setArgs(args);
        ParameterArgument result = this.parser.parseParameterArgument("param");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithValue() {
        String[] args = {"-p", "value"};
        this.parser.setArgs(args);
        ParameterArgument result = this.parser.parseParameterArgument("p");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithoutValue() {
        String[] args = {"-p"};
        this.parser.setArgs(args);
        ParameterArgument result = this.parser.parseParameterArgument("p");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentNotFound() {
        String[] args = {"--param", "value"};
        this.parser.setArgs(args);
        ParameterArgument result = this.parser.parseParameterArgument("other");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        this.parser.setArgs(args);
        List<StringArgument> result = this.parser.parseStringArgument();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg3", result.get(2).getValue());
    }
}