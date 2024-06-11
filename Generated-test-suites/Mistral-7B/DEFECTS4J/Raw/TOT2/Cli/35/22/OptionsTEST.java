import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testBasicUsage() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "output.txt"});
 assertEquals("output.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test
 public void testMissingOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
 assertNull(cmd.getOptionValue("output"));
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testDuplicateOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 options.addOption("o", "other", false, "another option");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o"});
 assertNull(cmd.getOptionValue("other"));
 } catch (ParseException e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");
 options.addOption("z", "zoo", true, "zoo file"); // invalid char
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("l", "longopt", true, "long option with value");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--longopt", "value"});
 assertEquals("value", cmd.getOptionValue("longopt"));
 } catch (ParseException e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test
 public void testGroupRequiredOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setRequired(true);
 options.addOptionGroup(group);
 group.addOption(new Option("g", "test", true, "test option in group"));

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g", "testvalue"});
 assertTrue(cmd.hasOption("test"));
 assertTrue(cmd.hasOption("group"));
 } catch (ParseException e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testGroupRequiredMissingOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setRequired(true);
 options.addOptionGroup(group);
 group.addOption(new Option("g", "test", true, "test option in group"));

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g"});
 fail("Test failed: expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testGroupOptionalOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setRequired(false);
 options.addOptionGroup(group);
 group.addOption(new Option("g", "test", true, "test option in group"));

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g", "testvalue"});
 assertTrue(cmd.hasOption("test"));
 assertTrue(cmd.hasOption("group"));
 } catch (ParseException e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test
 public void testGroupMixedOptions() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setRequired(false);
 options.addOptionGroup(group);
 group.addOption(new Option("g", "test1", true, "test option 1 in group"));
 group.addOption(new Option("g", "test2", true, "test option 2 in group"));
options.addOption("o", "output", true, "output file");

 CommandLineParser parser = new DefaultParser();
try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g", "test1value", "-o", "output.txt"});
 assertEquals("test1value", cmd.getOptionValue("test1"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testGroupInvalidOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group options");
 group.setRequired(false);
 options.addOptionGroup(group);
 group.addOption(new Option("g", "test", true, "test option in group"));
 options.addOption("z", "zoo", true, "zoo file"); // invalid char

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{});
 fail("Test failed: expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testHelp() {
 Options options = new Options();
 options.addOption("o", "output", true, "output file");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("OptionsTest", options.toArray(), true);
 }
}