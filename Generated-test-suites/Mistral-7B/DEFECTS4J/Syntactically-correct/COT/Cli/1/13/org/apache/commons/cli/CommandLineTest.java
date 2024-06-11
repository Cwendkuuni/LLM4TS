import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private String[] testArgs = new String[]{"-a", "1", "-b", "2", "--test", "3"};

 @Test
 public void testHasOption() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test hasOption for existing options
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 Assert.assertTrue(cmd.hasOption("test"));

 // Test hasOption for non-existing options
 Assert.assertFalse(cmd.hasOption("c"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test getOptionValue for existing options with no argument
 Assert.assertNull(cmd.getOptionValue("a"));
 Assert.assertEquals("1", cmd.getOptionValue("b"));
 Assert.assertNull(cmd.getOptionValue("test"));

 // Test getOptionValue for existing options with argument
 String[] argsWithArg = testArgs.clone();
 argsWithArg[4] = "--option=4";
 cmd = parseCommandLine(argsWithArg);
 Assert.assertNull(cmd.getOptionValue("a"));
 Assert.assertNull(cmd.getOptionValue("b"));
 Assert.assertEquals("4", cmd.getOptionValue("option"));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test getOptionValues for existing options with multiple arguments
 String[] argsWithArgList = testArgs.clone();
 argsWithArgList[4] = "--option";
 argsWithArgList[5] = "4";
 argsWithArgList[6] = "5";
 cmd = parseCommandLine(argsWithArgList);
 Assert.assertNull(cmd.getOptionValues("a"));
 Assert.assertNull(cmd.getOptionValues("b"));
 String[] optValues = cmd.getOptionValues("option");
 Assert.assertArrayEquals(new String[]{"4", "5"}, optValues);
 }

 @Test(expected = AssertionError.class)
 public void testHasOptionMissingOption() {
 CommandLine cmd = parseCommandLine(testArgs);
 Assert.assertFalse(cmd.hasOption("c"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionValueNullOption() {
 CommandLine cmd = parseCommandLine(testArgs);
 Assert.assertNull(cmd.getOptionValue(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValueInvalidOption() {
 CommandLine cmd = parseCommandLine(testArgs);
 Assert.assertNull(cmd.getOptionValue("x"));
 }

 @Test
 public void testGetArgs() {
 CommandLine cmd = parseCommandLine(testArgs);
 String[] args = cmd.getArgs();
 Assert.assertArrayEquals(new String[]{"3"}, args);
 }

 private CommandLine parseCommandLine(String[] args) {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("test", true, "Test Option");
 options.addOption("option", true, "Option with multiple arguments");
 CommandLine cmd = null;
 try {
 cmd = new CommandLine(args, options);
 } catch (ParseException e) {
 e.printStackTrace();
 }
 return cmd;
 }
}