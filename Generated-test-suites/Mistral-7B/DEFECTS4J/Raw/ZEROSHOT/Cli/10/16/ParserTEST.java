import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 4, "Option D");
 options.addOption("e", 5, "Option E");
 options.addOption("f", 6, "Option F");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = {"-a", "1", "--b", "2", "--c", "3", "--d", "4", "--e", "5", "--f", "6"};
 CommandLine cmd = parser.parse(arguments);

 Assert.assertEquals(7, cmd.getArgs().size());
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertEquals("1", cmd.getOptionValue("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 Assert.assertEquals("2", cmd.getOptionValue("b"));
 Assert.assertTrue(cmd.hasOption("c"));
 Assert.assertEquals("3", cmd.getOptionValue("c"));
 Assert.assertTrue(cmd.hasOption("d"));
 Assert.assertEquals("4", cmd.getOptionValue("d"));
 Assert.assertTrue(cmd.hasOption("e"));
 Assert.assertEquals("5", cmd.getOptionValue("e"));
 Assert.assertTrue(cmd.hasOption("f"));
 Assert.assertEquals("6", cmd.getOptionValue("f"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = {"-a", "1"};
 parser.parse(arguments);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = {"--x", "1"};
 parser.parse(arguments);
 }

 @Test
 public void testRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addRequiredOption("c", 1, "Option C");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = {"-a", "1"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 Assert.assertEquals("Missing required option: c", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C with argument");

 Parser parser = new DefaultParser();
 parser.setOptions(options);

 String[] arguments = {"-a", "1", "--b", "2"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 Assert.assertEquals("Missing argument for option: c", e.getMessage());
 }
 }
}