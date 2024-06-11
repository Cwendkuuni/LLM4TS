import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--option1", "value1", "--option2", "value2", "-o", "--option3", "--"};
 private static final String[] ARGUMENTS_NO_OPTIONS = {"arg1", "arg2", "arg3"};
 private static final String[] ARGUMENTS_MISSING_OPTION = {"--option1", "--", "value2"};
 private static final String[] ARGUMENTS_MISSING_VALUE = {"--option1"};
 private static final String[] ARGUMENTS_INVALID_VALUE = {"--option1=invalid", "--option2"};

 @Test
 public void testParseWithValidOptionsAndArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Help for option1");
 options.addOption("option2", false, "Help for option2");
 options.addOption("option3", 1, "Help for option3");

 CommandLine cmd = parser.parse(options, ARGUMENTS, false);

 Assert.assertEquals(3, cmd.getArgs().size());
 Assert.assertTrue(cmd.hasOption("option1"));
 Assert.assertFalse(cmd.hasOption("unknownOption"));
 Assert.assertEquals("value1", cmd.getOptionValue("option1"));
 Assert.assertEquals("value2", cmd.getOptionValue("option2"));
 Assert.assertEquals("value3", cmd.getOptionValue("option3"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("option1", true, "Help for option1");

 try {
 parser.parse(options, ARGUMENTS_MISSING_OPTION, false);
 } catch (ParseException e) {
 Assert.assertEquals("Missing required option: option1", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingValue() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Help for option1");

 try {
 parser.parse(options, ARGUMENTS_MISSING_VALUE, false);
 } catch (ParseException e) {
 Assert.assertEquals("Missing value for option: option1", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidValue() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", 1, "Help for option1");

 try {
 parser.parse(options, ARGUMENTS_INVALID_VALUE, false);
 } catch (ParseException e) {
 Assert.assertEquals("Invalid value for option: option1", e.getMessage());
 }
 }

 @Test
 public void testParseWithNoOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, ARGUMENTS_NO_OPTIONS, false);

 Assert.assertEquals(3, cmd.getArgs().size());
 Assert.assertFalse(cmd.hasOptions());
 }
}