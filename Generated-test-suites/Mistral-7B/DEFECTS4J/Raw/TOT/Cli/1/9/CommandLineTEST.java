import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for CommandLine class
 */
public class CommandLineTest {

 @Test
 public void testCommandLineWithValidOptions() throws ParseException {
 // create command line parser
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");

 // create sample command line arguments
 String[] args = new String[]{"-a", "-b", "arg"};

 // parse command line
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);

 // test if options are set
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));

 // test option values
 Assert.assertEquals("arg", cmd.getOptionValue("b"));
 Assert.assertNull(cmd.getOptionValue("c")); // unrecognized option

 // test get option object
 Option optionA = options.getOption("a");
 Object optionObject = cmd.getOptionObject("a");
 Assert.assertEquals(optionA, optionObject);

 // test get options
 Option[] optionsArray = cmd.getOptions();
 Assert.assertEquals(2, optionsArray.length);
 Assert.assertEquals(options.getOption("a"), optionsArray[0]);
 Assert.assertEquals(options.getOption("b"), optionsArray[1]);
 }

 @Test(expected = ParseException.class)
 public void testCommandLineWithInvalidOption() throws ParseException {
 // create command line parser
 Options options = new Options();
 options.addOption("a", true, "Option A");

 // create sample command line arguments with invalid option
 String[] args = new String[]{"-a", "-c", "arg"};

 // parse command line
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);
 }

 @Test(expected = ParseException.class)
 public void testCommandLineWithMissingRequiredOption() throws ParseException {
 // create command line parser
 Options options = new Options();
 options.addOption("a", 1, "Option A with required argument");

 // create sample command line arguments without required option
 String[] args = new String[]{"-b", "arg"};

 // parse command line
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);
 }

 @Test
 public void testCommandLineWithLeftOverArguments() throws ParseException {
 // create command line parser
 Options options = new Options();
 options.addOption("a", true, "Option A");

 // create sample command line arguments with left over arguments
 String[] args = new String[]{"-a", "arg1", "arg2"};

 // parse command line
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);

 // test left over arguments
 String[] leftOverArgs = cmd.getArgs();
 Assert.assertEquals(2, leftOverArgs.length);
 Assert.assertEquals("arg1", leftOverArgs[0]);
 Assert.assertEquals("arg2", leftOverArgs[1]);
 }
}