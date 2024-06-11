import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    // 

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsCount();
        assertEquals(3, count);
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
        parser.parseSwitchArgument("arg1");
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--arg1", "-a", "--arg2"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("arg1");
        assertTrue(switchArgument.isFound());
        assertEquals(0, switchArgument.getIndex());

        switchArgument = parser.parseSwitchArgument("arg2");
        assertTrue(switchArgument.isFound());
        assertEquals(2, switchArgument.getIndex());

        switchArgument = parser.parseSwitchArgument("arg3");
        assertFalse(switchArgument.isFound());
        assertEquals(-1, switchArgument.getIndex());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--arg1", "value1", "-a", "value2", "--arg2", "value3"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("arg1");
        assertNotNull(parameterArgument);
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("arg2");
        assertNotNull(parameterArgument);
        assertEquals(4, parameterArgument.getIndex());
        assertEquals("value3", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("arg3");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertEquals("arg3", stringArguments.get(2).getValue());
    }

    // 
}