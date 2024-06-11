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

 // Test for a valid option
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-a"}));
 Assert.assertTrue(commandLine.hasOption("a"));

 // Test for a non-existent option
 Assert.assertFalse(commandLine.hasOption("b"));
 }

 @Test
 public void testHasOptionChar() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 // Test for a valid option character
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-a"}));
 Assert.assertTrue(commandLine.hasOption('a'));

 // Test for a non-existent option character
 Assert.assertFalse(commandLine.hasOption('b'));
 }

 @Test
 public void testGetOptionObject() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 // Test for an option with no argument
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-a"}));
 Assert.assertNull(commandLine.getOptionObject("a"));

 // Test for an option with a value
 options.addOption("b", true, "Option B");
 commandLine.parse(options.parse(new String[]{"-b", "value"}));
 Assert.assertNotNull(commandLine.getOptionObject("b"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 // Test for an option with no argument
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-a"}));
 Assert.assertNull(commandLine.getOptionValue("a"));

 // Test for an option with an argument
 options.addOption("b", true, "Option B");
 commandLine.parse(options.parse(new String[]{"-b", "value"}));
 Assert.assertEquals("value", commandLine.getOptionValue("b"));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 // Test for an option with no argument
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-a"}));
 Assert.assertNull(commandLine.getOptionValues("a"));

 // Test for an option with multiple arguments
 options.addOption("b", true, "Option B");
 commandLine.parse(options.parse(new String[]{"-b", "value1", "value2"}));
 String[] values = commandLine.getOptionValues("b");
 Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test(expected = ParseException.class)
 public void testParseException() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"-c"}));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOptions() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(null);
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] args = new String[]{"arg1", "arg2", "--option", "arg3", "--a", "--"};
 try {
 commandLine = parser.parse(options, args);
 } catch (ParseException e) {
 // ignored
 }

 String[] expectedArgs = new String[]{"arg1", "arg2", "--option", "arg3"};
 Assert.assertArrayEquals(expectedArgs, commandLine.getArgs());
 }

 @Test
 public void testGetArgList() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] args = new String[]{"arg1", "arg2", "--option", "arg3", "--a", "--"};
 try {
 commandLine = parser.parse(options, args);
 } catch (ParseException e) {
 // ignored
 }

 List<String> expectedArgs = new LinkedList<>();
 expectedArgs.addAll(List.of("arg1", "arg2", "--option", "arg3"));
 Assert.assertEquals(expectedArgs, commandLine.getArgList());
 }
}