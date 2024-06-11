import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testFlattenNoOptions() throws ParseException {
 Options options = new Options();
 String[] args = new String[]{"MyProgram", "--foo", "--bar"};
 String[] expected = new String[]{"--foo", "--bar"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMinimalOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = new String[]{"MyProgram", "--a"};
 String[] expected = new String[]{"--a"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithArg() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 String[] args = new String[]{"MyProgram", "--a", "argA", "--b", "argB"};
 String[] expected = new String[]{"--a", "argA", "--b", "argB"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithMultipleArgs() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B");
 String[] args = new String[]{"MyProgram", "--a", "argA1", "--b", "argB1", "argB2"};
 String[] expected = new String[]{"--a", "argA1", "--b", "argB1", "argB2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B");
 String[] args = new String[]{"MyProgram", "--a", "argA1", "--b", "argB1", "argB2", "nonOption"};
 String[] expected = new String[]{"--a", "argA1", "--b", "argB1", "argB2", "--", "nonOption"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithDoubleHyphen() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = new String[]{"MyProgram", "--", "double--hyphen"};
 String[] expected = new String[]{"--", "double--hyphen"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = new String[]{"MyProgram", "--invalidOption"};
 }

 @Test
 public void testBurstToken() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B");
 String[] args = new String[]{"MyProgram", "-ab", "argA", "argB1", "argB2"};
 String[] expected = new String[]{"--a", "--b", "argA", "argB1", "argB2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B");
 String[] args = new String[]{"MyProgram", "-ab", "argA", "argB1", "argB2", "nonOption"};
 String[] expected = new String[]{"--a", "--b", "--", "nonOption"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B");
 String[] args = new String[]{"MyProgram", "-a-b", "argA", "argB1", "argB2"};
 String[] expected = new String[]{"--a", "--b", "argA", "argB1", "argB2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithLongOption() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 String[] args = new String[]{"MyProgram", "--longOption"};
 String[] expected = new String[]{"--longOption"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithLongOptionArg() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 options.addOption("anotherLongOption", true, "Another Long Option");
 String[] args = new String[]{"MyProgram", "--longOption=value", "--anotherLongOption=anotherValue"};
 String[] expected = new String[]{"--longOption=value", "--anotherLongOption=anotherValue"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithLongOptionNoArg() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 String[] args = new String[]{"MyProgram", "--longOption"};
 String[] expected = new String[]{"--longOption"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithLongOptionArgAndMoreArgs() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 options.addOption("anotherLongOption", true, "Another Long Option");
 String[] args = new String[]{"MyProgram", "--longOption=value1", "--longOption=value2", "--anotherLongOption=value3"};
 String[] expected = new String[]{"--longOption=value1", "--longOption=value2", "--anotherLongOption=value3"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }
}