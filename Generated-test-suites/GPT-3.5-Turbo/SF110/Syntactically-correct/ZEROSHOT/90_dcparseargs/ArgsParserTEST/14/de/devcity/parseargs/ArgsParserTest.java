import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import java.util.*;

public class ArgsParserTest {

    private ArgsParser parser;
    
    @Before
    public void setUp() {
        parser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
        
        parser.setArgs(new String[]{});
        assertEquals(0, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullInput() {
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
        
        parser.setArgs(new String[]{"arg1", null, "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
        
        parser.setArgs(new String[]{null, null, null});
        assertEquals(0, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_LongKey_Exists() {
        parser.setArgs(new String[]{"--flag"});
        SwitchArgument result = parser.parseSwitchArgument("flag");
        assertTrue(result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("flag", result.getKey());
        
        parser.setArgs(new String[]{"--flag", "value"});
        result = parser.parseSwitchArgument("flag");
        assertTrue(result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("flag", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_LongKey_NotExists() {
        parser.setArgs(new String[]{"--flag"});
        SwitchArgument result = parser.parseSwitchArgument("other");
        assertFalse(result.getValue());
        assertEquals(-1, result.getIndex());
        assertEquals("other", result.getKey());
        
        parser.setArgs(new String[]{"--flag", "value"});
        result = parser.parseSwitchArgument("other");
        assertFalse(result.getValue());
        assertEquals(-1, result.getIndex());
        assertEquals("other", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKey_Exists() {
        parser.setArgs(new String[]{"-f"});
        SwitchArgument result = parser.parseSwitchArgument("f");
        assertTrue(result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("f", result.getKey());
        
        parser.setArgs(new String[]{"-f", "value"});
        result = parser.parseSwitchArgument("f");
        assertTrue(result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("f", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_ShortKey_NotExists() {
        parser.setArgs(new String[]{"-f"});
        SwitchArgument result = parser.parseSwitchArgument("o");
        assertFalse(result.getValue());
        assertEquals(-1, result.getIndex());
        assertEquals("o", result.getKey());
        
        parser.setArgs(new String[]{"-f", "value"});
        result = parser.parseSwitchArgument("o");
        assertFalse(result.getValue());
        assertEquals(-1, result.getIndex());
        assertEquals("o", result.getKey());
    }
    
    @Test
    public void testParseParameterArgument_LongKey_Exists() {
        parser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());
        
        parser.setArgs(new String[]{"--key", "value", "--other"});
        result = parser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_LongKey_NotExists() {
        parser.setArgs(new String[]{"--key", "value"});
        ParameterArgument result = parser.parseParameterArgument("other");
        assertNull(result);
        
        parser.setArgs(new String[]{"--key", "value", "--other"});
        result = parser.parseParameterArgument("other");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_ShortKey_Exists() {
        parser.setArgs(new String[]{"-k", "value"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
        
        parser.setArgs(new String[]{"-k", "value", "--other"});
        result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_ShortKey_NotExists() {
        parser.setArgs(new String[]{"-k", "value"});
        ParameterArgument result = parser.parseParameterArgument("o");
        assertNull(result);
        
        parser.setArgs(new String[]{"-k", "value", "--other"});
        result = parser.parseParameterArgument("o");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_ShortKey_LastValue() {
        parser.setArgs(new String[]{"-k", "value"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
        
        parser.setArgs(new String[]{"-k", "value1", "-k", "value2"});
        result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(2, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value2", result.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
        assertEquals("arg3", result.get(2).getValue());
        
        parser.setArgs(new String[]{"arg1", null, "arg3"});
        result = parser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg3", result.get(1).getValue());
        
        parser.setArgs(new String[]{null, null, null});
        result = parser.parseStringArgument();
        assertEquals(0, result.size());
    }
}