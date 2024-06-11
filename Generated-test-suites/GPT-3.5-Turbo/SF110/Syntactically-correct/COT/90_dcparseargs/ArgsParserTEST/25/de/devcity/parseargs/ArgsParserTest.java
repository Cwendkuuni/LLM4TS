
import de.devcity.parseargs.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int count = parser.getArgsCount();
        Assert.assertEquals(2, count);
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1", "arg2"});
        String[] args = parser.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int count = parser.getArgsLeftCount();
        Assert.assertEquals(2, count);
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--long", "-s"});
        SwitchArgument result1 = parser.parseSwitchArgument("s");
        SwitchArgument result2 = parser.parseSwitchArgument("long");
        Assert.assertTrue(result1.isSuccessful());
        Assert.assertTrue(result2.isSuccessful());
        Assert.assertEquals(0, result1.getIndex());
        Assert.assertEquals(1, result2.getIndex());
        Assert.assertEquals("s", result1.getKey());
        Assert.assertEquals("long", result2.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"-p", "value", "--long", "value"});
        ParameterArgument result1 = parser.parseParameterArgument("p");
        ParameterArgument result2 = parser.parseParameterArgument("long");
        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(0, result1.getIndex());
        Assert.assertEquals(2, result2.getIndex());
        Assert.assertEquals("p", result1.getKey());
        Assert.assertEquals("long", result2.getKey());
        Assert.assertEquals("value", result1.getValue());
        Assert.assertEquals("value", result2.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        List<StringArgument> result = parser.parseStringArgument();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(0).getIndex());
        Assert.assertEquals(1, result.get(1).getIndex());
        Assert.assertEquals("arg1", result.get(0).getValue());
        Assert.assertEquals("arg2", result.get(1).getValue());
    }
}

