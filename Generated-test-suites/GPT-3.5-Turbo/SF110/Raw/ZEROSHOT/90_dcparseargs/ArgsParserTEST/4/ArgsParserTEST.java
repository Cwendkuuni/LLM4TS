import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsLeftCount());
        
        argsParser.parseSwitchArgument("-arg1");
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"-arg1", "--arg2", "-arg3"};
        argsParser.setArgs(args);

        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("arg1");
        assertTrue(switchArgument1.isSwitchPresent());
        assertEquals(0, switchArgument1.getIndex());

        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("arg2");
        assertTrue(switchArgument2.isSwitchPresent());
        assertEquals(1, switchArgument2.getIndex());

        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("arg3");
        assertTrue(switchArgument3.isSwitchPresent());
        assertEquals(2, switchArgument3.getIndex());

        SwitchArgument switchArgument4 = argsParser.parseSwitchArgument("arg4");
        assertFalse(switchArgument4.isSwitchPresent());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"-arg1", "value1", "--arg2", "value2", "-arg3"};
        argsParser.setArgs(args);

        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("arg1");
        assertNotNull(parameterArgument1);
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("value1", parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("arg2");
        assertNotNull(parameterArgument2);
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("value2", parameterArgument2.getValue());

        ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("arg3");
        assertNull(parameterArgument3);

        ParameterArgument parameterArgument4 = argsParser.parseParameterArgument("arg4");
        assertNull(parameterArgument4);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);

        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }
}