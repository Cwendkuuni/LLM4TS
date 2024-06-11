import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "c"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "c"});
        assertArrayEquals(new String[]{"-a", "--b", "c"}, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-a", "--b", "c"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "c"});
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b", "-c", "-d"});
        SwitchArgument argument = parser.parseSwitchArgument("c");
        assertTrue(argument.isValid());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "value1", "--b", "value2"});
        ParameterArgument argument = parser.parseParameterArgument("a");
        assertNotNull(argument);
        assertEquals("value1", argument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertNotNull(parser.parseStringArgument());
    }
}