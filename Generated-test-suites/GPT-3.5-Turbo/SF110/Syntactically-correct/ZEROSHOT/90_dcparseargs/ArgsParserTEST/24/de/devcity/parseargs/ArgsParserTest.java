import de.devcity.parseargs.arguments.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        int expected = 3;
        int actual = parser.getArgsCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] actual = parser.getArgs();
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetArgs_null() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }
    
    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] actual = parser.getArgs();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        int expected = 3;
        int actual = parser.getArgsLeftCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseSwitchArgument_longKey_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--switch"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "switch", true);
        SwitchArgument actual = parser.parseSwitchArgument("switch");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isPresent(), actual.isPresent());
    }
    
    @Test
    public void testParseSwitchArgument_longKey_notPresent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--other"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "switch", false);
        SwitchArgument actual = parser.parseSwitchArgument("switch");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isPresent(), actual.isPresent());
    }
    
    @Test
    public void testParseSwitchArgument_shortKey_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-s"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(0, "s", true);
        SwitchArgument actual = parser.parseSwitchArgument("s");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isPresent(), actual.isPresent());
    }
    
    @Test
    public void testParseSwitchArgument_shortKey_notPresent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-o"};
        parser.setArgs(args);
        SwitchArgument expected = new SwitchArgument(-1, "s", false);
        SwitchArgument actual = parser.parseSwitchArgument("s");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.isPresent(), actual.isPresent());
    }
    
    @Test
    public void testParseParameterArgument_longKey_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--param", "value"};
        parser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "param", "value");
        ParameterArgument actual = parser.parseParameterArgument("param");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }
    
    @Test
    public void testParseParameterArgument_longKey_notPresent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--other"};
        parser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = parser.parseParameterArgument("param");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseParameterArgument_shortKey_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-p", "value"};
        parser.setArgs(args);
        ParameterArgument expected = new ParameterArgument(0, "p", "value");
        ParameterArgument actual = parser.parseParameterArgument("p");
        assertEquals(expected.getIndex(), actual.getIndex());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }
    
    @Test
    public void testParseParameterArgument_shortKey_valueMissing() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"-o"};
        parser.setArgs(args);
        ParameterArgument expected = null;
        ParameterArgument actual = parser.parseParameterArgument("p");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(0, "arg1"));
        expected.add(new StringArgument(1, "arg2"));
        expected.add(new StringArgument(2, "arg3"));
        List<StringArgument> actual = parser.parseStringArgument();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getIndex(), actual.get(i).getIndex());
            assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
        }
    }
}