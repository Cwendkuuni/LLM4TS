import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCreateCommandLine() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNotNull(commandLine);
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("t", "test", false, "Run tests.");
 commandLine.parse(options.parse("--test"));

 Assert.assertTrue(commandLine.hasOption("t"));
 Assert.assertTrue(commandLine.hasOption('t'));
 Assert.assertFalse(commandLine.hasOption("a"));
 }

 @Test
 public void testHasOptionWithArg() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("o", "output", true, "Output file");
 commandLine.parse(options.parse("--output, output.txt"));

 Assert.assertTrue(commandLine.hasOption("o"));
 Assert.assertTrue(commandLine.hasOption('o'));
 Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
 Assert.assertEquals("output.txt", commandLine.getOptionValue('o'));
 }

 @Test(expected = AssertionError.class)
 public void testMissingOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("o", "output", true, "Output file");
 commandLine.parse(options.parse("--invalid"));

 Assert.assertFalse(commandLine.hasOption("invalid"));
 Assert.assertFalse(commandLine.hasOption('i'));
 }

 @Test
 public void testMultipleOptions() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("t", "test", false, "Run tests.");
 options.addOption("o", "output", true, "Output file");
 commandLine.parse(options.parse("--test --output, output.txt"));

 Assert.assertTrue(commandLine.hasOption("t"));
 Assert.assertTrue(commandLine.hasOption('t'));
 Assert.assertTrue(commandLine.hasOption("o"));
 Assert.assertTrue(commandLine.hasOption('o'));
 Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
 Assert.assertEquals("output.txt", commandLine.getOptionValue('o'));
 Assert.assertFalse(commandLine.hasOption("invalid"));
 Assert.assertFalse(commandLine.hasOption('i'));
 }

 @Test
 public void testExtraArguments() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 commandLine.parse(options.parse("--test --output, output.txt arg1 arg2"));

 Assert.assertTrue(commandLine.hasOption("t"));
 Assert.assertTrue(commandLine.hasOption('t'));
 Assert.assertTrue(commandLine.hasOption("o"));
 Assert.assertTrue(commandLine.hasOption('o'));
 Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
 Assert.assertEquals("output.txt", commandLine.getOptionValue('o'));
 Assert.assertFalse(commandLine.hasOption("invalid"));
 Assert.assertFalse(commandLine.hasOption('i'));

 String[] args = commandLine.getArgs();
 Assert.assertEquals(2, args.length);
 Assert.assertEquals("arg1", args[0]);
 Assert.assertEquals("arg2", args[1]);
 }

 @Test
 public void testToString() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("t", "test", false, "Run tests.");
 options.addOption("o", "output", true, "Output file");
 CommandLine parser = new CommandLine();
 parser.parse(options.parse("--test --output, output.txt arg1 arg2"));

 String testString = "[ CommandLine: [ options: {test=null, output=output.txt}, size=2 } [ args: [arg1, arg2] ] ]";
 String parserString = parser.toString();

 Assert.assertNotNull(parserString);
 Assert.assertEquals(testString, parserString);
 }
}