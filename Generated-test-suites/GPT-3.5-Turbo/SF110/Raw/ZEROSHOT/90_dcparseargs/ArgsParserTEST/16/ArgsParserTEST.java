import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsCount());

        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser2 = new ArgsParser(args);
        assertEquals(3, argsParser2.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());

        String[] args2 = null;
        try {
            argsParser.setArgs(args2);
        } catch (NullPointerException e) {
            assertEquals("args[] cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(3, argsParser.getArgsLeftCount());

        argsParser.parseSwitchArgument("arg1");
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--arg1", "-arg2", "--arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("arg1");
        assertTrue(switchArgument1.isLongKey());
        assertEquals(0, switchArgument1.getIndex());

        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("arg2");
        assertFalse(switchArgument2.isLongKey());
        assertEquals(1, switchArgument2.getIndex());
        assertTrue(switchArgument2.isFound());

        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("arg4");
        assertFalse(switchArgument3.isFound());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--arg1", "value1", "--arg2=value2"};
        ArgsParser argsParser = new ArgsParser(args);

        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("arg1");
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("value1", parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("arg2");
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("value2", parameterArgument2.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);

        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], stringArguments.get(i).getValue());
            assertNull(argsParser.getArgs()[i]);
        }
    }

}