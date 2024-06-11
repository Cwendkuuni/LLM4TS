import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--switch", "-s"};
        ArgsParser parser = new ArgsParser(args);
        assertTrue(parser.parseSwitchArgument("switch").isPresent());
        assertTrue(parser.parseSwitchArgument("s").isPresent());
        assertFalse(parser.parseSwitchArgument("nonexistent").isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--param value", "-pvalue"};
        ArgsParser parser = new ArgsParser(args);
        assertTrue(parser.parseParameterArgument("param").isPresent());
        assertTrue(parser.parseParameterArgument("p").isPresent());
        assertFalse(parser.parseParameterArgument("nonexistent").isPresent());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"string1", "string2", "string3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.parseStringArgument().size());
    }
}