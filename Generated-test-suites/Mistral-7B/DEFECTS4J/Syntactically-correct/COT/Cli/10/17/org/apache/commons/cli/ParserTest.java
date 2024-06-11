import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
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
 List<Option> requiredOptions = options.getRequiredOptions();
 parser.setOptions(options);
 assertEquals(requiredOptions, parser.getRequiredOptions());
 }

 @Test
 public void testFlatten_NullOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(null, arguments, false);
 assertNotNull(cmd);
 assertEmpty(cmd.getArgs());
 }

 @Test
 public void testFlatten_EmptyArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEmpty(cmd.getArgs());
 assertFalse(cmd.hasOptions());
}

 @Test
 public void testFlatten_SingleArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = {"arg1"};
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEquals(1, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testFlatten_SingleOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = {"-a"};
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 }

 @Test
 public void testFlatten_SingleOptionWithValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = {"-a", "value"};
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testFlatten_MultipleArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = {"arg1", "arg2"};
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testFlatten_MultipleOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 parser.setOptions(options);
 String[] arguments = {"-a", "-b"};
 CommandLine cmd = parser.parse(options, arguments, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_MissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 String[] arguments = {"-c"};
 CommandLine cmd = parser.parse(options, arguments, false);
 fail("ParseException expected, but not thrown");
 }

 @Test
 public void testParse_NullOptions() throws ParseException {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(null, arguments, false);
 assertNotNull(cmd);
 assertEmpty(cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParse_EmptyArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0], false);
 assertNotNull(cmd);
 assertEmpty(cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParse_SingleArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"arg1"}, false);
 assertNotNull(cmd);
 assertEquals(1, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParse_SingleOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"-a"}, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 }

 @Test
 public void testParse_SingleOptionWithValue() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"-a", "value"}, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParse_MultipleArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"}, false);
 assertNotNull(cmd);
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParse_MultipleOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"-a", "-b"}, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 }

 @Test
 public void testParse_MultipleOptionsWithValues() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[]{"-a", "valueA", "-b", "valueB"}, false);
 assertNotNull(cmd);
 assertEquals(0, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 }

 @Test
 public void testParse_MissingOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 try {
 CommandLine cmd = parser.parse(options, new String[]{"-c"}, false);
 fail("MissingOptionException expected, but not thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testParse_MissingArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 try {
 CommandLine cmd = parser.parse(options, new String[]{"-a"}, false);
 fail("MissingArgumentException expected, but not thrown");
 } catch (MissingArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("a", "valueA");
 CommandLine cmd = parser.parse(options, new String[0], properties, false);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 }

 @Test
 public void testCheckRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addRequiredOption("b", true, "Option B");
 parser.setOptions(options);
 try {
 parser.parse(options, new String[0], null, false);
 fail("MissingOptionException expected, but not thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testProcessProperties() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 parser.setOptions(options);
 Properties properties = new Properties();
 properties.setProperty("a", "valueA");
 parser.processProperties(properties);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 }
}