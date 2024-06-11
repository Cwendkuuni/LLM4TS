import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 // Initialize the Options object with some test options
 options = new Options();
 options.addOption("a", "alpha", false, "Option A");
 options.addOption("b", "beta", true, "Option B");
 options.addOption("c", "chi", 3, "Option C with an integer value");
 options.addOptionGroup(new OptionGroup("group1", "Group 1", true, "Options in group 1"));
 options.addOptionGroup(new OptionGroup("group2", "Group 2", false, "Options in group 2"));
 options.addOptionGroup(new OptionGroup("group3", "Group 3", false, "Options in group 3"));
 }

 @Test
 public void testAddOption() {
 Option optA = options.getOption("a");
 assertEquals("Option A", optA.getLongOpt());
 assertEquals(false, optA.isRequired());
 }

 @Test
 public void testAddOptionWithLongName() {
 Option optAlpha = options.getOption("alpha");
 assertEquals("alpha", optAlpha.getLongOpt());
 assertEquals(false, optAlpha.isRequired());
 }

 @Test
 public void testAddOptionWithValue() {
 Option optC = options.getOption("c");
 assertEquals("chi", optC.getLongOpt());
 assertEquals(3, optC.getValue().intValue());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group1 = options.getOptionGroup("group1");
 assertTrue(group1.isRequired());
 }

 @Test
 public void testHasShortOption() {
 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("b"));
 assertFalse(options.hasShortOption("z"));
 }

 @Test
 public void testHasLongOption() {
 assertTrue(options.hasLongOption("alpha"));
 assertTrue(options.hasLongOption("beta"));
 assertFalse(options.hasLongOption("gamma"));
 }

 @Test
 public void testHasOption() {
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alpha"));
 assertFalse(options.hasOption("z"));
 }

 @Test
 public void testGetMatchingOptions() {
 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertTrue(matchingOptions.containsAll(Arrays.asList("optgroup1", "optb")));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"-z"});
 fail("ParseException expected");
 }

 @Test
 public void testParseWithValidOptions() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "--chi=42"});
 assertEquals("-a", cmd.getOptions().get(0).getOpt());
 assertEquals("-b", cmd.getOptions().get(1).getOpt());
 assertEquals("42", cmd.getOptionValues("chi")[0]);
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"--group1"});
 assertTrue(cmd.getOptions().containsAll(options.getOptionGroup("group1").getOptions()));
 }

 @Test
 public void testToString() {
 String optionsString = options.toString();
 assertTrue(optionsString.contains("[ Options: [ short"));
 assertTrue(optionsString.contains("[ long"));
 }
}