import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ArgsParserTest {
  
  private ArgsParser parser;
  
  @Before
  public void setUp() {
    parser = new ArgsParser();
  }
  
  @Test
  public void testGetArgsCount() {
    String[] args = {"arg1", "arg2", "arg3"};
    parser.setArgs(args);
    assertEquals(3, parser.getArgsCount());
  }
  
  @Test
  public void testGetArgs() {
    String[] args = {"arg1", "arg2", "arg3"};
    parser.setArgs(args);
    assertArrayEquals(args, parser.getArgs());
  }
  
  @Test
  public void testSetArgs() {
    String[] args = {"arg1", "arg2", "arg3"};
    parser.setArgs(args);
    assertArrayEquals(args, parser.getArgs());
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetArgsWithNullArgs() {
    parser.setArgs(null);
  }
  
  @Test
  public void testGetArgsLeftCount() {
    String[] args = {"arg1", "arg2", "arg3"};
    parser.setArgs(args);
    assertEquals(3, parser.getArgsLeftCount());
  }
  
  @Test
  public void testParseSwitchArgumentWithLongKey() {
    String[] args = {"--switch"};
    parser.setArgs(args);
    SwitchArgument result = parser.parseSwitchArgument("switch");
    assertTrue(result.isPresent());
    assertEquals(0, result.getIndex());
    assertEquals("switch", result.getKey());
    assertTrue(result.isLongKey());
  }
  
  @Test
  public void testParseSwitchArgumentWithShortKey() {
    String[] args = {"-s"};
    parser.setArgs(args);
    SwitchArgument result = parser.parseSwitchArgument("s");
    assertTrue(result.isPresent());
    assertEquals(0, result.getIndex());
    assertEquals("s", result.getKey());
    assertFalse(result.isLongKey());
  }
  
  @Test
  public void testParseSwitchArgumentWithNonExistingLongKey() {
    String[] args = {"--switch"};
    parser.setArgs(args);
    SwitchArgument result = parser.parseSwitchArgument("nonExistingKey");
    assertFalse(result.isPresent());
    assertEquals(-1, result.getIndex());
    assertEquals("nonExistingKey", result.getKey());
    assertFalse(result.isLongKey());
  }
  
  @Test
  public void testParseSwitchArgumentWithNonExistingShortKey() {
    String[] args = {"-s"};
    parser.setArgs(args);
    SwitchArgument result = parser.parseSwitchArgument("nonExistingKey");
    assertFalse(result.isPresent());
    assertEquals(-1, result.getIndex());
    assertEquals("nonExistingKey", result.getKey());
    assertFalse(result.isLongKey());
  }
  
  @Test
  public void testParseParameterArgumentWithLongKey() {
    String[] args = {"--param", "value"};
    parser.setArgs(args);
    ParameterArgument result = parser.parseParameterArgument("param");
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertEquals(0, result.getIndex());
    assertEquals("param", result.getKey());
    assertEquals("value", result.getValue());
    assertTrue(result.isLongKey());
  }
  
  @Test
  public void testParseParameterArgumentWithShortKey() {
    String[] args = {"-p", "value"};
    parser.setArgs(args);
    ParameterArgument result = parser.parseParameterArgument("p");
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertEquals(0, result.getIndex());
    assertEquals("p", result.getKey());
    assertEquals("value", result.getValue());
    assertFalse(result.isLongKey());
  }
  
  @Test
  public void testParseParameterArgumentWithNonExistingLongKey() {
    String[] args = {"--param", "value"};
    parser.setArgs(args);
    ParameterArgument result = parser.parseParameterArgument("nonExistingKey");
    assertNull(result);
  }
  
  @Test
  public void testParseParameterArgumentWithNonExistingShortKey() {
    String[] args = {"-p", "value"};
    parser.setArgs(args);
    ParameterArgument result = parser.parseParameterArgument("nonExistingKey");
    assertNull(result);
  }
  
  @Test
  public void testParseParameterArgumentWithNoValue() {
    String[] args = {"--param"};
    parser.setArgs(args);
    ParameterArgument result = parser.parseParameterArgument("param");
    assertNull(result);
  }
  
  @Test
  public void testParseStringArgument() {
    String[] args = {"arg1", "arg2", "arg3"};
    parser.setArgs(args);
    List<StringArgument> result = parser.parseStringArgument();
    assertNotNull(result);
    assertEquals(3, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals(i, result.get(i).getIndex());
      assertEquals(args[i], result.get(i).getValue());
    }
  }
  
}