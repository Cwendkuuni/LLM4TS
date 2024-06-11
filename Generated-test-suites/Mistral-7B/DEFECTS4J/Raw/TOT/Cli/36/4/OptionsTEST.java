import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Test
 public void testBasicUsage() {
 options = new Options();
 options.addOption("i", "inputFile", true, "Input file path");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file"});
 Assert.assertEquals("/path/to/input/file", commandLine.getOptionValue("inputFile"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessage formatFailureMessage = new FailureMessage("Test failed: ", e);
 System.err.println(formatFailureMessage.getMessage());
 System.exit(1);
 }
 }

 @Test
 public void testLongOptionUsage() {
 options = new Options();
 options.addOption("i", "inputFile", true, "Input file path");
 options.addOption("l", "longOption", false, "Long option description");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"--longOption"});
 Assert.assertTrue(commandLine.hasOption("longOption"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessage formatFailureMessage = new FailureMessage("Test failed: ", e);
 System.err.println(formatFailureMessage.getMessage());
 System.exit(1);
 }
 }

 @Test
 public void testMissingOptionUsage() {
 options = new Options();
 options.addOption("i", "inputFile", true, "Input file path");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"--missingOption"});
 Assert.fail("Missing expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testMixedOptionUsage() {
 options = new Options();
 options.addOption("i", "inputFile", true, "Input file path");
 options.addOption("b", "booleanOption", false, "Boolean option description");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file", "--booleanOption"});
 Assert.assertTrue(commandLine.hasOption("inputFile"));
 Assert.assertTrue(commandLine.hasOption("booleanOption"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessage formatFailureMessage = new FailureMessage("Test failed: ", e);
 System.err.println(formatFailureMessage.getMessage());
 System.exit(1);
 }
 }

 @Test
 public void testOptionGroupUsage() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Group description");
 group.addOption("g", "groupOption", true, "Group option description");

 options.addOptionGroup(group);
 options.addOption("i", "inputFile", true, "Input file path");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"--groupOption", "-i", "/path/to/input/file"});
 Assert.assertTrue(commandLine.hasOption("groupOption"));
 Assert.assertTrue(commandLine.hasOption("inputFile"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessage formatFailureMessage = new FailureMessage("Test failed: ", e);
 System.err.println(formatFailureMessage.getMessage());
 System.exit(1);
 }
 }

 @Test
 public void testMissingGroupOptionUsage() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Group description");
 group.addOption("g", "groupOption", true, "Group option description");

 options.addOptionGroup(group);
 options.addOption("i", "inputFile", true, "Input file path");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file"});
 // This should fail as groupOption is a required option in the group
 Assert.fail("Missing expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }
}