import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseWithNoArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(0, cmd.getArgs().size());
 }

 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("a", "arg1", false, "required argument");
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 fail("MissingRequiredOptionException expected");
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("required argument"));
 }
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", false, "an option");
 String[] arguments = new String[]{"--arg1", "value"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getArgs()[0]);
 }

 @Test
 public void testParseWithMultipleOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", false, "option 1");
 options.addOption("b", "arg2", false, "option 2");
 String[] arguments = new String[]{"--arg1", "--arg2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.getOptions().size() > 1);
 }

 @Test
 public void testParseWithOptionValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "an option with value");
 String[] arguments = new String[]{"--arg1", "value"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithLongOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("longOption1", "long option 1", false, "a long option");
 String[] arguments = new String[]{"--long-option1"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.getOptions().size() > 0);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", false, "an option");
 Properties properties = new Properties();
 properties.setProperty("a", "value");
 CommandLine cmd = parser.parse(options, null, properties);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("value", cmd.getArgs()[0]);
 }

 @Test
 public void testParseWithMixedOptionsAndArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", false, "an option");
 options.addOption("b", "arg2", false, "another option");
 String[] arguments = new String[]{"--arg1", "value1", "--arg2", "value2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(0, cmd.getArgs().size());
 assertTrue(cmd.getOptions().size() > 1);
 }

 @Test
 public void testParseWithInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"--invalid"};
 try {
 parser.parse(options, arguments);
 fail("UnrecognizedOptionException expected");
 } catch (UnrecognizedOptionException e) {
 assertEquals("Unrecognized option: invalid", e.getMessage());
 }
 }

 @Test
 public void testParseWithInvalidOptionValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "an option with value");
 String[] arguments = new String[]{"--arg1", "invalid-value"};
 try {
 parser.parse(options, arguments);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid value for option: arg1", e.getMessage());
 }
 }

 @Test
 public void testParseWithExtraArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[]{"--arg1", "value1", "extra"};
 try {
 parser.parse(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected exception
 }
 }
}