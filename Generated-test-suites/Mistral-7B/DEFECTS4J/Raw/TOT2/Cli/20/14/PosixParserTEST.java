import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PosixParser class
 */
public class PosixParserTest {

 @Test
 public void testEmptyArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {};
 Options options = new Options();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[0], parsedArguments);
 }

 @Test
 public void testSingleOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option"};
 Options options = new Options();
 options.addOption("option", false, null, "Option description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(2, parsedArguments.length);
 Assert.assertEquals("option", parsedArguments[0]);
 }

 @Test
 public void testOptionWithValue() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option", "value"};
 Options options = new Options();
 options.addOption("option", true, null, "Option description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(3, parsedArguments.length);
 Assert.assertEquals("option", parsedArguments[0]);
 Assert.assertEquals("value", parsedArguments[1]);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--invalidOption"};
 Options options = new Options();
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithValue() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--invalidOption", "value"};
 Options options = new Options();
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testLongOptionWithValue() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--long-option", "value"};
 Options options = new Options();
 options.addOption("longOption", true, null, "Long option description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(3, parsedArguments.length);
 Assert.assertEquals("longOption", parsedArguments[0]);
 Assert.assertEquals("value", parsedArguments[1]);
 }

 @Test(expected = ParseException.class)
 public void testLongOptionNoValue() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--long-option"};
 Options options = new Options();
 options.addOption("longOption", false, null, "Long option description");
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testLongOptionWithValueAndArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--long-option", "value", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("longOption", true, null, "Long option description");
 options.addOption("longOptionArg", true, null, "Long option arg description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(5, parsedArguments.length);
 Assert.assertEquals("longOption", parsedArguments[0]);
 Assert.assertEquals("value", parsedArguments[1]);
 Assert.assertEquals("longOptionArg", parsedArguments[2]);
 Assert.assertEquals("arg1", parsedArguments[3]);
 Assert.assertEquals("arg2", parsedArguments[4]);
 }

 @Test(expected = ParseException.class)
 public void testLongOptionWithInvalidArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--long-option", "value", "invalid"};
 Options options = new Options();
 options.addOption("longOption", true, null, "Long option description");
 options.addOption("longOptionArg", true, null, "Long option arg description");
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testBurstOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o", "opt1", "--long-option", "value", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", false, null, "Option description");
 options.addOption("longOption", true, null, "Long option description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(7, parsedArguments.length);
 Assert.assertEquals("option", parsedArguments[0]);
 Assert.assertEquals("opt1", parsedArguments[1]);
 Assert.assertEquals("longOption", parsedArguments[2]);
 Assert.assertEquals("value", parsedArguments[3]);
 Assert.assertEquals("arg1", parsedArguments[4]);
 Assert.assertEquals("arg2", parsedArguments[5]);
 }

 @Test(expected = ParseException.class)
 public void testBurstOptionWithInvalidArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o", "opt1", "--long-option", "value", "arg1", "invalid"};
 Options options = new Options();
 options.addOption("option", false, null, "Option description");
 options.addOption("longOption", true, null, "Long option description");
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testBurstOptionWithNoArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o", "opt1", "--long-option"};
 Options options = new Options();
 options.addOption("option", false, null, "Option description");
 options.addOption("longOption", false, null, "Long option description");
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertEquals(4, parsedArguments.length);
 Assert.assertEquals("option", parsedArguments[0]);
 Assert.assertEquals("opt1", parsedArguments[1]);
 Assert.assertEquals("longOption", parsedArguments[2]);
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option", "value", "nonOption"};
 Options options = new Options();
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, true);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testStopAtNonOptionWithArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option", "value", "nonOption", "arg1", "arg2"};
 Options options = new Options();
 String[] parsedArguments = parser.flatten(options, arguments, true);
 Assert.assertEquals(3, parsedArguments.length);
 Assert.assertEquals("--", parsedArguments[0]);
 Assert.assertEquals("nonOption", parsedArguments[1]);
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOptionWithInvalidArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option", "value", "nonOption", "invalid"};
 Options options = new Options();
 String[] parsedArguments = null;
 try {
 parsedArguments = parser.flatten(options, arguments, true);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(parsedArguments);
 }

 @Test
 public void testStopAtNonOptionWithValue() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option", "value", "--"};
 Options options = new Options();
 String[] parsedArguments = parser.flatten(options, arguments, true);
 Assert.assertEquals(3, parsedArguments.length);
 Assert.assertEquals("--option", parsedArguments[0]);
 Assert.assertEquals("value", parsedArguments[1]);
 }
}