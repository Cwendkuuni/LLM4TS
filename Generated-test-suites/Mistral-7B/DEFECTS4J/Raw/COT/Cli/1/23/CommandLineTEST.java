import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

 private CommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new CommandLine();
 }

 @Test
 public void testHasOption() {
 // Test hasOption method with valid option
 Option option = new Option("t", "test", false, "Test flag");
 commandLine.addOption(option);
 Assert.assertTrue(commandLine.hasOption("t"));
 Assert.assertTrue(commandLine.hasOption('t'));

 // Test hasOption method with invalid option
 Assert.assertFalse(commandLine.hasOption("invalidOption"));
 Assert.assertFalse(commandLine.hasOption('z'));
 }

 @Test
 public void testGetOptionObject() {
 // Test getOptionObject method with valid option
 Option option = new Option("t", "test", false, "Test flag");
 commandLine.addOption(option);
 commandLine.parse(new String[]{"-t"});
 Assert.assertEquals("Test flag", commandLine.getOptionObject("t"));
 Assert.assertEquals("Test flag", commandLine.getOptionObject('t'));

 // Test getOptionObject method with invalid option
 Assert.assertNull(commandLine.getOptionObject("invalidOption"));
 Assert.assertNull(commandLine.getOptionObject('z'));
 }

 @Test
 public void testGetOptionValue() {
 // Test getOptionValue method with valid option and no argument
 Option option = new Option("t", "test", false, "Test flag");
 commandLine.addOption(option);
 commandLine.parse(new String[]{"-t"});
 Assert.assertNull(commandLine.getOptionValue("t"));
 Assert.assertNull(commandLine.getOptionValue('t'));

 // Test getOptionValue method with valid option and argument
 Option optionWithArg = new Option("a", "arg", true, "Argument option");
 commandLine.addOption(optionWithArg);
 commandLine.parse(new String[]{"-a", "testValue"});
 Assert.assertEquals("testValue", commandLine.getOptionValue("a"));
 Assert.assertEquals("testValue", commandLine.getOptionValue('a'));

 // Test getOptionValue method with invalid option
 Assert.assertNull(commandLine.getOptionValue("invalidOption"));
 Assert.assertNull(commandLine.getOptionValue('z'));
 }

 @Test
 public void testGetOptionValues() {
 // Test getOptionValues method with valid option and multiple arguments
 Option optionWithArgs = new Option("m", "multiArg", true, "Multiple arguments option");
 commandLine.addOption(optionWithArgs);
 commandLine.parse(new String[]{"-m", "arg1", "arg2", "arg3"});
 String[] result = commandLine.getOptionValues("m");
 Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);

 // Test getOptionValues method with invalid option
 Assert.assertNull(commandLine.getOptionValues("invalidOption"));
 }

 @Test(expected = ParseException.class)
 public void testParseException() {
 commandLine.parse(new String[]{"invalidCommand"});
 }

 @Test
 public void testGetArgs() {
 // Test getArgs method with no arguments
 String[] args = commandLine.getArgs();
 Assert.assertArrayEqual(new String[0], args);

 // Test getArgs method with arguments
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testGetArgList() {
 // Test getArgList method with no arguments
 List argList = commandLine.getArgList();
 Assert.assertEquals(0, argList.size());

 // Test getArgList method with arguments
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 argList = commandLine.getArgList();
 Assert.assertEquals(2, argList.size());
 Assert.assertEquals("arg1", argList.get(0));
 Assert.assertEquals("arg2", argList.get(1));
 }

 @Test
 public void testToString() {
 // Test toString method (commented out in the class, so cannot test directly)
 // CommandLine commandLine = new CommandLine();
 // commandLine.parse(new String[]{"-t", "testValue", "arg1", "arg2"});
 // System.out.println(commandLine.toString());
 }

 @Test
 public void testAddArg() {
 // Test addArg method
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 List args = commandLine.getArgList();
 Assert.assertEquals(2, args.size());
 Assert.assertEquals("arg1", args.get(0));
 Assert.assertEquals("arg2", args.get(1));
 }

 @Test
 public void testAddOption() {
 // Test addOption method
 Option option = new Option("t", "test", false, "Test flag");
 commandLine.addOption(option);
 List options = new ArrayList(commandLine.iterator());
 Assert.assertEquals(1, options.size());
 Assert.assertEquals(option, options.get(0));
 }

 @Test
 public void testIterator() {
 // Test iterator method
 Option option1 = new Option("t", "test1", false, "Test flag1");
 Option option2 = new Option("t", "test2", false, "Test flag2");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 List options = new ArrayList(commandLine.iterator());
 Assert.assertEquals(2, options.size());
 Assert.assertEquals(option1, options.get(0));
 Assert.assertEquals(option2, options.get(1));
 }

 @Test
 public void testGetOptions() {
 // Test getOptions method
 Option[] options = commandLine.getOptions();
 Assert.assertEquals(0, options.length);

 Option option1 = new Option("t", "test1", false, "Test flag1");
 Option option2 = new Option("t", "test2", false, "Test flag2");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 options = commandLine.getOptions();
 Assert.assertEquals(2, options.length);
 Assert.assertEquals(option1, options[0]);
 Assert.assertEquals(option2, options[1]);
 }
}