import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.*;
import java.util.*;

public class ArgsParserTest {
    
    // Test the getArgsCount method
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int count = parser.getArgsCount();
        Assert.assertEquals(3, count);
    }
    
    // Test the getArgs method
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }
    
    // Test the setArgs method
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        Assert.assertArrayEquals(args, parser.getArgs());
    }
    
    // Test the getArgsLeftCount method
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        parser.parseSwitchArgument("arg1");
        parser.parseSwitchArgument("arg2");
        int count = parser.getArgsLeftCount();
        Assert.assertEquals(1, count);
    }
    
    // Test the parseSwitchArgument method for long key
    @Test
    public void testParseSwitchArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        SwitchArgument arg = parser.parseSwitchArgument("arg1");
        Assert.assertTrue(arg.isFound());
        Assert.assertEquals("--arg1", parser.getArgs()[arg.getIndex()]);
    }
    
    // Test the parseSwitchArgument method for short key
    @Test
    public void testParseSwitchArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        SwitchArgument arg = parser.parseSwitchArgument("arg2");
        Assert.assertTrue(arg.isFound());
        Assert.assertEquals("-arg2", parser.getArgs()[arg.getIndex()]);
    }
    
    // Test the parseSwitchArgument method when argument is not found
    @Test
    public void testParseSwitchArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "-arg2", "--arg3"});
        SwitchArgument arg = parser.parseSwitchArgument("arg4");
        Assert.assertFalse(arg.isFound());
    }
    
    // Test the parseParameterArgument method for long key
    @Test
    public void testParseParameterArgumentLong() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "value1", "-param2", "value2"});
        ParameterArgument arg = parser.parseParameterArgument("param1");
        Assert.assertEquals("value1", arg.getValue());
        Assert.assertEquals("--param1", parser.getArgs()[arg.getIndex()]);
        Assert.assertNull(parser.getArgs()[arg.getIndex()+1]);
    }
    
    // Test the parseParameterArgument method for short key
    @Test
    public void testParseParameterArgumentShort() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "-param2", "value2"});
        ParameterArgument arg = parser.parseParameterArgument("param2");
        Assert.assertEquals("value2", arg.getValue());
        Assert.assertEquals("-param2", parser.getArgs()[arg.getIndex()]);
        Assert.assertNull(parser.getArgs()[arg.getIndex()+1]);
    }
    
    // Test the parseParameterArgument method when argument is not found
    @Test
    public void testParseParameterArgumentNotFound() {
        ArgsParser parser = new ArgsParser(new String[]{"--param1", "-param2", "value2"});
        ParameterArgument arg = parser.parseParameterArgument("param3");
        Assert.assertNull(arg);
    }
    
    // Test the parseStringArgument method
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        List<StringArgument> args = parser.parseStringArgument();
        Assert.assertEquals(3, args.size());
        Assert.assertEquals("arg1", args.get(0).getValue());
        Assert.assertEquals("arg2", args.get(1).getValue());
        Assert.assertEquals("arg3", args.get(2).getValue());
    }
}