import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--option1", "value1", "--option2", "value2", "--option3", "--"};
 private static final String[] ARGUMENTS_WITH_MISSING_OPTION = {"--option1", "--option4"};
 private static final String[] ARGUMENTS_WITH_MISSING_VALUE = {"--option1", "--option2="};
 private static final String[] ARGUMENTS_WITH_EXTRA_ARGUMENT = {"--option1", "value1", "--option2", "value2", "extra"};

 @Test
 public void testParseNormal() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGUMENTS, null, false);
 Assert.assertEquals(3, cmd.getArgs().size());
 Assert.assertEquals("value1", cmd.getOptionValue("option1"));
 Assert.assertEquals("value2", cmd.getOptionValue("option2"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(options, ARGUMENTS_WITH_MISSING_OPTION, null, false);
 } catch (MissingOptionException e) {
 Assert.assertEquals("Missing required option: option4", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(options, ARGUMENTS_WITH_MISSING_VALUE, null, false);
 } catch (MissingArgumentException e) {
 Assert.assertEquals("Missing argument for option: option2", e.getMessage());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 try {
 parser.parse(options, ARGUMENTS_WITH_EXTRA_ARGUMENT, null, false);
 } catch (UnrecognizedOptionException e) {
 Assert.assertEquals("Unrecognized option: extra", e.getMessage());
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 Parser parser = new DefaultParser();
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.put("option1", "value1");
 properties.put("option2", "value2");
 CommandLine cmd = parser.parse(options, null, properties, false);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertEquals("value1", cmd.getOptionValue("option1"));
 Assert.assertEquals("value2", cmd.getOptionValue("option2"));
 }
}