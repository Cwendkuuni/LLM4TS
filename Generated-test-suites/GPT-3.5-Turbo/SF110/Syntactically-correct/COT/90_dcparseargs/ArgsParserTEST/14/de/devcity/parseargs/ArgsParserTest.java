import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int expectedCount = 2;
        assertEquals(expectedCount, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int expectedCount = 2;
        assertEquals(expectedCount, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "-f"});
        String key = "key";
        SwitchArgument expectedArgument = new SwitchArgument(0, key, true);
        assertEquals(expectedArgument, parser.parseSwitchArgument(key));
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key", "value", "-p", "param"});
        String key = "key";
        ParameterArgument expectedArgument = new ParameterArgument(0, key, "value");
        assertEquals(expectedArgument, parser.parseParameterArgument(key));
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> expectedArguments = new ArrayList<>();
        expectedArguments.add(new StringArgument(0, "arg1"));
        expectedArguments.add(new StringArgument(1, "arg2"));
        assertEquals(expectedArguments, parser.parseStringArgument());
    }
}