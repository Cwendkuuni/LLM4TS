import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {
    ArgsParser parser;

    @Test
    public void testGetArgsCount() {
        parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        parser = new ArgsParser(new String[]{"arg1", "arg2"});
        parser.parseSwitchArgument("arg1");
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        parser = new ArgsParser(new String[]{"--key1", "value1"});
        assertNotNull(parser.parseSwitchArgument("key1"));
        assertNull(parser.parseSwitchArgument("key2"));
    }

    @Test
    public void testParseParameterArgument() {
        parser = new ArgsParser(new String[]{"-k", "v"});
        assertNotNull(parser.parseParameterArgument("k"));
        assertNull(parser.parseParameterArgument("key"));
    }

    @Test
    public void testParseStringArgument() {
        parser = new ArgsParser(new String[]{"str1", "str2"});
        assertFalse(parser.parseStringArgument().isEmpty());
    }
}