import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseWithValidOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOptionGroup(new OptionGroup("group1")
 .addOption(new Option("c", false, "Option C"))
 .addOption(new Option("d", false, "Option D"))
 .isRequired(false));

 Parser parser = new DefaultParser();
 String[] arguments = {"--optionA", "true", "--optionB", "value"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(2, cmd.getArgs().size());
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertFalse(cmd.hasOption("c"));
 assertFalse(cmd.hasOption("d"));
 }

 @Test
 public void testParseWithMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", true, "Option C");
 options.addRequiredOption("e", false, "Option E");

 Parser parser = new DefaultParser();
 String[] arguments = {"--optionA", "true", "--optionB", "value"};

 try {
 parser.parse(options, arguments);
 fail("MissingRequiredOptionException expected");
 } catch (MissingOptionException e) {
 assertEquals("Missing required option: e", e.getMessage());
 }
 }

 @Test
 public void testParseWithUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 Parser parser = new DefaultParser();
 String[] arguments = {"--unknownOption", "value"};

 try {
 parser.parse(options, arguments);
 fail("UnrecognizedOptionException expected");
 } catch (UnrecognizedOptionException e) {
 assertEquals("Unrecognized option: unknownOption", e.getMessage());
 }
 }

 @Test
 public void testParseWithMultipleValuesForOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", true, "Option C with multiple values");

 Parser parser = new DefaultParser();
 String[] arguments = {"--optionA", "value1", "--optionA", "value2", "--optionC", "value1", "--optionC", "value2"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(4, cmd.getArgs().size());
 assertTrue(cmd.hasOption("a", new String[]{"value1", "value2"}));
 assertTrue(cmd.hasOption("c", new String[]{"value1", "value2"}));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", true, "Option C with multiple values");

 Properties properties = new Properties();
 properties.setProperty("optionE", "value1");
 properties.setProperty("optionE", "value2");

 CommandLine cmd = new DefaultParser().parse(options, null, properties, false);

 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c", new String[0]));
 assertTrue(cmd.hasOption("e", new String[]{"value1", "value2"}));
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", true, "Option C with multiple values");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0]);
 assertEquals(0, cmd.getArgs().size());
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");
 options.addOption("c", true, "Option C with multiple values");

 Parser parser = new DefaultParser();
 String[] arguments = {"--optionA", "value1", "--nonOption"};
 CommandLine cmd = parser.parse(options, arguments, true);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("nonOption", cmd.getArgs().get(0));
 }

}