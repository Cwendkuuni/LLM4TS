package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with single options and no stopAtNonOption
 */
 @Test
 public void testFlattenSingleOptions() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 String[] arguments = {"-D", "debug", "--help"};
 String[] expected = {"-D", "debug", "--", "--help"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with single options and stopAtNonOption
 */
 @Test
 public void testFlattenSingleOptionsStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 String[] arguments = {"-D", "debug", "--help", "nonOption"};
 String[] expected = {"-D", "debug"};
 String[] result = new GnuParser().flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with two options and no stopAtNonOption
 */
 @Test
 public void testFlattenTwoOptions() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 String[] arguments = {"-Ddebug", "--help"};
 String[] expected = {"-D", "debug", "--", "--help"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with two options and stopAtNonOption
 */
 @Test
 public void testFlattenTwoOptionsStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 String[] arguments = {"-Ddebug", "--help", "nonOption"};
 String[] expected = {"-D", "debug"};
 String[] result = new GnuParser().flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with long option
 */
 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("a", "append", true, "append to existing file.");
 String[] arguments = {"--append"};
 String[] expected = {"--", "--append"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
 * with long option and stopAtNonOption
 */
 @Test(expected = ParseException.class)
 public void testFlattenLongOptionStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", "append", true, "append to existing file.");
 String[] arguments = {"--append", "nonOption"};
 new GnuParser().flatten(options, arguments, true);
 }
}