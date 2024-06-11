import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option opt = new Option("o", "opt", false, "Option test");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createOptions();
 Option opt = new Option("o", "opt", true, "Option test with argument");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = createOptions();
 Option opt = new Option("o", "option", false, "Option test with long name");
 options.addOption(opt);
 assertTrue(options.hasOption("option"));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("group", "Test Option Group");
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group"));
 }

 @Test
 public void testAddRequiredOption() {
 Options options = createOptions();
 Option opt = new Option("o", "opt", false, "Option test");
 options.addOption(opt);
 opt.setRequired(true);
 options.addOptionGroup(new OptionGroup("group", "Test Option Group"));
 List<Object> requiredOpts = options.getRequiredOptions();
 assertTrue(requiredOpts.contains(opt));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionTwice() {
 Options options = createOptions();
 Option opt = new Option("o", "opt", false, "Option test");
 options.addOption(opt);
 options.addOption(opt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOption() {
 Options options = createOptions();
 Option opt = null;
 options.addOption(opt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullShortOpt() {
 Options options = createOptions();
 Option opt = new Option(null, "opt", false, "Option test");
 options.addOption(opt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullLongOpt() {
 Options options = createOptions();
 Option opt = new Option("o", null);
 options.addOption(opt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = createOptions();
 Option opt = new Option("o", "opt", false, null);
 options.addOption(opt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOpt() {
 Options options = createOptions();
 Option opt1 = new Option("l", "longOpt", false, "First long option");
 Option opt2 = new Option("l", "longOpt", false, "Second long option");
 options.addOption(opt1);
 options.addOption(opt2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOpt() {
 Options options = createOptions();
 Option opt1 = new Option('o', "shortOpt", false, "First short option");
 Option opt2 = new Option('o', "shortOpt", false, "Second short option");
 options.addOption(opt1);
 options.addOption(opt2);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 Option opt1 = new Option("lo", "longOpt1", false, "First long option");
 Option opt2 = new Option("log", "longOpt2", false, "Second long option");
 Option opt3 = new Option("shortOpt", "Short opt", false, "Short option");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> matchingOpts = options.getMatchingOptions("log");
 assertEquals(1, matchingOpts.size());
 assertEquals("logOpt2", matchingOpts.get(0));
 }

}