
import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        // typical use case
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        int expectedCount = 2;
        int actualCount = parser.getArgsCount();
        assertEquals(expectedCount, actualCount);
        
        // edge case
        args = new String[0];
        parser.setArgs(args);
        expectedCount = 0;
        actualCount = parser.getArgsCount();
        assertEquals(expectedCount, actualCount);

        // error scenario
        args = null;
        try {
            parser.setArgs(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testSetArgs() {
        // typical use case
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());

        // edge case
        args = new String[0];
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());

        // error scenario
        args = null;
        try {
            parser.setArgs(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testGetArgsLeftCount() {
        // typical use case
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        int expectedCount = 2;
        int actualCount = parser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);

        // edge case
        args = new String[0];
        parser.setArgs(args);
        expectedCount = 0;
        actualCount = parser.getArgsLeftCount();
        assertEquals(expectedCount, actualCount);

        // error scenario
        args = null;
        try {
            parser.setArgs(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testParseSwitchArgument() {
        // typical use case - short key
        String[] args = {"-k", "value"};
        ArgsParser parser = new ArgsParser(args);

        SwitchArgument expectedArg = new SwitchArgument(0, "k", true);
        SwitchArgument actualArg = parser.parseSwitchArgument("k");
        assertEquals(expectedArg, actualArg);
        assertNull(parser.parseSwitchArgument("invalid"));

        // typical use case - long key
        args = new String[]{"--key", "value"};
        parser.setArgs(args);

        expectedArg = new SwitchArgument(0, "key", true);
        actualArg = parser.parseSwitchArgument("key");
        assertEquals(expectedArg, actualArg);
        assertNull(parser.parseSwitchArgument("invalid"));

        // edge case - empty args
        args = new String[0];
        parser.setArgs(args);
        assertNull(parser.parseSwitchArgument("key"));

        // error scenario - null args
        args = null;
        try {
            parser.setArgs(args);
            parser.parseSwitchArgument("key");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testParseParameterArgument() {
        // typical use case - short key
        String[] args = {"-k", "value"};
        ArgsParser parser = new ArgsParser(args);

        ParameterArgument expectedArg = new ParameterArgument(0, "k", "value");
        ParameterArgument actualArg = parser.parseParameterArgument("k");
        assertEquals(expectedArg, actualArg);
        assertNull(parser.parseParameterArgument("invalid"));

        // typical use case - long key
        args = new String[]{"--key", "value"};
        parser.setArgs(args);

        expectedArg = new ParameterArgument(0, "key", "value");
        actualArg = parser.parseParameterArgument("key");
        assertEquals(expectedArg, actualArg);
        assertNull(parser.parseParameterArgument("invalid"));

        // edge case - empty args
        args = new String[0];
        parser.setArgs(args);
        assertNull(parser.parseParameterArgument("key"));

        // error scenario - null args
        args = null;
        try {
            parser.setArgs(args);
            parser.parseParameterArgument("key");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testParseStringArgument() {
        // typical use case
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);

        List<StringArgument> expectedArgs = new ArrayList<>();
        expectedArgs.add(new StringArgument(0, "arg1"));
        expectedArgs.add(new StringArgument(1, "arg2"));

        List<StringArgument> actualArgs = parser.parseStringArgument();
        assertEquals(expectedArgs.size(), actualArgs.size());
        for (int i = 0; i < expectedArgs.size(); i++) {
            assertEquals(expectedArgs.get(i), actualArgs.get(i));
        }

        // edge case - empty args
        args = new String[0];
        parser.setArgs(args);
        expectedArgs = new ArrayList<>();
        actualArgs = parser.parseStringArgument();
        assertEquals(expectedArgs.size(), actualArgs.size());

        // error scenario - null args
        args = null;
        try {
            parser.setArgs(args);
            parser.parseStringArgument();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }
}
