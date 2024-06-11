package org.apache.commons.cli;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;
 import static org.junit.Assert.assertTrue;

 import org.junit.Test;

 import org.apache.commons.cli.Options;
 import org.apache.commons.cli.ParseException;

 /**
 * Test cases for PosixParser class.
 */
 public class PosixParserTest {

 /**
 * Test flatten method with empty arguments array.
 */
 @Test
 public void testEmptyArgumentsArray() {
 PosixParser parser = new PosixParser();
 String[] arguments = {};
 Options options = new Options();
 String[] result = parser.flatten(options, arguments, false);
 assertNull(result);
 }

 /**
 * Test flatten method with simple arguments.
 */
 @Test
 public void testSimpleArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--foo", "-h", "-foo=bar", "baz"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("help", false, "h");
 options.addOption("foo", false, "f");
 options.addOption("bar", true, "b");
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(5, result.length);
 assertEquals("--foo", result[0]);
 assertEquals("-h", result[1]);
 assertEquals("-foo", result[2]);
 assertEquals("bar", result[3]);
 assertEquals("baz", result[4]);
 }

 /**
 * Test flatten method with long options and arguments.
 */
 @Test
 public void testLongOptions() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--foo=bar", "--long-option=value"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("long-option", true, "l");
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(3, result.length);
 assertEquals("--foo", result[0]);
 assertEquals("bar", result[1]);
 assertEquals("--long-option=value", result[2]);
 }

 /**
 * Test flatten method with short options and arguments.
 */
 @Test
 public void testShortOptions() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-f", "--foo=bar", "-h", "--long-option=value"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("help", false, "h");
 options.addOption("long-option", true, "l");
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(5, result.length);
 assertEquals("-f", result[0]);
 assertEquals("--foo", result[1]);
 assertEquals("bar", result[2]);
 assertEquals("-h", result[3]);
 assertEquals("--long-option=value", result[4]);
 }

 /**
 * Test flatten method with bursting.
 */
 @Test
 public void testBursting() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--foo--bar", "-h", "--long-option---value"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("help", false, "h");
 options.addOption("long-option", true, "l");
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(5, result.length);
 assertEquals("--foo", result[0]);
 assertEquals("--bar", result[1]);
 assertEquals("-h", result[2]);
 assertEquals("--long-option", result[3]);
 assertEquals("value", result[4]);
 }

 /**
 * Test flatten method with stopAtNonOption.
 */
 @Test
 public void testStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--foo", "-h", "-foo=bar", "baz", "--non-option"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("help", false, "h");
 options.addOption("foo", false, "f");
 options.addOption("bar", true, "b");
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(4, result.length);
 assertEquals("--foo", result[0]);
 assertEquals("-foo=bar", result[1]);
 assertEquals("baz", result[2]);
 assertNull(result[3]);
 }

 /**
 * Test init method.
 */
 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();
 parser.init();
 assertFalse(parser.eatTheRest);
 assertTrue(parser.tokens.isEmpty());
 }

 /**
 * Test processNonOptionToken method.
 */
 @Test
 public void testProcessNonOptionToken() {
 PosixParser parser = new PosixParser();
 String token = "--non-option";
 parser.processNonOptionToken(token);
 assertEquals(2, parser.tokens.size());
 assertEquals("--", parser.tokens.get(0));
 assertEquals(token, parser.tokens.get(1));
 }

 /**
 * Test processOptionToken method.
 */
 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 String token = "-f";
 parser.processOptionToken(token, false);
 assertEquals(1, parser.tokens.size());
 assertEquals("-f", parser.tokens.get(0));
 }

 /**
 * Test processOptionToken method with stopAtNonOption.
 */
 @Test
 public void testProcessOptionTokenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String token = "--non-option";
 parser.processOptionToken(token, true);
 assertFalse(parser.eatTheRest);
 assertEquals(1, parser.tokens.size());
 assertNull(parser.tokens.get(0));
 }

 /**
 * Test burstToken method with valid option.
 */
 @Test
 public void testBurstTokenWithValidOption() {
 PosixParser parser = new PosixParser();
 String token = "--foo--bar";
 parser.burstToken(token, false);
 assertEquals(2, parser.tokens.size());
 assertEquals("-foo", parser.tokens.get(0));
 assertEquals("-bar", parser.tokens.get(1));
 }

 /**
 * Test burstToken method with invalid option and stopAtNonOption.
 */
 @Test
 public void testBurstTokenWithInvalidOptionAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String token = "--non-option--value";
 parser.burstToken(token, true);
 assertTrue(parser.eatTheRest);
 assertEquals(3, parser.tokens.size());
 assertEquals("--non-option", parser.tokens.get(0));
 assertEquals("value", parser.tokens.get(1));
 assertSame(parser.tokens.get(2), "--");
 }

 /**
 * Test burstToken method with invalid option and no stopAtNonOption.
 */
 @Test
 public void testBurstTokenWithInvalidOptionAndNoStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String token = "--non-option--value";
 parser.burstToken(token, false);
 assertEquals(3, parser.tokens.size());
 assertEquals("--non-option", parser.tokens.get(0));
 assertEquals("value", parser.tokens.get(1));
 assertEquals("-", parser.tokens.get(2));
 }
 }