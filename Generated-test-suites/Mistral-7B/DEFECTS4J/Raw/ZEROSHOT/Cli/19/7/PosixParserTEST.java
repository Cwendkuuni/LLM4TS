import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "An option with an argument");

 String[] arguments = new String[]{"-o", "arg", "--arg2=value", "arg3"};
 String[] expected = new String[]{"-o", "arg", "--arg2", "value"};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenNoArgument() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option");

 String[] arguments = new String[]{"-o", "--arg2=value", "arg3"};
 String[] expected = new String[]{"-o", "--arg2"};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option1", true);
 options.addOption("a", "option2");
 options.addOption("l", "option3");

 String[] arguments = new String[]{"-o", "arg1", "--option2", "-l", "arg3", "--", "arg4", "arg5"};
 String[] expected = new String[]{"-o", "arg1", "--option2", "-l", "arg3", "--", "arg4", "arg5"};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleArguments() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true);

 String[] arguments = new String[]{"-o", "arg1", "--option=value1", "arg2", "--option=value2", "arg3"};
 String[] expected = new String[]{"-o", "arg1", "--option", "value1", "arg2", "--option", "value2", "arg3"};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option");

 String[] arguments = new String[]{"-z", "arg1"}; // Invalid option 'z'
 String[] result = new PosixParser().flatten(options, arguments, false);

 fail("Expected ParseException but no exception was thrown");
 }

 @Test
 public void testFlattenStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option");

 String[] arguments = new String[]{"-o", "--arg2=value", "arg3", "invalidOption"};
 String[] expected = new String[]{"-o", "--arg2=value", "arg3"};
 String[] result = new PosixParser().flatten(options, arguments, true);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenEmptyArguments() throws ParseException {
 Options options = new Options();

 String[] arguments = new String[]{};
 String[] expected = new String[]{};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenNullArguments() throws ParseException {
 Options options = new Options();

 String[] arguments = null;
 String[] expected = new String[]{};
 String[] result = new PosixParser().flatten(options, arguments, false);

 Assert.assertArrayEquals(expected, result);
 }
}