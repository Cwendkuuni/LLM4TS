import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        int result = argsParser.getArgsCount();
        assertEquals(2, result);
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] result = argsParser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        String[] result = argsParser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "arg2"});
        int result = argsParser.getArgsLeftCount();
        assertEquals(2, result);
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key"});
        SwitchArgument result = argsParser.parseSwitchArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertTrue(result.isLongKey());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyNotPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--other"});
        SwitchArgument result = argsParser.parseSwitchArgument("key");
        assertEquals(-1, result.getIndex());
        assertEquals("key", result.getKey());
        assertFalse(result.isLongKey());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-k"});
        SwitchArgument result = argsParser.parseSwitchArgument("k");
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertFalse(result.isLongKey());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyNotPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-o"});
        SwitchArgument result = argsParser.parseSwitchArgument("k");
        assertEquals(-1, result.getIndex());
        assertEquals("k", result.getKey());
        assertFalse(result.isLongKey());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyAndValuePresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyPresentButNoValue() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyNotPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--other"});
        ParameterArgument result = argsParser.parseParameterArgument("key");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyAndValuePresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-k", "value"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("k", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyPresentButNoValue() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-k"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyNotPresent() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-o"});
        ParameterArgument result = argsParser.parseParameterArgument("k");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getValue());
    }
}