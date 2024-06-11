package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GnuParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 Options options = new Options();
 String[] arguments = new String[0];
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-D"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D"}, result);
 }

 @Test
 public void testFlattenSingleArgument() {
 Options options = new Options();
 String[] arguments = new String[]{"arg"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"arg"}, result);
 }

 @Test
 public void testFlattenDoubleOption() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 options.addOption("V", "verbose", true, "Set verbose mode");
 String[] arguments = new String[]{"-D", "-V"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "-V"}, result);
 }

 @Test
 public void testFlattenOptionWithArg() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-D", "test"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "test"}, result);
 }

 @Test
 public void testFlattenOptionWithArgSingleDash() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-Dtest"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "test"}, result);
 }

 @Test
 public void testFlattenOptionWithArgMultipleDashes() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"--Dtest"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--D", "test"}, result);
 }

 @Test
 public void testFlattenSpecialOption() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-Dproperty=value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "property=value"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-I"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-D", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-D", "arg1"}, result);
 }

 @Test
 public void testFlattenStopAtNonOptionTwoArgs() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-D", "arg1", "arg2", "arg3"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-D", "arg1"}, result);
 }

 @Test
 public void testFlattenStopAtNonOptionThreeArgs() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Set debug mode");
 String[] arguments = new String[]{"-D", "arg1", "arg2", "arg3", "arg4"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-D", "arg1"}, result);
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 String[] arguments = new String[]{"--"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--"}, result);
 }

 @Test
 public void testFlattenDoubleDashWithArg() {
 Options options = new Options();
 String[] arguments = new String[]{"--arg"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--arg"}, result);
 }
}