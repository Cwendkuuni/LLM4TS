import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    
    private ArgsParser argsParser;
    
    @Before
    public void setup() {
        argsParser = new ArgsParser();
    }
    
    @Test
    public void testGetArgsCount_emptyArgs_zeroCount() {
        argsParser.setArgs(new String[]{});
        assertEquals(0, argsParser.getArgsCount());
    }
    
    @Test
    public void testGetArgsCount_nonEmptyArgs_correctCount() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        assertEquals(3, argsParser.getArgsCount());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_nullArgs_throwNullPointerException() {
        argsParser.setArgs(null);
    }
    
    @Test
    public void testGetArgsLeftCount_allArgsLeft_fullCount() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        assertEquals(3, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testGetArgsLeftCount_someArgsParsed_correctCount() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        argsParser.parseSwitchArgument("a");
        assertEquals(2, argsParser.getArgsLeftCount());
        argsParser.parseSwitchArgument("b");
        assertEquals(1, argsParser.getArgsLeftCount());
        argsParser.parseSwitchArgument("c");
        assertEquals(0, argsParser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument_shortSwitchValid_existsInArgs() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        SwitchArgument result = argsParser.parseSwitchArgument("a");
        assertNotNull(result);
        assertTrue(result.exists());
        assertEquals("a", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_shortSwitchValid_notExistsInArgs() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        SwitchArgument result = argsParser.parseSwitchArgument("d");
        assertNotNull(result);
        assertFalse(result.exists());
        assertEquals("d", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_longSwitchValid_existsInArgs() {
        argsParser.setArgs(new String[]{"--arg1", "--arg2", "--arg3"});
        SwitchArgument result = argsParser.parseSwitchArgument("arg2");
        assertNotNull(result);
        assertTrue(result.exists());
        assertEquals("arg2", result.getKey());
    }
    
    @Test
    public void testParseSwitchArgument_longSwitchValid_notExistsInArgs() {
        argsParser.setArgs(new String[]{"--arg1", "--arg2", "--arg3"});
        SwitchArgument result = argsParser.parseSwitchArgument("arg4");
        assertNotNull(result);
        assertFalse(result.exists());
        assertEquals("arg4", result.getKey());
    }
    
    @Test
    public void testParseParameterArgument_shortParamValid_withValueExistsInArgs() {
        argsParser.setArgs(new String[]{"-a", "value1", "-b", "value2"});
        ParameterArgument result = argsParser.parseParameterArgument("a");
        assertNotNull(result);
        assertEquals("a", result.getKey());
        assertEquals("value1", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_shortParamValid_withValueNotExistsInArgs() {
        argsParser.setArgs(new String[]{"-a"});
        ParameterArgument result = argsParser.parseParameterArgument("a");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_longParamValid_withValueExistsInArgs() {
        argsParser.setArgs(new String[]{"--param1", "value1", "--param2", "value2"});
        ParameterArgument result = argsParser.parseParameterArgument("param2");
        assertNotNull(result);
        assertEquals("param2", result.getKey());
        assertEquals("value2", result.getValue());
    }
    
    @Test
    public void testParseParameterArgument_longParamValid_withValueNotExistsInArgs() {
        argsParser.setArgs(new String[]{"--param1"});
        ParameterArgument result = argsParser.parseParameterArgument("param1");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_shortParamValid_withoutValueExistsInArgs() {
        argsParser.setArgs(new String[]{"-a"});
        ParameterArgument result = argsParser.parseParameterArgument("a");
        assertNull(result);
    }
    
    @Test
    public void testParseParameterArgument_longParamValid_withoutValueExistsInArgs() {
        argsParser.setArgs(new String[]{"--param1"});
        ParameterArgument result = argsParser.parseParameterArgument("param1");
        assertNull(result);
    }
    
    @Test
    public void testParseStringArgument_allArgsParsed_emptyList() {
        argsParser.setArgs(new String[]{"-a", "-b", "-c"});
        argsParser.parseSwitchArgument("a");
        argsParser.parseSwitchArgument("b");
        argsParser.parseSwitchArgument("c");
        assertEquals(0, argsParser.parseStringArgument().size());
    }
    
    @Test
    public void testParseStringArgument_someArgsLeft_listWithCorrectStringArguments() {
        argsParser.setArgs(new String[]{"-a", "--param1", "value1", "-b", "value2"});
        argsParser.parseSwitchArgument("b");
        argsParser.parseParameterArgument("param1");
        assertEquals(2, argsParser.parseStringArgument().size());
        assertEquals("a", argsParser.parseStringArgument().get(0).getValue());
        assertEquals("value2", argsParser.parseStringArgument().get(1).getValue());
    }
}