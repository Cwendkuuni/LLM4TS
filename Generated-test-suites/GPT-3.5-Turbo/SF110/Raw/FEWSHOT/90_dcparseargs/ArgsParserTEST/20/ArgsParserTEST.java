import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.apache.commons.cli.Util;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;

import java.util.List;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "-b", "-c"});
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        String[] args = new String[]{"-a", "-b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        String[] args = new String[]{"-a", "-b", "-c"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertEquals(args, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "-b", "-c"});
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--verbose", "-a", "--debug", "-b", "-c"});
        SwitchArgument result1 = parser.parseSwitchArgument("verbose");
        assertNotNull(result1);
        assertEquals(0, result1.getIndex());
        assertEquals("verbose", result1.getKey());
        assertEquals(true, result1.hasValue());
        
        SwitchArgument result2 = parser.parseSwitchArgument("a");
        assertNotNull(result2);
        assertEquals(1, result2.getIndex());
        assertEquals("a", result2.getKey());
        assertEquals(true, result2.hasValue());
        
        SwitchArgument result3 = parser.parseSwitchArgument("debug");
        assertNotNull(result3);
        assertEquals(2, result3.getIndex());
        assertEquals("debug", result3.getKey());
        assertEquals(true, result3.hasValue());
        
        SwitchArgument result4 = parser.parseSwitchArgument("b");
        assertNotNull(result4);
        assertEquals(3, result4.getIndex());
        assertEquals("b", result4.getKey());
        assertEquals(true, result4.hasValue());
        
        SwitchArgument result5 = parser.parseSwitchArgument("c");
        assertNotNull(result5);
        assertEquals(4, result5.getIndex());
        assertEquals("c", result5.getKey());
        assertEquals(true, result5.hasValue());
        
        SwitchArgument result6 = parser.parseSwitchArgument("d");
        assertNotNull(result6);
        assertEquals(-1, result6.getIndex());
        assertEquals("d", result6.getKey());
        assertEquals(false, result6.hasValue());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--verbose", "true", "--debug", "false", "-a", "1", "-b", "2", "-c"});
        ParameterArgument result1 = parser.parseParameterArgument("verbose");
        assertNotNull(result1);
        assertEquals(0, result1.getIndex());
        assertEquals("verbose", result1.getKey());
        assertEquals("true", result1.getValue());
        
        ParameterArgument result2 = parser.parseParameterArgument("debug");
        assertNotNull(result2);
        assertEquals(2, result2.getIndex());
        assertEquals("debug", result2.getKey());
        assertEquals("false", result2.getValue());
        
        ParameterArgument result3 = parser.parseParameterArgument("a");
        assertNotNull(result3);
        assertEquals(4, result3.getIndex());
        assertEquals("a", result3.getKey());
        assertEquals("1", result3.getValue());
        
        ParameterArgument result4 = parser.parseParameterArgument("b");
        assertNotNull(result4);
        assertEquals(6, result4.getIndex());
        assertEquals("b", result4.getKey());
        assertEquals("2", result4.getValue());
        
        ParameterArgument result5 = parser.parseParameterArgument("c");
        assertNull(result5);
        
        ParameterArgument result6 = parser.parseParameterArgument("d");
        assertNull(result6);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "hello", "-b", "world", "-c"});
        List<StringArgument> result = parser.parseStringArgument();
        
        assertEquals(3, result.size());
        
        StringArgument arg1 = result.get(0);
        assertEquals(0, arg1.getIndex());
        assertEquals("a", arg1.getValue());
        
        StringArgument arg2 = result.get(1);
        assertEquals(2, arg2.getIndex());
        assertEquals("b", arg2.getValue());
        
        StringArgument arg3 = result.get(3);
        assertEquals(4, arg3.getIndex());
        assertEquals("c", arg3.getValue());
    }
}