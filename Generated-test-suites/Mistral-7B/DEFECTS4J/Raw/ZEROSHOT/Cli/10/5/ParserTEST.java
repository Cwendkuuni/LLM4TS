import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOptionGroup(new OptionGroup("group", "Group options")
 .addOption("g", true, "Group option G")
 .addOption("h", true, "Group option H"));

 String[] arguments = {"-a", "--b", "valueB", "--c", "value1", "value2"};
 Parser parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments);

 Assert.assertFalse(commandLine.hasOption("unknown"));
 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertTrue(commandLine.hasOption("b"));
 Assert.assertEquals(1, commandLine.getOptionValues("b").length);
 Assert.assertEquals("valueB", commandLine.getOptionValues("b")[0]);
 Assert.assertTrue(commandLine.hasOption("c"));
 Assert.assertEquals(2, commandLine.getOptionValues("c").length);
 Assert.assertEquals("value1", commandLine.getOptionValues("c")[0]);
 Assert.assertEquals("value2", commandLine.getOptionValues("c")[1]);
 Assert.assertTrue(commandLine.hasOption("group.g"));
 Assert.assertTrue(commandLine.hasOption("group.h"));
 Assert.assertNull(commandLine.getOptionValues("group.h")[0]);
 Assert.assertEquals(0, commandLine.getArgs().length);
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addRequiredOption("b", true, "Required option B");
 String[] arguments = {"-a"};
 Parser parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"--unknown"};
 Parser parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 String[] arguments = {"-a", "--b"};
 Parser parser = new DefaultParser();
 parser.parse(options, arguments);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {};
 Properties properties = new Properties();
 properties.setProperty("a", "valueA");
 Parser parser = new DefaultParser();
 CommandLine commandLine = parser.parse(options, arguments, properties, false);

 Assert.assertFalse(commandLine.hasOption("unknown"));
 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertEquals(1, commandLine.getOptionValues("a").length);
 Assert.assertEquals("valueA", commandLine.getOptionValues("a")[0]);
 }
}