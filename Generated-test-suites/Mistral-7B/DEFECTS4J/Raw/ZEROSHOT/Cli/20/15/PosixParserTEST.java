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
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with a string value");
 options.addOption("d", 3, "Option D with a long value");
 }

 @After
 public void tearDown() {
 options = null;
 args = null;
 }

 @Test
 public void testFlattenEmpty() {
 String[] result = PosixParser.getInstance().flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleOption() {
 args = new String[]{"-a"};
 String[] result = PosixParser.getInstance().flatten(options, args, false);
 assertEquals(1, result.length);
 assertEquals("-a", result[0]);
 }

 @Test
 public void testFlattenOptionWithValue() {
 args = new String[]{"-a", "value"};
 String[] result = PosixParser.getInstance().flatten(options, args, false);
 assertEquals(2, result.length);
 assertEquals("-a", result[0]);
 assertEquals("value", result[1]);
 }

 @Test
 public void testFlattenLongOption() {
 args = new String[]{"--option-a", "value"};
 String[] result = PosixParser.getInstance().flatten(options, args, false);
 assertEquals(2, result.length);
 assertEquals("--option-a", result[0]);
 assertEquals("value", result[1]);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 args = new String[]{"--option-a", "value1", "value2"};
 String[] result = PosixParser.getInstance().flatten(options, args, false);
 assertEquals(3, result.length);
 assertEquals("--option-a", result[0]);
 assertEquals("value1", result[1]);
 assertEquals("value2", result[2]);
 }

 @Test
 public void testFlattenMultipleOptions() {
 args = new String[]{"-a", "-b", "-d", "2", "-c", "value", "--", "value2"};
 String[] result = PosixParser.getInstance().flatten(options, args, false);
 assertEquals(8, result.length);
 assertEquals("-a", result[0]);
 assertEquals("-b", result[1]);
 assertEquals("-d", result[2]);
 assertEquals("2", result[3]);
 assertEquals("-c", result[4]);
 assertEquals("value", result[5]);
 assertEquals("--", result[6]);
 assertEquals("value2", result[7]);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 args = new String[]{"-x", "-a", "-b", "-d", "2", "-c", "value", "--", "value2"};
 String[] result = null;
 try {
 result = PosixParser.getInstance().flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 assertEquals(7, args.length);
 assertEquals("-x", args[0]);
 assertEquals("-a", args[1]);
 assertEquals("-b", args[2]);
 assertEquals("-d", args[3]);
 assertEquals("2", args[4]);
 assertEquals("-c", args[5]);
 assertEquals("value", args[6]);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 args = new String[]{"--", "-a", "-b", "-d", "2", "-c", "value", "--value"};
 String[] result = PosixParser.getInstance().flatten(options, args, true);
 assertEquals(5, result.length);
 assertEquals("--", result[0]);
 assertEquals("-a", result[1]);
 assertEquals("-b", result[2]);
 assertEquals("-d", result[3]);
 assertEquals("2", result[4]);
 }
}

class PosixParser {

 private static PosixParser parser = new PosixParser();

 static PosixParser getInstance() {
 return parser;
 }

 // All methods from PosixParser class here
}