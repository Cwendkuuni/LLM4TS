import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private static final String[] ARGS = {"--foo", "--bar=baz", "-a", "-b", "-c", "--", "-d", "--foo=qux"};

 @Test
 public void testFlatten() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, ARGS, false);

 assertEquals(ARGS.length, result.length);
 for (int i = 0; i < ARGS.length; i++) {
 assertEquals(ARGS[i], result[i]);
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c"};
 String[] result = parser.flatten(options, ARGS, true);

 assertEquals(args.length + 1, result.length);
 assertEquals("--", result[args.length]);
 }

 @Test
 public void testProcessOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);

 parser.init();
 parser.processOptionToken("--a", false);

 assertSame(option, parser.currentOption);
 assertNull(parser.currentOption.getValue());
 assertEquals(1, parser.tokens.size());
 assertEquals("--a", parser.tokens.get(0));
 }

 @Test
 public void testProcessOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);

 parser.init();
 parser.processOptionToken("--a=value", false);

 assertSame(option, parser.currentOption);
 assertEquals("value", parser.currentOption.getValue());
 assertEquals(2, parser.tokens.size());
 assertEquals("--a", parser.tokens.get(0));
 assertEquals("value", parser.tokens.get(1));
 }

 @Test
 public void testProcessLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("foo", "fooOption");
 option.hasArg(false);
 options.addOption(option);

 parser.init();
 parser.process("--foo", false);

 assertNull(parser.currentOption);
 assertEquals(1, parser.tokens.size());
 assertEquals("--foo", parser.tokens.get(0));
 }

 @Test
 public void testProcessLongOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("foo", "fooOption");
 option.hasArg(false);
 options.addOption(option);

 parser.init();
 parser.process("--foo=value", false);

 assertNull(parser.currentOption);
 assertEquals(1, parser.tokens.size());
 assertEquals("--foo=value", parser.tokens.get(0));
 }

 @Test
 public void testProcessStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);

 parser.init();
 parser.process("--", true);

 assertFalse(parser.eatTheRest);
 assertEquals(1, parser.tokens.size());
 assertEquals("--", parser.tokens.get(0));
 }

 @Test
 public void testProcessStopAtNonOptionWithArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);

 parser.init();
 parser.process("--", true);
 parser.process("arg1", true);
 parser.process("arg2", true);
 parser.process("arg3", true);

 assertTrue(parser.eatTheRest);
 assertEquals(5, parser.tokens.size());
 assertEquals("--", parser.tokens.get(0));
 assertEquals("arg1", parser.tokens.get(1));
 assertEquals("arg2", parser.tokens.get(2));
 assertEquals("arg3", parser.tokens.get(3));
 assertEquals("--", parser.tokens.get(4));
 }

 @Test(expected = ParseException.class)
 public void testProcessInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);

 parser.init();
 parser.processOptionToken("--invalid", false);
 }

 @Test
 public void testBurstToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);
 Option option2 = new Option("b", "arg2");
 option2.hasArg(true);
 options.addOption(option2);

 parser.init();
 parser.burstToken("-a--b", false);

 assertEquals(2, parser.tokens.size());
 assertEquals("-a", parser.tokens.get(0));
 assertEquals("-b", parser.tokens.get(1));
 }

 @Test
 public void testBurstTokenWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);
 Option option2 = new Option("b", "arg2");
 option2.hasArg(true);
 options.addOption(option2);

 parser.init();
 parser.burstToken("-a--b=value", false);

 assertEquals(3, parser.tokens.size());
 assertEquals("-a", parser.tokens.get(0));
 assertEquals("-b=value", parser.tokens.get(1));
 assertEquals("value", parser.tokens.get(2));
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);
 Option option2 = new Option("b", "arg2");
 option2.hasArg(true);
 options.addOption(option2);

 parser.init();
 parser.burstToken("-a--b", true);

 assertEquals(3, parser.tokens.size());
 assertEquals("--", parser.tokens.get(0));
 assertEquals("-b", parser.tokens.get(1));
 assertEquals("--", parser.tokens.get(2));
 }

 @Test
 public void testBurstTokenWithStopAtNonOptionAndArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("a", "arg");
 option.hasArg(true);
 options.addOption(option);
 Option option2 = new Option("b", "arg2");
 option2.hasArg(true);
 options.addOption(option2);

 parser.init();
 parser.burstToken("-a--b=value", true);

 assertEquals(4, parser.tokens.size());
 assertEquals("--", parser.tokens.get(0));
 assertEquals("--", parser.tokens.get(1));
 assertEquals("-b=value", parser.tokens.get(2));
 assertEquals("value", parser.tokens.get(3));
 }
}