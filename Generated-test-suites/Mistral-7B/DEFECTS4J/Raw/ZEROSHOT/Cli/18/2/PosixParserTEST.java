import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 String[] args = {};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSimpleArgs() {
 String[] args = {"--option1", "-o", "value1", "--option2", "-v"};
 Options options = new Options();
 options.addOption("option1", true, "o", "option for option1");
 options.addOption("option2", true, "v", "option for option2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--option1", "true", "--option2", "true"}, result);
 }

 @Test
 public void testFlattenLongArgs() {
 String[] args = {"--long-option-name-that-can-have-an-argument", "value", "--option2", "-o", "value2"};
 Options options = new Options();
 options.addOption("longOptionNameThatCanHaveAnArgument", true, null, "long option name that can have an argument");
 options.addOption("option2", true, "o", "option for option2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-name-that-can-have-an-argument", "value", "--option2", "true"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 String[] args = {"--invalidOption", "value"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 fail("Expected ParseException but got " + Arrays.toString(result));
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 String[] args = {"--long-option-name-that-can-have-an-argument=value"};
 Options options = new Options();
 options.addOption("longOptionNameThatCanHaveAnArgument", true, null, "long option name that can have an argument");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-name-that-can-have-an-argument", "value"}, result);
 }

 @Test
 public void testFlattenBurstedOption() {
 String[] args = {"--long-option-th-that-can-have-an-argument"};
 Options options = new Options();
 options.addOption("longOptionThThatCanHaveAnArgument", true, null, "long option that can have an argument");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-th-that-can-have-an-argument"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithValue() {
 String[] args = {"--long-option-th-that-can-have-an-argument=value"};
 Options options = new Options();
 options.addOption("longOptionThThatCanHaveAnArgument", true, null, "long option that can have an argument");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-th-that-can-have-an-argument", "value"}, result);
 }

 @Test
 public void testFlattenMultipleBurstedOptions() {
 String[] args = {"--option1=value1", "--long-op--that-can-have-an-arg", "--option2=value2"};
 Options options = new Options();
 options.addOption("option1", true, null, "option1");
 options.addOption("longOp--that-can-have-arg", true, null, "long option that can have an arg");
 options.addOption("option2", true, null, "option2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--option1", "value1", "--long-op--that-can-have-arg", "--option2", "value2"}, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 String[] args = {"--option1", "--", "nonOption"};
 Options options = new Options();
 options.addOption("option1", true, null, "option1");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--option1", "--", "nonOption"}, result);
 }
}