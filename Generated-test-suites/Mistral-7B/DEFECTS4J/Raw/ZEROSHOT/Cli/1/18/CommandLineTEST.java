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
 options.addOption("a", "apple", true, "Apple value");
 options.addOption("b", "banana", false);
 CommandLine commandLine = new CommandLine();
 }

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--apple", "1", "--banana"});

 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertTrue(commandLine.hasOption('a'));
 Assert.assertFalse(commandLine.hasOption("c"));
 Assert.assertFalse(commandLine.hasOption('c'));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--apple", "1", "--banana"});

 Assert.assertEquals("1", commandLine.getOptionValue("a"));
 Assert.assertEquals("1", commandLine.getOptionValue('a'));
 Assert.assertNull(commandLine.getOptionValue("c"));
 Assert.assertNull(commandLine.getOptionValue('c'));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionValueNull() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--apple", "1"});

 Assert.assertEquals("1", commandLine.getOptionValue("a"));
 Assert.assertEquals("1", commandLine.getOptionValue('a'));
 commandLine = null;
 commandLine.getOptionValue("a");
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--apple", "1", "--apple", "2"});

 String[] values = commandLine.getOptionValues("apple");
 Assert.assertArrayEquals(new String[]{"1", "2"}, values);
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--apple", "1", "--banana", "2", "3"});

 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"2", "3"}, args);
 }

 @Test
 public void testParseException() {
 Options options = new Options();
 options.addOption("a", "apple", true, "Apple value");
 CommandLine commandLine = null;
 try {
 commandLine = new CommandLine(options, new String[]{"--banana"});
 } catch (ParseException e) {
 // do nothing
 }
 Assert.assertNotNull(commandLine.getOptions());
 Assert.assertEquals(1, commandLine.getOptions().length);
 }

 @Test(expected = NullPointerException.class)
 public void testParseExceptionNull() {
 Options options = new Options();
 CommandLine commandLine = null;
 try {
 commandLine = new CommandLine(options, new String[0]);
 } catch (ParseException e) {
 // do nothing
 }
 commandLine.getOptions();
 }

}