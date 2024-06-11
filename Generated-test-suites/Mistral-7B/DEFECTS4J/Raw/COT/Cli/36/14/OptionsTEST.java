import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "alias", true, "An alias option")
 .addOption("b", "booleanOption", false, "A boolean option")
 .addOption("c", "charOption", true, "A char option")
 .addOption("d", "digitOption", true, "A digit option")
 .addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("e", "longOptionGroup1", true, "A long option in group 1")
 .addOption("f", "shortOptionGroup1", 'f', "A short option in group 1")
 )
 .addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("g", "longOptionGroup2", true, "A long option in group 2")
 .addOption("h", "shortOptionGroup2", 'h', "A short option in group 2")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");

 assertEquals("alias", optionA.getLongOpt());
 assertEquals(true, optionA.isOptional());
 assertNull(optionA.getValue());

 assertEquals("booleanOption", optionB.getLongOpt());
 assertFalse(optionB.isOptional());
 assertNull(optionB.getValue());

 assertEquals("An alias option", optionA.getDescription());
 assertNull(optionB.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 Options options = new Options();
 try {
 options.addOption(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = createOptions();
 Option optionD = options.getOption("d");

 assertEquals("digitOption", optionD.getLongOpt());
 assertTrue(optionD.isOptional());
 assertNull(optionD.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = new Options();
 Option optionA = options.addOption("a", "alias", true, "An alias option");
 try {
 options.addOption("a", "anotherAlias", true, "Another alias option");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOpt() {
 Options options = new Options();
 Option optionA = options.addOption("a", "alias", true, "An alias option");
 try {
 options.addOption("--alias", "alias2", true, "Another alias option");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidShortOpt() {
 Options options = new Options();
 try {
 options.addOption('0', "zero", true, "A zero option");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup optionGroup1 = options.getOptionGroup("group1");
 OptionGroup optionGroup2 = options.getOptionGroup("group2");

 assertEquals("Group 1", optionGroup1.getName());
 assertEquals("group1", optionGroup1.getOptParser().getLongOptPrefix());
 assertEquals('-', optionGroup1.getOptParser().getOptPrefix());
 assertEquals(1, optionGroup1.getOptions().size());
 assertEquals(2, optionGroup2.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOptionGroup() {
 Options options = new Options();
 try {
 options.addOptionGroup(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetOptions() {
 Options options = createOptions();
 List<Option> optionsList = options.getOptions();

 assertEquals(11, optionsList.size());
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("--alias"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("d"));
 assertTrue(options.hasOption("--digitOption"));
 assertTrue(options.hasOption("e"));
 assertTrue(options.hasOption("f"));
 assertTrue(options.hasOption("--shortOptionGroup1"));
 assertTrue(options.hasOption("g"));
 assertTrue(options.hasOption("h"));
 }

 @Test
 public void testGetOptionWithMissingOption() {
 Options options = createOptions();
 assertNull(options.getOption("missingOption"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionWithNullOptions() {
 Options options = new Options();
 try {
 options.getOptions();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 List<String> optionsList = options.getMatchingOptions("gr");

 assertEquals(2, optionsList.size());
 assertEquals("group2", optionsList.get(0));
 assertEquals("g", optionsList.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMatchingOptionsWithEmptyPrefix() {
 Options options = new Options();
 try {
 options.getMatchingOptions("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMatchingOptionsWithInvalidPrefix() {
 Options options = new Options();
 try {
 options.getMatchingOptions("x");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithValidOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createOptions();
 String[] args = {"-a", "valueA", "--booleanOption", "--group1", "-f", "valueF"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals(args.length, cmd.getArgs().length);

 Option optionA = options.getOption("a");
 assertEquals("valueA", optionA.getValue());
 Option optionB = options.getOption("b");
 assertFalse(optionB.getValue() != null);
 Option optionC = options.getOption("c");
 assertNull(optionC.getValue());
 Option optionD = options.getOption("d");
 assertNull(optionD.getValue());
 Option optionE = options.getOption("e");
 assertNull(optionE.getValue());
 Option optionF = options.getOption("f");
 assertEquals("valueF", optionF.getValue());
 Option optionG = options.getOption("g");
 assertNull(optionG.getValue());
 Option optionH = options.getOption("h");
 assertNull(optionH.getValue());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createOptions();
 String[] args = {"--invalidOption"};
 try {
 parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createOptions();
 String[] args = {"--booleanOption"};
 try {
 parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 assertEquals("Error parsing command line: The following required options do not have values, and no default values were provided: b", e.getMessage());
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingAllRequiredOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = createOptions();
 String[] args = {};
 try {
 parser.parse(options, args);
 fail("Expected ParseException");
 } catch (ParseException e) {
 assertEquals("Error parsing command line: All required options must be provided", e.getMessage());
 // Expected
 }
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 assertEquals("[ Options: [ short {" +
 "a={alias, true}" +
 "c={}" +
 "d={}" +
 "} ] [ long {" +
 "alias={alias, true}" +
 "booleanOption={}" +
 "digitOption={}" +
 "group1={group1, true}" +
 "shortOptionGroup1={shortOptionGroup1, false}" +
 "} ]]", options.toString());
 }
}