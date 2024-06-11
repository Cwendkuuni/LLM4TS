import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 Parser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[0];
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenSingleOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option"};
 String[] expected = {"option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenSingleLongOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option", true, "Long option description");
 String[] args = {"--long-option"};
 String[] expected = {"long-option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenSingleOptionWithValue() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option", "value"};
 String[] expected = {"option", "value"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option1", true, "Option1 description");
 options.addOption("option2", true, "Option2 description");
 String[] args = {"-option1", "-option2"};
 String[] expected = {"option1", "option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option", true, "Long option description");
 String[] args = {"--long-option", "value"};
 String[] expected = {"long-option", "value"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndAnother() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", true, "Long option2 description");
 String[] args = {"--long-option1", "value1", "--long-option2", "value2"};
 String[] expected = {"long-option1", "value1", "long-option2", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenMixedOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option1", true, "Option1 description");
 options.addOption("option2", false, "Option2 description");
 String[] args = {"-option1", "--option2"};
 String[] expected = {"option1", "--option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenMixedLongOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", false, "Long option2 description");
 String[] args = {"--long-option1", "--long-option2"};
 String[] expected = {"long-option1", "--long-option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndAnother() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", true, "Long option2 description");
 String[] args = {"--long-option1", "value1", "--long-option2", "value2"};
 String[] expected = {"long-option1", "value1", "long-option2", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option", "value", "non-option"};
 String[] expected = {"option", "value"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenSingleOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"--option"};
 String[] expected = {"option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenSingleLongOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option", true, "Long option description");
 String[] args = {"--long-option"};
 String[] expected = {"long-option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenSingleOptionWithValue() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option", "value"};
 String[] expected = {"option", "value"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenMultipleOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option1", true, "Option1 description");
 options.addOption("option2", true, "Option2 description");
 String[] args = {"-option1", "-option2"};
 String[] expected = {"option1", "option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenLongOptionWithValue() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option", true, "Long option description");
 String[] args = {"--long-option", "value"};
 String[] expected = {"long-option", "value"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenLongOptionWithValueAndAnother() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", true, "Long option2 description");
 String[] args = {"--long-option1", "value1", "--long-option2", "value2"};
 String[] expected = {"long-option1", "value1", "long-option2", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenMixedOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option1", true, "Option1 description");
 options.addOption("option2", false, "Option2 description");
 String[] args = {"-option1", "--option2"};
 String[] expected = {"option1", "--option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenMixedLongOptions() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", false, "Long option2 description");
 String[] args = {"--long-option1", "--long-option2"};
 String[] expected = {"long-option1", "--long-option2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenLongOptionWithValueAndAnother() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("long-option1", true, "Long option1 description");
 options.addOption("long-option2", true, "Long option2 description");
 String[] args = {"--long-option1", "value1", "--long-option2", "value2"};
 String[] expected = {"long-option1", "value1", "long-option2", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option", "value", "non-option"};
 String[] expected = {"option", "value"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testPosixParserInit() {
 Parser parser = new PosixParser();
 assertEquals(0, parser.getTokens().size());
 assertNull(parser.getCurrentOption());
 }

 @Test
 public void testProcessSingleHyphen() {
 Parser parser = new PosixParser();
 String[] args = {"-"};
 parser.processSingleHyphen(args[0]);
 assertEquals(1, parser.getTokens().size());
 assertEquals("-", parser.getTokens().get(0));
 }

 @Test
 public void testProcessOptionToken() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"-option", "value"};
 parser.init();
 parser.processOptionToken(args[0], false);
 assertEquals(2, parser.getTokens().size());
 assertEquals("option", parser.getTokens().get(0));
 assertEquals("value", parser.getTokens().get(1));
 }

 @Test
 public void testBurstTokenSingleCharacter() {
 Parser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--single-character-option"};
 parser.init();
 parser.burstToken("single-character-option", false);
 assertEquals(1, parser.getTokens().size());
 assertEquals("single-character-option", parser.getTokens().get(0));
 }

 @Test
 public void testBurstTokenMultipleCharacters() {
 Parser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--multiple-characters-option"};
 parser.init();
 parser.burstToken("multiple-characters-option", false);
 assertEquals(2, parser.getTokens().size());
 assertEquals("multiple-characters-option", parser.getTokens().get(0));
 assertEquals("option", parser.getCurrentOption().getLongOpt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstTokenNonExistentOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--non-existent-option"};
 parser.init();
 parser.burstToken("non-existent-option", false);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 Parser parser = new PosixParser();
 Options options = new Options();
 options.addOption("option", true, "Option description");
 String[] args = {"--option", "value", "non-option"};
 parser.init();
 parser.burstToken("option", true);
 assertEquals(1, parser.getTokens().size());
 assertEquals("option", parser.getTokens().get(0));
 }
}