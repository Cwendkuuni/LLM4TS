import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", "optionA", true, "Option A");
 options.addOption("b", "optionB", false, "Option B");
 options.addOption("c", "optionC", 3, "Option C with value 3");
 }

 @After
 public void tearDown() {
 parser = null;
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 String[] args = {};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[0], flattenedArgs);
 }

 @Test
 public void testSingleArg() throws ParseException {
 String[] args = {"arg1"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"arg1"}, flattenedArgs);
 }

 @Test
 public void testDoubleDash() throws ParseException {
 String[] args = {"--"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--"}, flattenedArgs);
 }

 @Test(expected = ParseException.class)
 public void testUnrecognizedSingleArg() throws ParseException {
 String[] args = {"z"};
 parser.flatten(options, args, false);
 fail("Should have thrown ParseException for unrecognized option 'z'");
 }

 @Test(expected = ParseException.class)
 public void testUnrecognizedDoubleArg() throws ParseException {
 String[] args = {"zz"};
 parser.flatten(options, args, false);
 fail("Should have thrown ParseException for unrecognized option 'zz'");
 }

 @Test
 public void testSingleOption() throws ParseException {
 String[] args = {"-a"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a"}, flattenedArgs);
 }

 @Test
 public void testOptionWithArg() throws ParseException {
 String[] args = {"-a", "arg"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "arg"}, flattenedArgs);
 }

 @Test
 public void testOptionWithMultipleArgs() throws ParseException {
 String[] args = {"-a", "arg1", "arg2"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "arg1", "arg2"}, flattenedArgs);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 String[] args = {"-a", "arg1", "nonOption"};
 String[] flattenedArgs = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"-a", "arg1"}, flattenedArgs);
 }

 @Test
 public void testLongOption() throws ParseException {
 String[] args = {"--optionC", "3"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--optionC", "3"}, flattenedArgs);
 }

 @Test
 public void testBurstedOption() throws ParseException {
 String[] args = {"-ac"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "-c"}, flattenedArgs);
 }

 @Test
 public void testBurstedOptionWithArg() throws ParseException {
 String[] args = {"-abc"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "-c"}, flattenedArgs);
 }

 @Test
 public void testBurstedOptionWithMultipleArgs() throws ParseException {
 String[] args = {"-acd"};
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "-c", "-d"}, flattenedArgs);
 }

 @Test
 public void testBurstedOptionWithArgAndStopAtNonOption() throws ParseException {
 String[] args = {"-acd", "nonOption"};
 String[] flattenedArgs = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"-acd"}, flattenedArgs);
 }
}