import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C with arg");
 options.addOption("d", 4, "Option D with arg");
 options.addOption("e", 5, "Option E with required arg");
 options.addOption("f", null, "Option F with variable arg");
 options.addOption("g", false, "Option G without arg");

 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments,
 boolean stopAtNonOption) {
 return arguments;
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return super.parse(options, arguments);
 }
 };

 String[] args = new String[]{"-a", "-b", "2", "--c", "abc", "--d", "def",
 "--e", "xyz", "--f", "ghijk", "--g"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals(11, cmd.getArgs().size());
 assertEquals("xyz", cmd.getOptionValue("e"));
 assertEquals("abc", cmd.getOptionValue("c"));
 assertEquals("def", cmd.getOptionValue("d"));
 assertEquals("ghijk", cmd.getOptionValue("f"));
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.hasOption("d"));
 assertTrue(cmd.hasOption("e"));
 assertFalse(cmd.hasOption("f"));
 assertFalse(cmd.hasOption("h"));
 assertEquals(2, cmd.getOptionIntValue("b"));
 assertEquals(3, cmd.getOptionIntValue("c"));
 assertEquals(4, cmd.getOptionIntValue("d"));
 assertNull(cmd.getOptionValue("h"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments,
 boolean stopAtNonOption) {
 return arguments;
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return super.parse(options, arguments, true, false);
 }
 };

 String[] args = new String[]{"-b"};
 parser.parse(options, args);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("e", true, "Option E with required arg");
 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments,
 boolean stopAtNonOption) {
 return arguments;
 }

 @Override
 public CommandLine parse(Options options, String[] arguments)
 throws ParseException {
 return super.parse(options, arguments);
 }
 };

 String[] args = new String[]{"-a"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Missing required option"));
 assertTrue(message.contains("Option E"));
 }
 }
}