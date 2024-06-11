import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with two integers arguments");
 }

 @After
 public void tearDown() {
 options = null;
 args = null;
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 String[] result = new PosixParser().flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSingleArg() throws ParseException {
 args = new String[]{"--foo"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo"}, result);
 }

 @Test
 public void testSingleShortOption() throws ParseException {
 args = new String[]{"-a"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("-a", result[0]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 1, result.length));
 }

 @Test
 public void testSingleLongOption() throws ParseException {
 args = new String[]{"--optionA"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("--optionA", result[0]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 1, result.length));
 }

 @Test
 public void testShortOptionWithArg() throws ParseException {
 args = new String[]{"-a", "argA"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("-a", result[0]);
 assertEquals("argA", result[1]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 2, result.length));
 }

 @Test
 public void testLongOptionWithArg() throws ParseException {
 args = new String[]{"--optionA", "argA"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("--optionA", result[0]);
 assertEquals("argA", result[1]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 2, result.length));
 }

 @Test
 public void testMultipleShortOptions() throws ParseException {
 args = new String[]{"-a", "-b", "argB"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("-a", result[0]);
 assertEquals(false, result[1].equals("--"));
 assertEquals("-b", result[2]);
 assertEquals("argB", result[3]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 4, result.length));
 }

 @Test
 public void testMultipleLongOptions() throws ParseException {
 args = new String[]{"--optionB", "--optionC", "argC1", "argC2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("--optionB", result[0]);
 assertEquals(false, result[1].equals("--"));
 assertEquals("--optionC", result[2]);
 assertEquals("argC1", result[3]);
 assertEquals("argC2", result[4]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 5, result.length));
 }

 @Test
 public void testBursting() throws ParseException {
 args = new String[]{"-abc"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals("-a", result[0]);
 assertEquals("-b", result[1]);
 assertEquals("-c", result[2]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 3, result.length));
 }

 @Test (expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 args = new String[]{"--invalid"};
 new PosixParser().flatten(options, args, false);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 args = new String[]{"-a", "--nonOption", "-b"};
 options.addOption("nonOption", false, "NonOption");
 String[] result = new PosixParser().flatten(options, args, true);
 assertEquals("-a", result[0]);
 assertEquals("--nonOption", result[1]);
 assertEquals("-b", result[2]);
 assertArrayEquals(new String[0], Arrays.copyOfRange(result, 3, result.length));
 }
}