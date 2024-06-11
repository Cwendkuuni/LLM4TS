import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        int expected = 3;
        int actual = argsParser.getArgsCount();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        String[] expected = {"-a", "--b", "-c"};
        String[] actual = argsParser.getArgs();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"-a", "--b", "-c"};

        argsParser.setArgs(args);

        String[] expected = {"-a", "--b", "-c"};
        String[] actual = argsParser.getArgs();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetArgs_NullArgs_ExceptionThrown() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = null;

        try {
            argsParser.setArgs(args);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        int expected = 3;
        int actual = argsParser.getArgsLeftCount();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument_LongKey_Found() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        SwitchArgument expected = new SwitchArgument(1, "b", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("b");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isLongKey(), actual.isLongKey());
    }

    @Test
    public void testParseSwitchArgument_ShortKey_Found() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        SwitchArgument expected = new SwitchArgument(0, "a", true);
        SwitchArgument actual = argsParser.parseSwitchArgument("a");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isLongKey(), actual.isLongKey());
    }

    @Test
    public void testParseSwitchArgument_LongKey_NotFound() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        SwitchArgument expected = new SwitchArgument(-1, "d", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("d");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isLongKey(), actual.isLongKey());
    }

    @Test
    public void testParseSwitchArgument_ShortKey_NotFound() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        SwitchArgument expected = new SwitchArgument(-1, "x", false);
        SwitchArgument actual = argsParser.parseSwitchArgument("x");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isLongKey(), actual.isLongKey());
    }

    @Test
    public void testParseParameterArgument_LongKey_Found() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c", "value"});

        ParameterArgument expected = new ParameterArgument(1, "b", "value");
        ParameterArgument actual = argsParser.parseParameterArgument("b");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKey_Found() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c", "value"});

        ParameterArgument expected = new ParameterArgument(0, "a", "value");
        ParameterArgument actual = argsParser.parseParameterArgument("a");

        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKey_NotFound() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        ParameterArgument actual = argsParser.parseParameterArgument("d");

        assertNull(actual);
    }

    @Test
    public void testParseParameterArgument_ShortKey_NotFound() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        ParameterArgument actual = argsParser.parseParameterArgument("x");

        assertNull(actual);
    }

    @Test
    public void testParseParameterArgument_LongKey_NoValue() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        ParameterArgument actual = argsParser.parseParameterArgument("b");

        assertNull(actual);
    }

    @Test
    public void testParseParameterArgument_ShortKey_NoValue() {
        ArgsParser argsParser = new ArgsParser(new String[]{"-a", "--b", "-c"});

        ParameterArgument actual = argsParser.parseParameterArgument("a");

        assertNull(actual);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});

        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));

        List<StringArgument> actual = argsParser.parseStringArgument();

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getIndex(), actual.get(i).getIndex());
            assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
        }
    }

}