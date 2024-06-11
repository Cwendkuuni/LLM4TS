import static org.junit.Assert.*;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testSetAndGetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        parser.parseSwitchArgument("arg2");
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switch", "-s"});
        SwitchArgument switchArg = parser.parseSwitchArgument("switch");
        assertTrue(switchArg.isFound());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("param");
        assertNotNull(paramArg);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
    }
}