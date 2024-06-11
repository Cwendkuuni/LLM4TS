import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testDefaultConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNull(parser.getArgs());
    }

    @Test
    public void testConstructorWithArgs() {
        String[] args = {"-a", "-b", "--c"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
        assertTrue(parser.getArgsLeftCount() > 0);
    }

    @Test
    public void testSetArgs() {
        String[] args = {"-a", "-b", "--c"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
        assertTrue(parser.getArgsLeftCount() > 0);
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-a", "-b", "--c"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertEquals(0, switchArg.getIndex());
        assertEquals("a", switchArg.getKey());
        assertTrue(switchArg.isValue());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"-a", "value", "-b", "--c"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument paramArg = parser.parseParameterArgument("a");
        assertEquals(0, paramArg.getIndex());
        assertEquals("a", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "value", "-b", "word", "--c"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertTrue(stringArgs.size() > 0);
    }
}