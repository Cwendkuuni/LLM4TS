import org.junit.Test;
import static org.junit.Assert.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
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
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.innerArgs);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", null, "arg4"});
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument argument = parser.parseSwitchArgument("switch");
        assertTrue(argument.isSwitchOn());
    }
    
    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-s"});
        SwitchArgument argument = parser.parseSwitchArgument("s");
        assertTrue(argument.isSwitchOn());
    }
    
    @Test
    public void testParseSwitchArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch"});
        SwitchArgument argument = parser.parseSwitchArgument("key");
        assertFalse(argument.isSwitchOn());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument argument = parser.parseParameterArgument("param");
        assertNotNull(argument);
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--param"});
        ParameterArgument argument = parser.parseParameterArgument("param");
        assertNull(argument);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-p", "value"});
        ParameterArgument argument = parser.parseParameterArgument("p");
        assertNotNull(argument);
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-p"});
        ParameterArgument argument = parser.parseParameterArgument("p");
        assertNull(argument);
    }
    
    @Test
    public void testParseParameterArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument argument = parser.parseParameterArgument("key");
        assertNull(argument);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> arguments = parser.parseStringArgument();
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0).getValue());
        assertEquals("arg2", arguments.get(1).getValue());
    }
}