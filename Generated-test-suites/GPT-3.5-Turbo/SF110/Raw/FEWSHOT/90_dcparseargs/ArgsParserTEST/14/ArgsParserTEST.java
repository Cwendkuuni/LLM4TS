import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsCount());

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        String[] result = argsParser.getArgs();
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], result[i]);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("arg2");
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser();

        String[] args = {"--arg1", "-arg2", "--arg3"};
        argsParser.setArgs(args);

        SwitchArgument result1 = argsParser.parseSwitchArgument("arg1");
        assertEquals(0, result1.getIndex());
        assertEquals("arg1", result1.getKey());
        assertEquals(true, result1.isSwitchOn());

        SwitchArgument result2 = argsParser.parseSwitchArgument("arg2");
        assertEquals(1, result2.getIndex());
        assertEquals("arg2", result2.getKey());
        assertEquals(true, result2.isSwitchOn());

        SwitchArgument result3 = argsParser.parseSwitchArgument("arg3");
        assertEquals(2, result3.getIndex());
        assertEquals("arg3", result3.getKey());
        assertEquals(true, result3.isSwitchOn());

        SwitchArgument result4 = argsParser.parseSwitchArgument("arg4");
        assertEquals(-1, result4.getIndex());
        assertEquals("arg4", result4.getKey());
        assertEquals(false, result4.isSwitchOn());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser();

        String[] args = {"--key1", "value1", "-key2", "value2"};
        argsParser.setArgs(args);

        ParameterArgument result1 = argsParser.parseParameterArgument("key1");
        assertEquals(0, result1.getIndex());
        assertEquals("key1", result1.getKey());
        assertEquals("value1", result1.getValue());

        ParameterArgument result2 = argsParser.parseParameterArgument("key2");
        assertEquals(2, result2.getIndex());
        assertEquals("key2", result2.getKey());
        assertEquals("value2", result2.getValue());

        ParameterArgument result3 = argsParser.parseParameterArgument("key3");
        assertNull(result3);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();

        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        List<StringArgument> result = argsParser.parseStringArgument();
        assertEquals(3, result.size());
        
        for (int i = 0; i < args.length; i++) {
            assertEquals(i, result.get(i).getIndex());
            assertEquals(args[i], result.get(i).getValue());
        }
    }
}