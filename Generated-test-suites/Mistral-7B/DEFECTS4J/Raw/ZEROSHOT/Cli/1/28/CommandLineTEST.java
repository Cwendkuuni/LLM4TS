import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

 @Before
 public void setUp() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C with an argument");
 }

 @Test
 public void testHasOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--a", "1", "--b", "2", "--c", "3"});

 // Test hasOption with short option
 Assert.assertTrue(commandLine.hasOption('a'));

 // Test hasOption with long option
 Assert.assertTrue(commandLine.hasOption("a"));

 // Test hasOption with non-existent option
 Assert.assertFalse(commandLine.hasOption("x"));
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--a", "1", "--b", "2", "--c", "3"});

 // Test getOptionValue with boolean option
 Assert.assertEquals("1", commandLine.getOptionValue("a"));

 // Test getOptionValue with value option
 Assert.assertEquals("2", commandLine.getOptionValue("b"));

 // Test getOptionValue with option requiring an argument
 Assert.assertEquals("3", commandLine.getOptionValue("c"));

 // Test getOptionValue with non-existent option
 Assert.assertNull(commandLine.getOptionValue("x"));
 }

 @Test
 public void testGetOptionValues() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--a", "1", "--b", "2", "--c", "value1"});

 // Test getOptionValues with boolean option
 Assert.assertNull(commandLine.getOptionValues("a"));

 // Test getOptionValues with value option
 String[] values = commandLine.getOptionValues("b");
 Assert.assertEquals(2, values.length);
 Assert.assertEquals("2", values[0]);

 // Test getOptionValues with option requiring an argument
 values = commandLine.getOptionValues("c");
 Assert.assertEquals(1, values.length);
 Assert.assertEquals("value1", values[0]);

 // Test getOptionValues with non-existent option
 Assert.assertNull(commandLine.getOptionValues("x"));
 }

 @Test
 public void testGetArgs() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"--a", "1", "--b", "2", "--c", "value1", "arg1", "arg2"});

 String[] args = commandLine.getArgs();
 Assert.assertEquals(2, args.length);
 Assert.assertEquals("arg1", args[0]);
 Assert.assertEquals("arg2", args[1]);
 }
}