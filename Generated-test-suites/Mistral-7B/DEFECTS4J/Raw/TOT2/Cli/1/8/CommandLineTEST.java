import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Test
 public void testHasOption() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test"});
 assertTrue(cmdLine.hasOption("test"));
 }

 @Test
 public void testHasOptionFalse() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--not-test"});
 assertFalse(cmdLine.hasOption("test"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=value"});
 assertEquals("value", cmdLine.getOptionValue("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValueNull() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test"});
 try {
 cmdLine.getOptionValue("test");
 } catch (IllegalArgumentException e) {
 assertEquals("Option 'test' not found", e.getMessage());
 }
 }

 @Test
 public void testGetOptionValues() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=value1,value2"});
 String[] values = cmdLine.getOptionValues("test");
 assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValuesEmpty() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test"});
 try {
 cmdLine.getOptionValues("test");
 } catch (IllegalArgumentException e) {
 assertEquals("Option 'test' not found", e.getMessage());
 }
 }

 @Test
 public void testGetArgs() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test", "arg1", "arg2"});
 String[] args = cmdLine.getArgs();
 assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testGetArgList() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test", "arg1", "arg2"});
 List argList = cmdLine.getArgList();
 assertEquals(2, argList.size());
 }

 @Test(expected = ClassCastException.class)
 public void testGetOptionObjectNull() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test"});
 try {
 cmdLine.getOptionObject("test");
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testGetOptionObject() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=123"});
 assertEquals(Integer.valueOf(123), cmdLine.getOptionObject("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionObjectInvalidType() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=123"});
 try {
 cmdLine.getOptionObject("test", "InvalidType.class");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid type for option 'test'. Expected 'java.lang.Integer', but was 'class java.lang.String' instead", e.getMessage());
 }
 }

 @Test
 public void testGetOptionObjectInvalidValue() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=abcd"});
 try {
 cmdLine.getOptionObject("test", Integer.class);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid value 'abcd' for option 'test'. Expected 'integer', but was 'java.lang.String' instead", e.getMessage());
 }
 }

 @Test
 public void testAddArg() {
 CommandLine cmdLine = new CommandLine();cmdLine.parse(new String[]{"--test", "arg1", "arg2"});
 cmdLine.addArg("arg3");
 List argList = cmdLine.getArgList();
 assertEquals(3, argList.size());
 assertEquals("arg3", argList.get(2));
 }

 @Test
 public void testAddOption() {
 Option option = new Option("test", true, false, "default");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option);
 assertTrue(cmdLine.hasOption("test"));
 }

 @Test
 public void testAddOptionWithValue() {
 Option option = new Option("test", true, false, "default");
 option.setArgName("arg");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option);
 cmdLine.parse(new String[]{"--test", "value"});
 assertEquals("value", cmdLine.getOptionValue("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidArgumentName() {
 Option option = new Option("test", true, false, "default");
 option.setArgName("arg");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 try {
 cmdLine.addOption(option);
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option);
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate option: test", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidOption() {
 Option option = new Option("test", true, false, "default");
 option.setArgName("arg");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--test=value"});
 try {
 cmdLine.addOption(option);
 } catch (IllegalArgumentException e) {
 assertEquals("Option 'test' already set", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOption() {
 Option option = new Option("t", true, false, "default");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option);
 try {
 cmdLine.addOption(new Option('t', false, false, "default"));
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate option: t", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOption() {
 Option option1 = new Option("test", true, false, "default");
 Option option2 = new Option("test", true, false, "default");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option1);
 try {
 cmdLine.addOption(option2);
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate option: test", e.getMessage());
 }
 }

 @Test
 public void testAddOptionWithArg() {
 Option option = new Option("test", true, true, "default");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 cmdLine.addOption(option);
 cmdLine.parse(new String[]{"--test=value1", "--test=value2"});
 String[] values = cmdLine.getOptionValues("test");
 assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidOptionName() {
 Option option = new Option("test", true, false, "default");
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 try {
 cmdLine.addOption(new Option("testing", true, false, "default"));
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid option name: testing", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOption() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[0]);
 try {
 cmdLine.addOption(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid option: null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidValue() {
 Option option = new Option("test", true, false, "default");
 CommandLine cmdLine = new CommandLine();
 try {
 cmdLine.parse(new String[]{"--test=abcd"});
 } catch (ParseException e) {
 assertEquals("Invalid value 'abcd' for option 'test'. Expected 'boolean', but was 'java.lang.String' instead", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 CommandLine cmdLine = new CommandLine();
 try {
 cmdLine.parse(new String[]{"--invalid"});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidArguments() {
 CommandLine cmdLine = new CommandLine();
 try {
 cmdLine.parse(new String[]{"--test"});
 cmdLine.parse(new String[]{"--test=value"});
 cmdLine.parse(new String[]{"--test=value1,value2"});
 cmdLine.parse(new String[]{"--test=value1,value2", "arg1", "arg2"});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullArguments() {
 CommandLine cmdLine = new CommandLine();
 try {
 cmdLine.parse((String[]) null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyArguments() {
 CommandLine cmdLine = new CommandLine();
 try {
 cmdLine.parse(new String[0]);
 } catch (IllegalArgumentException e) {
 assertEquals("This program should be run with specified options", e.getMessage());
 }
 }

 @Test
 public void testParseValidArguments() {
 CommandLine cmdLine = new CommandLine();
 cmdLine.parse(new String[]{"--option", "value1", "--option2", "value2"});
 assertEquals(2, cmdLine.getOptions().length);
 assertEquals("value1", cmdLine.getOptionValue("option"));
 assertEquals("value2", cmdLine.getOptionValue("option2"));
 }
}