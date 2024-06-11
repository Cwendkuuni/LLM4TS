import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("o", "option", true, "option description");
 options.addOption("a", "anOption", null, "anOption description");
 parser = new PosixParser();
 }

 @After
 public void tearDown() {
 parser = null;
 options = null;
 }

 @Test
 public void testEmptyArguments() {
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSingleHyphen() {
 String[] arguments = {"-"};
 String[] expected = {"-"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDoubleHyphen() {
 String[] arguments = {"--"};
 String[] expected = {"--"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTwoCharacterOption() {
 String[] arguments = {"-o"};
 String[] expected = {"-o"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testOptionWithArg() {
 String[] arguments = {"-o", "value"};
 String[] expected = {"-o", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testOptionWithLongArg() {
 String[] arguments = {"-o", "longOptionValue"};
 String[] expected = {"-o", "longOptionValue"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testOptionWithShortArgAndLongArg() {
 String[] arguments = {"-o", "value", "--longOption", "longOptionValue"};
 String[] expected = {"-o", "value", "--longOption", "longOptionValue"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBursting() {
 String[] arguments = {"-foo"};
 String[] expected = {"-f", "-o"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingWithArg() {
 String[] arguments = {"-foo", "arg"};
 String[] expected = {"-f", "-o", "arg"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingWithMultipleOptions() {
 String[] arguments = {"-foo", "-bar"};
 String[] expected = {"-f", "-o", "-b", "-a"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstingWithInvalidOption() {
 String[] arguments = {"-invalidOption"};
 String[] result = parser.flatten(options, arguments, false);
 assertNull(result);
 }

 @Test
 public void testStopAtNonOption() {
 String[] arguments = {"-o", "value", "nonOption"};
 String[] expected = {"-o", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testStopAtNonOptionWithEmptyArguments() {
 String[] arguments = {};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testStopAtNonOptionWithSingleArgument() {
 String[] arguments = {"nonOption"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testStopAtNonOptionWithMultipleArguments() {
 String[] arguments = {"nonOption1", "nonOption2"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testProcessSingleHyphen() {
 String[] arguments = {"-"};
 boolean eatTheRest = parser.processSingleHyphen("-");
 assertTrue(eatTheRest);
 }

 @Test
 public void testProcessSingleHyphenTwice() {
 String[] arguments = {"-", "-"};
 boolean eatTheRest = parser.processSingleHyphen("-");
 assertTrue(eatTheRest);
 eatTheRest = parser.processSingleHyphen("-");
 assertTrue(eatTheRest);
 }

 @Test
 public void testProcessSingleHyphenAfterOption() {
 String[] arguments = {"-o", "-"};
 boolean eatTheRest = parser.processSingleHyphen("-");
 assertFalse(eatTheRest);
 }

 @Test
 public void testProcessSingleHyphenAfterDoubleHyphen() {
 String[] arguments = {"--", "-"};
 boolean eatTheRest = parser.processSingleHyphen("-");
 assertFalse(eatTheRest);
 }

 @Test
 public void testProcessOptionToken() {
 String[] arguments = {"-o"};
 Option option = parser.processOptionToken("-o", false);
 assertEquals("o", option.getOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 }

 @Test
 public void testProcessOptionTokenWithLongOption() {
 String[] arguments = {"--longOption"};
 Option option = parser.processOptionToken("--longOption", false);
 assertEquals("longOption", option.getLongOpt());
 assertNull(option.getOpt());
 assertNull(option.getValue());
 }

 @Test
 public void testProcessOptionTokenWithValue() {
 String[] arguments = {"-o", "value"};
 Option option = parser.processOptionToken("-o", false);
 assertEquals("o", option.getOpt());
 assertEquals("value", option.getValue());
 assertNull(option.getLongOpt());
 }

 @Test
 public void testProcessOptionTokenWithValueLongOption() {
 String[] arguments = {"--longOption", "value"};
 Option option = parser.processOptionToken("--longOption", false);
 assertEquals("longOption", option.getLongOpt());
 assertEquals("value", option.getValue());
 assertNull(option.getOpt());
 }
}