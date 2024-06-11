import org.junit.*;
import static org.junit.Assert.*;
import de.devcity.parseargs.*;

public class ArgsParserTest {
  
  @Test
  public void testGetArgsCount() {
    ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
    assertEquals(3, parser.getArgsCount());
  }
  
  @Test
  public void testGetArgs() {
    ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
    assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetArgsNull() {
    ArgsParser parser = new ArgsParser();
    parser.setArgs(null);
  }
  
  @Test
  public void testSetArgs() {
    ArgsParser parser = new ArgsParser();
    parser.setArgs(new String[]{"arg1", "arg2", "arg3"});
    assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
  }
  
  @Test
  public void testGetArgsLeftCount() {
    ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
    assertEquals(3, parser.getArgsLeftCount());
    parser.parseSwitchArgument("arg1");
    assertEquals(2, parser.getArgsLeftCount());
  }
  
  @Test
  public void testParseSwitchArgumentLongKeyExists() {
    ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2", "--arg3"});
    SwitchArgument result = parser.parseSwitchArgument("arg2");
    assertEquals(new SwitchArgument(1, "arg2", true), result);
    assertEquals(null, parser.parseSwitchArgument("arg2"));
  }
  
  @Test
  public void testParseSwitchArgumentLongKeyDoesNotExist() {
    ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2", "--arg3"});
    SwitchArgument result = parser.parseSwitchArgument("arg4");
    assertEquals(new SwitchArgument(-1, "arg4", false), result);
  }
  
  @Test
  public void testParseSwitchArgumentShortKeyExists() {
    ArgsParser parser = new ArgsParser(new String[]{"-a", "--arg2", "-b"});
    SwitchArgument result = parser.parseSwitchArgument("a");
    assertEquals(new SwitchArgument(0, "a", true), result);
    assertEquals(null, parser.parseSwitchArgument("a"));
  }
  
  @Test
  public void testParseSwitchArgumentShortKeyDoesNotExist() {
    ArgsParser parser = new ArgsParser(new String[]{"-a", "--arg2", "-b"});
    SwitchArgument result = parser.parseSwitchArgument("c");
    assertEquals(new SwitchArgument(-1, "c", false), result);
  }
  
  @Test
  public void testParseParameterArgumentLongKeyExists() {
    ArgsParser parser = new ArgsParser(new String[]{"--arg1", "value1", "--arg2", "value2"});
    ParameterArgument result = parser.parseParameterArgument("arg2");
    assertEquals(new ParameterArgument(2, "arg2", "value2"), result);
    assertEquals(null, parser.parseParameterArgument("arg2"));
  }
  
  @Test
  public void testParseParameterArgumentLongKeyDoesNotExist() {
    ArgsParser parser = new ArgsParser(new String[]{"--arg1", "value1", "--arg2", "value2"});
    ParameterArgument result = parser.parseParameterArgument("arg3");
    assertEquals(null, result);
  }
  
  @Test
  public void testParseParameterArgumentShortKeyExists() {
    ArgsParser parser = new ArgsParser(new String[]{"-a", "value1", "-b", "value2"});
    ParameterArgument result = parser.parseParameterArgument("a");
    assertEquals(new ParameterArgument(0, "a", "value1"), result);
    assertEquals(null, parser.parseParameterArgument("a"));
  }
  
  @Test
  public void testParseParameterArgumentShortKeyDoesNotExist() {
    ArgsParser parser = new ArgsParser(new String[]{"-a", "value1", "-b", "value2"});
    ParameterArgument result = parser.parseParameterArgument("c");
    assertEquals(null, result);
  }
  
  @Test
  public void testParseStringArgument() {
    ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
    List<StringArgument> result = parser.parseStringArgument();
    assertEquals(3, result.size());
    assertEquals(new StringArgument(0, "arg1"), result.get(0));
    assertEquals(new StringArgument(1, "arg2"), result.get(1));
    assertEquals(new StringArgument(2, "arg3"), result.get(2));
  }
  
  @Test
  public void testParseStringArgumentNull() {
    ArgsParser parser = new ArgsParser(new String[]{null, "arg2", null});
    List<StringArgument> result = parser.parseStringArgument();
    assertEquals(1, result.size());
    assertEquals(new StringArgument(1, "arg2"), result.get(0));
  }
  
  @Test
  public void testParseStringArgumentEmpty() {
    ArgsParser parser = new ArgsParser(new String[]{"", "arg2", ""});
    List<StringArgument> result = parser.parseStringArgument();
    assertEquals(1, result.size());
    assertEquals(new StringArgument(1, "arg2"), result.get(0));
  }
}