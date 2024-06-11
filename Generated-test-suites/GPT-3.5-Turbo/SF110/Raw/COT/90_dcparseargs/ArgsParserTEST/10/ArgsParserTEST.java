import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Test;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "arg2", "arg3"});
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
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        try {
            parser.setArgs(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", null, "arg2", null, "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"--switch1", "-switch2"});
        SwitchArgument switch1 = parser.parseSwitchArgument("switch1");
        assertTrue(switch1.isSwitchOn());
        assertEquals(0, switch1.getIndex());
        
        SwitchArgument switch2 = parser.parseSwitchArgument("switch2");
        assertTrue(switch2.isSwitchOn());
        assertEquals(1, switch2.getIndex());
        
        SwitchArgument switch3 = parser.parseSwitchArgument("switch3");
        assertFalse(switch3.isSwitchOn());
        assertEquals(-1, switch3.getIndex());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"--param1", "value1", "-param2=value2"});
        
        ParameterArgument param1 = parser.parseParameterArgument("param1");
        assertNotNull(param1);
        assertEquals("value1", param1.getValue());
        
        ParameterArgument param2 = parser.parseParameterArgument("param2");
        assertNotNull(param2);
        assertEquals("value2", param2.getValue());
        
        ParameterArgument param3 = parser.parseParameterArgument("param3");
        assertNull(param3);
        
        ParameterArgument param4 = parser.parseParameterArgument("param2");
        assertNotNull(param4);
        assertEquals("value2", param4.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
        assertEquals("arg1", stringArgs.get(0).getValue());
        assertEquals("arg2", stringArgs.get(1).getValue());
        assertEquals("arg3", stringArgs.get(2).getValue());
    }
}