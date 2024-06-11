import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenWithSingleOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-D"};
 String[] expected = {"-D"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithOptionAndValue() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-Dlog", "INFO"};
 String[] expected = {"-D", "log", "INFO"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOption() {
 Options options = new Options();
 options.addOption("longOption", "aLongOption", false, "A long option");
 String[] arguments = {"--longOption"};
 String[] expected = {"--longOption"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithOptionAndArguments() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-D", "path/to/file", "arg1", "arg2"};
 String[] expected = {"-D", "path/to/file", "arg1", "arg2"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithNonOption() {
 Options options = new Options();
 String[] arguments = {"arg1"};
 new GnuParser().flatten(options, arguments, false);
 }

 @Test
 public void testFlattenWithDoubleDash() {
 Options options = new Options();
 String[] arguments = {"--"};
 String[] expected = {"--"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-D", "arg1", "-n", "arg2"};
 String[] expected = {"-D", "arg1"};
 String[] result = new GnuParser().flatten(options, arguments, true);
 Assert.assertArrayEquals(expected, result);
 }
}