import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private String[] arguments;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 }

 @After
 public void tearDown() {
 options = null;
 arguments = null;
 }

 @Test
 public void testEmpty() {
 String[] result = new PosixParser().flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSingleToken() {
 String[] result = new PosixParser().flatten(options, new String[]{"token"}, false);
 assertEquals("token", result[0]);
 }

 @Test
 public void testDoubleHyphen() {
 String[] result = new PosixParser().flatten(options, new String[]{"--"}, false);
 assertArrayEquals(new String[]{"--"}, result);
 }

 @Test
 public void testSingleOption() {
 String[] result = new PosixParser().flatten(options, new String[]{"-a"}, false);
 assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testOptionWithArg() {
 String[] result = new PosixParser().flatten(options, new String[]{"-a", "arg"}, false);
 assertArrayEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testOptionWithMultipleArgs() {
 String[] result = new PosixParser().flatten(options, new String[]{"-c", "arg1", "arg2"}, false);
 assertArrayEquals(new String[]{"-c", "arg1", "arg2"}, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption1() {
 new PosixParser().flatten(options, new String[]{"-x"}, false);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption2() {
 new PosixParser().flatten(options, new String[]{"--x"}, false);
 }

 @Test
 public void testStopAtNonOption() {
 String[] result = new PosixParser().flatten(options, new String[]{"-a", "arg", "token"}, true);
 assertArrayEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testBurstedToken() {
 String[] result = new PosixParser().flatten(options, new String[]{"-abc"}, false);
 assertArrayEquals(new String[]{"-a", "-c"}, result);
 }

 @Test
 public void testBurstedTokenWithArg() {
 String[] result = new PosixParser().flatten(options, new String[]{"-ab"}, false);
 assertArrayEquals(new String[]{"-a", "b"}, result);
 }

 @Test
 public void testBurstedTokenWithMultipleArgs() {
 String[] result = new PosixParser().flatten(options, new String[]{"-abcdefg"}, false);
 assertArrayEquals(new String[]{"-a", "-c", "-e", "-f", "-g"}, result);
 }

 @Test
 public void testBurstedTokenWithStopAtNonOption() {
 String[] result = new PosixParser().flatten(options, new String[]{"-abcdefg"}, true);
 assertArrayEquals(new String[]{"-a", "-c"}, result);
 }

 @Test(expected = ParseException.class)
 public void testBurstedTokenWithInvalidOption() {
 new PosixParser().flatten(options, new String[]{"-xabcdefg"}, false);
 }
}