import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");
 options.addOption("h", "help", false, "display help message");

 Parser parser = new DefaultParser();
 String[] args = {"-o", "value", "--help"};
 CommandLine cmd = parser.parse(options, args, false);

 assertEquals(2, cmd.getArgs().size());
 assertEquals("value", cmd.getOptionValue("o"));
 assertFalse(cmd.hasOption("h"));
 assertTrue(cmd.hasOption("o"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");

 Parser parser = new DefaultParser();
 String[] args = {"--missing"};
 parser.parse(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testMissingValue() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");

 Parser parser = new DefaultParser();
 String[] args = {"-o"};
 parser.parse(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");

 Parser parser = new DefaultParser();
 String[] args = {"-x"};
 parser.parse(options, args, false);
 }

 @Test
 public void testProcessProperties() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");

 Parser parser = new DefaultParser();
 Properties properties = new Properties();
 properties.setProperty("o", "value");
 CommandLine cmd = parser.parse(options, new String[0], properties, false);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getOptionValue("o"));
 }

 @Test
 public void testCheckRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");
 options.addOption("r", "required", true, "a required option");

 Parser parser = new DefaultParser();
 String[] args = {"-o", "value"};
 CommandLine cmd = parser.parse(options, args, false);
 assertNull(cmd.getOptionValue("r"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");
 options.addOption("r", "required", true, "a required option");

 Parser parser = new DefaultParser();
 String[] args = {"-o", "value"};
 parser.parse(options, args, false);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "option", true, "an option");
 options.addOption("h", "help", false, "display help message");

 Parser parser = new DefaultParser();
 String[] args = {"--help", "-o", "value"};
 CommandLine cmd = parser.parse(options, args, false);

 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getOptionValue("o"));
 assertFalse(cmd.hasOption("h"));
 }
}