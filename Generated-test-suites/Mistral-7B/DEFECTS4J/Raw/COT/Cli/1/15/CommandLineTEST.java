import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testHasOption() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "option A");
 cmd.parse(options.parse(new String[]{"--a"}));
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption('a'));
 }

 @Test
 public void testHasOptionFalse() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 cmd.parse(options.parse(new String[]{}));
 Assert.assertFalse(cmd.hasOption("a"));
 Assert.assertFalse(cmd.hasOption('a'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "option A");
 cmd.parse(options.parse(new String[]{"--a", "true"}));
 Assert.assertNull(cmd.getOptionValue("a"));
 Assert.assertEquals("true", cmd.getOptionValue("a", "default"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOptionValueNullOption() {
 CommandLine cmd = new CommandLine();
 cmd.parse(new String[]{"--foo"});
 cmd.getOptionValue("foo");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOptionValueEmptyArg() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "option A");
 cmd.parse(options.parse(new String[]{"--a"}));
 cmd.getOptionValue("a");
 }

 @Test
 public void testGetArgs() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 cmd.parse(options.parse(new String[]{"--a", "arg1", "arg2"}));
 String[] args = cmd.getArgs();
 Assert.assertEquals("arg1", args[0]);
 Assert.assertEquals("arg2", args[1]);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetArgsEmpty() {
 CommandLine cmd = new CommandLine();
 cmd.parse(new String[]{});
 cmd.getArgs();
 }

 @Test
 public void testGetArgList() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 cmd.parse(options.parse(new String[]{"--a", "arg1", "arg2"}));
 List argList = cmd.getArgList();
 Assert.assertEquals("arg1", argList.get(0));
 Assert.assertEquals("arg2", argList.get(1));
 }

 @Test
 public void testParseWithExceptions() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "option A");
 try {
 cmd.parse(options.parse(new String[]{"--a=abc", "--b"}));
 Assert.fail();
 } catch (ParseException e) {
 // Expected
 }
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertEquals("abc", cmd.getOptionValue("a"));
 Assert.assertFalse(cmd.hasOption("b"));
 }
}