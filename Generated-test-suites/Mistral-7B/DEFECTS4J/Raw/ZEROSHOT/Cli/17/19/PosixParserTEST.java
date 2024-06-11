import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private String[] expectedArgs;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C");
 options.addOption("d", 3, "Option D");
 options.addOption("e", 4, "Option E");
 options.addOption("f", 5, "Option F");
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testFlattenSimple() throws ParseException {
 args = new String[]{"--a", "--b=1", "--c=2", "--d=3", "--e=4", "--f=5"};
 expectedArgs = new String[]{"-a", "-b", "1", "-c", "2", "-d", "3", "-e", "4", "-f", "5"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testFlattenSingleArg() throws ParseException {
 args = new String[]{"--a=value"};
 expectedArgs = new String[]{"-a", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testFlattenNoOption() throws ParseException {
 args = new String[]{"value"};
 expectedArgs = new String[]{"--", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testFlattenMultiple() throws ParseException {
 args = new String[]{"--a=value1", "--b=value2", "--c=value3"};
 expectedArgs = new String[]{"-a", "value1", "-b", "value2", "-c", "value3"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testFlattenMultiArg() throws ParseException {
 args = new String[]{"--a=value1 value2", "--b=value3 value4"};
 expectedArgs = new String[]{"-a", "value1", "value2", "-b", "value3", "value4"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() throws ParseException {
 args = new String[]{"--x"};
 assertNull(options.getOption("x"));
 }

 @Test(expected = ParseException.class)
 public void testUnknownOptionWithValue() throws ParseException {
 args = new String[]{"--x=value"};
 assertNull(options.getOption("x"));
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 args = new String[]{"--a", "--b", "value"};
 expectedArgs = new String[]{"-a", "-b", "--", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args, true));
 }

 @Test
 public void testBurstTokens() throws ParseException {
 args = new String[]{"--abc"};
 expectedArgs = new String[]{"-a", "-b", "-c"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensWithArg() throws ParseException {
 args = new String[]{"--abc=value"};
 expectedArgs = new String[]{"-a", "-b", "-c", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensLongOption() throws ParseException {
 args = new String[]{"--long-option"};
 expectedArgs = new String[]{"--long-option"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensLongOptionWithArg() throws ParseException {
 args = new String[]{"--long-option=value"};
 expectedArgs = new String[]{"--long-option", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test(expected = ParseException.class)
 public void testBurstTokensInvalidOption() throws ParseException {
 args = new String[]{"--invalid"};
 assertNull(options.getOption("invalid"));
 }

 @Test(expected = ParseException.class)
 public void testBurstTokensInvalidOptionWithArg() throws ParseException {
 args = new String[]{"--invalid=value"};
 assertNull(options.getOption("invalid"));
 }

 @Test
 public void testBurstTokensMixed() throws ParseException {
 args = new String[]{"--a=value1", "--abc", "--d=value3"};
 expectedArgs = new String[]{"-a", "value1", "-a", "-b", "-c", "--", "d", "value3"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensStopAtNonOption() throws ParseException {
 args = new String[]{"--a=value1", "--abc", "value"};
 expectedArgs = new String[]{"-a", "value1", "--abc", "--", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args, true));
 }

 @Test
 public void testBurstTokensWithLongOption() throws ParseException {
 args = new String[]{"--long-option-with-arg=value"};
 expectedArgs = new String[]{"--long-option-with-arg", "value"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensWithLongOptionArg() throws ParseException {
 args = new String[]{"--long-option-with-arg=value1 value2"};
 expectedArgs = new String[]{"--long-option-with-arg", "value1", "value2"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensWithLongOptionMultipleArgs() throws ParseException {
 args = new String[]{"--long-option-with-args=value1 value2 value3"};
 expectedArgs = new String[]{"--long-option-with-args", "value1", "value2", "value3"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }

 @Test
 public void testBurstTokensWithLongOptionNoValue() throws ParseException {
 args = new String[]{"--long-option-with-args"};
 expectedArgs = new String[]{"--long-option-with-args"};
 assertArrayEquals(expectedArgs, PosixParser.parse(options, args));
 }
}