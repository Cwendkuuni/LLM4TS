import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 CommandLine cmd = new Parser().parse(options, new String[]{"-o", "test.txt"});
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("nonExistentOption"));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("r", "required", true, "Required option");
 new Parser().parse(options, new String[0]);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 Properties properties = new Properties();
 properties.put("output", "test.txt");
 CommandLine cmd = new Parser().parse(options, new String[0], properties);
 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 new Parser().parse(options, new String[]{"-x", "value"});
 }

 @Test
 public void testParseWithMultiValueOption() throws ParseException {
 Options options = new Options();
 options.addOption("l", "logs", true, "Log files to display, multiple allowed");
 CommandLine cmd = new Parser().parse(options, new String[]{"-l", "log1.txt", "-l", "log2.txt"});
 List<String> logs = cmd.getOptionValues("logs");
 assertEquals(2, logs.size());
 assertTrue(logs.contains("log1.txt"));
 assertTrue(logs.contains("log2.txt"));
 }

 @Test
 public void testParseWithOptionalValueOption() throws ParseException {
 Options options = new Options();
 options.addOption("p", "port", true, "Port number to listen on");
 CommandLine cmd = new Parser().parse(options, new String[]{"-p"});
 assertNull(cmd.getOptionValue("port"));
 }

 @Test
 public void testParseWithUnusedArguments() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 CommandLine cmd = new Parser().parse(options, new String[]{"-o", "test.txt", "arg1", "arg2"});
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertEquals(2, cmd.getArgs().length);
 }
}