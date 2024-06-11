import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 return new Options()
 .addOption("i", "inputFile", true, "input file")
 .addOption("o", "outputFile", true, "output file")
 .addOption("h", "help", false, "print this message");
 }

 @Test
 public void testBasicUsage() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[]{"i", "input.txt", "o", "output.txt"});
 assertEquals("input.txt", cmd.getOptionValue("inputFile"));
 assertEquals("output.txt", cmd.getOptionValue("outputFile"));
 assertFalse(cmd.hasOption("help"));
 } catch (ParseException e) {
 fail("Test parsing failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = createTestOptions();
 options.getOption("i").setRequired(true);
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[0]);
 fail("Missing required option");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testLongOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[]{"--inputFile", "input.txt", "--outputFile", "output.txt"});
 assertEquals("input.txt", cmd.getOptionValue("inputFile"));
 assertEquals("output.txt", cmd.getOptionValue("outputFile"));
 assertFalse(cmd.hasOption("help"));
 } catch (ParseException e) {
 fail("Test parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testDuplicateOption() {
 Options options = new Options().addOption("i", "input", false, "input option");
 options.addOption("i", "input2", false, "input option 2");
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[0]);
 fail("Should not be able to parse with duplicate options");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options()
 .addOption("a", "argument", true, "an argument")
 .addOption("b", "binding", false, "a binding")
 .addOption("x", "extra", false, "an extra option");

 List<String> matchingOptions = options.getMatchingOptions("a");
 assertEquals(1, matchingOptions.size());
 assertEquals("argument", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 Options options = new Options()
 .addOption("a", "argument", true, "an argument")
 .addOption("b", "binding", false, "a binding")
 .addOption("x", "extra", false, "an extra option");

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("x"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options()
 .addOption("inputFile", true, "input file")
 .addOption("outputFile", true, "output file")
 .addOption("help", false, "print this message");

 assertTrue(options.hasLongOption("inputFile"));
 assertTrue(options.hasLongOption("outputFile"));
 assertFalse(options.hasLongOption("random"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options()
 .addOption("i", "inputFile", true, "input file")
 .addOption("o", "outputFile", true, "output file")
 .addOption("h", "help", false, "print this message");

 assertTrue(options.hasShortOption("i"));
 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("z"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options()
 .addOptionGroup(new OptionGroup("group1", "group1")
 .addOption("g1", true, "option in group1"));

 OptionGroup optionGroup = options.getOptionGroup("group1");
 assertEquals("group1", optionGroup.getName());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group1", "group1")
 .addOption("g1", true, "option in group1");
 options.addOptionGroup(optionGroup);

 assertTrue(options.hasOption("g1"));
 }
}