import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with argument");

 String[] args = {"-a", "--b", "--c", "value"};
 String[] expected = {"-a", "--b", "--c", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with argument");

 String[] args = {"-a", "--b", "--", "value", "-d"};
 String[] expected = {"-a", "--b--", "value", "-d"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption"};
 String[] expected = {"--longOption"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption=value"};
 String[] expected = {"--longOption", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--invalid"};
 try {
 new PosixParser().flatten(options, args, false);
 } catch (IllegalArgumentException e) {
 assertEquals("The option 'invalid' does not exist", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidLongOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--invalid-long-option=value"};
 try {
 new PosixParser().flatten(options, args, false);
 } catch (IllegalArgumentException e) {
 assertEquals("The option 'invalid-long-option' does not exist", e.getMessage());
 }
 }

 @Test
 public void testFlattenMultipleValidOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with argument");

 String[] args = {"-a", "--b=2", "--c=value"};
 String[] expected = {"-a", "--b", "--c", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");

 String[] args = {"--ab"};
 String[] expected = {"-ab"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithValue() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");

 String[] args = {"--ab=value"};
 String[] expected = {"-ab", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionLonger() {
 Options options = new Options();
 options.addOption("abcd", true, "Option ABCD");

 String[] args = {"--abcd"};
 String[] expected = {"-abcd"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithValueLonger() {
 Options options = new Options();
 options.addOption("abcd", true, "Option ABCD");

 String[] args = {"--abcd=value"};
 String[] expected = {"-abcd", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOptionBurstOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");

 String[] args = {"--ab", "--"};
 String[] expected = {"-ab--"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOptionBurstOptionWithValue() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");

 String[] args = {"--ab=value", "--"};
 String[] expected = {"-ab=value--"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArg() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB with arg");
 options.addOption("cd", true, "Option CD with arg");

 String[] args = {"--ab=value1--cd=value2"};
 String[] expected = {"-ab", "value1", "-cd", "value2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArgLonger() {
 Options options = new Options();
 options.addOption("abcd", true, "Option ABCD with arg");
 options.addOption("efgh", true, "Option EFGH with arg");

 String[] args = {"--abcd=value1--efgh=value2"};
 String[] expected = {"-abcd", "value1", "-efgh", "value2"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArgStopAtNonOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB with arg");
 options.addOption("cd", true, "Option CD with arg");

 String[] args = {"--ab=value1--cd=value2--"};
 String[] expected = {"-ab=value1--cd=value2--"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArgStopAtNonOptionLonger() {
 Options options = new Options();
 options.addOption("abcd", true, "Option ABCD with arg");
 options.addOption("efgh", true, "Option EFGH with arg");

 String[] args = {"--abcd=value1--efgh=value2--"};
 String[] expected = {"-abcd=value1--efgh=value2--"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongestBurstOption() {
 Options options = new Options();
 options.addOption("abcdefghijklmnopqrstuvwxyz", true, "Option ABCDEFGHIJKLMNOPQRSTUVWXYZ");

 String[] args = {"--abcdefghijklmnopqrstuvwxyz"};
 String[] expected = {"-abcdefghijklmnopqrstuvwxyz"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongestBurstOptionWithValue() {
 Options options = new Options();
 options.addOption("abcdefghijklmnopqrstuvwxyz", true, "Option ABCDEFGHIJKLMNOPQRSTUVWXYZ");

 String[] args = {"--abcdefghijklmnopqrstuvwxyz=value"};
 String[] expected = {"-abcdefghijklmnopqrstuvwxyz", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongestBurstOptionStopAtNonOption() {
 Options options = new Options();
 options.addOption("abcdefghijklmnopqrstuvwxyz", true, "Option ABCDEFGHIJKLMNOPQRSTUVWXYZ");

 String[] args = {"--abcdefghijklmnopqrstuvwxyz=value--"};
 String[] expected = {"-abcdefghijklmnopqrstuvwxyz=value--"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }
}