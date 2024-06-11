import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option option = new Option("o", "opt", false, "Option example");
 options.addOption(option);
 Assert.assertNotNull(options.getOption("o"));
 Assert.assertFalse(options.getOption("o").isRequired());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = createOptions();
 Option option = new Option("r", "req", true, "Required option example");
 options.addOption(option);
 Assert.assertTrue(options.getOption("r").isRequired());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createOptions();
 Option option = new Option("a", "arg", true, "Option with argument example");
 options.addOption(option);
 Assert.assertEquals("arg", options.getOption("a").getLongOpt());
 Assert.assertTrue(options.getOption("a").hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("g", "Group example");
 Option option = new Option("g", null, false, "Group option example");
 group.addOption(option);
 options.addOptionGroup(group);
 Assert.assertNotNull(options.getOptionGroup("g"));
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 Option option = new Option("o", "opt", false, "Option example");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("o"));
 Assert.assertTrue(options.hasOption("opt"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();
 Option option = new Option("o", "opt", false, "Option example");
 options.addOption(option);
 Assert.assertTrue(options.hasLongOption("opt"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();
 Option option = new Option("o", "opt", false, "Option example");
 options.addOption(option);
 Assert.assertTrue(options.hasShortOption("o"));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option example");
 options.addOption(option1);
 Option option2 = new Option("o", "opt", false, "Option example");
 options.addOption(option2);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateLongOption() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option example");
 options.addOption(option1);
 Option option2 = new Option("o", "opt2", false, "Option example");
 options.addOption(option2);
 }
}