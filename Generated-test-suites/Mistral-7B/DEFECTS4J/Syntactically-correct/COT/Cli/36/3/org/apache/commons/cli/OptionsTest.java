import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group1", "Group 1");
 options.addOptionGroup(optionGroup);

 assertEquals(1, options.getOptionGroups().size());
 assertSame(optionGroup, options.getOptionGroups().iterator().next());
 }

 @Test
 public void testAddOptionWithShortName() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short name");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithShortNameAndLongName() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short and long name");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = new Options();
 Option opt = new Option("o", "opt", true, "Option with short name and argument");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option opt = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithLongNameAndShortName() {
 Options options = new Options();
 Option opt = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));

 Option shortOpt = new Option("o", "opt", false, "Option with short name");
 options.addOption(shortOpt);

 assertEquals(2, options.getOptions().size());
 assertSame(shortOpt, options.getOptions().get(1));
 }

 @Test
 public void testAddOptionWithLongNameAndArgument() {
 Options options = new Options();
 Option opt = new Option("longOpt", "long option", true, "Option with long name and argument");
 options.addOption(opt);

 assertEquals(1, options.getOptions().size());
 assertSame(opt, options.getOptions().get(0));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short name");
 options.addOption(opt);

 Option option = options.getOption("o");
 assertSame(opt, option);
 }

 @Test
 public void testGetOptionWithLongName() {
 Options options = new Options();
 Option opt = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(opt);

 Option option = options.getOption("longOpt");
 assertSame(opt, option);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option opt1 = new Option("longOpt1", "long opt 1", false, "Option 1");
 Option opt2 = new Option("longOpt2", "long opt 2", false, "Option 2");
 Option opt3 = new Option("longOpt3", "long opt 3", false, "Option 3");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);

 List<String> optionsWithPrefix = options.getMatchingOptions("longOpt");
 assertEquals(2, optionsWithPrefix.size());
 assertTrue(optionsWithPrefix.contains("longOpt1"));
 assertTrue(optionsWithPrefix.contains("longOpt2"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short name");
 options.addOption(opt);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("opt"));
 assertTrue(options.hasOption("O"));
 assertTrue(options.hasOption("OPT"));
 assertTrue(options.hasLongOption("longOpt"));
 assertTrue(options.hasLongOption("LongOpt"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short name");
 options.addOption(opt);

 assertTrue(options.hasShortOption("o"));
 assertTrue(options.hasShortOption("O"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option opt = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(opt);

 assertTrue(options.hasLongOption("longOpt"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group1", "Group 1");
 Option opt = new Option("o", "opt", false, "Option with short name");
 optionGroup.addOption(opt);
 options.addOptionGroup(optionGroup);

 OptionGroup result = options.getOptionGroup(opt);
 assertSame(optionGroup, result);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group1", "Group 1");
 Option opt1 = new Option("o1", "opt1", false, "Option 1");
 Option opt2 = new Option("o2", "opt2", false, "Option 2");
 Option opt3 = new Option("o3", "opt3", false, "Option 3");
 optionGroup.addOption(opt1);
 optionGroup.addOption(opt2);
 options.addOptionGroup(optionGroup);

 assertEquals(2, options.getRequiredOptions().size());
 assertSame(opt1, options.getRequiredOptions().get(0));
 assertSame(opt2, options.getRequiredOptions().get(1));
 }

 @Test
 public void testGetOptionWithMissingName() {
 Options options = new Options();
 Option opt = new Option("o", "opt", false, "Option with short name");
 options.addOption(opt);

 assertNull(options.getOption("missingName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortName() {
 Options options = new Options();
 Option opt1 = new Option("o", "opt1", false, "Option 1");
 options.addOption(opt1);
 Option opt2 = new Option("o", "opt2", false, "Option 2");

 options.addOption(opt2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongName() {
 Options options = new Options();
 Option opt1 = new Option("longOpt1", "longOpt1", false, "Option 1");
 options.addOption(opt1);
 Option opt2 = new Option("longOpt1", "longOpt2", false, "Option 2");

 options.addOption(opt2);
 }

 @Test
 public void testAddOptionWithNullOption() {
 Options options = new Options();
 Option opt = null;

 try {
 options.addOption(opt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}