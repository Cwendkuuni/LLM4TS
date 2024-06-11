import static org.junit.Assert.*;
import org.junit.Test;
import de.devcity.parseargs.*;


public class ArgsParserTest {
    
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[] {"arg1", "arg2", "arg3"}, parser.getArgs());
    }
    
    @Test
    public void testSetArgs_validInput() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "arg2"});
        assertArrayEquals(new String[] {"arg1", "arg2"}, parser.getArgs());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_nullInput() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
    }

    // Edge case: long key not found
    @Test
    public void testParseSwitchArgument_longKeyNotFound() {
        ArgsParser parser = new ArgsParser(new String[] {"-a", "-b"});
        SwitchArgument result = parser.parseSwitchArgument("c");
        assertFalse(result.isActive());
    }
    
    // Exception handling: Invalid parameter argument
    @Test
    public void testParseParameterArgument_invalidArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--key"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertNull(result);
    }
    
    // Typical use case: parsing string arguments
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
    }
}