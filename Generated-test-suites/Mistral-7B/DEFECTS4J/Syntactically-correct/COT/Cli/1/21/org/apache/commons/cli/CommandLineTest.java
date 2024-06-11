import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CommandLineTest {

 private CommandLine cmd;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 }

 @Test
 public void testHasOption() {
 Option option = new Option("t", "test", true, "test description");
 cmd.addOption(option);
 Assert.assertTrue(cmd.hasOption("t"));
 Assert.assertTrue(cmd.hasOption('t'));
 }

 @Test
 public void testHasOptionFalse() {
 Assert.assertFalse(cmd.hasOption("nonexistent"));
 Assert.assertFalse(cmd.hasOption('n'));
 }

 @Test
 public void testGetOptionObjectString() throws ParseException {
 Option option = new Option("t", "test", true, "test description");
 cmd.addOption(option);
 cmd.parse("--test somevalue");
 Assert.assertEquals("somevalue", cmd.getOptionObject("t"));
 }

 @Test(expected = ParseException.class)
 public void testGetOptionObjectUnknownOption() {
 cmd.parse("--unknown");
 }

 @Test
 public void testGetOptionValue() {
 Option option = new Option("t", "test", true, "test description");
 cmd.addOption(option);
 cmd.parse("--test somevalue");
 Assert.assertEquals("somevalue", cmd.getOptionValue("t"));
 }

 @Test
 public void testGetOptionValueEmpty() {
 Option option = new Option("t", "test", false, "test description");
 cmd.addOption(option);
 cmd.parse("--test");
 Assert.assertNull(cmd.getOptionValue("t"));
 }

 @Test
 public void testGetOptionValues() {
 Option option = new Option("t", "test", true, "test description");
 cmd.addOption(option);
 cmd.parse("--test value1 --test value2");
 String[] values = cmd.getOptionValues("t");
 Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test
 public void testGetOptionValuesEmpty() {
 Option option = new Option("t", "test", false, "test description");
 cmd.parse("--test");
 String[] values = cmd.getOptionValues("t");
 Assert.assertArrayEquals(new String[0], values);
 }

 @Test
 public void testGetArgs() {
 cmd.parse("arg1 arg2 arg3");
 String[] args = cmd.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
 }

 @Test
 public void testGetArgList() {
 cmd.parse("arg1 arg2 arg3");
 List argList = cmd.getArgList();
 Assert.assertEquals(3, argList.size());
 Assert.assertEquals("arg1", argList.get(0));
 Assert.assertEquals("arg2", argList.get(1));
 Assert.assertEquals("arg3", argList.get(2));
 }
}