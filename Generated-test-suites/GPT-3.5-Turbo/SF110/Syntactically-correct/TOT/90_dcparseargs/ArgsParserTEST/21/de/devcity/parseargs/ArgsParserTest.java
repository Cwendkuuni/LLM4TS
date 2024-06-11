import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArgsParserTest {
    private static ArgsParser parser;
    
    @BeforeClass
    public static void setUp() {
        parser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        int count = parser.getArgsCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testGetArgs() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        String[] result = parser.getArgs();
        assertArrayEquals(args, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        String[] args = null;
        parser.setArgs(args);
    }
    
    @Test
    public void testSetArgs() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        String[] result = parser.getArgs();
        assertArrayEquals(args, result);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        int count = parser.getArgsLeftCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("c");
        assertTrue(result.isExists());
        assertEquals(2, result.getIndex());
        assertEquals("c", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgumentLongKeyNotExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("d");
        assertFalse(result.isExists());
        assertEquals(-1, result.getIndex());
        assertEquals("d", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("b");
        assertTrue(result.isExists());
        assertEquals(1, result.getIndex());
        assertEquals("b", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgumentShortKeyNotExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("d");
        assertFalse(result.isExists());
        assertEquals(-1, result.getIndex());
        assertEquals("d", result.getKey());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistWithValue() {
        String[] args = {"-a", "-b", "--c", "value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("c");
        assertNotNull(result);
        assertEquals(2, result.getIndex());
        assertEquals("c", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentLongKeyExistWithoutValue() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("c");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentLongKeyNotExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("d");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistWithValue() {
        String[] args = {"-a", "-b", "--c", "value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("b");
        assertNotNull(result);
        assertEquals(1, result.getIndex());
        assertEquals("b", result.getKey());
        assertEquals("value", result.getValue());
    }
    
    @Test
    public void testParseParameterArgumentShortKeyExistWithoutValue() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("b");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgumentShortKeyNotExist() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("d");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument() {
        String[] args = {"-a", "-b", "--c"};
        parser.setArgs(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(3, result.size());
        assertEquals(new StringArgument(0, "-a"), result.get(0));
        assertEquals(new StringArgument(1, "-b"), result.get(1));
        assertEquals(new StringArgument(2, "--c"), result.get(2));
    }
}