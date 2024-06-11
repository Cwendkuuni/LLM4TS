
import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertEquals(3, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertEquals(3, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsLeftCount();
        assertEquals(3, count);
        
        parser.parseSwitchArgument("-arg1");
        count = parser.getArgsLeftCount();
        assertEquals(2, count);
        
        parser.parseParameterArgument("arg2");
        count = parser.getArgsLeftCount();
        assertEquals(1, count);
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "-switch1", "--switch2", "-switch3", "arg2"});
        
        SwitchArgument switch1 = parser.parseSwitchArgument("switch1");
        assertTrue(switch1.isSwitched());
        assertEquals(1, switch1.getIndex());
        assertEquals("switch1", switch1.getKey());
        
        SwitchArgument switch2 = parser.parseSwitchArgument("switch2");
        assertTrue(switch2.isSwitched());
        assertEquals(2, switch2.getIndex());
        assertEquals("switch2", switch2.getKey());
        
        SwitchArgument switch3 = parser.parseSwitchArgument("switch3");
        assertTrue(switch3.isSwitched());
        assertEquals(3, switch3.getIndex());
        assertEquals("switch3", switch3.getKey());
        
        SwitchArgument switch4 = parser.parseSwitchArgument("switch4");
        assertFalse(switch4.isSwitched());
        assertEquals(-1, switch4.getIndex());
        assertEquals("switch4", switch4.getKey());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "-param1", "-param2=value1", "--param3", "value2", "-param4=5", "arg2"});
        
        ParameterArgument param1 = parser.parseParameterArgument("param1");
        assertNull(param1);
        
        ParameterArgument param2 = parser.parseParameterArgument("param2");
        assertEquals(1, param2.getIndex());
        assertEquals("param2", param2.getKey());
        assertEquals("value1", param2.getValue());
        
        ParameterArgument param3 = parser.parseParameterArgument("param3");
        assertNull(param3);
        
        ParameterArgument param4 = parser.parseParameterArgument("param4");
        assertEquals(5, param4.getIndex());
        assertEquals("param4", param4.getKey());
        assertEquals("5", param4.getValue());
        
        ParameterArgument param5 = parser.parseParameterArgument("param5");
        assertNull(param5);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> args = parser.parseStringArgument();
        
        assertEquals(3, args.size());
        
        StringArgument arg1 = args.get(0);
        assertEquals(0, arg1.getIndex());
        assertEquals("arg1", arg1.getValue());
        
        StringArgument arg2 = args.get(1);
        assertEquals(1, arg2.getIndex());
        assertEquals("arg2", arg2.getValue());
        
        StringArgument arg3 = args.get(2);
        assertEquals(2, arg3.getIndex());
        assertEquals("arg3", arg3.getValue());
    }
}
