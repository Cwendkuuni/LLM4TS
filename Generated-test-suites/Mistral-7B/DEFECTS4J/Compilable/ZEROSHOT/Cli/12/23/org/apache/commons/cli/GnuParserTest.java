package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GnuParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 Options options = new Options();
 String[] arguments = {};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertNull(result);
 }

 @Test
 public void testFlattenSimpleArgs() {
 Options options = new Options();
 String[] arguments = {"A", "B", "C"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(3, result.length);
 assertEquals("-", result[0]);
 assertEquals("A", result[1]);
 assertEquals("B", result[2]);
 assertEquals("C", result[3]);
 }

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 String[] arguments = {"-A"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(1, result.length);
 assertEquals("-A", result[0]);
 }

 @Test
 public void testFlattenOptionWithValue() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 String[] arguments = {"-A", "value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(2, result.length);
 assertEquals("-A", result[0]);
 assertEquals("value", result[1]);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
 Options options = new Options();
 String[] arguments = {"-X", "Y"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertNull(result);
 }

 @Test
 public void testFlattenUnknownOptionWithValue() {
 Options options = new Options();
 String[] arguments = {"-X", "Y", "value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertFalse(result == null);
 assertEquals(3, result.length);
 assertEquals("-X", result[0]);
 assertEquals("Y", result[1]);
 assertEquals("value", result[2]);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 options.addOption("B", true, "b option");
 String[] arguments = {"-A", "1", "-B", "2"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(4, result.length);
 assertEquals("-A", result[0]);
 assertEquals("1", result[1]);
 assertEquals("-B", result[2]);
 assertEquals("2", result[3]);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "long option");
 String[] arguments = {"--longOption"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(1, result.length);
 assertEquals("--longOption", result[0]);
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 String[] arguments = {"--"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(1, result.length);
 assertEquals("--", result[0]);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 String[] arguments = {"-A", "value", "nonOption"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(2, result.length);
 assertEquals("-A", result[0]);
 assertEquals("value", result[1]);
 }

 @Test
 public void testFlattenStopAtNonOptionWithLongOption() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 options.addOption("B", true, "b option");
 String[] arguments = {"--", "-B", "value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(1, result.length);
 assertEquals("--", result[0]);
 }

 @Test
 public void testFlattenStopAtNonOptionWithNoArguments() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 String[] arguments = {"--"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertNull(result);
 }

 @Test
 public void testFlattenStopAtNonOptionWithLongAndShortOptions() {
 Options options = new Options();
 options.addOption("A", true, "a option");
 options.addOption("B", true, "b option");
 String[] arguments = {"--", "-B", "value", "-A"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(2, result.length);
 assertEquals("-B", result[0]);
 assertEquals("value", result[1]);
 }
}