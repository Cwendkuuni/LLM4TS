import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GnuParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 String[] arguments = {"-a", "-b", "--", "c", "d", "e"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"-a", "-b", "--", "c", "d", "e"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithValue() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug info.");
 String[] arguments = {"--D", "debug", "arg1", "arg2", "arg3"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"--", "-D", "debug", "arg1", "arg2", "arg3"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("A", "argument", true, "Specify an argument.");
 String[] arguments = {"--argument=value1", "--argument=value2", "arg"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"--", "-A", "argument=value1", "argument=value2", "arg"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOptionWithValue() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug info.");
 String[] arguments = {"-D", "debug", "arg1", "arg2", "arg3"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"--", "-D", "debug", "arg1", "arg2", "arg3"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMixed() {
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug info.");
 String[] arguments = {"-a", "--D", "debug", "-b", "--", "c", "-c", "arg1", "--argument=value1"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"-a", "--D", "debug", "-b", "--", "c", "-c", "arg1", "--argument=value1"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"-a", "--", "c", "d", "e"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, true);
 String[] expected = {"-a"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleNonOptions() {
 Options options = new Options();
 String[] arguments = {"arg1", "arg2", "arg3"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"arg1", "arg2", "arg3"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithHelpOption() {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints this message.");
 String[] arguments = {"--help"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"--", "-h"};
 Assertions.assertArrayEquals(expected, result);
 }

 @Test
 public void testParseInvalidOption() {
 Options options = new Options();
 String[] arguments = {"--invalid"};
 GnuParser parser = new GnuParser();

 Assertions.assertThrows(ParseException.class, () -> parser.flatten(options, arguments, false));
 }
}