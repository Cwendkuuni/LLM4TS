import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 }

 @After
 public void tearDown() {
 // Clear the options to prepare for next test
 options.clearOptions();
 }

 @Test
 public void testEmptyArguments() throws ParseException {
 String[] result = new PosixParser().flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSimpleOptions() throws ParseException {
 String[] args = {"-a"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals(Arrays.asList("-a").toArray(new String[0]), result);
 }

 @Test
 public void testOptionsWithArguments() throws ParseException {
 String[] args = {"-a", "arg1", "--b=val1", "arg2", "--c=val2", "--"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals(Arrays.asList("-a", "--b=val1", "arg1", "-c", "--c=val2", "arg2", "--").toArray(new String[0]), result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 String[] args = {"--invalid"};
 new PosixParser().flatten(options, args, false);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 options.addOption("x", false, "Option X");
 String[] args = {"abc", "--", "-x", "def", "--", "-y"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertEquals(Arrays.asList("abc", "--", "-x", "--", "-y").toArray(new String[0]), result);
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"--abc", "def", "--def=ghi", "jkl"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertEquals(Arrays.asList("--abc", "-", "a", "b", "c", "--def=ghi", "jkl").toArray(new String[0]), result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 String[] args = {"--abc", "def", "--def=ghi", "jkl"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertEquals(Arrays.asList("--abc", "def", "--def=ghi", "jkl").toArray(new String[0]), result);
 }

 @Test
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, new String[]{"-"}, false);
 assertEquals(Arrays.asList("-").toArray(new String[0]), result);
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, new String[]{"-a"}, false);
 assertSame(options.getOption("a"), parser.currentOption);
 assertEquals(Arrays.asList("-a").toArray(new String[0]), result);
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, new String[]{"--", "value"}, false);
 assertNull(parser.currentOption);
 assertEquals(Arrays.asList("--", "value").toArray(new String[0]), result);
 }

 @Test
 public void testGobble() {
 PosixParser parser = new PosixParser();
 String[] args = {"--", "value1", "value2", "--"};
 parser.init();
 parser.flatten(options, Arrays.asList(args.clone()).iterator(), false);
 List<String> tokens = (List<String>)parser.tokens;
 assertEquals(2, tokens.size());
 assertEquals("value1", tokens.get(0));
 assertEquals("value2", tokens.get(1));
 }

 @Test
 public void testEatTheRest() {
 PosixParser parser = new PosixParser();
 String[] args = {"--", "value1", "value2"};
 parser.init();
 parser.flatten(options, Arrays.asList(args.clone()).iterator(), true);
 List<String> tokens = (List<String>)parser.tokens;
 assertEquals(2, tokens.size());
 assertEquals("value1value2", tokens.get(0));
 }
}