import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private String[] expectedTokens;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with argument");
 options.addOption("d", 3, "Option D with arguments");
 }

 @After
 public void tearDown() {
 args = null;
 options = null;
 }

 @Test
 public void testEmptyArgs() {
 String[] result = new PosixParser().flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSingleArg() {
 args = new String[]{"arg"};
 expectedTokens = new String[]{"arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testDoubleHyphenArg() {
 args = new String[]{"--arg"};
 expectedTokens = new String[]{"--arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testSingleOption() {
 args = new String[]{"-a"};
 expectedTokens = new String[]{"-a"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testShortOptionWithArg() {
 args = new String[]{"-a", "arg"};
 expectedTokens = new String[]{"-a", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testLongOptionWithArg() {
 args = new String[]{"--option-b", "arg"};
 expectedTokens = new String[]{"--option-b", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testLongOptionWithMultipleArgs() {
 args = new String[]{"--option-d", "arg1", "arg2"};
 expectedTokens = new String[]{"--option-d", "arg1", "arg2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testLongOptionWithNoArg() {
 args = new String[]{"--option-a"};
 expectedTokens = new String[]{"--option-a"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() {
 args = new String[]{"-z", "value"};
 new PosixParser().flatten(options, args, false);
 }

 @Test
 public void testStopAtNonOption() {
 args = new String[]{"-a", "-b", "-c", "nonOption"};
 expectedTokens = new String[]{"-a", "-b", "-c"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testBurstingOption() {
 args = new String[]{"-abc"};
 expectedTokens = new String[]{"-a", "-b", "-c"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testBurstingOptionWithArg() {
 args = new String[]{"-ac"};
 expectedTokens = new String[]{"-a", "c"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test
 public void testBurstingOptionWithMultipleArgs() {
 args = new String[]{"-abcdef"};
 expectedTokens = new String[]{"-a", "-b", "-c", "-d", "-e", "-f"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expectedTokens, result);
 }

 @Test(expected = ParseException.class)
 public void testBurstingUnknownOption() { { args = new String[]{"-zxyz"};
 new PosixParser().flatten(options, args, false); }
 }

 @Test
 public void testBurstingOptionWithNoArgAndStopAtNonOption() {
 args = new String[]{"-a", "-bz", "nonOption"};
 expectedTokens = new String[]{"-a", "-b"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expectedTokens, result);
 }
}