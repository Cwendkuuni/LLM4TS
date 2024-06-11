import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 Options options = new Options();

 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", "optionC", false, "Option C in Group 1")
 .addOption("d", "optionD", false, "Option D in Group 1"));
 options.addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("e", "optionE", true, "Option E in Group 2")
 .addOption("f", "optionF", true, "Option F in Group 2"));

 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();

 Option optionA = options.getOption("a");
 assertNotNull(optionA);
 assertEquals("Option A", optionA.getLongOpt());
 assertFalse(optionA.hasArg());

 Option optionB = options.getOption("b");
 assertNotNull(optionB);
 assertEquals("optionB", optionB.getKey());
 assertTrue(optionB.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group1 = options.getOptionGroup("group1");
 OptionGroup group2 = options.getOptionGroup("group2");

 assertEquals(2, options.getOptionGroups().size());

 assertEquals("Group 1", group1.getName());
 assertEquals(2, group1.getOptions().size());

 assertEquals("Group 2", group2.getName());
 assertEquals(2, group2.getOptions().size());
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("d"));
 assertTrue(options.hasOption("e"));
 assertTrue(options.hasOption("f"));

 assertFalse(options.hasOption("x"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();

 List<String> optionsMatching = options.getMatchingOptions("optio");
 assertEquals(4, optionsMatching.size());
 assertTrue(optionsMatching.contains("optionA"));
 assertTrue(optionsMatching.contains("optionB"));
 assertTrue(optionsMatching.contains("optionC"));
 assertTrue(optionsMatching.contains("optionD"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionE = options.getOption("e");

 assertNotNull(optionA);
 assertNotNull(optionB);
 assertNotNull(optionC);
 assertNotNull(optionE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option optionA = new Option("a", "optionA", false, "Option A");
 options.addOption(optionA);
 try {
 options.addOption(optionA);
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate option: a", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = new Options();
 Option optionA = new Option("a", "optionA", false, "Option A");
 options.addOption(optionA);
 try {
 options.addOption("--optionA", optionA);
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate long option: --optionA", e.getMessage());
 }
 }
}