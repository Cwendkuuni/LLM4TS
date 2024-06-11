import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 3, "Option C with an optional argument");
 }

 @Test
 public void testEmpty() {
 String[] expected = {};
 parser = new PosixParser();
 String[] result = parser.flatten(options, new String[0], false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSimpleOption() {
 String[] args = new String[]{"--a"};
 String[] expected = {"--a"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testLongOptionWithArgument() {
 String[] args = new String[]{"--b", "value"};
 String[] expected = {"--b", "value"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidLongOption() {
 String[] args = new String[]{"--z"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--z"}, result); // should throw ParseException
 }

 @Test
 public void testShortOption() {
 String[] args = new String[]{"-a"};
 String[] expected = {"-a"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testShortOptionWithArgument() {
 String[] args = new String[]{"-b", "value"};
 String[] expected = {"-b", "value"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidShortOption() {
 String[] args = new String[]{"-z"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-z"}, result); // should throw ParseException
 }

 @Test
 public void testBursting() {
 String[] args = new String[]{"-abc"};
 String[] expected = {"-a", "-b", "-c"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingWithArgument() {
 String[] args = new String[]{"-ac", "value"};
 String[] expected = {"-a", "-c", "value"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testBurstingWithInvalidOption() {
 String[] args = new String[]{"-zc"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-zc"}, result); // should throw ParseException
 }

 @Test
 public void testStopAtNonOption() {
 options.addRequiredOption("a", true, "Option A is required");
 String[] args = new String[]{"foo", "bar", "--b", "value"};
 String[] expected = {"--b", "value"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testStopAtNonOptionWithLongOption() {
 options.addRequiredOption("a", true, "Option A is required");
 String[] args = new String[]{"--foo", "--b", "value"};
 String[] expected = {"--b", "value"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOptionWithInvalidOption() {
 options.addRequiredOption("a", true, "Option A is required");
 String[] args = new String[]{"--z"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"--z"}, result); // should throw ParseException
 }
}