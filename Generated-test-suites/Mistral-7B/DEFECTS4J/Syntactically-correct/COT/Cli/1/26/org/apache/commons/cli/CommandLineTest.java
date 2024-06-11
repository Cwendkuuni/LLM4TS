import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CommandLineTest {

 private CommandLine commandLine;

 @Test
 public void testHasOption() {
 commandLine = new CommandLine();
 commandLine.addOption(new Option("t", "test", false, "Test option"));
 assertTrue("Test option should be present", commandLine.hasOption("t"));
 assertFalse("Non-existent option should not be present", commandLine.hasOption("a"));
 }

 @Test
 public void testHasOptionCharacter() {
 commandLine = new CommandLine();
 commandLine.addOption(new Option('t', "test", false, "Test option"));
 assertTrue("Test option should be present", commandLine.hasOption('t'));
 assertFalse("Non-existent option should not be present", commandLine.hasOption('a'));
 }

 @Test
 public void testGetOptionObject() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", false, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test");
 assertSame("Option object should match", option, commandLine.getOptionObject("t"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionObjectNull() {
 commandLine = new CommandLine();
 commandLine.addOption(new Option("t", "test", false, "Test option"));
 commandLine.parse("--test");
 assertNull("Should return null when option does not have an arg", commandLine.getOptionObject("x"));
 }

 @Test
 public void testGetOptionObjectWithArg() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", true, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test arg");
 assertEquals("Test option argument should be 'arg'", "arg", commandLine.getOptionObject("t"));
 }

 @Test
 public void testGetOptionValue() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", false, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test");
 assertNull("Should return null when option does not have an arg", commandLine.getOptionValue("t"));
 }

 @Test
 public void testGetOptionValueWithArg() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", true, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test arg");
 assertEquals("Test option argument should be 'arg'", "arg", commandLine.getOptionValue("t"));
 }

 @Test
 public void testGetOptionValues() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", true, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test value1 --test value2");
 assertArrayEquals("Test option arguments should be ['value1', 'value2']", new String[]{"value1", "value2"}, commandLine.getOptionValues("t"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValuesNull() {
 commandLine = new CommandLine();
 Option option = new Option("t", "test", true, "Test option");
 commandLine.addOption(option);
 commandLine.parse("--test");
 assertNull("Should return null when option does not have an arg", commandLine.getOptionValues("x"));
 }

 @Test
 public void testGetArgs() {
 commandLine = new CommandLine();
 commandLine.parse("arg1 arg2 arg3");
 assertArrayEquals("Args should be ['arg1', 'arg2', 'arg3']", new String[]{"arg1", "arg2", "arg3"}, commandLine.getArgs());
 }

 @Test
 public void testGetArgList() {
 commandLine = new CommandLine();commandLine.parse("arg1 arg2 arg3"); assertSame("Arg list should be the same as args array", commandLine.getArgs(), commandLine.getArgList()); }

 @Test (expected = IllegalArgumentException.class) public void testParseWithInvalidOption() { commandLine = new CommandLine(); commandLine.parse("--invalid"); }
}