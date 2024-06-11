import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option opt = new Option("a", "a", false, "Option A");
 options.addOption(opt);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionWithDescription() {
 Option opt = new Option("b", null, false, "Option B");
 options.addOption(opt);
 assertTrue(options.hasOption("b"));
 assertEquals("Option B", options.getOption("b").getDescription());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option opt = new Option("c", "c", true, "Option C with argument");
 options.addOption(opt);
 assertTrue(options.hasOption("c"));
 assertTrue(options.getOption("c").hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidShortOption() {
 Option opt = new Option("0", "0", false, "Option 0");
 options.addOption(opt);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidLongOption() {
 Option opt = new Option("--0", "0", false, "Option 0");
 options.addOption(opt);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group");
 Option opt = new Option("g", null, false, "Group option", group);
 options.addOptionGroup(group);
 options.addOption(opt);
 assertTrue(options.hasOption("g"));
 assertEquals("Group", options.getOptionGroup("g").getName());
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = options.getOptions();
 assertTrue(optionsList.isEmpty());
 Option opt = new Option("x", "x", false, "Option X");
 options.addOption(opt);
 List<Option> newOptionsList = options.getOptions();
 assertTrue(newOptionsList.contains(opt));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyOptions() {
 List<Option> optionsList = options.getOptions();
 Option opt = optionsList.get(0);
 opt.setRequired(true);
 fail("An UnsupportedOperationException should have been thrown");
 }

 @Test
 public void testHasLongOption() {
 Option opt = new Option("long", "Long option", false, "Long option description");
 options.addOption(opt);
 assertTrue(options.hasLongOption("long"));
 }

 @Test
 public void testHasShortOption() {
 Option opt = new Option("a", "Option A", false, "Option A description");
 options.addOption(opt);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testGetOption() {
 Option opt = new Option("d", "D", false, "Option D");
 options.addOption(opt);
 Option result = options.getOption("d");
 assertEquals(opt, result);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetOptionNotFound() {
 Option result = options.getOption("e");
 fail("A NoSuchElementException should have been thrown");
 }

 @Test
 public void testGetMatchingOptions() {
 Option opt1 = new Option("long1", "Long option 1", false, "Long option 1 description");
 Option opt2 = new Option("long2", "Long option 2", false, "Long option 2 description");
 Option opt3 = new Option("long3", "Long option 3", false, "Long option 3 description");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> result = options.getMatchingOptions("long");
 assertEquals(3, result.size());
 }

 @Test
 public void testToString() {
 String expected = "[ Options: [ short {} ] [ long {} ]]";
 assertEquals(expected, options.toString());
 }
}