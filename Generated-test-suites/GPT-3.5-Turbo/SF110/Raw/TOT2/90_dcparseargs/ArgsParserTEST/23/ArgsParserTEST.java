import static org.junit.Assert.*;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--flag1", "-f", "value"});
        assertEquals(new SwitchArgument(0, "flag1", true), parser.parseSwitchArgument("flag1"));
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "value1", "-pval", "-p"});
        assertEquals(new ParameterArgument(0, "param1", "value1"), parser.parseParameterArgument("param1"));
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "-a", "arg2", "--str", "arg3"});
        assertEquals(3, parser.parseStringArgument().size());
    }

    // Additional edge cases and exception handling scenarios can be added here

}