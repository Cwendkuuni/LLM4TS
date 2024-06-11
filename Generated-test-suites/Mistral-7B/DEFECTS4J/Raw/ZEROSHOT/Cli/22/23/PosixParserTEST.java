import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a positional argument and an integer argument");
 }

 @Test
 public void testFlatten_Simple() throws ParseException {
 String[] args = { "--a", "1", "2" };
 String[] expected = { "-a", "-1", "-2" };
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_LongOptions() throws ParseException {
 String[] args = { "--option-a", "value-a", "--option-b=value-b" };
 String[] expected = { "--option-a", "value-a", "--option-b", "value-b" };
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_Mixed() throws ParseException {
 String[] args = { "-a", "--option-b=value-b", "-c", "arg1", "arg2" };
 String[] expected = { "-a", "-c", "--option-b", "arg1", "arg2" };
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption() throws ParseException {
 String[] args = { "-a", "--not-an-option", "-c", "arg1", "arg2" };
 String[] expected = { "-a", "--not-an-option", "-c", "arg1", "arg2" };
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_Bursting() throws ParseException {
 String[] args = { "-abc" };
 String[] expected = { "-a", "-b", "-c" };
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() throws ParseException {
 String[] args = { "--invalid-option" };
 String[] expected = { "--invalid-option" };
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 Assert.assertEquals("Unrecognized option: invalid-option", parser.getParseException().getMessage());
 }

 @Test
 public void testBurstToken() {
 String token = "-abc";
 String[] expected = { "-a", "-b", "-c" };
 List<String> result = parser.burstToken(token, false);
 assertArrayEquals(expected.toArray(), result.toArray(new String[0]));
 }

 @Test
 public void testBurstToken_StopAtNonOption() {
 String token = "-abcd";
 String[] expected = { "--", "abcd" };
 List<String> result = parser.burstToken(token, true);
 assertArrayEquals(expected, result.toArray(new String[0]));
 }

 private PosixParser parser() {
 if (parser == null) {
 parser = new PosixParser();
 }
 return parser;
 }
}