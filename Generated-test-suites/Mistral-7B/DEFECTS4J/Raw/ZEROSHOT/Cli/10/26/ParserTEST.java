import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParser() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a double argument");
 options.addOption("d", null, "Option D with a string argument");
 options.addRequiredOption("e", true, "Required Option E");
 options.addRequiredOption("f", true, "Required Option F");

 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return super.flatten(opts, arguments, stopAtNonOption);
 }
 };

 String[] args = new String[]{"-a", "--b", "3", "--c", "4.5", "--d", "stringValue", "--e", "--f"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.getOptionValue("b").equals("3"));
 assertTrue(cmd.getOptionValue("c").equals("4.5"));
 assertTrue(cmd.getOptionValue("d").equals("stringValue"));
 assertTrue(cmd.hasOption("e"));
 assertTrue(cmd.hasOption("f"));
 assertFalse(cmd.getOptions().hasOption("g"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("e", true, "Required Option E");
 options.addRequiredOption("f", true, "Required Option F");

 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return super.flatten(opts, arguments, stopAtNonOption);
 }
 };

 String[] args = new String[]{"--a", "--b"};
 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 fail("Expected MissingOptionException but got " + e.getClass().getName());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return super.flatten(opts, arguments, stopAtNonOption);
 }
 };

 String[] args = new String[]{"--g", "--a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 fail("Expected UnrecognizedOptionException but got " + e.getClass().getName());
 }
 }

 @Test
 public void testParsingWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", null, "Option B with a string argument");

 Parser parser = new Parser() {
 @Override
 protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
 return super.flatten(opts, arguments, stopAtNonOption);
 }

 @Override
 protected void processProperties(Properties properties) {
 super.processProperties(properties);

 if (cmd.hasOption("b")) {
 Option bOption = cmd.getOption("b");
 List<String> values = bOption.getValues();
 assertEquals("value1", values.get(0));
 assertNull(values.get(1));
 }
 }
 };

 String[] args = new String[]{"--a", "--b=value1"};
 Properties properties = new Properties();
 properties.setProperty("b", "value2");
 CommandLine cmd = parser.parse(options, args, properties);
 }
}