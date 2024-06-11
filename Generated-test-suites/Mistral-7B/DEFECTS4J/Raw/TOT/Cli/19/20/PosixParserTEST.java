import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 PosixParser parser = new PosixParser();
 String[] args = {};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenSimple() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenShortOptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testFlattenLongOptionWithoutArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", false, "Long Option");
 String[] args = {"--longOption"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption"}, result);
 }

 @Test
 public void testFlattenLongOptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 String[] args = {"--longOption", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption", "arg"}, result);
 }

 @Test
 public void testFlattenLongOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");
 String[] args = {"--longOption", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption", "arg1", "arg2"}, result);
 }

 @Test
 public void testFlattenShortOptionWithArgAndAnotherShortOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] args = {"-a", "arg1", "-b"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "arg1", "-b"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--invalidOption"};
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenSpecialToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenSpecialTokenWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a", "--nonOption"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--nonOption", "-a", "--nonOption"};
 parser.process(new PosixParser.ParserContext(options, args, false));
 assertEquals(3, parser.tokens.size());
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"--nonOption", "-a", "--nonOption"})));
 parser.init();
 }

 @Test
 public void testProcessOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a", "arg"};
 parser.process(new PosixParser.ParserContext(options, args, false));
 assertEquals(2, parser.tokens.size());
 assertTrue(parser.tokens.containsAll(Arrays.asList(new String[]{"-a", "arg"})));
 parser.init();
 }

 @Test
 public void testProcessInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"--invalidOption"};
 parser.process(new PosixParser.ParserContext(options, args, false));
 assertEquals(1, parser.tokens.size());
 assertNull(parser.currentOption);
 parser.init();
 }

 @Test
 public void testProcessSpecialToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--"};
 parser.process(new PosixParser.ParserContext(options, args, false));
 assertEquals(0, parser.tokens.size());
 parser.init();
 }

 @Test
 public void testProcessStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a", "--nonOption"};
 parser.process(new PosixParser.ParserContext(options, args, true));
 assertEquals(1, parser.tokens.size());
 assertSame(options.getOption("a"), parser.currentOption);
 parser.init();
 }

 @Test
 public void testBurstTokenEmpty() {
 PosixParser parser = new PosixParser();
 String token = "";
 parser.burstToken(token, false);
 assertEquals(0, parser.tokens.size());
 }

 @Test
 public void testBurstTokenSingleCharacter() {
 PosixParser parser = new PosixParser();
 String token = "a";
 parser.burstToken(token, false);
 assertEquals(1, parser.tokens.size());
 assertTrue(parser.tokens.contains("--"));
 parser.init();
 }

 @Test
 public void testBurstTokenShortOption() {
 PosixParser parser = new PosixParser();
 String token = "a";
 parser.burstToken(token, false);
 assertEquals(1, parser.tokens.size());
 assertTrue(parser.tokens.contains("-a"));
 parser.init();
 }

 @Test
 public void testBurstTokenLongOptionWithoutArg() {
 PosixParser parser = new PosixParser();
 String token = "longOption";
 parser.burstToken(token, false);
 assertEquals(1, parser.tokens.size());
 assertTrue(parser.tokens.contains("--longOption"));
 parser.init();
 }

 @Test
 public void testBurstTokenLongOptionWithArg() {
 PosixParser parser = new PosixParser();
 String token = "longOption=arg";
 parser.burstToken(token, false);
 assertEquals(2, parser.tokens.size());
 assertTrue(parser.tokens.contains("--longOption"));
 assertTrue(parser.tokens.contains("arg"));
 parser.init();
 }

 @Test
 public void testBurstTokenLongOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 String token = "longOption=arg1 arg2";
 parser.burstToken(token, false);
 assertEquals(3, parser.tokens.size());
 assertTrue(parser.tokens.contains("--longOption"));
 assertTrue(parser.tokens.contains("arg1"));
 assertTrue(parser.tokens.contains("arg2"));
 parser.init();
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String token = "nonOption";
 parser.burstToken(token, true);
 assertEquals(1, parser.tokens.size());
 assertTrue(parser.tokens.contains("nonOption"));
 parser.init();
 }
}