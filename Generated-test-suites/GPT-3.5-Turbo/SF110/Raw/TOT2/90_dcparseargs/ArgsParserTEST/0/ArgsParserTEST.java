import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        parser.parseSwitchArgument("-arg1");
        assertEquals(2, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-arg1", "--arg2"});
        SwitchArgument switchArg = parser.parseSwitchArgument("arg1");
        assertTrue(switchArg.isSuccess());
        switchArg = parser.parseSwitchArgument("arg2");
        assertFalse(switchArg.isSuccess());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key1", "value1"});
        ParameterArgument paramArg = parser.parseParameterArgument("key1");
        assertNotNull(paramArg);
        assertEquals("value1", paramArg.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.parseStringArgument().size());
    }
}