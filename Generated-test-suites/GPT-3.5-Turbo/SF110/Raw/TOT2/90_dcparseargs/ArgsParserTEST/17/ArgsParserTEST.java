import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-switch", "--key"});
        assertEquals(2, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_longKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        SwitchArgument switchArg = parser.parseSwitchArgument("key");
        assertTrue(switchArg.isValid());
    }
    
    @Test
    public void testParseSwitchArgument_shortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-k"});
        SwitchArgument switchArg = parser.parseSwitchArgument("k");
        assertTrue(switchArg.isValid());
    }
    
    @Test
    public void testParseParameterArgument_longKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("param");
        assertEquals("value", paramArg.getValue());
    }
    
    @Test
    public void testParseParameterArgument_shortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-pvalue"});
        ParameterArgument paramArg = parser.parseParameterArgument("p");
        assertEquals("value", paramArg.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"string1", "string2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_nullArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
}