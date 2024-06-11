package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
  
  private ArgsParser argsParser;
  
  @Test
  public void testGetArgsCount() {
    argsParser = new ArgsParser();
    String[] args = {"hello", "world"};
    argsParser.setArgs(args);
    
    int expected = 2;
    int actual = argsParser.getArgsCount();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetArgs() {
    argsParser = new ArgsParser();
    String[] args = {"hello", "world"};
    argsParser.setArgs(args);
    
    String[] expected = {"hello", "world"};
    String[] actual = argsParser.getArgs();
    
    assertArrayEquals(expected, actual);
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetArgsNull() {
    argsParser = new ArgsParser();
    String[] args = null;
    argsParser.setArgs(args);
  }
  
  @Test
  public void testGetArgsLeftCount() {
    argsParser = new ArgsParser();
    String[] args = {"hello", "world"};
    argsParser.setArgs(args);
    
    int expected = 2;
    int actual = argsParser.getArgsLeftCount();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseSwitchArgumentLongKeyFound() {
    argsParser = new ArgsParser();
    String[] args = {"--verbose"};
    argsParser.setArgs(args);
    
    SwitchArgument expected = new SwitchArgument(0, "verbose", true);
    SwitchArgument actual = argsParser.parseSwitchArgument("verbose");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseSwitchArgumentLongKeyNotFound() {
    argsParser = new ArgsParser();
    String[] args = {"--verbose"};
    argsParser.setArgs(args);
    
    SwitchArgument expected = new SwitchArgument(-1, "debug", false);
    SwitchArgument actual = argsParser.parseSwitchArgument("debug");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseSwitchArgumentShortKeyFound() {
    argsParser = new ArgsParser();
    String[] args = {"-v"};
    argsParser.setArgs(args);
    
    SwitchArgument expected = new SwitchArgument(0, "v", true);
    SwitchArgument actual = argsParser.parseSwitchArgument("v");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseSwitchArgumentShortKeyNotFound() {
    argsParser = new ArgsParser();
    String[] args = {"-v"};
    argsParser.setArgs(args);
    
    SwitchArgument expected = new SwitchArgument(-1, "d", false);
    SwitchArgument actual = argsParser.parseSwitchArgument("d");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentLongKeyFoundWithValue() {
    argsParser = new ArgsParser();
    String[] args = {"--name", "John"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = new ParameterArgument(0, "name", "John");
    ParameterArgument actual = argsParser.parseParameterArgument("name");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentLongKeyFoundWithoutValue() {
    argsParser = new ArgsParser();
    String[] args = {"--name"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = null;
    ParameterArgument actual = argsParser.parseParameterArgument("name");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentLongKeyNotFound() {
    argsParser = new ArgsParser();
    String[] args = {"--name", "John"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = null;
    ParameterArgument actual = argsParser.parseParameterArgument("age");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentShortKeyFoundWithValue() {
    argsParser = new ArgsParser();
    String[] args = {"-n", "John"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = new ParameterArgument(0, "n", "John");
    ParameterArgument actual = argsParser.parseParameterArgument("n");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentShortKeyFoundWithoutValue() {
    argsParser = new ArgsParser();
    String[] args = {"-n"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = null;
    ParameterArgument actual = argsParser.parseParameterArgument("n");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseParameterArgumentShortKeyNotFound() {
    argsParser = new ArgsParser();
    String[] args = {"-n", "John"};
    argsParser.setArgs(args);
    
    ParameterArgument expected = null;
    ParameterArgument actual = argsParser.parseParameterArgument("age");
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testParseStringArgument() {
    argsParser = new ArgsParser();
    String[] args = {"hello", "world"};
    argsParser.setArgs(args);
    
    List<StringArgument> expected = new ArrayList<StringArgument>();
    expected.add(new StringArgument(0, "hello"));
    expected.add(new StringArgument(1, "world"));
    
    List<StringArgument> actual = argsParser.parseStringArgument();
    
    assertEquals(expected, actual);
  }
  
}