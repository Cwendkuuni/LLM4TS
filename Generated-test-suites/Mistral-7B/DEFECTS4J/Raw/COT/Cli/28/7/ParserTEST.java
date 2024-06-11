import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 parser = new DefaultParser();
 options = new Options();
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 CommandLine cmd = parser.parse(options, new String[0]);
 assertNull(cmd.getArgs());
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithSingleArgument() throws ParseException {
 CommandLine cmd = parser.parse(options, new String[]{"arg1"});
 assertTrue(cmd.getArgs().length == 1);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithOption() throws ParseException {
 options.addOption("o", "opt", false, "Option test");
 CommandLine cmd = parser.parse(options, new String[]{"-o"});
 assertTrue(cmd.hasOption("opt"));
 assertFalse(cmd.getArgs().length > 0);
 }

 @Test
 public void testParseWithValueOption() throws ParseException {
 options.addOption("v", "value", true, "Value Option test");
 CommandLine cmd = parser.parse(options, new String[]{"-v", "value1"});
 assertTrue(cmd.hasOption("value"));
 assertEquals("value1", cmd.getOptionValue("value"));
 assertFalse(cmd.getArgs().length > 0);
 }

 @Test
 public void testParseWithRequiredOption() throws ParseException {
 options.addOption("r", "required", true, "Required Option test");
 CommandLine cmd = parser.parse(options, new String[0]);
 throwException(ParseException.class, "Missing required option: required");
 }

 @Test
 public void testParseWithRequiredOptionAndValue() throws ParseException {
 options.addOption("r", "required", true, "Required Option test");
 CommandLine cmd = parser.parse(options, new String[]{"-r", "value1"});
 assertTrue(cmd.hasOption("required"));
 assertEquals("value1", cmd.getOptionValue("required"));
 }

 @Test
 public void testParseWithMultipleOptions() throws ParseException {
 options.addOption("o1", "opt1", false, "Option 1 test");
 options.addOption("o2", "opt2", false, "Option 2 test");
 CommandLine cmd = parser.parse(options, new String[]{"-o1", "-o2"});
 assertTrue(cmd.hasOption("opt1"));
 assertTrue(cmd.hasOption("opt2"));
 assertFalse(cmd.getArgs().length > 0);
 }

 @Test
 public void testParseWithMixedOptionsAndArguments() throws ParseException {
 options.addOption("o", "opt", false, "Option test");
 CommandLine cmd = parser.parse(options, new String[]{"-o", "arg1", "-o2", "arg2"});
 assertTrue(cmd.hasOption("opt"));
 assertTrue(cmd.hasOption("opt2"));
 assertEquals(2, cmd.getArgs().length);
 assertEquals("arg1", cmd.getArgs()[0]);
 assertEquals("arg2", cmd.getArgs()[1]);
 }

 @Test
 public void testParseWithLongOptions() throws ParseException {
 options.addOption("l1", "long-option-1", false, "Long option 1 test");
 options.addOption("l2", "long-option-2", false, "Long option 2 test");
 CommandLine cmd = parser.parse(options, new String[]{"--long-option-1", "--long-option-2"});
 assertTrue(cmd.hasOption("longOption1"));
 assertTrue(cmd.hasOption("longOption2"));
 assertFalse(cmd.getArgs().length > 0);
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("opt1", "value1");
 properties.setProperty("opt2.name", "value2");
 CommandLine cmd = parser.parse(options, null, properties);
 assertTrue(cmd.hasOption("opt1"));
 assertEquals("value1", cmd.getOptionValue("opt1"));
 assertTrue(cmd.hasOption("opt2"));
 assertEquals("value2", cmd.getOptionValue("opt2.name"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 options.addOption("r", "required", true, "Required Option test");
 parser.parse(options, new String[0]);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 options.addOption("u", "unrecognized", false, "Unrecognized Option test");
 CommandLine cmd = parser.parse(options, new String[]{"-u"});
 }

 // Helper methods
 private void throwException(Class<? extends ParseException> expected, String message) throws ParseException {
 ParseException e = null;
 try {
 parser.parse(options, new String[0]);
 } catch (ParseException ex) {
 e = ex;
 }
 assertTrue(expected.isInstance(e));
 assertEquals(message, e.getMessage());
}
}