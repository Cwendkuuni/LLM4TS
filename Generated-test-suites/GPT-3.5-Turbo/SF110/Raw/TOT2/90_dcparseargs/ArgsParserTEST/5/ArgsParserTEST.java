import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @Before
    public void setUp() {
        argsParser = new ArgsParser();
    }

    @Test
    public void testGetArgsCount() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        argsParser.setArgs(new String[]{"-a", "--b", "c"});
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("a");
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        argsParser.setArgs(new String[]{"-a", "--b", "c"});
        assertEquals(true, argsParser.parseSwitchArgument("a").isEnabled());
        assertEquals(true, argsParser.parseSwitchArgument("b").isEnabled());
        assertEquals(false, argsParser.parseSwitchArgument("c").isEnabled());
    }

    @Test
    public void testParseParameterArgument() {
        argsParser.setArgs(new String[]{"--key1", "value1", "-key2=value2"});
        assertEquals("value1", argsParser.parseParameterArgument("key1").getValue());
        assertEquals("value2", argsParser.parseParameterArgument("key2").getValue());
    }

    @Test
    public void testParseStringArgument() {
        argsParser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, argsParser.parseStringArgument().size());

        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.parseStringArgument().size());
    }
}