import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 }

 @Test
 public void testFlattenSimple() {
 String[] args = {"--foo", "-a", "-b", "1", "--bar", "-c", "arg1", "arg2"};
 String[] expected = {"--foo", "-a", "--", "-b", "1", "--", "-c", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = {"--foo", "-a", "-b", "1", "--bar", "-c", "arg1", "arg2", "arg3"};
 String[] expected = {"--foo", "-a", "--bar", "arg3"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testPosixParser() {
 parser = new PosixParser();
 }

 @Test(expected = IllegalStateException.class)
 public void testFlattenNullOptions() {
 String[] args = {"--foo", "-a", "-b", "1", "--bar", "-c", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 parser.flatten(null, args, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenNullArgs() {
 PosixParser parser = new PosixParser();
 parser.flatten(options, null, false);
 }

 @Test(expected = IllegalStateException.class)
 public void testFlattenStopAtNonOptionNullArgs() {
 PosixParser parser = new PosixParser();
 parser.flatten(options, null, true);
 }

 @Test(expected = IllegalStateException.class)
 public void testFlattenNullOptionsNullArgs() {
 PosixParser parser = new PosixParser();
 parser.flatten(null, null, false);
 }

 @Test
 public void testBurstTokenSimple() {
 String token = "-ab";
 String[] expected = {"-a", "-b"};
 String[] result = parser.burstToken(token, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenOptionWithArg() {
 String token = "-abc";
 String[] expected = {"-a", "-c"};
 String[] result = parser.burstToken(token, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenOptionWithTwoArgs() {
 String token = "-abcdef";
 String[] expected = {"-a", "-c", "-d", "-e", "-f"};
 String[] result = parser.burstToken(token, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 String token = "-abcd";
 String[] expected = {"-a", "--", "bcd"};
 String[] result = parser.burstToken(token, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testProcessOptionToken() {
 String token = "-a";
 boolean stopAtNonOption = false;
 parser.init();
 parser.processOptionToken(token, stopAtNonOption);
 assertEquals(true, parser.hasOption("a"));
 assertEquals(null, parser.getCurrentOption());
 assertEquals("--", parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcessOptionTokenWithArg() {
 String token = "-a";
 boolean stopAtNonOption = false;
 parser.init();
 options.findOption("a").setValue("arg");
 parser.processOptionToken(token, stopAtNonOption);
 assertEquals("arg", parser.getCurrentOption().getValue());
 assertEquals(null, parser.getCurrentOption());
 assertEquals("--", parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcessOptionTokenStopAtNonOption() {
 String token = "-a";
 String[] args = {"-a", "arg"};
 String[] expected = {"--", "-a", "arg"};
 boolean stopAtNonOption = true;
 parser.init();
 parser.processOptionToken(token, stopAtNonOption);
 assertEquals(args[1], parser.getCurrentOption().getValue());
 assertEquals("--", parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcess() {
 String token = "arg";
 parser.init();
 parser.process(token);
 assertEquals("--", parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcessDoubleHyphen() {
 String token = "--";
 parser.init();
 parser.process(token);
 assertEquals(new String[] {"--"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcessSingleHyphen() {
 String token = "-";
 parser.init();
 parser.process(token);
 assertEquals(new String[] {"--"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testProcessUnknownToken() {
 String token = "unknown";
 parser.init();
 parser.process(token);
 assertEquals(new String[] {"unknown"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testInit() {
 parser.init();
 assertEquals(new ArrayList(), parser.getTokens());
 }

 @Test
 public void testGetCurrentOption() {
 parser.init();
 assertEquals(null, parser.getCurrentOption());
 }

 @Test
 public void testHasOption() {
 parser.init();
 assertEquals(false, parser.hasOption("a"));
 }

 @Test
 public void testGetTokens() {
 parser.init();
 assertEquals(new ArrayList(), parser.getTokens());
 }

 @Test
 public void testEatTheRest() {
 String[] args = {"--foo", "-a", "arg1", "arg2", "arg3"};
 parser.flatten(options, args, false);
 parser.process("arg3");
 assertEquals(new String[] {"--foo", "-a", "--", "arg1", "arg2", "arg3"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }

 @Test
 public void testGobble() {
 String[] args = {"--foo", "-a", "arg1", "arg2", "arg3"};
 String[] expected = {"--foo", "-a", "--"};
 parser.flatten(options, args, false);
 parser.gobble(args.iterator());
 assertArrayEquals(expected, parser.getTokens().toArray(new String[parser.getTokens().size()]));
 }
}