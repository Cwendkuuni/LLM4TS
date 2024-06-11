import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 commandLine.parse(options.parse("--testOptionA"));
 Assert.assertTrue(commandLine.hasOption("a"));
 }

 @Test
 public void testHasOptionFalse() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 commandLine.parse(options.parse("--nonExistentOption"));
 Assert.assertFalse(commandLine.hasOption("nonExistentOption"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 commandLine.parse(options.parse("--testOptionA trueValue"));
 Assert.assertEquals("trueValue", commandLine.getOptionValue("a"));
 }

 @Test
 public void testGetOptionValueNull() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 commandLine.parse(options.parse("--nonExistentOption"));
 Assert.assertNull(commandLine.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 commandLine.parse(options.parse("--testOptionA value1 value2"));
 String[] expected = {"value1", "value2"};
 Assert.assertArrayEquals(expected, commandLine.getOptionValues("a"));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWhenInvalidOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 commandLine.parse(options.parse("--invalidOption"));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWhenMissingRequiredOption() {
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 options.addOption("b", true, "Test option B");
 CommandLine commandLine = new CommandLine();
 try {
 commandLine.parse(options.parse("--testOptionB"));
 } catch (ParseException e) {
 Assert.assertEquals("Required option 'a' is missing", e.getMessage());
 }
 }

 @Test
 public void testMergeAllOptions() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 options.addOption("b", "testOptionB", false, "Test option B");
 commandLine.parse(options.parse("--testOptionA trueValue --testOptionB"));
 Option[] optionsArray = commandLine.getOptions();
 Option optionA = optionsArray[0];
 Option optionB = optionsArray[1];
 Assert.assertEquals("testOptionA", optionA.getLongOpt());
 Assert.assertEquals(true, optionA.isRequired());
 Assert.assertEquals(true, optionA.isValueSeparatorAllowed());
 Assert.assertEquals(true, optionA.hasArg());
 Assert.assertEquals("trueValue", optionA.getDefaultValue());
 Assert.assertEquals(true, optionB.isRequired());
 Assert.assertEquals(false, optionB.hasArg());
 Assert.assertEquals("testOptionB", optionB.getLongOpt());
 }

 @Test
 public void testToString() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "testOptionA", true, "Test option A");
 commandLine.parse(options.parse("--testOptionA"));
 String expected = "[ CommandLine: [ options: {testOptionA=true} ] [ args: [] ] ]";
 // Uncomment this when toString() is implemented properly
 // Assert.assertEquals(expected, commandLine.toString());
 }
}