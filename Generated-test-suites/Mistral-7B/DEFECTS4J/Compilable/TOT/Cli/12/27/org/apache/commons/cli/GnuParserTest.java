package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenWithOptionsAndValues() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information.");
 String[] arguments = {"-D", "debug", "--foo", "value", "--", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "debug", "--", "foo", "value", "arg1", "arg2"}, flattened);
 }

 @Test
 public void testFlattenWithShortOptionsAndValues() {
 Options options = new Options();
 options.addOption("d", "debug", true, "Print debug information.");
 String[] arguments = {"-d", "true", "--foo", "value", "--", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-d", "true", "--", "foo", "value", "arg1", "arg2"}, flattened);
 }

 @Test
 public void testFlattenWithLongOptionsAndValues() {
 Options options = new Options();
 options.addOption("debug", true, "Print debug information.");
 String[] arguments = {"--debug", "true", "--foo", "value", "--", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--debug", "true", "--", "foo", "value", "arg1", "arg2"}, flattened);
 }

 @Test
 public void testFlattenWithOptionAndArgument() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information.");
 String[] arguments = {"-D", "debug", "arg"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "debug", "arg"}, flattened);
 }

 @Test
 public void testFlattenWithLongOptionAndArgument() {
 Options options = new Options();
 options.addOption("debug", true, "Print debug information.");
 String[] arguments = {"--debug", "arg"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--debug", "arg"}, flattened);
 }

 @Test
 public void testFlattenWithNoOptions() {
 String[] arguments = {"arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(null, arguments, false);
 assertArrayEquals(new String[]{"arg1", "arg2"}, flattened);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithIllegalOption() {
 Options options = new Options();
 String[] arguments = {"--illegalOption", "arg"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFlattenWithMissingArgument() {
 Options options = new Options();
 String[] arguments = {"-D", "debug"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }
}