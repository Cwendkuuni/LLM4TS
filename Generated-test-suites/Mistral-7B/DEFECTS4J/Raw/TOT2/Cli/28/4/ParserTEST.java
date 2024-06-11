import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 // Add your test cases for each method here

 @Test
 public void testParse() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C");
 options.addRequiredOption("r", true, "Required Option");

 String[] arguments = {"--a", "--b", "1", "--c", "2", "--r"};
 CommandLine cmd = new Parser().parse(options, arguments);

 assertEquals(4, cmd.getArgs().size());
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.hasOption("r"));
 assertEquals(true, cmd.getOptionValue("a"));
 assertEquals(1, (int) cmd.getOptionValue("b"));
 assertEquals(2, (int) cmd.getOptionValue("c"));
 assertTrue(cmd.getOptionValue("r"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addRequiredOption("r", true, "Required Option");

 String[] arguments = {"--a"};
 try {
 CommandLine cmd = new Parser().parse(options, arguments);
 fail("Missing required option exception not thrown");
 } catch (MissingOptionException e) {
 // Expected
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] arguments = {"--x"};
 try {
 CommandLine cmd = new Parser().parse(options, arguments);
 fail("Unrecognized option exception not thrown");
 } catch (UnrecognizedOptionException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidArgument() {
 Options options = new Options();
 options.addOption("a", true, "Option A with invalid argument");

 String[] arguments = {"--a", "invalid"};
 try {
 CommandLine cmd = new Parser().parse(options, arguments);
 fail("ParseException not thrown for invalid argument");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with required argument");

 String[] arguments = {"--a"};
 try {
 CommandLine cmd = new Parser().parse(options, arguments);
 fail("Missing argument exception not thrown");
 } catch (MissingArgumentException e) {
 // Expected
 }
 }
}