import org.junit.*;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        assertArrayEquals(new String[]{"-a", "--b"}, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-a", "--b"});
        assertArrayEquals(new String[]{"-a", "--b"}, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        assertEquals(2, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentFound() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        SwitchArgument argument = parser.parseSwitchArgument("a");
        assertNotNull(argument);
        assertEquals(0, argument.getIndex());
        assertEquals("a", argument.getKey());
        assertTrue(argument.isFound());
    }
    
    @Test
    public void testParseSwitchArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        SwitchArgument argument = parser.parseSwitchArgument("c");
        assertNotNull(argument);
        assertEquals(-1, argument.getIndex());
        assertEquals("c", argument.getKey());
        assertFalse(argument.isFound());
    }
    
    @Test
    public void testParseParameterArgumentFound() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value", "--b", "value"});
        ParameterArgument argument = parser.parseParameterArgument("a");
        assertNotNull(argument);
        assertEquals(0, argument.getIndex());
        assertEquals("a", argument.getKey());
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseParameterArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        ParameterArgument argument = parser.parseParameterArgument("c");
        assertNull(argument);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value", "--b", "value"});
        List<StringArgument> arguments = parser.parseStringArgument();
        assertEquals(2, arguments.size());
        assertEquals(0, arguments.get(0).getIndex());
        assertEquals("value", arguments.get(0).getValue());
        assertEquals(2, arguments.get(1).getIndex());
        assertEquals("value", arguments.get(1).getValue());
    }
}