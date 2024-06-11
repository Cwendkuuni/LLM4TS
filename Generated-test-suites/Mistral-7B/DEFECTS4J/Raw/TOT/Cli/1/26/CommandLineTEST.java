import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 private String[] testArgs = new String[]{"-a", "1", "-b", "2", "--unrecognized"};

 @Test
 public void testParsing() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");

 CommandLine cmd = new CommandLineParser().parse(options, testArgs);

 assertEquals(true, cmd.hasOption("a"));
 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals(true, cmd.hasOption("b"));
 assertEquals("2", cmd.getOptionValue("b"));
 assertEquals(null, cmd.getOptionValue("nonExistingOption"));
 assertEquals(null, cmd.getOptionValue((char) 0));
 assertNull(cmd.getOptionObject("nonExistingOption"));
 assertNull(cmd.getOptionObject((char) 0));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A", true);

 CommandLine cmd = new CommandLineParser().parse(options, testArgs);
 assertFalse(cmd.hasOption("a"));
 throw new ParseException("Missing required option: a", 0);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");

 CommandLine cmd = new CommandLineParser().parse(options, new String[]{"-a", "1", "-c", "2"});
 assertFalse(cmd.hasOption("c"));
 throw new ParseException("Unrecognized option: c", 0);
 }

 @Test
 public void testUnrecognizedOptions() {
 CommandLine cmd = new CommandLine(testArgs);
 String[] args = cmd.getArgs();
 assertEquals(1, args.length);
 assertEquals("--unrecognized", args[0]);
 }

 @Test
 public void testEmptyArgs() {
 CommandLine cmd = new CommandLine(new String[0]);
 assertTrue(cmd.getArgs().isEmpty());
 }

}