import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private Options createOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with an optional argument");
 options.addOption("d", null, "Option D with an optional argument");
 return options;
 }

 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();
 parser.init();
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);
 assertNull(parser.currentOption);
 }

 @Test
 public void testFlattenEmptyArgs() {
 PosixParser parser = new PosixParser();
 String[] args = {};
 String[] expected = {};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSingleOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--a"};
 String[] expected = {"--a"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] args = {"-"};
 String[] expected = {"-"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-a", "arg"};
 String[] expected = {"-a", "arg"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithOptionalArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-b", "arg"};
 String[] expected = {"-b", "arg"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--x"};
 String[] options = createOptions().toArray(new String[0]);
 try {
 parser.flatten(Arrays.asList(options), args, false);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--a", "arg1", "--", "arg2", "arg3"};
 String[] expected = {"--a", "arg1", "--", "arg2", "arg3"};
 String[] result = parser.flatten(createOptions(), args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstSingleOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-ab"};
 String[] expected = {"-a", "-b"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-acarg"};
 String[] expected = {"-a", "-c", "arg"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithOptionalArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-carg1", "arg2"};
 String[] expected = {"-c", "arg1", "arg2"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 String[] args = {"-darg1arg2"};
 String[] expected = {"-d", "arg1", "arg2"};
 String[] result = parser.flatten(createOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenBurstInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-xarg"};
 String[] options = createOptions().toArray(new String[0]);
 try {
 parser.flatten(Arrays.asList(options), args, false);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}