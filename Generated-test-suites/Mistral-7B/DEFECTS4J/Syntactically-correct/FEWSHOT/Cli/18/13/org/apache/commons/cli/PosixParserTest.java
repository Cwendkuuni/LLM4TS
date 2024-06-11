package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

 private static final String[] ARGUMENTS = {"--foo", "--bar=value", "--baz=value1,value2", "-f", "--", "-a", "-b", "-c=value", "--qux", "--quux"};

 @Test
 public void testFlatten() {
 PosixParser parser = new PosixParser();
 String[] results = parser.flatten(new Options(), ARGUMENTS, false);

 assertEquals(ARGUMENTS.length, results.length);
 for (int i = 0; i < ARGUMENTS.length; i++) {
 assertEquals(ARGUMENTS[i], results[i]);
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] results = parser.flatten(new Options(), ARGUMENTS, true);

 assertArrayEquals(new String[]{"--foo", "--bar=value", "--baz=value1,value2", "-f", "-a", "-b", "--qux", "--quux"}, results);
 }

 @Test
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();

 String[] results = parser.flatten(new Options(), new String[]{"-"}, false);
 assertEquals(1, results.length);
 assertEquals("-", results[0]);
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", false, null, "optional-flag");
 options.addOption("a", true, "num", "an-option");

 String[] arguments = new String[]{"-f", "-a", "1"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(3, results.length);
 assertEquals("-f", results[0]);
 assertEquals("-a", results[1]);
 assertEquals("1", results[2]);
 }

 @Test
 public void testProcessOptionTokenWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("b", true, null, "a-flag");
 options.addOption("z", null, null, "a-long-option");

 String[] arguments = new String[]{"--b", "--z"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(2, results.length);
 assertEquals("--b", results[0]);
 assertEquals("--z", results[1]);
 }

 @Test
 public void testProcessOptionTokenWithValueLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("z", null, null, "a-long-option");

 String[] arguments = new String[]{"--z", "value"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(2, results.length);
 assertEquals("--z", results[0]);
 assertEquals("value", results[1]);
 }

 @Test
 public void testBurstToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", false, null, "optional-flag");
 options.addOption("a", true, null, "a-option");
 options.addOption("l", null, null, "long-option");

 String[] arguments = new String[]{"-fl", "1", "2"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(4, results.length);
 assertEquals("-f", results[0]);
 assertEquals("-l", results[1]);
 assertEquals("1", results[2]);
 assertEquals("2", results[3]);
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", false, null, "optional-flag");
 options.addOption("a", true, null, "a-option");
 options.addOption("l", null, null, "long-option");

 String[] arguments = new String[]{"-fl", "1", "2", "--non-option"};
 String[] results = parser.flatten(options, arguments, true);

 assertEquals(5, results.length);
 assertEquals("-fl", results[0]);
 assertEquals("--non-option", results[3]);
 assertEquals("1", results[4]);
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();

 String[] arguments = new String[]{"--foo", "--bar=value", "--baz=value1,value2", "-f", "--", "-a", "-b", "-c=value", "--qux", "--quux"};
 String[] results = parser.flatten(new Options(), arguments, false);

 String[] expected = new String[]{"--foo", "--bar=value", "--baz=value1,value2", "--", "-a", "-b", "--qux", "--quux"};
 assertArrayEquals(expected, results);
 }

 @Test
 public void testProcessCurrentOptionHasArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, null, "an-option");
 options.addOption("b", true, null, "another-option");

 String[] arguments = new String[]{"-a", "--value"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(3, results.length);
 assertEquals("-a", results[0]);
 assertEquals("--value", results[1]);
 assertEquals(null, results[2]);
 }

 @Test
 public void testProcessCurrentOptionHasArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("b", null, null, "another-option");

 String[] arguments = new String[]{"-a", "--value1", "--value2"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(5, results.length);
 assertEquals("-a", results[0]);
 assertEquals("--value1", results[1]);
 assertEquals("--value2", results[2]);
 assertEquals(null, results[3]);
 assertEquals(null, results[4]);
 }

 @Test
 public void testProcessNoCurrentOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("b", null, null, "another-option");

 String[] arguments = new String[]{"--foo", "--bar=value"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(3, results.length);
 assertEquals("--foo", results[0]);
 assertEquals("--bar=value", results[1]);
 assertEquals(null, results[2]);
 }

 @Test
 public void testProcessNoCurrentOptionStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("b", null, null, "another-option");

 String[] arguments = new String[]{"--foo", "--bar=value", "--non-option"};
 String[] results = parser.flatten(options, arguments, true);

 assertEquals(4, results.length);
 assertEquals("--foo", results[0]);
 assertEquals("--bar=value", results[1]);
 assertEquals("--non-option", results[2]);
 assertEquals(null, results[3]);
 }

 @Test
 public void testBurstTokenWithLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("l", null, null, "a-long-option");

 String[] arguments = new String[]{"-al", "value1", "value2"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(4, results.length);
 assertEquals("-a", results[0]);
 assertEquals("--", results[1]);
 assertEquals("value1", results[2]);
 assertEquals("value2", results[3]);
 }

 @Test
 public void testBurstTokenWithLongOptionStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("l", null, null, "a-long-option");

 String[] arguments = new String[]{"-al", "value1", "value2", "--non-option"};
 String[] results = parser.flatten(options, arguments, true);

 assertEquals(5, results.length);
 assertEquals("-al", results[0]);
 assertEquals("--non-option", results[3]);
 assertEquals("value1", results[4]);
 }

 @Test
 public void testBurstTokenWithOptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("o", true, null, "an-option-with-arg");

 String[] arguments = new String[]{"-ao", "value"};
 String[] results = parser.flatten(options, arguments, false);

 assertEquals(3, results.length);
 assertEquals("-a", results[0]);
 assertEquals("-o", results[1]);
 assertEquals("value", results[2]);
 }

 @Test
 public void testBurstTokenWithOptionWithArgStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", null, null, "an-option");
 options.addOption("o", true, null, "an-option-with-arg");

 String[] arguments = new String[]{"-ao", "value", "--non-option"};
 String[] results = parser.flatten(options, arguments, true);

 assertEquals(4, results.length);
 assertEquals("-ao", results[0]);
 assertEquals("--non-option", results[3]);
 assertEquals("value", results[1]);
 assertEquals(null, results[2]);
 }
}