package de.devcity.parseargs;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
        parser.parseSwitchArgument("arg1");
        assertEquals(2, parser.getArgsLeftCount());
    }

    // Additional test cases for parseSwitchArgument and parseParameterArgument can be added here

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.parseStringArgument().size());
    }
}