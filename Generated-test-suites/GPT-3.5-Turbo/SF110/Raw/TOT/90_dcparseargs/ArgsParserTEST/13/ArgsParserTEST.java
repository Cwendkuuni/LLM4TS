import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    // Test case for the constructor ArgsParser()
    @Test
    public void testArgsParserConstructor() {
        ArgsParser argsParser = new ArgsParser();
        assertNotNull(argsParser);
    }
    
    // Test case for the constructor ArgsParser(String[] args)
    @Test
    public void testArgsParserConstructorWithArgs() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        assertNotNull(argsParser);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    // Test case for getArgsCount()
    @Test
    public void testGetArgsCount() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        int expectedCount = args.length;
        assertEquals(expectedCount, argsParser.getArgsCount());
    }
    
    // Test case for getArgs()
    @Test
    public void testGetArgs() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    // Test case for setArgs(String[] args)
    @Test
    public void testSetArgs() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(args);
        assertArrayEquals(args, argsParser.getArgs());
    }
    
    // Test case for getArgsLeftCount()
    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        int expectedCount = args.length;
        assertEquals(expectedCount, argsParser.getArgsLeftCount());
        
        argsParser.parseSwitchArgument("arg1");
        expectedCount--;
        assertEquals(expectedCount, argsParser.getArgsLeftCount());
        
        argsParser.parseSwitchArgument("arg2");
        expectedCount--;
        assertEquals(expectedCount, argsParser.getArgsLeftCount());
    }
    
    // Test case for parseSwitchArgument(String key)
    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        
        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("arg1");
        assertNotNull(switchArgument1);
        assertEquals(0, switchArgument1.getIndex());
        assertEquals("arg1", switchArgument1.getKey());
        assertTrue(switchArgument1.isLongKey());
        
        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("arg2");
        assertEquals(2, switchArgument2.getIndex());
        assertEquals("arg2", switchArgument2.getKey());
        assertTrue(switchArgument2.isLongKey());
        
        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("arg3");
        assertEquals(-1, switchArgument3.getIndex());
        assertEquals("arg3", switchArgument3.getKey());
        assertFalse(switchArgument3.isLongKey());
    }
    
    // Test case for parseParameterArgument(String key)
    @Test
    public void testParseParameterArgument() {
        String[] args = {"--arg1", "param1", "--arg2", "param2"};
        ArgsParser argsParser = new ArgsParser(args);
        
        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("arg1");
        assertNotNull(parameterArgument1);
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("arg1", parameterArgument1.getKey());
        assertEquals("param1", parameterArgument1.getValue());
        
        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("arg2");
        assertNotNull(parameterArgument2);
        assertEquals(2, parameterArgument2.getIndex());
        assertEquals("arg2", parameterArgument2.getKey());
        assertEquals("param2", parameterArgument2.getValue());
        
        ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("arg3");
        assertNull(parameterArgument3);
    }
    
    // Test case for parseStringArgument()
    @Test
    public void testParseStringArgument() {
        String[] args = {"--arg1", "value1", "--arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(args.length, stringArguments.size());
        
        for (int i = 0; i < args.length; i++) {
            StringArgument stringArgument = stringArguments.get(i);
            assertNotNull(stringArgument);
            assertEquals(i, stringArgument.getIndex());
            assertEquals(args[i], stringArgument.getValue());
        }
    }
}