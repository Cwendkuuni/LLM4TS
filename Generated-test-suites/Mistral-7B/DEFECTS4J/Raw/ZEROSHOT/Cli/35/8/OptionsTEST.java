import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testAddOption() {
 Option opt = new Option("o", "output", true, "Output file path");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 assertEquals("Output file path", options.getOptions().get(0).getLongOpt());
 }

 @Test
 public void testAddRequiredOption() {
 Option opt = new Option("o", "output", true, "Output file path");
 options.addOption(opt);
 options.addOptionGroup(new OptionGroup("g", "group", false, "Option group"));
 Option optGroupOption = new Option("g", null, false, "Option group");
 optGroupOption.setRequired(true);
 options.addOption(optGroupOption);
 options.addOptionGroup(new OptionGroup("g2", "group2", false, "Option group2"));

 assertEquals(3, options.getOptions().size());
 assertEquals(3, options.getRequiredOptions().size());

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "file.txt", "-g"});
 fail("Expected MissingOptionException for required -g option");
 } catch (ParseException e) {
 // expected
 }

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "file.txt", "-g2"});
 fail("Expected MissingOptionException for required -g option");
 } catch (ParseException e) {
 // expected
 }

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "file.txt"});
 assertEquals("-output", cmd.getOptionNames().get(0));
 assertEquals("file.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 fail("Unexpected ParseException");
 }
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("g", "group", false, "Option group");
 Option opt1 = new Option("o1", "option1", false, "Option 1");
 Option opt2 = new Option("o2", "option2", false, "Option 2");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);

 assertEquals(3, options.getOptions().size());
 assertEquals(3, options.getRequiredOptions().size());
 assertEquals(2, group.getOptions().size());
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption(new Option("a", null, false, "Option A"));
 options.addOption(new Option("b1", null, false, "Option B1"));
 options.addOption(new Option("b2", null, false, "Option B2"));
 options.addOption(new Option("c", null, false, "Option C"));

 List<String> matchingOptions = options.getMatchingOptions("b");
 assertEquals(1, matchingOptions.size());
 assertEquals("b1", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 options.addOption(new Option("a", null, false, "Option A"));
 options.addOption(new Option("b1", null, false, "Option B1"));
 options.addOption(new Option("b2", null, false, "Option B2"));
 options.addOption(new Option("c", null, false, "Option C"));

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b1"));
 assertTrue(options.hasOption("b2"));
 assertTrue(options.hasOption("c"));
 }

 @Test
 public void testHasShortOption() {
 options.addOption(new Option('a', null, false, "Option A"));
 options.addOption(new Option('b1', null, false, "Option B1"));
 options.addOption(new Option('b2', null, false, "Option B2"));
 options.addOption(new Option('c', null, false, "Option C"));

 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("b1"));
 assertTrue(options.hasShortOption("b2"));
 assertTrue(options.hasShortOption("c"));
 }

 @Test
 public void testHasLongOption() {
 options.addOption(new Option('a', "optionA", false, "Option A"));
 options.addOption(new Option('b1', "optionB1", false, "Option B1"));
 options.addOption(new Option('b2', "optionB2", false, "Option B2"));
 options.addOption(new Option('c', "optionC", false, "Option C"));

 assertTrue(options.hasLongOption("optionA"));
 assertTrue(options.hasLongOption("optionB1"));
 assertTrue(options.hasLongOption("optionB2"));
 assertTrue(options.hasLongOption("optionC"));
 }
}