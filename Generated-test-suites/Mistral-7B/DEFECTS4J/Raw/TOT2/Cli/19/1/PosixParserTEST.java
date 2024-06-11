import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

 @Test
 public void testFlattenWithEmptyArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {};
 Options options = new Options();
 String[] flattened = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{}, flattened);
 }

 @Test
 public void testFlattenWithValidOptions() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option1", "--option2=value", "-o", "--", "-a", "-b"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2 with value");
 options.addOption("o", "output", true, "Output file");
 String[] flattened = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--option1", "--option2=value", "-o", "--", "-a", "-b"}, flattened);
 }

 @Test
 public void testFlattenWithInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--invalidOption"};
 Options options = new Options();
 String[] flattened = null;
 try {
 flattened = parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 Assert.assertNull(flattened);
 }

 @Test
 public void testFlattenWithLongOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOptionThatCanHaveArguments=value1 value2"};
 Options options = new Options();
 options.addOption("longOptionThatCanHaveArguments", true, "Long option that can have arguments");
 String[] flattened = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--longOptionThatCanHaveArguments=value1 value2"}, flattened);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option1", "--", "-a", "-b"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 String[] flattened = parser.flatten(options, arguments, true);
 Assert.assertArrayEquals(new String[]{"--option1", "-a", "-b"}, flattened);
 }

 @Test
 public void testBurstTokenWithValidOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o", "-p", "option", "with", "value"};
 Options options = new Options();
 options.addOption("option", true, "Option with value");
 String[] flattened = parser.flatten(options, arguments, false)[0];
 String[] tokens = flattened.clone();
 parser.burstToken("optionwithvalue", false);
 Assert.assertArrayEquals(new String[]{"-o", "--option", "with", "value"}, tokens);
 }

 @Test
 public void testBurstTokenWithInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-i", "invalidOption"};
 Options options = new Options();
 String[] tokens = parser.flatten(options, arguments, false)[0];
 parser.burstToken("invalidOption", true);
 Assert.assertArrayEquals(new String[]{"--", "-i", "invalidOption"}, tokens);
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o", "--option"};
 Options options = new Options();
 options.addOption("option", true, "Option");
 String[] flattened = parser.flatten(options, arguments, false)[0];
 parser.init();
 parser.processOptionToken("o", false);
 Assert.assertNull(parser.currentOption);
 Assert.assertEquals(1, parser.tokens.size());
 Assert.assertEquals("--option", parser.tokens.get(0).toString());
 }

 @Test
 public void testGobble() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--option1", "--option2=value1 value2", "-a", "-b"};
 Options options = new Options();
 String[] flattened = parser.flatten(options, arguments, false)[0];
 parser.init();
 parser.gobble(Arrays.asList(arguments).iterator());
 Assert.assertEquals(5, parser.tokens.size());
 Assert.assertEquals("--option1", parser.tokens.get(0).toString());
 Assert.assertEquals("--option2=value1 value2", parser.tokens.get(1).toString());
 Assert.assertEquals("-a", parser.tokens.get(2).toString());
 Assert.assertEquals("-b", parser.tokens.get(3).toString());
 Assert.assertEquals("--", parser.tokens.get(4).toString());
 }

}