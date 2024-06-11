import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {

    private ArgsParser parser;
    
    @Before
    public void setUp() {
        String[] args = {"-a", "--b", "value"};
        parser = new ArgsParser(args);
    }
    
    @Test
    public void testGetArgsCount() {
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[]{"-a", "--b", "value"}, parser.getArgs());
    }
    
    @Test
    public void testSetArgs() {
        String[] newArgs = {"-c", "--d"};
        parser.setArgs(newArgs);
        assertArrayEquals(new String[]{"-c", "--d"}, parser.getArgs());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        assertEquals(3, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        SwitchArgument argument = parser.parseSwitchArgument("a");
        assertTrue(argument.isValid());
    }
    
    @Test
    public void testParseParameterArgument() {
        ParameterArgument argument = parser.parseParameterArgument("b");
        assertNotNull(argument);
        assertEquals("value", argument.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        List<StringArgument> arguments = parser.parseStringArgument();
        assertEquals(3, arguments.size());
    }
}