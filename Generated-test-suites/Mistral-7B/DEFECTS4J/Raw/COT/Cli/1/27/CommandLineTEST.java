import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private String[] testArgs = new String[]{"-a", "--optionB=valueB", "--optionC", "arg1", "arg2"};

 @Test
 public void testHasOption() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test hasOption with short option
 Assert.assertTrue(cmd.hasOption("a"));

 // Test hasOption with long option
 Assert.assertTrue(cmd.hasOption("optionB"));

 // Test hasOption with non-existing option
 Assert.assertFalse(cmd.hasOption("nonExistingOption"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test getOptionValue with short option
 Assert.assertNull(cmd.getOptionValue("a"));

 // Test getOptionValue with long option that doesn't require an argument
 Assert.assertNull(cmd.getOptionValue("optionB"));

 // Test getOptionValue with long option that requires an argument
 Assert.assertEquals("valueB", cmd.getOptionValue("optionC"));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine cmd = parseCommandLine(testArgs);

 // Test getOptionValues with short option
 Assert.assertNull(cmd.getOptionValues('a'));

 // Test getOptionValues with long option that doesn't require an argument
 Assert.assertNull(cmd.getOptionValues("optionB"));

 // Test getOptionValues with long option that requires multiple arguments
 String[] optionsCValues = cmd.getOptionValues("optionC");
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, optionsCValues);
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionValueWithNonExistingOption() {
 CommandLine cmd = parseCommandLine(testArgs);
 Assert.assertNull(cmd.getOptionValue("nonExistingOption"));
 }

 @Test
 public void testGetArgs() {
 CommandLine cmd = parseCommandLine(testArgs);
 String[] args = cmd.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testAddArg() {
 CommandLine cmd = new CommandLine();
 cmd.addArg("testArg");
 List args = cmd.getArgList();
 Assert.assertEquals(1, args.size());
 Assert.assertEquals("testArg", args.get(0));
 }

 @Test
 public void testAddOption() {
 CommandLine cmd = new CommandLine();
 Option optionA = new Option("a", true, false, "Option A");
 Option optionB = new Option("b", "valueB", true, "Option B");
 cmd.addOption(optionA);
 cmd.addOption(optionB);
 Option[] options = cmd.getOptions();
 Assert.assertEquals(2, options.length);
 Assert.assertEquals(optionA, options[0]);
 Assert.assertEquals(optionB, options[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateOption() {
 CommandLine cmd = new CommandLine();
 Option optionA = new Option("a", true, false, "Option A");
 cmd.addOption(optionA);
 Option optionAdup = new Option("a", true, false, "Option A");
 cmd.addOption(optionAdup);
 }

 private CommandLine parseCommandLine(String[] args) {
 Options options = new Options();
 options.addOPTION("a", false, false, "Option A");
 options.addOPTION("b", "valueB", true, "Option B");
 options.addOPTION("c", "arg", 1, "Option C");
 CommandLine cmd = null;
 try {
 cmd = new CommandLine(args);
 cmd.parse(options);
 } catch (ParseException e) {
 e.printStackTrace();
 }
 return cmd;
 }

}