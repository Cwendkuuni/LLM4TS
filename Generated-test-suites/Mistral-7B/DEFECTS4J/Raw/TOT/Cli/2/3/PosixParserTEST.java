import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 @Test
 public void testEmptyArguments() {
 Options options = new Options();
 String[] arguments = {};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[0], flattened);
 }

 @Test
 public void testEmptyArgumentsWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertEquals(new String[0], flattened);
 }

 @Test
 public void testSingleArgument() {
 Options options = new Options();
 options.addOption("a", true, "default option");
 String[] arguments = {"-a"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-a"}, flattened);
 }

 @Test
 public void testSingleArgumentWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "default option");
 String[] arguments = {"-a"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-a"}, flattened);
 }

 @Test
 public void testTwoArguments() {
 Options options = new Options();
 options.addOption("a", true, "default option");
 options.addOption("b", true, "another option");
 String[] arguments = {"-a", "-b"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-a", "-b"}, flattened);
 }

 @Test
 public void testTwoArgumentsWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "default option");
 options.addOption("b", true, "another option");
 String[] arguments = {"-a", "-b"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-a", "-b"}, flattened);
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "description of long option");
 String[] arguments = {"--longOption"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"--longOption"}, flattened);
 }

 @Test
 public void testLongOptionWithValue() {
 Options options = new Options();
 options.addOption("longOption", true, "description of long option");
 String[] arguments = {"--longOption", "value"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"--longOption", "value"}, flattened);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 Options options = new Options();
 String[] arguments = {"--invalid"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testInvalidOptionWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"--invalid"};
 PosixParser parser = new PosixParser();
 assertFalse(parser.flatten(options, arguments, true) instanceof String[]);
 }

 @Test
 public void testDoubleDashOption() {
 Options options = new Options();
 options.addOption("doubleDash", false, "description of double-dash option");
 String[] arguments = {"--doubleDash"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"--doubleDash"}, flattened);
 }

 @Test
 public void testDoubleDashOptionWithValue() {
 Options options = new Options();
 options.addOption("doubleDash", false, "description of double-dash option");
 String[] arguments = {"--doubleDash", "value"};
 PosixParser parser = new PosixParser();
 assertArrayEquals(new String[]{"--doubleDash"}, flattened);
 }

 @Test
 public void testDoubleDashOptionWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("doubleDash", false, "description of double-dash option");
 String[] arguments = {"--doubleDash"};
 PosixParser parser = new PosixParser();
 assertFalse(parser.flatten(options, arguments, true) instanceof String[]);
 }

 @Test
 public void testDoubleDashOptionWithValueAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("doubleDash", false, "description of double-dash option");
 String[] arguments = {"--doubleDash", "value"};
 PosixParser parser = new PosixParser();
 assertFalse(parser.flatten(options, arguments, true) instanceof String[]);
 }

 @Test
 public void testNoOption() {
 Options options = new Options();
 String[] arguments = {"nonOption"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"nonOption"}, flattened);
 }

 @Test
 public void testNoOptionWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"nonOption"};
 PosixParser parser = new PosixParser();
 assertFalse(parser.flatten(options, arguments, true) instanceof String[]);
 }

 @Test
 public void testCustomOptionWithOneValue() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "value"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-c", "value"}, flattened);
 }

 @Test
 public void testCustomOptionWithMultipleValues() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "value1", "value2"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-c", "value1"}, flattened);
 assertEquals(new String[]{"-c", "value2"}, parser.flatten(options, arguments, false));
 }

 @Test
 public void testCustomOptionWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "value1", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertEquals(new String[]{"-c", "value1"}, flattened);
 assertArrayEquals(new String[0], parser.flatten(options, arguments, false));
 }

 @Test
 public void testCustomOptionWithEmptyArgument() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "", "value2"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-c"}, flattened);
 assertEquals(new String[]{"-c", "value2"}, parser.flatten(options, arguments, false));
 }

 @Test
 public void testCustomOptionWithEmptyArgumentAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertEquals(new String[]{"-c"}, flattened);
 assertFalse(parser.flatten(options, arguments, false) instanceof String[]);
 }

 @Test
 public void testCustomOptionWithLongArgument() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "longValue"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, false);
 assertEquals(new String[]{"-c", "longValue"}, flattened);
 }

 @Test
 public void testCustomOptionWithLongArgumentAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("customOption", true, "description of custom option");
 String[] arguments = {"-c", "longValue", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, arguments, true);
 assertEquals(new String[]{"-c", "longValue"}, flattened);
 assertFalse(parser.flatten(options, arguments, false) instanceof String[]);
 }
}