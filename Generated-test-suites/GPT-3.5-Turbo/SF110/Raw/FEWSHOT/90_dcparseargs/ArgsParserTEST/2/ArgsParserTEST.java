import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "-c"});
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "-c"});
        String[] args = parser.getArgs();
        assertEquals(3, args.length);
        assertEquals("-a", args[0]);
        assertEquals("--b", args[1]);
        assertEquals("-c", args[2]);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"-a", "--b", "-c"};
        parser.setArgs(args);
        assertEquals(3, parser.getArgsCount());
        String[] storedArgs = parser.getArgs();
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], storedArgs[i]);
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "-c"});
        assertEquals(3, parser.getArgsLeftCount());
        parser.parseSwitchArgument("a");
        assertEquals(2, parser.getArgsLeftCount());
        parser.parseParameterArgument("b");
        assertEquals(0, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--a", "-b"});
        SwitchArgument result1 = parser.parseSwitchArgument("a");
        assertEquals(0, result1.getIndex());
        assertEquals("a", result1.getKey());
        assertEquals(true, result1.getValue());
        SwitchArgument result2 = parser.parseSwitchArgument("b");
        assertEquals(2, result2.getIndex());
        assertEquals("b", result2.getKey());
        assertEquals(true, result2.getValue());
        SwitchArgument result3 = parser.parseSwitchArgument("c");
        assertEquals(-1, result3.getIndex());
        assertEquals("c", result3.getKey());
        assertEquals(false, result3.getValue());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value1", "--b", "value2", "-c"});
        ParameterArgument result1 = parser.parseParameterArgument("a");
        assertEquals(0, result1.getIndex());
        assertEquals("a", result1.getKey());
        assertEquals("value1", result1.getValue());
        ParameterArgument result2 = parser.parseParameterArgument("b");
        assertEquals(2, result2.getIndex());
        assertEquals("b", result2.getKey());
        assertEquals("value2", result2.getValue());
        ParameterArgument result3 = parser.parseParameterArgument("c");
        assertNull(result3);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value1", "--b", "value2", "-c"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).getIndex());
        assertEquals("-a", result.get(0).getArgument());
        assertEquals(1, result.get(1).getIndex());
        assertEquals("value1", result.get(1).getArgument());
        assertEquals(2, result.get(2).getIndex());
        assertEquals("--b", result.get(2).getArgument());
        assertEquals(0, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseStringArgumentEmptyArgs() {
        ArgsParser parser = new ArgsParser(new String[]{});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(0, result.size());
    }
}