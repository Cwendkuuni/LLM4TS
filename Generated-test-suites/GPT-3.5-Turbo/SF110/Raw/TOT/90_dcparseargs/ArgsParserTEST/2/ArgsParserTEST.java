import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
        assertArrayEquals(args, parser.getInnerArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyExists() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("b");
        assertEquals(1, switchArg.getIndex());
        assertEquals("b", switchArg.getKey());
        assertTrue(switchArg.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("d");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("d", switchArg.getKey());
        assertFalse(switchArg.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyExists() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertEquals(0, switchArg.getIndex());
        assertEquals("a", switchArg.getKey());
        assertTrue(switchArg.getValue());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("d");
        assertEquals(-1, switchArg.getIndex());
        assertEquals("d", switchArg.getKey());
        assertFalse(switchArg.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistsWithValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c", "--d", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("d");
        assertEquals(3, paramArg.getIndex());
        assertEquals("d", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistsWithoutValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c", "--d"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("d");
        assertNull(paramArg);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("e");
        assertNull(paramArg);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistsWithValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c", "-d", "value"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("d");
        assertEquals(3, paramArg.getIndex());
        assertEquals("d", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistsWithoutValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c", "-d"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("d");
        assertNull(paramArg);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyDoesNotExist() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};
        parser.setArgs(args);
        ParameterArgument paramArg = parser.parseParameterArgument("e");
        assertNull(paramArg);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "string", "--b", "-c"};
        parser.setArgs(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
        assertEquals(0, stringArgs.get(0).getIndex());
        assertEquals("a", stringArgs.get(0).getValue());
        assertEquals(1, stringArgs.get(1).getIndex());
        assertEquals("string", stringArgs.get(1).getValue());
    }

}