import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 Options options = new Options();
 parser = new DefaultParser();
 parser.setOptions(options);
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("2", cmd.getOptionValue("optionB"));
 assertEquals(0, cmd.getArgs().length);
 }

 @Test
 public void testParseWithOptionalArguments() throws ParseException {
 String[] arguments = new String[]{"-a", "1", "--optionB", "2", "--optionC"};
 Options options = new Options();
 Option optionC = new Option("c", "cVal", false, "Optional C");
 options.addOption(optionC);
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("2", cmd.getOptionValue("optionB"));
 assertTrue(cmd.hasOption("c"));
 assertNull(cmd.getOptionValue("c"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 String[] arguments = new String[]{"-a"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Should have thrown a MissingArgumentException");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 String[] arguments = new String[]{"--unknownOption"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Should have thrown an UnrecognizedOptionException");
 }

 @Test
 public void testParseWithLongOptions() throws ParseException {
 String[] arguments = new String[]{"--longOptionA", "1", "--longOptionB=2"};
 Options options = new Options();
 Option optionA = new Option("long-option-a", "longOptionA", false, "Long option A");
 Option optionB = new Option("long-option-b", "longOptionB", true, "Long option B");
 options.addOption(optionA);
 options.addOption(optionB);
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals("1", cmd.getOptionValue("long-option-a"));
 assertTrue(cmd.hasOption("long-option-b"));
 assertNull(cmd.getOptionValue("long-option-b"));
 }

 @Test
 public void testParseWithMultipleValuesForAnOption() throws ParseException {
 String[] arguments = new String[]{"-a", "1", "--optionB", "2", "--optionB", "3"};
 Options options = new Options();
 Option optionB = new Option("b", "values", true, "Option B");
 options.addOption(optionB);
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals(Arrays.asList("2", "3"), cmd.getOptionValues("optionB"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArgumentValues() throws ParseException {
 String[] arguments = new String[]{"-a", "1", "--optionB=invalid"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Should have thrown a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptions() throws ParseException {
 String[] arguments = new String[]{"--invalidOption"};
 CommandLine cmd = parser.parse(options, arguments);
 fail("Should have thrown a ParseException");
 }
}