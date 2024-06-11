import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testSimpleOption() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addBooleanOption("test", "test", false);

 try {
 cmd = new CommandLine(opts, new String[]{"--test"});
 Assert.assertTrue(cmd.hasOption("test"));
 Assert.assertFalse(cmd.hasOption("unknown"));
 } catch (ParseException e) {
 // Should not come here as test case is valid
 }
 }

 @Test
 public void testOptionWithArgument() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addRequiredOption("input", "i", true, "file");

 try {
 cmd = new CommandLine(opts, new String[]{"-i", "test.txt"});
 Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
 } catch (ParseException e) {
 // Should not come here as test case is valid
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addBooleanOption("test", "test", false);

 try {
 cmd = new CommandLine(opts, new String[]{"--unknown"});
 } catch (ParseException e) {
 Assert.assertEquals("No option: --unknown", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingArgument() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addRequiredOption("input", "i", true, "file");

 try {
 cmd = new CommandLine(opts, new String[]{"-i"});
 } catch (ParseException e) {
 Assert.assertEquals("The option '-i' requires an argument.", e.getMessage());
 }
 }

 @Test
 public void testMultipleOptions() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addBooleanOption("test1", "t1", false);
 opts.addBooleanOption("test2", "t2", false);

 try {
 cmd = new CommandLine(opts, new String[]{"--test1", "--test2"});
 Assert.assertTrue(cmd.hasOption("test1"));
 Assert.assertTrue(cmd.hasOption("test2"));
 Assert.assertFalse(cmd.hasOption("unknown"));
 } catch (ParseException e) {
 // Should not come here as test case is valid
 }
 }

 @Test
 public void testMixedOptions() {
 CommandLine cmd;
 Options opts = new Options();
 opts.addBooleanOption("test1", "t1", false);
 opts.addBooleanOption("test2", "t2", false);
 opts.addRequiredOption("input", "i", true, "file");

 try {
 cmd = new CommandLine(opts, new String[]{"--test1", "--test2", "-i", "file.txt"});
 Assert.assertTrue(cmd.hasOption("test1"));
 Assert.assertTrue(cmd.hasOption("test2"));
 Assert.assertEquals("file.txt", cmd.getOptionValue("input"));
 } catch (ParseException e) {
 // Should not come here as test case is valid
 }
 }
}