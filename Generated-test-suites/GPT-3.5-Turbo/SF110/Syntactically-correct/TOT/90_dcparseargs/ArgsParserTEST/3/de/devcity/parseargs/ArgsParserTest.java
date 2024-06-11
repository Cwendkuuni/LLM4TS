import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    // Test getArgsCount method
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        int expected = 3;
        int actual = parser.getArgsCount();
        assertEquals(expected, actual);
    }

    // Test setArgs method with null argument
    @Test(expected = NullPointerException.class)
    public void testSetArgs_NullArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
        fail("NullPointerException should be thrown.");
    }

    // Test getArgs method
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        String[] expected = args;
        String[] actual = parser.getArgs();
        assertArrayEquals(expected, actual);
    }

    // Test getArgsLeftCount method
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        int expected = 3;
        int actual = parser.getArgsLeftCount();
        assertEquals(expected, actual);
    }

    // Test parseSwitchArgument method with valid long key
    @Test
    public void testParseSwitchArgument_ValidLongKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--switch"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "switch", true);
        SwitchArgument actual = parser.parseSwitchArgument("switch");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isFound(), actual.isFound());
    }

    // Test parseSwitchArgument method with invalid long key
    @Test
    public void testParseSwitchArgument_InvalidLongKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--switch"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "invalid", false);
        SwitchArgument actual = parser.parseSwitchArgument("invalid");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isFound(), actual.isFound());
    }

    // Test parseSwitchArgument method with valid short key
    @Test
    public void testParseSwitchArgument_ValidShortKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-s"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "s", true);
        SwitchArgument actual = parser.parseSwitchArgument("s");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isFound(), actual.isFound());
    }

    // Test parseSwitchArgument method with invalid short key
    @Test
    public void testParseSwitchArgument_InvalidShortKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-s"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "invalid", false);
        SwitchArgument actual = parser.parseSwitchArgument("invalid");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isFound(), actual.isFound());
    }

    // Test parseParameterArgument method with valid long key and value
    @Test
    public void testParseParameterArgument_ValidLongKeyAndValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--key", "value"};
        parser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "key", "value");
        ParameterArgument actual = parser.parseParameterArgument("key");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }

    // Test parseParameterArgument method with invalid long key
    @Test
    public void testParseParameterArgument_InvalidLongKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--key", "value"};
        parser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = parser.parseParameterArgument("invalid");
        assertEquals(expected, actual);
    }

    // Test parseParameterArgument method with valid short key and value
    @Test
    public void testParseParameterArgument_ValidShortKeyAndValue() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-k", "value"};
        parser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "k", "value");
        ParameterArgument actual = parser.parseParameterArgument("k");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }

    // Test parseParameterArgument method with invalid short key
    @Test
    public void testParseParameterArgument_InvalidShortKey() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-k", "value"};
        parser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = parser.parseParameterArgument("invalid");
        assertEquals(expected, actual);
    }

    // Test parseStringArgument method with multiple arguments
    @Test
    public void testParseStringArgument_MultipleArguments() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));
        List<StringArgument> actual = parser.parseStringArgument();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            StringArgument expectedArg = expected.get(i);
            StringArgument actualArg = actual.get(i);
            assertEquals(expectedArg.getIndex(), actualArg.getIndex());
            assertEquals(expectedArg.getValue(), actualArg.getValue());
        }
    }

}