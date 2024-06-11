import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private static final String[] ARGUMENTS = {"-a", "--option1", "-b", "--option2", "arg1", "arg2"};
 private static final String[] ARGUMENTS_WITH_LONG_OPTION = {"--option1", "arg1"};
 private static final String[] ARGUMENTS_WITH_SHORT_OPTION = {"-a", "arg1"};
 private static final String[] ARGUMENTS_WITH_NO_OPTION = {"arg1", "arg2"};

 @Test
 public void testHasOption() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("option1"));
 Assert.assertFalse(cmd.hasOption("c"));
 }

 @Test
 public void testHasOptionChar() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 Assert.assertTrue(cmd.hasOption('a'));
 Assert.assertTrue(cmd.hasOption('o')); // option1
 Assert.assertFalse(cmd.hasOption('c'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 Assert.assertNull(cmd.getOptionValue("c"));
 Assert.assertNull(cmd.getOptionValue("o")); // option1 has no value
 Assert.assertNull(cmd.getOptionValue("a")); // "a" has no value
 Assert.assertEquals("arg1", cmd.getOptionValue("option1"));
 Assert.assertEquals("arg2", cmd.getOptionValue("b"));
 }

 @Test
 public void testGetOptionObject() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 Assert.assertEquals(Boolean.TRUE, cmd.getOptionObject("help")); // no such option
 Assert.assertEquals(Boolean.TRUE, cmd.getOptionObject("silent")); // no such option
 Assert.assertNull(cmd.getOptionObject("a")); // "a" has no value
 Assert.assertNull(cmd.getOptionObject("option1")); // option1 has no value object
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionObjectNullOption() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 cmd.getOptionObject(null);
 }

 @Test
 public void testGetOptionValues() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 String[] values = cmd.getOptionValues("b");
 Assert.assertNull(values);
 String[] values1 = cmd.getOptionValues("option1");
 Assert.assertArrayEquals(ARGUMENTS_WITH_LONG_OPTION, values1);
 String[] values2 = cmd.getOptionValues('a');
 Assert.assertArrayEquals(ARGUMENTS_WITH_SHORT_OPTION, values2);
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionValuesNullOption() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 cmd.getOptionValues(null);
 }

 @Test
 public void testGetArgs() {
 CommandLine cmd = new CommandLine().parse(ARGUMENTS);
 String[] args = cmd.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testAddArg() {
 CommandLine cmd = new CommandLine();
 cmd.addArg("test");
 List<String> argList = cmd.getArgList();
 Assert.assertEquals(1, argList.size());
 Assert.assertEquals("test", argList.get(0));
 }

 @Test
 public void testParseException() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("b", false, false, "beta");
 try {
 cmd.parse(ARGUMENTS, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionInvalidOption() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("c", false, false, "gamma");
 try {
 cmd.parse(ARGUMENTS, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseExceptionMissingArgument() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "arg");
 try {
 cmd.parse(ARGUMENTS_WITH_NO_OPTION, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionMissingRequiredOption() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "arg", "required");
 try {
 cmd.parse(ARGUMENTS_WITH_NO_OPTION, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionInvalidOptionValueType() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, Integer.TYPE, "anInteger");
 try {
 cmd.parse(ARGUMENTS_WITH_LONG_OPTION, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionInvalidOptionValue() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, Integer.TYPE, "anInteger");
 try {
 cmd.parse(ARGUMENTS_WITH_SHORT_OPTION, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionInvalidOptionValueLength() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, Integer.TYPE, "anInteger");
 try {
 cmd.parse(new String[]{"-a", "0123456789"}, options);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 CommandLine cmd = new CommandLine();
 Options options = new Options();
 options.addOption("a", true, "arg");
 try {
 cmd.parse(ARGUMENTS, options);
 } catch (ParseException e) {
 // ignored
 }
 // todo: test toString() implementation
 }

}