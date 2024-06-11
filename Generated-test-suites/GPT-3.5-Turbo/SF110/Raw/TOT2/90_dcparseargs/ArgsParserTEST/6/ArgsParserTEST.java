package de.devcity.parseargs.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import de.devcity.parseargs.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        parser.parseSwitchArgument("arg1");
        assertEquals(1, parser.getArgsLeftCount());
    }
    
    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "--b"});
        SwitchArgument switchArg = parser.parseSwitchArgument("a");
        assertTrue(switchArg.isSuccess());
    }
    
    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value"});
        ParameterArgument paramArg = parser.parseParameterArgument("key");
        assertEquals("value", paramArg.getValue());
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(1, stringArgs.size());
    }
}