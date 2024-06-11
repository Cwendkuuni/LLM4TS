import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options createDefaultOptions() {
 return new Options()
 .addOption("o", "output", true, "Output file for the generated reports")
 .addOption("h", "help", false, "Print help message");
 }

 @Test
 public void testGetOptions() {
 Options options = createDefaultOptions();
 List<Option> optionsList = options.getOptions();

 assertEquals(2, optionsList.size());

 Option outputOption = optionsList.get(0);
 assertEquals("o", outputOption.getOpt());
 assertEquals("output", outputOption.getLongOpt());
 assertTrue(outputOption.hasArg());
 assertEquals("Output file for the generated reports", outputOption.getDescription());

 Option helpOption = optionsList.get(1);
 assertEquals("h", helpOption.getOpt());
 assertEquals("help", helpOption.getLongOpt());
 assertFalse(helpOption.hasArg());
 assertEquals("Print help message", helpOption.getDescription());
 }

 @Test
 public void testHasOption() {
 Options options = createDefaultOptions();

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("output"));
 assertTrue(options.hasOption("h"));
 assertTrue(options.hasOption("help"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createDefaultOptions();

 assertTrue(options.hasShortOption("o"));
 assertTrue(options.hasShortOption("output"));
 assertTrue(options.hasShortOption("h"));
 assertTrue(options.hasShortOption("help"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createDefaultOptions();

 assertTrue(options.hasLongOption("output"));
 assertTrue(options.hasLongOption("help"));
 assertFalse(options.hasLongOption("non-existent"));
 }

 @Test
 public void testGetOption() {
 Options options = createDefaultOptions();
 Option outputOption = options.getOption("o");
 Option helpOption = options.getOption("h");

 assertNotNull(outputOption);
 assertNotNull(helpOption);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullOption() {
 Options options = createDefaultOptions();
 Option nullOption = options.getOption(null);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createDefaultOptions();
 List<String> matchingOptions = options.getMatchingOptions("optio");

 assertEquals(1, matchingOptions.size());
 assertEquals("output", matchingOptions.get(0));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("testGroup", "Test group");
 options.addOptionGroup(optionGroup);

 assertTrue(options.hasOptionGroup("testGroup"));
 }

 @Test
 public void testParsingOptions() throws ParseException {
 Options options = createDefaultOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-h"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("h"));
 assertFalse(cmd.hasOption("o"));
 }

 @Test
 public void testParsingOptionsWithValue() throws ParseException {
 Options options = createDefaultOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("o"));
 assertEquals("output.txt", cmd.getOptionValue("o"));
 }

 @Test(expected = ParseException.class)
 public void testParsingMissingOption() throws ParseException {
 Options options = createDefaultOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-x"};
 CommandLine cmd = parser.parse(options, args);
 }

 @Test(expected = ParseException.class)
 public void testParsingMissingValueOption() throws ParseException {
 Options options = createDefaultOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o"};
 CommandLine cmd = parser.parse(options, args);
 }

 @Test
 public void testParsingValueOption() throws ParseException {
 Options options = createDefaultOptions();CommandLineParser parser = new DefaultParser();String[] args = {"-o", "output.txt"};CommandLine cmd = parser.parse(options, args);assertTrue(cmd.hasOption("o"));assertEquals("output.txt", cmd.getOptionValue("o")); }

 @Test
 public void testParsingValueOptionalOption() throws ParseException {
 Options options = createDefaultOptions();CommandLineParser parser = new DefaultParser();String[] args = {"-h"};CommandLine cmd = parser.parse(options, args);assertTrue(cmd.hasOption("h"));assertNull(cmd.getOptionValue("h")); }

 @Test
 public void testToString() {
 Options options = createDefaultOptions();
 String toString = options.toString();

 assertTrue(toString.contains("[ Options:"));
 assertTrue(toString.contains("[ short"));
 assertTrue(toString.contains("[ long"));
 }
}