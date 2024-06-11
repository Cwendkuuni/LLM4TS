import org.junit.Test;
import static org.junit.Assert.*;
import de.devcity.parseargs.*; 

public class ArgsParserTest {

    // Test case for ArgsParser constructor without arguments
    @Test
    public void testArgsParserConstructorNoArgs() {
        ArgsParser parser = new ArgsParser();
        assertNotNull(parser);
    }
    
    // Test case for ArgsParser constructor with arguments
    @Test
    public void testArgsParserConstructorWithArgs() {
        String[] args = {"-a", "--b", "-c", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertNotNull(parser);
        assertArrayEquals(args, parser.getArgs());
        assertEquals(args.length, parser.getArgsCount());
        assertEquals(args.length, parser.getArgsLeftCount());
    }
    
    // Test case for setting arguments in ArgsParser
    @Test
    public void testSetArgs() {
        String[] args = {"-a", "--b", "-c", "value"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
        assertEquals(args.length, parser.getArgsCount());
        assertEquals(args.length, parser.getArgsLeftCount());
    }
    
    // Test case for getting the total number of arguments in ArgsParser
    @Test
    public void testGetArgsCount() {
        String[] args = {"-a", "--b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }
    
    // Test case for getting the arguments array in ArgsParser
    @Test
    public void testGetArgs() {
        String[] args = {"-a", "--b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }
    
    // Test case for getting the remaining number of arguments in ArgsParser
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"-a", "--b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsLeftCount());
        
        parser.parseSwitchArgument("a");
        assertEquals(args.length-1, parser.getArgsLeftCount());
    }
    
    // Test case for parsing a valid switch argument in ArgsParser
    @Test
    public void testParseValidSwitchArgument() {
        String[] args = {"-a", "--b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertTrue(switchArg.isValid());
        assertEquals(0, switchArg.getIndex());
        assertEquals("a", switchArg.getKey());
        
        assertEquals(args.length-1, parser.getArgsLeftCount());
    }
    
    // Test case for parsing an invalid switch argument in ArgsParser
    @Test
    public void testParseInvalidSwitchArgument() {
        String[] args = {"-a", "--b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        
        SwitchArgument switchArg = parser.parseSwitchArgument("d");
        assertFalse(switchArg.isValid());
        assertEquals(-1, switchArg.getIndex());
        assertEquals("d", switchArg.getKey());
        
        assertEquals(args.length, parser.getArgsLeftCount());
    }
    
    // Test case for parsing a valid parameter argument in ArgsParser
    @Test
    public void testParseValidParameterArgument() {
        String[] args = {"-a", "value", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        
        ParameterArgument paramArg = parser.parseParameterArgument("a");
        assertNotNull(paramArg);
        assertTrue(paramArg.isValid());
        assertEquals(0, paramArg.getIndex());
        assertEquals("a", paramArg.getKey());
        assertEquals("value", paramArg.getValue());
        
        assertEquals(args.length-2, parser.getArgsLeftCount());
    }
    
    // Test case for parsing an invalid parameter argument in ArgsParser
    @Test
    public void testParseInvalidParameterArgument() {
        String[] args = {"-a", "value", "--b", "value2"};
        ArgsParser parser = new ArgsParser(args);
        
        ParameterArgument paramArg = parser.parseParameterArgument("d");
        assertNull(paramArg);
        
        assertEquals(args.length, parser.getArgsLeftCount());
    }
    
    // Test case for parsing string arguments in ArgsParser
    @Test
    public void testParseStringArguments() {
        String[] args = {"-a", "value", "--b", "value2", "-c"};
        ArgsParser parser = new ArgsParser(args);
        
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(3, stringArgs.size());
        
        assertEquals(args.length-3, parser.getArgsLeftCount());
    }

}