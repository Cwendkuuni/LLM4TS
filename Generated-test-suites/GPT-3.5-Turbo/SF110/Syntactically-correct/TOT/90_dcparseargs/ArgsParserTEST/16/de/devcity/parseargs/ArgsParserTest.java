import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        assertArrayEquals(new String[]{"-a", "--test"}, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        assertEquals(2, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument result = parser.parseSwitchArgument("test");
        assertTrue(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertTrue(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseSwitchArgumentNoKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument result = parser.parseSwitchArgument("b");
        assertFalse(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseSwitchArgumentAlreadyParsed() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        parser.parseSwitchArgument("a");
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertFalse(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseSwitchArgumentOnlyDash() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument result = parser.parseSwitchArgument("-");
        assertFalse(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseSwitchArgumentOnlyDoubleDash() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        SwitchArgument result = parser.parseSwitchArgument("--");
        assertFalse(result.isSwitchEnabled());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNotNull(result);
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-k", "value"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertNotNull(result);
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[]{"-k"});
        ParameterArgument result = parser.parseParameterArgument("k");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentNoKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test"});
        ParameterArgument result = parser.parseParameterArgument("b");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentAlreadyParsed() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        parser.parseParameterArgument("key");
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--test", "value"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals("value", result.get(2).getValue());
    }
}