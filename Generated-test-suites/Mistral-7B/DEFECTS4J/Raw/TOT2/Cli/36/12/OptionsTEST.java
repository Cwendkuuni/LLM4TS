import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();
 options.addOption("a", "alias", false, "A test option with an alias");
 options.addOption("o", "output", true, "Output file path");
 options.addOptionGroup(new OptionGroup("g", "group")
 .addOption("g1", true, "Test option in a group")
 .addOption("g2", true, "Another test option in a group"));
 return options;
 }

 @Test
 public void testBasicOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new BasicParser();

 String[] args = {"-a"};
 try {
 CommandLine commandLine = parser.parse(options.getOptions(), args);
 assertTrue(commandLine.hasOption("a"));
 assertFalse(commandLine.hasOption("o"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test failed due to parse exception");
 }
 }

 @Test
 public void testOptionWithArgument() {
 Options options = createTestOptions();
 CommandLineParser parser = new BasicParser();

 String[] args = {"-o", "output.txt"};
 try {
 CommandLine commandLine = parser.parse(options.getOptions(), args);
 assertEquals("output.txt", commandLine.getOptionValue("o"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test failed due to parse exception");
 }
 }

 @Test
 public void testOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new BasicParser();

 String[] args = {"--group", "g1"};
 try {
 CommandLine commandLine = parser.parse(options.getOptions(), args);
 assertTrue(commandLine.hasOption("g1"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test failed due to parse exception");
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new BasicParser();

 String[] args = {};
 try {
 CommandLine commandLine = parser.parse(options.getOptions(), args);
 fail("Test did not throw ParseException as expected");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testMismatchedOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new BasicParser();

 String[] args = {"-o", "a"};
 try {
 CommandLine commandLine = parser.parse(options.getOptions(), args);
 fail("Test did not throw ParseException as expected");
 } catch (ParseException e) {
 // Expected
 }
 }
}