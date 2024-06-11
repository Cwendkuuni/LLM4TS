import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCreateCommandLine() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getArgs());
 Assert.assertEquals(0, commandLine.size());
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertTrue(commandLine.hasOption('a'));
 }

 @Test(expected = AssertionError.class)
 public void testHasOptionFalse() {
 CommandLine commandLine = new CommandLine();
 Assert.assertFalse(commandLine.hasOption("x"));
 Assert.assertFalse(commandLine.hasOption('x'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine.parse("--apple");
 Assert.assertEquals("true", commandLine.getOptionValue("a"));
 Assert.assertEquals("true", commandLine.getOptionValue('a'));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValueNull() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse("--apple");
 Assert.assertNull(commandLine.getOptionValue("x"));
 Assert.assertNull(commandLine.getOptionValue('x'));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine.addOption(new Option("a", "banana", true, "Banana is yellow"));
 commandLine.parse("--apple=test --a=banana");
 String[] expected = {"test", "banana"};
 Assert.assertArrayEquals(expected, commandLine.getOptionValues("a"));
 Assert.assertArrayEquals(expected, commandLine.getOptionValues('a'));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValuesEmpty() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse("--apple");
 String[] expected = {};
 Assert.assertArrayEquals(expected, commandLine.getOptionValues("a"));
 Assert.assertArrayEquals(expected, commandLine.getOptionValues('a'));
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine.parse("--apple test --banana potato");
 String[] expected = {"test", "potato"};
 Assert.assertArrayEquals(expected, commandLine.getArgs());
 }

 @Test
 public void testGetArgList() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine.parse("--apple test --banana potato");
 List<String> expected = new LinkedList<>();
 expected.add("test");
 expected.add("potato");
 Assert.assertEquals(expected, commandLine.getArgList());
 }

 @Test
 public void testToString() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine = new CommandLine(options, new String[]{"--apple", "test", "--banana", "potato"});
 String expected = "[ CommandLine: [ options: { Apple=true, Banana=potato } ] [ args: [ test, potato ] ] ]";
 Assert.assertEquals(expected, commandLine.toString());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse("--x");
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidArgument() {
 CommandLine commandLine = new CommandLine();
 commandLine.addOption(new Option("a", "apple", true, "Apple is red"));
 commandLine.parse("--apple test potato");
 }

 @Test
 public void testParseMultipleOptions() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption(new Option("a", "apple", true, "Apple is red"));
 options.addOption(new Option("b", "banana", true, "Banana is yellow"));
 commandLine = new CommandLine(options, new String[]{"--apple=test", "--b"});
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"test"}, args);
 Assert.assertEquals(2, commandLine.size());
 }

 @Test
 public void testParseMultipleOptionsWithValues() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption(new Option("a", "apple", true, "Apple is red"));
 options.addOption(new Option("b", "banana", true, "Banana is yellow"));
 commandLine = new CommandLine(options, new String[]{"--apple=test", "--b=potato"});
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"test", "potato"}, args);
 Assert.assertEquals(2, commandLine.size());
 }
}