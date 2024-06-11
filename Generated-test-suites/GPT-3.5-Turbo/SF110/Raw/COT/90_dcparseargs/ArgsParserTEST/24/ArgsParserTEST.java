
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
  
  @Test
  public void testConstructorWithArgs(){
    String[] args = {"arg1", "arg2", "arg3"};
    ArgsParser parser = new ArgsParser(args);
    assertArrayEquals(args, parser.getArgs());
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullArgs(){
    ArgsParser parser = new ArgsParser(null);
  }
  
  @Test
  public void testGetArgsCount(){
    String[] args = {"arg1", "arg2", "arg3"};
    ArgsParser parser = new ArgsParser(args);
    assertEquals(3, parser.getArgsCount());
  }
  
  @Test
  public void testGetArgs(){
    String[] args = {"arg1", "arg2", "arg3"};
    ArgsParser parser = new ArgsParser();
    parser.setArgs(args);
    assertArrayEquals(args, parser.getArgs());
  }
  
  @Test
  public void testSetArgs(){
    String[] args = {"arg1", "arg2", "arg3"};
    ArgsParser parser = new ArgsParser();
    parser.setArgs(args);
    assertArrayEquals(args, parser.getArgs());
  }
  
  @Test
  public void testGetArgsLeftCount(){
    String[] args = {"arg1", "arg2", "arg3"};
    ArgsParser parser = new ArgsParser(args);
    assertEquals(3, parser.getArgsLeftCount());
  }
  
  @Test
  public void testParseSwitchArgument(){
    String[] args = {"--switch"};
    ArgsParser parser = new ArgsParser(args);
    SwitchArgument switchArg = parser.parseSwitchArgument("switch");
    assertEquals(0, switchArg.getIndex());
    assertEquals("switch", switchArg.getKey());
    assertTrue(switchArg.isLongKey());
  }
  
  @Test
  public void testParseParameterArgument(){
    String[] args = {"--param", "value"};
    ArgsParser parser = new ArgsParser(args);
    ParameterArgument paramArg = parser.parseParameterArgument("param");
    assertEquals(0, paramArg.getIndex());
    assertEquals("param", paramArg.getKey());
    assertEquals("value", paramArg.getValue());
  }
  
  @Test
  public void testParseStringArgument(){
    String[] args = {"string1", "string2"};
    ArgsParser parser = new ArgsParser(args);
    List<StringArgument> stringArgs = parser.parseStringArgument();
    assertEquals(2, stringArgs.size());
    assertEquals(0, stringArgs.get(0).getIndex());
    assertEquals("string1", stringArgs.get(0).getValue());
    assertEquals(1, stringArgs.get(1).getIndex());
    assertEquals("string2", stringArgs.get(1).getValue());
  }
}
