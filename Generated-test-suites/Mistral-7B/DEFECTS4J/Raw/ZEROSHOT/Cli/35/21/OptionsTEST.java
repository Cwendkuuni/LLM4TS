import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testAddOptionShort() {
 Option option = new Option("a", "optionA", false, "Option A");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionShortWithArg() {
 Option option = new Option("a", "optionA", true, "Option A with arg");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionLong() {
 Option option = new Option("longOptionA", "longOptionA", false, "Long Option A");
 options.addOption(option);
 assertTrue(options.hasOption("longOptionA"));
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 Option option = new Option("s", "longOptionB", false, "Short Option B, Long Option B");
 options.addOption(option);
 assertTrue(options.hasOption("s"));
 assertTrue(options.hasOption("longOptionB"));
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "My Group");
 Option option = new Option("g", "optionG", false, "Group Option", group);
 options.addOptionGroup(group);
 group.setRequired(true);

 assertEquals(1, options.getRequiredOptions().size());
 assertTrue(options.getRequiredOptions().get(0) instanceof OptionGroup);
 assertTrue(options.getRequiredOptions().get(0).getKey().equals("group"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option optionA = new Option("optionA", false, "Option A");
 Option optionB = new Option("optionB", false, "Option B");
 Option optionC = new Option("optionC", false, "Option C");
 options.addOption(optionA);
 options.addOption(optionB);
 options.addOption(optionC);

 List<String> optionsMatchingB = options.getMatchingOptions("option");
 assertEquals(1, optionsMatchingB.size());
 assertEquals(optionB.getKey(), optionsMatchingB.get(0));
 }

 @Test
 public void testParseShortOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 assertTrue(cmd.hasOption("a"));
 }

 @Test
 public void testParseShortOptionWithArg() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "arg"};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 assertEquals("arg", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseLongOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--longOptionA"};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 assertTrue(cmd.hasOption("longOptionA"));
 }

 @Test
 public void testParseOptionGroup() throws ParseException {
 OptionGroup group = new OptionGroup("group", "My Group");
 Option option = new Option("g", "optionG", false, "Group Option", group);
 group.setRequired(true);
 options.addOptionGroup(group);
 String[] args = {};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 assertNull(cmd.getOptionValue("g"));

 args = new String[]{"--group"};
 cmd = parser.parse(options.toBuilder().build(), args);
 assertFalse(cmd.hasOption("g"));
 assertNull(cmd.getOptionValue("g"));

 args = new String[]{"--group", "--optionG"};
 cmd = parser.parse(options.toBuilder().build(), args);
 assertTrue(cmd.hasOption("group"));
 assertTrue(cmd.hasOption("optionG"));
 }

 @Test(expected = ParseException.class)
 public void testParseMismatchedOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a"};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 // Should fail as "a" is a short option and "--" was used
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 OptionGroup group = new OptionGroup("group", "My Group");
 Option option = new Option("g", "optionG", false, "Group Option", group);
 group.setRequired(true);
 options.addOptionGroup(group);
 String[] args = {};
 CommandLine cmd = parser.parse(options.toBuilder().build(), args);
 // Should fail as "group" is a required OptionGroup
 }
}