import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with an optional argument");
 }

 @Test
 public void testEmptyArgs() {
 parser = new PosixParser();
 String[] result = parser.flatten(options, new String[0], false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testSingleOption() {
 args = new String[]{"-a"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testSingleOptionWithArg() {
 args = new String[]{"-a", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testOptionWithArg() {
 args = new String[]{"-b", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-b", "arg"}, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 args = new String[]{"--x"};
 parser = new PosixParser();
 try {
 parser.flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testBursting() {
 args = new String[]{"-abc"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-b", "-c"}, result);
 }

 @Test
 public void testBurstingWithArg() {
 args = new String[]{"-abc", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-b", "arg"}, result);
 }

 @Test
 public void testBurstingWithOptionalArg() {
 args = new String[]{"-ac", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-c", "arg"}, result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 args = new String[]{"-a", "--", "-b", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertEquals(new String[]{"-a", "--", "-b", "arg"}, result);
 }

 @Test
 public void testMultipleOptions() {
 args = new String[]{"-a", "-b", "-c", "--", "-a"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-b", "-c", "--", "-a"}, result);
 }
}