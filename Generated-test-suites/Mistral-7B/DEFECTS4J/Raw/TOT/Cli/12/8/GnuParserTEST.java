import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

 private static final String ARG_1 = "arg1";
 private static final String ARG_2 = "arg2";
 private static final String ARG_3 = "arg3";
 private static final String ARG_4 = "arg4";
 private static final String ARG_5 = "arg5";

 @Test
 public void testFlattenNoOptionsGiven() {
  String[] args = new String[0];
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleCharacterOption() {
  String[] args = {"-D"};
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"-D"}, result);
 }

 @Test
 public void testFlattenSingleCharacterOptionWithArgument() {
  String[] args = {"-Dmyprop=value"};
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"-D", "myprop", "value"}, result);
 }

 @Test
 public void testFlattenMultipleCharacterOption() {
  String[] args = {"--foo"};
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"--foo"}, result);
 }

 @Test
 public void testFlattenMultipleCharacterOptionWithArgument() {
  String[] args = {"--foo=value"};
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"--foo", "value"}, result);
 }

 @Test
 public void testFlattenSingleCharacterOptionWithExistingOption() {
  String[] args = {"-D", "arg1"};
  Options options = new Options().addOption("d", "arg1", true);
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"-D", "arg1"}, result);
 }

 @Test
 public void testFlattenSingleCharacterOptionWithExistingOptionAndArgument() {
  String[] args = {"-Dmyprop=value", "arg1"};
  Options options = new Options().addOption("d", "myprop", true);
  String[] result = new GnuParser().flatten(options, args, false);
  Assert.assertArrayEquals(new String[]{"-D", "myprop", "value"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
  String[] args = {"-X", "arg1"};
  Options options = new Options();
  new GnuParser().flatten(options, args, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
  String[] args = {"-a", "arg1", "-b", "arg2", "nonOption"};
  Options options = new Options();
  String[] result = new GnuParser().flatten(options, args, true);
  String[] expected = {"-a", "arg1", "-b", "arg2"};
  Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testHelpFormatter() {
  String[] args = {"--help"};
  Options options = new Options();
  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp("Test", "Test program", options, true);
 }
}