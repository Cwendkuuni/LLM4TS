import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 private static final String TEST_ARGUMENTS[] = {
 "--test", "arg1", "-a", "--optionB", "valueB", "-o", "outputFile.txt",
 "--optionC=valueC", "--", "-d", "dir1", "/path/to/nonexistent", "arg5"
 };

 @Test
 public void testFlattening() throws ParseException {
 // given
 Options options = new Options();
 options.addOption("test", true, "Test option");
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with value");
 options.addOption("c", true, "Option C with value");
 options.addOption("d", true, "Option D");
 options.addRequiredOption("o", true, "Output file");

 PosixParser parser = new PosixParser();

 // when
 String[] parsedArgs = parser.flatten(options, TEST_ARGUMENTS, false);

 // then
 Assert.assertArrayEquals(new String[]{"--test", "arg1", "-a", "--optionB",
 "valueB", "-o", "outputFile.txt", "--optionC=valueC", "--", "-d", "dir1"},
 parsedArgs);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 // given
 Options options = new Options();
 options.addOption("a", true, "Option A");

 PosixParser parser = new PosixParser();

 // when
 parser.flatten(options, new String[]{"--invalid"}, false);

 // then
 // an exception should be thrown
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 // given
 Options options = new Options();
 options.addOption("o", true, "Output file");
 options.addRequiredOption("a", true, "Argument A");

 PosixParser parser = new PosixParser();

 // when
 parser.flatten(options, new String[]{}, false);

 // then
 // an exception should be thrown
 }

 @Test
 public void testBursting() throws ParseException {
 // given
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");
 options.addOption("d", true, "Option D");
 options.addOption("e", true, "Option E");
 options.addOption("f", true, "Option F");

 PosixParser parser = new PosixParser();

 // when
 String[] parsedArgs = parser.flatten(options, new String[]{"-abcd--efg"}, false);

 // then
 Assert.assertArrayEquals(new String[]{"-a", "--optionB", "-c", "--optionD"},
 parsedArgs);
 }
}