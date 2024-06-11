import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testSetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertSame(options, parser.getOptions());
 }

 @Test
 public void testGetOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 List requiredOptions = parser.getRequiredOptions();
 assertTrue(requiredOptions.isEmpty());

 options.addOption("option1", true, "Description1");
 requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("option1", requiredOptions.get(0).getKey());
 }

 @Test
 public void testFlattenNullOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 assertEquals(new String[0], parser.flatten(null, arguments, false));
 }

 @Test
 public void testFlattenEmptyArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[0];
 assertEquals(new String[0], parser.flatten(options, arguments, false));
 }

 @Test
 public void testFlattenSimpleArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true);
 String[] arguments = {"option1"};
 String[] expected = {"option1"};
 assertArrayEquals(expected, parser.flatten(options, arguments, false));
 }

 @Test
 public void testFlattenWithStopAtNonOptionTrue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true);
 String[] arguments = {"--", "option1"};
 String[] expected = {"--", "option1"};
 assertArrayEquals(expected, parser.flatten(options, arguments, true));
 }

 @Test
 public void testParseWithNoArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().isEmpty());
 }

 @Test
 public void testParseWithSingleArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = {"arg1"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 }

 @Test
 public void testParseWithSingleOptionalOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 String[] arguments = {"option1"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("option1"));
 }

 @Test
 public void testParseWithSingleRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 options.addRequiredOption("option2", false, "Description2");
 String[] arguments = {"option1"};
 try {
 parser.parse(options, arguments);
 fail();
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("Missing required option"));
 }
 }

 @Test
 public void testParseWithSingleRequiredOptionValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 options.addRequiredOption("option2", false, "Description2");
 String[] arguments = {"option1", "value1"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals("value1", cmd.getOptionValue("option2"));
 }

 @Test
 public void testParseWithMultipleArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true);
 String[] arguments = {"arg1", "arg2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("arg2", cmd.getArgs().get(1));
 }

 @Test
 public void testParseWithMultipleOptionalOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 options.addOption("option2", true, "Description2");
 String[] arguments = {"option1", "option2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 }

 @Test
 public void testParseWithMultipleRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 options.addRequiredOption("option2", false, "Description2");
 options.addRequiredOption("option3", false, "Description3");
 String[] arguments = {"option1"};
 try {
 parser.parse(options, arguments);
 fail();
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("Missing required option"));
 }
 }

 @Test
 public void testParseWithMultipleRequiredOptionsValues() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 options.addRequiredOption("option2", false, "Description2");
 options.addRequiredOption("option3", false, "Description3");
 String[] arguments = {"option1", "value1", "value2"};
 CommandLine cmd = parser.parse(options, arguments);
 assertEquals("value1", cmd.getOptionValue("option2"));
 assertEquals("value2", cmd.getOptionValue("option3"));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 Properties properties = new Properties();
 properties.setProperty("option1", "value1");
 properties.setProperty("option2", "value2");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 assertTrue(cmd.hasOption("option1"));
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("value2", cmd.getOptionValue("option2"));
 }

 @Test
 public void testParseWithStopAtNonOptionTrue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "Description1");
 String[] arguments = {"--", "option1", "arg1"};
 CommandLine cmd = parser.parse(options, arguments, true);
 assertEquals(1, cmd.getArgs().size());
 assertEquals("option1", cmd.getArgs().get(0));
 }
}