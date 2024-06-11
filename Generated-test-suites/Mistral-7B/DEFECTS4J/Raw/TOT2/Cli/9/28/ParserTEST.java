import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParserWithValidOptionsAndArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with an argument");

 Parser parser = new DefaultParser();
 String[] args = {"-a", "--b=10", "-c", "test"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals(10, cmd.getOptionValue("b").intValue());
 assertEquals("test", cmd.getOptionValue("c"));
 }

 @Test
 public void testParserWithRequiredOptions() throws ParseException, MissingOptionException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B, required");

 Parser parser = new DefaultParser();
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParserWithMissingRequiredOptions() throws ParseException, MissingOptionException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B, required");

 Parser parser = new DefaultParser();
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options, args);
 fail("Missing required option: b");
 }

 @Test
 public void testParserWithUnrecognizedOption() throws ParseException, UnrecognizedOptionException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 Parser parser = new DefaultParser();
 String[] args = {"--x", "-a"};
 CommandLine cmd = parser.parse(options, args);
 assertFalse(cmd.hasOption("x"));
 }

 @Test(expected = ParseException.class)
 public void testParserWithInvalidArgumentType() throws ParseException {
 Options options = new Options();
 options.addOption("a", 1, "Option A with an invalid argument type");

 Parser parser = new DefaultParser();
 String[] args = {"-a", "test"};
 fail("Invalid argument type for option: a");
 }

 @Test
 public void testParserWithOptionGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.addOption("a", true, "Option A");
 group.addOption("b", true, "Option B");
 options.addOptionGroup(group);

 Parser parser = new DefaultParser();
 String[] args = {"--group", "-a"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test
 public void testParserWithLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("long-option-a", true, "Long Option A");
 options.addOption("long-option-b", 1, "Long Option B with an argument");

 Parser parser = new DefaultParser();
 String[] args = {"--long-option-a", "--long-option-b=10"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("long-option-a"));
 assertTrue(cmd.hasOption("long-option-b"));
 assertEquals(10, cmd.getOptionValue("long-option-b").intValue());
 }
}