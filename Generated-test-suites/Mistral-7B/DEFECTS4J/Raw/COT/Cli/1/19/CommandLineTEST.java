import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class CommandLineTest {

 private CommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new CommandLine();
 }

 /**
 * Test for the hasOption method with a valid option.
 */
 @Test
 public void testHasOptionTrue() {
 String option = "testOption";
 Option testOption = Option.builder("t").longOpt("testOption").build();
 commandLine.addOption(testOption);
 Assert.assertTrue(commandLine.hasOption(option));
 }

 /**
 * Test for the hasOption method with an invalid option.
 */
 @Test
 public void testHasOptionFalse() {
 String option = "invalidOption";
 Assert.assertFalse(commandLine.hasOption(option));
 }

 /**
 * Test for the getOptionValue method with a valid option with no argument.
 */
 @Test
 public void testGetOptionValueNoArg() {
 String option = "noArgOption";
 Option noArgOption = Option.builder("n").longOpt("noArgOption").build();
 commandLine.addOption(noArgOption);
 commandLine.parse("--noArgOption");
 Assert.assertEquals("", commandLine.getOptionValue(option));
 }

 /**
 * Test for the getOptionValue method with a valid option with an argument.
 */
 @Test
 public void testGetOptionValueArg() {
 String option = "argOption";
 String arg = "testArg";
 Option argOption = Option.builder("a").longOpt("argOption").hasArg(true).build();
 commandLine.addOption(argOption);
 commandLine.parse(new String[]{"--argOption", arg});
 Assert.assertEquals(arg, commandLine.getOptionValue(option));
 }

 /**
 * Test for the getOptionValues method with a valid option with multiple arguments.
 */
 @Test
 public void testGetOptionValuesMultipleArgs() {
 String option = "multiArgOption";
 String[] args = {"--multiArgOption", "test1", "test2"};
 Option multiArgOption = Option.builder("m").longOpt("multiArgOption")
 .hasArg(true).build();
 commandLine.addOption(multiArgOption);
 commandLine.parse(args);
 String[] result = commandLine.getOptionValues(option);
 Assert.assertArrayEquals(args, result);
 }

 /**
 * Test for the getOptionValues method with a valid option with no arguments.
 */
 @Test
 public void testGetOptionValuesNoArg() {
 String option = "noArgOption";
 Option noArgOption = Option.builder("n").longOpt("noArgOption").build();
 commandLine.addOption(noArgOption);
 commandLine.parse(new String[]{"--noArgOption"});
 String[] result = commandLine.getOptionValues(option);
 Assert.assertArrayEquals(new String[0], result);
 }

 /**
 * Test for the getOptionObject method with a valid option.
 */
 @Test
 public void testGetOptionObject() {
 String option = "integerOption";
 int value = 10;
 Option integerOption = Option.builder("i").longOpt("integerOption")
 .type(Integer.TYPE).build();
 commandLine.addOption(integerOption);
 commandLine.parse(new String[]{"--integerOption", Integer.toString(value)});
 int result = (Integer) commandLine.getOptionObject(option);
 Assert.assertEquals(value, result);
 }

 /**
 * Test for the getOptionObject method with an invalid option.
 */
 @Test(expected = NullPointerException.class)
 public void testGetOptionObjectInvalidOption() {
 String option = "invalidOption";
 commandLine.parse(new String[0]);
 commandLine.getOptionObject(option);
 }

 /**
 * Test for the getOptionObject method with an option that does not have a value.
 */
 @Test
 public void testGetOptionObjectNoValue() {
 String option = "noValueOption";
 Option noValueOption = Option.builder("n").longOpt("noValueOption").build();
 commandLine.addOption(noValueOption);
 commandLine.parse(new String[0]);
 Object result = commandLine.getOptionObject(option);
 Assert.assertNull(result);
 }

 /**
 * Test for the parse method with valid options and arguments.
 */
 @Test
 public void testParseValidOptions() {
 String[] args = {"--testOption", "test1", "--argOption=testArg"};
 Option testOption = Option.builder("t").longOpt("testOption").build();
 Option argOption = Option.builder("a").longOpt("argOption").hasArg(true).build();
 commandLine.addOption(testOption);
 commandLine.addOption(argOption);
 try {
 commandLine.parse(args);
 } catch (ParseException e) {
 // should not reach here
 }
 Assert.assertTrue(commandLine.hasOption("testOption"));
 Assert.assertEquals("testArg", commandLine.getOptionValue("argOption"));
 }

 /**
 * Test for the parse method with invalid options.
 */
 @Test(expected = ParseException.class)
 public void testParseInvalidOptions() {
 String[] args = {"--invalidOption", "--testOption=invalidValue"};
 Option testOption = Option.builder("t").longOpt("testOption").build();
 Option invalidOption = Option.builder("i").longOpt("invalidOption").build();
 commandLine.addOption(testOption);
 commandLine.addOption(invalidOption);
 try {
 commandLine.parse(args);
 } catch (ParseException e) {
 // expected
 }
 }

 /**
 * Test for the parse method with no options or arguments.
 */
 @Test
 public void testParseNoOptions() {
 try {
 commandLine.parse(new String[0]);
 } catch (ParseException e) {
 // expected
 }
 }

 /**
 * Test for the parse method with too many arguments.
 */
 @Test(expected = ParseException.class)
 public void testParseTooManyArguments() {
 String[] args = {"--testOption", "test1", "test2", "test3"};
 Option testOption = Option.builder("t").longOpt("testOption").build();
 commandLine.addOption(testOption);
 try {
 commandLine.parse(args);
 } catch (ParseException e) {
 // expected
 }
 }

 /**
 * Test for the parse method with missing arguments for options requiring arguments.
 */
 @Test(expected = ParseException.class)
 public void testParseMissingArguments() {
 String[] args = {"--argOption"};
 Option argOption = Option.builder("a").longOpt("argOption").hasArg(true).build();
 commandLine.addOption(argOption);
 try {
 commandLine.parse(args);
 } catch (ParseException e) {
 // expected
 }
 }

 /**
 * Test for the parse method with unrecognized options.
 */
 @Test
 public void testParseUnrecognizedOptions() {
 String[] args = {"--unknownOption", "test1", "--testOption", "test2"};
 Option testOption = Option.builder("t").longOpt("testOption").build();
 commandLine.addOption(testOption);
 try {
 commandLine.parse(args);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertEquals(Arrays.asList("--unknownOption", "test1"), commandLine.getArgList());
 }
}