import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsInstance() {
 Options options = new Options();
 assertNotNull(options);
 }

 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 Option optionA = options.getOption("a");
 assertNotNull(optionA);
 assertEquals("optionA", optionA.getLongOpt());
 assertFalse(optionA.hasArg());
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = new Options();
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 Option optionB = options.getOption("b");
 assertNotNull(optionB);
 assertEquals("optionB", optionB.getLongOpt());
 assertTrue(optionB.hasArg());
 }

 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 options.addOption("optionC", true, "An option with long name optionC and argument");
 Option optionC = options.getOption("optionC");
 assertNotNull(optionC);
 assertNull(optionC.getShortOpt());
 assertTrue(optionC.hasArg());
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = new Options();
 options.addOption("optionD", true, "An option with long name optionD and required argument");
 Option optionD = options.getOption("optionD");
 assertNotNull(optionD);
 assertNull(optionD.getShortOpt());
 assertTrue(optionD.hasArg());
 assertFalse(optionD.isRequired());
 }

 @Test
 public void testAddOptionRequired() {
 Options options = new Options();
 options.addOption("optionE", false, "An option with long name optionE and is required");
 Option optionE = options.getOption("optionE");
 assertNotNull(optionE);
 assertNull(optionE.getShortOpt());
 assertFalse(optionE.hasArg());
 assertTrue(optionE.isRequired());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "My Option Group");
 options.addOptionGroup(group);
 Option optionG = new Option("g", "optionG", false, "An optionG in my group");
 group.addOption(optionG);
 Option optionH = new Option("h", "optionH", false, "An optionH in my group");
 group.addOption(optionH);
 Option optionI = new Option("i", "optionI", false, "An optionI in my group and is required");
 group.addOption(optionI);
 group.setRequired(true);

 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Option optionE = options.getOption("e");
 Option optionF = options.getOption("f"); // should be null
 Option optionG = options.getOption("g");
 Option optionH = options.getOption("h");
 Option optionI = options.getOption("i");
 Option optionJ = options.getOption("j"); // should be null

 assertNull(optionF);
 assertNull(optionJ);
 assertNotNull(optionA);
 assertNotNull(optionB);
 assertNotNull(optionC);
 assertNull(optionD);
 assertNotNull(optionE);
 assertNotNull(optionG);
 assertNotNull(optionH);
 assertNotNull(optionI);
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 options.addOption("c", "optionC", true, "An option with long name optionC and argument");
 List<Option> optionsList = options.getOptions();
 assertEquals(3, optionsList.size());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 options.addOption("c", "optionC", true, "An option with long name optionC and argument");

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 options.addOption("optionA", false, "An option with long name optionA");
 options.addOption("optionB", true, "An option with long name optionB and argument");
 options.addOption("optionC", false, "An option with long name optionC");

 assertTrue(options.hasLongOption("optionA"));
 assertTrue(options.hasLongOption("optionB"));
 assertTrue(options.hasLongOption("optionC"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 options.addOption("c", "optionC", true, "An option with long name optionC and argument");

 assertTrue(options.hasShortOption("a")); // short name 'a' maps to 'optionA'
 assertTrue(options.hasShortOption("b")); // short name 'b' maps to 'optionB'
 assertFalse(options.hasShortOption("c")); // no short name for 'optionC'
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 options.addOption("optionA", true, "An option with long name optionA");
 options.addOption("optionB", true, "An option with long name optionB");
 options.addOption("optionC", true, "An option with long name optionC");
 options.addOption("optionD", true, "An option with long name optionD");

 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(3, matchingOptions.size());
 assertEquals("optionB", matchingOptions.get(0));
 assertEquals("optionC", matchingOptions.get(1));
 assertEquals("optionD", matchingOptions.get(2));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 options.addOption("optionA", true, "An option with long name optionA");
 options.addOption("optionB", true, "An option with long name optionB and argument");
 options.addOption("optionC", false, "An option with long name optionC that doesn't take an argument");

 Option optionA = options.getOption("optionA");
 Option optionB = options.getOption("optionB");
 Option optionC = options.getOption("optionC");

 assertEquals(optionA, options.getOption("optionA"));
 assertEquals(optionB, options.getOption("optionB"));
 assertNull(options.getOption("optionD"));
 assertNull(options.getOption("nonexistent"));
 }

 @Test
 public void testParse() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 options.addOption("c", "optionC", true, "An option with long name optionC and argument");

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "test", "--optionB", "valueB"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Option optionA = cmd.getOption("a");
 Option optionB = cmd.getOption("optionB");
 assertEquals("test", optionB.getValue());
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testToString() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "An option with short name a");
 options.addOption("b", "optionB", true, "An option with short name b and argument");
 options.addOption("c", "optionC", true, "An option with long name optionC and argument");

 String optionsString = options.toString();
 assertTrue(optionsString.contains("short"));
 assertTrue(optionsString.contains("optionA"));
 assertTrue(optionsString.contains("false"));
 assertTrue(optionsString.contains("long"));
 assertTrue(optionsString.contains("optionC"));
 assertTrue(optionsString.contains("true"));
 }
}