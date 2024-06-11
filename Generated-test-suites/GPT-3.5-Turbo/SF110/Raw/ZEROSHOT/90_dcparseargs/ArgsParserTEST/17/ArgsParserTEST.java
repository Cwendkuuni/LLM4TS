import static org.junit.Assert.*;
import org.junit.*;

public class ArgsParserTest {
  
  // Test getArgsCount method
  @Test
  public void testGetArgsCount() {
    ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
    int count = argsParser.getArgsCount();
    assertEquals(2, count);
  }
  
  // Test getArgs method
  @Test
  public void testGetArgs() {
    ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
    String[] args = argsParser.getArgs();
    assertArrayEquals(new String[]{"arg1", "arg2"}, args);
  }
  
  // Test setArgs method with valid args
  @Test
  public void testSetArgs_validArgs() {
    ArgsParser argsParser = new ArgsParser();
    argsParser.setArgs(new String[]{"arg1", "arg2"});
    String[] args = argsParser.getArgs();
    assertArrayEquals(new String[]{"arg1", "arg2"}, args);
  }
  
  // Test setArgs method with null args
  @Test(expected=NullPointerException.class)
  public void testSetArgs_nullArgs() {
    ArgsParser argsParser = new ArgsParser();
    argsParser.setArgs(null);
  }
  
  // Test getArgsLeftCount method
  @Test
  public void testGetArgsLeftCount() {
    ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
    int count = argsParser.getArgsLeftCount();
    assertEquals(2, count);
  }
  
  // Test parseSwitchArgument method with long key
  @Test
  public void testParseSwitchArgument_longKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"--arg1", "--arg2"});
    SwitchArgument switchArg = argsParser.parseSwitchArgument("arg1");
    assertTrue(switchArg.isFound());
    assertEquals(0, switchArg.getIndex());
  }
  
  // Test parseSwitchArgument method with short key
  @Test
  public void testParseSwitchArgument_shortKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"-a", "-b"});
    SwitchArgument switchArg = argsParser.parseSwitchArgument("a");
    assertTrue(switchArg.isFound());
    assertEquals(0, switchArg.getIndex());
  }
  
  // Test parseSwitchArgument method with invalid key
  @Test
  public void testParseSwitchArgument_invalidKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"--arg1", "--arg2"});
    SwitchArgument switchArg = argsParser.parseSwitchArgument("arg3");
    assertFalse(switchArg.isFound());
    assertEquals(-1, switchArg.getIndex());
  }
  
  // Test parseParameterArgument method with long key
  @Test
  public void testParseParameterArgument_longKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"--arg1", "value1"});
    ParameterArgument paramArg = argsParser.parseParameterArgument("arg1");
    assertNotNull(paramArg);
    assertEquals(0, paramArg.getIndex());
    assertEquals("value1", paramArg.getValue());
  }
  
  // Test parseParameterArgument method with short key
  @Test
  public void testParseParameterArgument_shortKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"-a", "value"});
    ParameterArgument paramArg = argsParser.parseParameterArgument("a");
    assertNotNull(paramArg);
    assertEquals(0, paramArg.getIndex());
    assertEquals("value", paramArg.getValue());
  }
  
  // Test parseParameterArgument method with invalid key
  @Test
  public void testParseParameterArgument_invalidKey() {
    ArgsParser argsParser = new ArgsParser(new String[]{"--arg1", "value1"});
    ParameterArgument paramArg = argsParser.parseParameterArgument("arg3");
    assertNull(paramArg);
  }
  
  // Test parseStringArgument method
  @Test
  public void testParseStringArgument() {
    ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
    List<StringArgument> stringArgs = argsParser.parseStringArgument();
    assertEquals(2, stringArgs.size());
    assertEquals("arg1", stringArgs.get(0).getValue());
    assertEquals("arg2", stringArgs.get(1).getValue());
  }
}