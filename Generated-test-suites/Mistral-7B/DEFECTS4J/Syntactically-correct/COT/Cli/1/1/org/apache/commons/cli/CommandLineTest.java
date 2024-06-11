import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

 private CommandLine commandLine;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 commandLine = new CommandLine();
 }

 @Test
 public void testHasOption() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));

 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertTrue(commandLine.hasOption('a'));

 Assert.assertFalse(commandLine.hasOption("b"));
 Assert.assertFalse(commandLine.hasOption('b'));
 }

 @Test
 public void testGetOptionValue() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "value"}));

 Assert.assertEquals("value", commandLine.getOptionValue("a"));
 Assert.assertEquals("value", commandLine.getOptionValue('a'));

 Assert.assertNull(commandLine.getOptionValue("b"));
 Assert.assertNull(commandLine.getOptionValue('b'));
 }

 @Test
 public void testGetOptionValues() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "value1", "value2"}));

 String[] values = commandLine.getOptionValues("a");

 Assert.assertNotNull(values);
 Assert.assertEquals(2, values.length);
 Assert.assertEquals("value1", values[0]);
 Assert.assertEquals("value2", values[1]);

 Assert.assertNull(commandLine.getOptionValues("b"));
 }

 @Test
 public void testGetArgs() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"arg1", "--a", "arg3"}));

 String[] args = commandLine.getArgs();

 Assert.assertNotNull(args);
 Assert.assertEquals(2, args.length);
 Assert.assertEquals("arg1", args[0]);
 Assert.assertEquals("arg3", args[1]);
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithMissingOption() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--b"}));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithInvalidOption() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--c"}));
 }

 @Test
 public void testParseExceptionWithInvalidLongOption() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--0a"}));
 }
}