import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        int expected = 2;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] expected = {"arg1", "arg2"};
        String[] actual = argsParser.getArgs();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        argsParser.setArgs(args);
        String[] expected = {"arg1", "arg2"};
        String[] actual = argsParser.getArgs();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testSetArgsWithNull() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = null;
        try {
            argsParser.setArgs(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        argsParser.parseStringArgument();
        int expected = 0;
        int actual = argsParser.getArgsLeftCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgumentWithLongKey() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--switch1", "--switch2"});
        SwitchArgument expected = new SwitchArgument(0, "switch1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("switch1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgumentWithShortKey() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-s1", "-s2"});
        SwitchArgument expected = new SwitchArgument(0, "s1", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("s1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgumentWithLongKey() {
        ArgsParser argsParser = new ArgsParser(new String[]{"--param1", "value1", "--param2"});
        ParameterArgument expected = new ParameterArgument(0, "param1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("param1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgumentWithShortKey() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-p1", "value1", "-p2"});
        ParameterArgument expected = new ParameterArgument(0, "p1", "value1");
        ParameterArgument actual = argsParser.parseParameterArgument("p1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        List<StringArgument> actual = argsParser.parseStringArgument();
        assertEquals(expected, actual);
        int expectedArgsLeftCount = 0;
        int actualArgsLeftCount = argsParser.getArgsLeftCount();
        assertEquals(expectedArgsLeftCount, actualArgsLeftCount);
    }
}