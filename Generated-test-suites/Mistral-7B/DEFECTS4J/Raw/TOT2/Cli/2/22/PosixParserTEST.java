import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenBasicUsage() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--foo", "--bar=value", "-a", "-b", "--"};
 String[] expected = {"java", "--foo", "--bar=value", "-a", "-b"};
 String[] flatArgs = parser.flatten(options, args, false);
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testFlattenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--foo", "--bar=value", "-a", "-b", "--", "arg"};
 String[] expected = {"java", "--foo", "--bar=value", "-a", "-b", "--arg"};
 String[] flatArgs = parser.flatten(options, args, true);
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testFlattenOptionWithArgument() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"java", "--foo", "value"};
 String[] expected = {"java", "--foo", "value"};
 String[] flatArgs = parser.flatten(options, args, false);
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testFlattenOptionWithMultipleArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"java", "--foo", "arg1", "arg2"};
 String[] expected = {"java", "--foo", "arg1", "arg2"};
 String[] flatArgs = parser.flatten(options, args, false);
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--invalidOption"};
 String[] flatArgs = parser.flatten(options, args, false);
 assertNull(flatArgs);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--invalidOption=arg1,arg2"};
 String[] flatArgs = parser.flatten(options, args, false);
 assertNull(flatArgs);
 }

 @Test
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "-"};
 String[] expected = {"java", "-"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"java", "--foo"};
 String[] expected = {"java", "--foo"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test(expected = ParseException.class)
 public void testProcessOptionTokenInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--invalidOption"};
 parser.flatten(options, args, false);
 assertNull(parser.tokens.toArray(new String[0]));
 }

 @Test(expected = ParseException.class)
 public void testProcessOptionTokenInvalidOptionWithArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("invalidOption", true, "i");
 String[] args = {"java", "--invalidOption=arg1,arg2"};
 parser.flatten(options, args, false);
 assertNull(parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testBurstTokenBasicUsage() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--foo=arg1 arg2"};
 String[] expected = {"java", "--foo", "arg1", "arg2"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testBurstTokenNoOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "arg1 arg2"};
 String[] expected = {"java", "arg1", "arg2"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testBurstTokenOptionWithSingleArgument() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"java", "--foo=arg1"};
 String[] expected = {"java", "--foo", "arg1"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test
 public void testBurstTokenOptionWithMultipleArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"java", "--foo=arg1 arg2"};
 String[] expected = {"java", "--foo", "arg1", "arg2"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertEquals(Arrays.asList(expected), Arrays.asList(flatArgs));
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--invalidOption=arg1 arg2"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertNull(flatArgs);
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenInvalidOptionNoArgument() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"java", "--invalidOption"};
 String[] flatArgs = parser.flatten(options, args, false)[0];
 assertNull(flatArgs);
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenInvalidOptionWithArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("invalidOption", true, "i");
 String[] args = {"java", "--invalidOption=arg1 arg2"};
 parser.flatten(options, args, false);
 assertNull(parser.tokens.toArray(new String[0]));
 }
}