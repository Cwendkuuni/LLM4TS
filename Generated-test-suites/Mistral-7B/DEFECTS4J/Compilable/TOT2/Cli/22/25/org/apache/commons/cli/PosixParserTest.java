package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSimpleArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--foo", "--bar=baz"};
 String[] expected = {"--foo", "--bar", "baz"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithoutValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--invalid"};
 String[] expected = {"--invalid"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--invalid=value"};
 String[] expected = {"--invalid", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] arguments = {"--foo"};
 String[] expected = {"--foo"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 String[] arguments2 = {"--foo=true"};
 String[] expected2 = {"--foo", "true"};
 String[] result2 = parser.flatten(options, arguments2, false);
 assertArrayEquals(expected2, result2);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--unknown"};
 String[] expected = {"--unknown"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownLongOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--unknown=value"};
 String[] expected = {"--unknown", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test
 public void testFlattenShortOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 String[] arguments = {"-f"};
 String[] expected = {"-f"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 String[] arguments = {"-f", "value"};
 String[] expected = {"-f", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownShortOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"-x"};
 String[] expected = {"-x"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownShortOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"-x=value"};
 String[] expected = {"-x", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);

 parser.flatten(options, arguments, false); // Exception should be thrown here
 }

 @Test
 public void testFlattenMultipleShortOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 options.addOption("a", true, "a");
 String[] arguments = {"-fa"};
 String[] expected = {"-f", "-a"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleShortOptionsWithValues() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 options.addOption("a", true, "a");
 String[] arguments = {"-fa=value1", "-f=value2"};
 String[] expected = {"-f", "value2", "-a", "value1"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMixedShortAndLongOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 options.addOption("a", true, "a");
 options.addOption("foo", true, null);
 String[] arguments = {"-fa", "--foo"};
 String[] expected = {"-f", "-a", "--foo"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMixedShortAndLongOptionsWithValues() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("f", true, "f");
 options.addOption("a", true, "a");
 options.addOption("foo", true, null);
 String[] arguments = {"-fa=value1", "--foo=value2"};
 String[] expected = {"-f", "-a", "--foo", "value2"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"--foo", "--bar", "-x", "value"};
 String[] expected = {"--foo", "--bar"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }
}