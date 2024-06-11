import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", null, "arg2"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        SwitchArgument switchArg = parser.parseSwitchArgument("key");
        assertTrue(switchArg.isValid());
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-k"});
        SwitchArgument switchArg = parser.parseSwitchArgument("k");
        assertTrue(switchArg.isValid());
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("key");
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-k", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("k");
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
    }
}