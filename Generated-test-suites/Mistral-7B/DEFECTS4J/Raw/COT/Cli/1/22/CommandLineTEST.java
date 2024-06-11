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

 /**
 * Test method for {@link CommandLine#CommandLine()}.
 */
 @Test
 public void testConstructor() {
 // Verify that the default constructor works correctly
 }

 /**
 * Test method for {@link CommandLine#hasOption(String)} using a valid option.
 */
 @Test
 public void testHasOptionString() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));
 Assert.assertTrue(commandLine.hasOption("a"));
 }

 /**
 * Test method for {@link CommandLine#hasOption(String)} using an invalid option.
 */
 @Test
 public void testHasOptionStringFalse() {
 commandLine.parse(options.parse(new String[]{"--b"}));
 Assert.assertFalse(commandLine.hasOption("a"));
 }

 /**
 * Test method for {@link CommandLine#hasOption(char)} using a valid option.
 */
 @Test
 public void testHasOptionChar() {
 options.addOption("a", true, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));
 Assert.assertTrue(commandLine.hasOption('a'));
 }

 /**
 * Test method for {@link CommandLine#hasOption(char)} using an invalid option.
 */
 @Test
 public void testHasOptionCharFalse() {
 commandLine.parse(options.parse(new String[]{"--b"}));
 Assert.assertFalse(commandLine.hasOption('a'));
 }

 /**
 * Test method for {@link CommandLine#getOptionObject(String)} using a valid option.
 */
 @Test
 public void testGetOptionObjectStringValid() throws ParseException {
 options.addOption("a", Integer.TYPE, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "1"}));
 Assert.assertEquals(1, commandLine.getOptionObject("a"));
 }

 /**
 * Test method for {@link CommandLine#getOptionObject(String)} using an invalid option.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionObjectStringInvalid() {
 commandLine.parse(options.parse(new String[]{}));
 commandLine.getOptionObject("a");
 }

 /**
 * Test method for {@link CommandLine#getOptionObject(String)} using an option with no argument.
 */
 @Test
 public void testGetOptionObjectStringNoArg() throws ParseException {
 options.addOption("a", false, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));
 Assert.assertEquals(null, commandLine.getOptionObject("a"));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(String)} using a valid option.
 */
 @Test
 public void testGetOptionValueStringValid() throws ParseException {
 options.addOption("a", false, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "value"}));
 Assert.assertEquals("value", commandLine.getOptionValue("a"));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(String)} using an invalid option.
 */
 @Test
 public void testGetOptionValueStringInvalid() {
 Assert.assertEquals(null, commandLine.getOptionValue("a"));
 }

 /**
 * Test method for {@link CommandLine#getOptionValues(String)} using a valid option.
 */
 @Test
 public void testGetOptionValuesStringValid() throws ParseException {
 options.addOption("a", false, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "value1", "value2"}));
 String[] values = commandLine.getOptionValues("a");
 Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 /**
 * Test method for {@link CommandLine#getOptionValues(String)} using an invalid option.
 */
 @Test
 public void testGetOptionValuesStringInvalid() {
 String[] values = commandLine.getOptionValues("a");
 Assert.assertNull(values);
 }

 /**
 * Test method for {@link CommandLine#getOptionValues(char)} using a valid option.
 */
 @Test
 public void testGetOptionValuesCharValid() throws ParseException {
 options.addOption("a", false, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "value1", "value2"}));
 String[] values = commandLine.getOptionValues('a');
 Assert.assertArrayEqul(new String[]{"value1", "value2"}, values);
 }

 /**
 * Test method for {@link CommandLine#getOptionValues(char)} using an invalid option.
 */
 @Test
 public void testGetOptionValuesCharInvalid() {
 String[] values = commandLine.getOptionValues('a');
 Assert.assertNull(values);
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(String, String)} using a valid option with an argument.
 */
 @Test
 public void testGetOptionValueStringArgValid() throws ParseException {
 options.addOption("a", Integer.TYPE, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "123"}));
 Assert.assertEquals(123, commandLine.getOptionValue("a", null));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(String, String)} using a valid option with a default value.
 */
 @Test
 public void testGetOptionValueStringArgDefaultValid() throws ParseException {
 options.addOption("a", Integer.TYPE, "Option A", "defaultValue");
 commandLine.parse(options.parse(new String[]{"--a", "123"}));
 Assert.assertEquals(123, commandLine.getOptionValue("a", "defaultValue"));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(String, String)} using a valid option with no argument.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValueStringArgNoArg() throws ParseException {
 options.addOption("a", Boolean.TYPE, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));
 commandLine.getOptionValue("a", null);
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(char, String)} using a valid option with an argument.
 */
 @Test
 public void testGetOptionValueCharArgValid() throws ParseException {
 options.addOption("a", Integer.TYPE, "Option A");
 commandLine.parse(options.parse(new String[]{"--a", "123"}));
 Assert.assertEquals(123, commandLine.getOptionValue('a', null));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(char, String)} using a valid option with a default value.
 */
 @Test
 public void testGetOptionValueCharArgDefaultValid() throws ParseException {
 options.addOption("a", Integer.TYPE, "Option A", "defaultValue");
 commandLine.parse(options.parse(new String[]{"--a", "123"}));
 Assert.assertEquals(123, commandLine.getOptionValue('a', "defaultValue"));
 }

 /**
 * Test method for {@link CommandLine#getOptionValue(char, String)} using a valid option with no argument.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionValueCharArgNoArg() throws ParseException {
 options.addOption("a", Boolean.TYPE, "Option A");
 commandLine.parse(options.parse(new String[]{"--a"}));
 commandLine.getOptionValue('a', null);
 }

 /**
 * Test method for {@link CommandLine#getArgs()} when there are no arguments.
 */
 @Test
 public void testGetArgsEmpty() {
 Assert.assertArrayEquals(new String[0], commandLine.getArgs());
 }

 /**
 * Test method for {@link CommandLine#getArgs()} when there are arguments.
 */
 @Test
 public void testGetArgs() {
 String[] args = new String[]{"arg1", "arg2", "arg3"};
 commandLine.parse(options.parse(args));
 Assert.assertArrayEquals(args, commandLine.getArgs());
 }

 /**
 * Test method for {@link CommandLine#getArgList()} when there are no arguments.
 */
 @Test
 public void testGetArgListEmpty() {
 Assert.assertEquals(commandLine.getArgList(), commandLine.getArgList());
 }

 /**
 * Test method for {@link CommandLine#getArgList()} when there are arguments.
 */
 @Test
 public void testGetArgList() {
 List argList = new LinkedList();
 argList.add("arg1");
 argList.add("arg2");
 argList.add("arg3");
 commandLine.parse(options.parse(new String[0]));
 commandLine.addArg("arg4");
 Assert.assertEquals(argList, commandLine.getArgList());
 }
}