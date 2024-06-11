import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        // Testing getArgsCount method
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        int expectedCount = 3;
        assertEquals(expectedCount, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        // Testing getArgs method
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        // Testing setArgs method
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        // Testing getArgsLeftCount method
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", null, "arg3"};
        argsParser.setArgs(args);
        int expectedCount = 2;
        assertEquals(expectedCount, argsParser.getArgsLeftCount());
    }

    // Additional test cases for parsing methods...

}