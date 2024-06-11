import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A with boolean value");
 options.addOption("b", 1, "Option B with integer value");
 options.addOption("c", 2, "Option C with integer value");
 options.addOption("d", "default", "Option D with default value");
 options.addOption("e", null, "Option E with no argument");
 }

 @Test
 public void testFlattenSimple() {
 String[] expected = new String[]{"a", "true"};
 args = new String[]{"-a", "--"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenSingleOption() {
 String[] expected = new String[]{"b", "1"};
 args = new String[]{"-b", "1"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenTwoOptions() {
 String[] expected = new String[]{"a", "true", "b", "1"};
 args = new String[]{"-a", "--", "-b", "1"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenOptionWithArg() {
 String[] expected = new String[]{"c", "2", "value"};
 args = new String[]{"-c", "2", "--", "value"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenOptionWithMultipleArgs() {
 String[] expected = new String[]{"c", "2", "value1", "value2"};
 args = new String[]{"-c", "2", "--", "value1", "value2"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenMultipleOptions() {
 String[] expected = new String[]{"a", "true", "b", "1", "c", "2", "d", "default", "e"};
 args = new String[]{"-a", "--", "-b", "1", "-c", "2", "--", "value", "--", "e"};
 String[] actual = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 args = new String[]{"-x", "--"};
 try {
 parser.flatten(options, args, false);
 } catch (ParseException ex) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithValue() {
 args = new String[]{"-x", "value"};
 try {
 parser.flatten(options, args, false);
 } catch (ParseException ex) {
 // expected
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 args = new String[]{"-a", "--", "nonOption"};
 String[] actual = parser.flatten(options, args, true);
 String[] expected = new String[]{"a", "true", "nonOption"};
 Assert.assertArrayEquals(expected, actual);
 }
}