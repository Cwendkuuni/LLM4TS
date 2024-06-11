import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 3, "Option D");
 options.addOption("e", 3, "Option E", true, "Optional Description E");
 }

 @Test
 public void testFlattenPositive() {
 String[] args = new String[]{"--a", "--b=1", "--c=2", "--d=3", "--e=4", "arg1", "arg2"};
 String[] expected = new String[]{"--a", "--b", "1", "--c", "2", "--d", "3", "--e", "4", "arg1", "arg2"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = new String[]{"--a", "--b=1", "--c=2", "--d=3", "--e=4", "arg1", "arg2", "--f"};
 String[] expected = new String[]{"--a", "--b", "1", "--c", "2", "--d", "3", "--e", "4", "--f", "arg1", "arg2"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption1() {
 String[] args = new String[]{"--z"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption2() {
 String[] args = new String[]{"--a=", "--b", "--d=5"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testProcessSingleHyphen() {
 String[] args = new String[]{"-"};
 String[] expected = new String[]{"--"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testProcessSingleHyphenStopAtNonOption() {
 String[] args = new String[]{"-", "--f"};
 String[] expected = new String[]{"--", "-", "--f"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testProcessOptionToken() {
 String[] args = new String[]{"-a", "--b=1"};
 String[] expected = new String[]{"--a", "--b", "1"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testProcessOptionTokenInvalidOption() {
 String[] args = new String[]{"-z", "--b=1"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testBurstToken() {
 String[] args = new String[]{"-ab"};
 String[] expected = new String[]{"--a", "--b"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstTokenHasArg() {
 String[] args = new String[]{"-ac=3"};
 String[] expected = new String[]{"--a", "--c", "3"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstTokenHasArgs() {
 String[] args = new String[]{"-ad=1,2,3"};
 String[] expected = new String[]{"--a", "--d", "1", "2", "3"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenInvalidOption() {
 String[] args = new String[]{"-zc=3"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenInvalidOptionWithArg() {
 String[] args = new String[]{"-zc=3="};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 String[] args = new String[]{"-ab", "--f"};
 String[] expected = new String[]{"--a", "--b", "--f"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGobble() {
 String[] args = new String[]{"--a", "--b=1", "--c=2", "--d=3", "--e=4", "arg1", "arg2", "--f"};
 String[] expected = new String[]{"--a", "--b", "1", "--c", "2", "--d", "3", "--e", "4", "arg1", "arg2", "--f"};
 String[] actual = parser.flatten(options, args, false).clone();
 parser.gobble(Arrays.asList(args).iterator());
 String[] actualGobbled = parser.flatten(options, args, false).clone();
 assertArrayEquals(expected, actual);
 assertArrayEquals(Arrays.asList("--f").toArray(new String[0]), actualGobbled);
 }
}