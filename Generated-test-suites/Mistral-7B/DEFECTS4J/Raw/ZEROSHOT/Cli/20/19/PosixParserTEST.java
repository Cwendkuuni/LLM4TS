import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 Options options = new Options();
 String[] args = {};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a"}, result);
 }

 @Test
 public void testFlattenSingleValueOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "value"}, result);
 }

 @Test
 public void testFlattenLongValueOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a", "long value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "long value"}, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] args = {"--a", "--b"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "--b"}, result);
 }

 @Test
 public void testFlattenMultipleValuesOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a", "value1", "--a", "value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "value1", "--a", "value2"}, result);
 }

 @Test
 public void testFlattenLongValuesOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a", "long value1", "--a", "long value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "long value1", "--a", "long value2"}, result);
 }

 @Test
 public void testFlattenOptionWithArgument() {
 Options options = new Options();
 options.addOption("a", 1, "Option A with argument");
 String[] args = {"--a", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "value"}, result);
 }

 @Test
 public void testFlattenOptionWithArgumentValueTooLong() {
 Options options = new Options();
 options.addOption("a", 1, "Option A with argument");
 String[] args = {"--a", "long value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "long value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithArgument() {
 Options options = new Options();
 options.addOption("a", 1, "Option A with argument");
 String[] args = {"--a value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--a", "value"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] args = {"--invalid"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--a", "value", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"--a", "value", "arg1", "arg2"}, result);
 }

 @Test
 public void testBurstTokenSingleOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false)[0];
 assertEquals("-a", result[0]);
 assertTrue( Arrays.asList(result).subList(1, result.length).containsAll(Arrays.asList("-a")));
 }

 @Test
 public void testBurstTokenLongOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a--long"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false)[0];
 assertEquals("-a--long", result[0]);
 assertTrue( Arrays.asList(result).subList(1, result.length).containsAll(Arrays.asList("-a", "--long")));
 }

 @Test
 public void testBurstTokenOptionWithArgument() {
 Options options = new Options();
 options.addOption("a", 1, "Option A with argument");
 String[] args = {"-a", "arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false)[0];
 assertEquals("-a", result[0]);
 assertTrue( Arrays.asList(result).subList(1, result.length).containsAll(Arrays.asList("-a", "arg")));
 }

 @Test
 public void testBurstTokenInvalidToken() {
 Options options = new Options();
 String[] args = {"-invalid"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 assertFalse(parser.eatTheRest);
 assertNull(parser.currentOption);
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, true);
 assertTrue(parser.eatTheRest);
 assertNull(parser.currentOption);
 }
}