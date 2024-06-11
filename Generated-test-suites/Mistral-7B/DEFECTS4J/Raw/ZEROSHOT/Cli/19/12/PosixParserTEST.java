package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

 private static final String[] ARGUMENTS = {"--foo", "-a", "--bar=value", "-b", "optionX", "--baz=value2", "-c", "--", "arg1", "arg2"};

 @Test
 public void testFlatten() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", false, null, "Foo Option");
 options.addOption("bar", true, "value", "Bar Option");
 options.addOption("baz", true, "value2", "Baz Option");
 options.addOption("x", false, null, "Option X");

 String[] expected = {"--foo", "-a", "--bar", "value", "-b", "optionX", "--baz", "value2", "--", "arg1", "arg2"};
 String[] actual = parser.flatten(options, ARGUMENTS, false);
 assertEquals(expected.length, actual.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", false, null, "Foo Option");
 options.addOption("bar", true, "value", "Bar Option");
 options.addOption("baz", true, "value2", "Baz Option");

 String[] arguments = {"--foo", "-a", "--invalidOption", "value", "-b", "--baz", "value2", "--"};
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", false, null, "Foo Option");
 options.addOption("bar", true, "value", "Bar Option");
 options.addOption("baz", true, "value2", "Baz Option");

 String[] expected = {"--foo", "-a", "--", "bar", "value", "baz", "value2", "arg1", "arg2"};
 String[] actual = parser.flatten(options, ARGUMENTS, true);
 assertEquals(expected.length, actual.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }

 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();
 parser.init();
 assertNull(parser.currentOption);
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", false, null, "Foo Option");
 options.addOption("bar", true, "value", "Bar Option");

 parser.processOptionToken("--foo", false);
 assertSame(options.findOption("foo"), parser.currentOption);
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);

 parser.processOptionToken("--bar", false);
 assertNull(parser.currentOption);
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);

 parser.processOptionToken("-b", false);
 assertNull(parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"-b"})));
 assertFalse(parser.eatTheRest);

 parser.processOptionToken("--bar=value", false);
 assertSame(options.findOption("bar"), parser.currentOption);
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);

 parser.processOptionToken("--bar=value", true);
 assertSame(options.findOption("bar"), parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--bar", "value"})));
 assertFalse(parser.eatTheRest);
}

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();

 parser.process("arg1");
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1"})));
 assertFalse(parser.eatTheRest);

 parser.process("--");
 assertFalse(parser.eatTheRest);
 assertFalse(parser.tokens.add("--"));
 assertFalse(parser.tokens.add("arg2"));

 parser.process("arg2");
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1", "arg2"})));
 assertFalse(parser.eatTheRest);

 parser.eatTheRest = true;
 parser.process("arg3");
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1", "arg2", "arg3"})));
 assertFalse(parser.eatTheRest);

 parser.process("arg4");
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1", "arg2", "arg3", "arg4"})));
 assertFalse(parser.eatTheRest);

 parser.eatTheRest = true;
 parser.process("arg5");
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1", "arg2", "arg3", "arg4", "arg5"})));
 assertFalse(parser.eatTheRest);
}

 @Test
 public void testBurstToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", false, null, "Foo Option");
 options.addOption("bar", true, "value", "Bar Option");
 options.addOption("baz", true, "value2", "Baz Option");
 options.addOption("x", false, null, "Option X");

 parser.burstToken("--foo", false);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--foo"})));
 assertNull(parser.currentOption);
 assertFalse(parser.eatTheRest);

 parser.burstToken("-f", false);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"-f"})));
 assertNull(parser.currentOption);
 assertFalse(parser.eatTheRest);

 parser.burstToken("--bar", false);
 assertSame(options.findOption("bar"), parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--bar"})));
 assertFalse(parser.eatTheRest);

 parser.burstToken("bar", false);
 assertNull(parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--bar", "bar"})));
 assertFalse(parser.eatTheRest);

 parser.burstToken("--bar=value", false);
 assertSame(options.findOption("bar"), parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--bar", "value"})));
 assertFalse(parser.eatTheRest);

 parser.burstToken("--bar=value", true);
 assertSame(options.findOption("bar"), parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--bar", "value"})));
 assertFalse(parser.eatTheRest);

 parser.burstToken("--baz", false);
 assertSame(options.findOption("baz"), parser.currentOption);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--baz"})));
 assertFalse(parser.eatTheRest);

 parser.burstToken("baz", false);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--baz", "baz"})));
 assertNull(parser.currentOption);
 assertFalse(parser.eatTheRest);

 parser.burstToken("--", false);
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--", "arg1", "arg2"})));
 assertNull(parser.currentOption);
 assertFalse(parser.eatTheRest);
}
}