import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testOptionsEmpty() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();

 Assert.assertEquals(Collections.emptyList(), options.getOptions());
 }

 @Test
 public void testOptionsWithShortOptions() {
 Options options = new Options();

 options.addOption("a", "option-a", false, "Option A");
 options.addOption("b", "option-b", true, "Option B");

 Assert.assertEquals(2, options.getOptions().size());

 Option optA = options.getOption("a");
 Option optB = options.getOption("b");

 Assert.assertEquals("Option A", optA.getLongOpt());
 Assert.assertFalse(optA.hasArg());
 Assert.assertFalse(optA.isRequired());

 Assert.assertEquals("Option B", optB.getLongOpt());
 Assert.assertTrue(optB.hasArg());
 Assert.assertFalse(optB.isRequired());
 }

 @Test
 public void testOptionsWithLongOptions() {
 Options options = new Options();

 options.addOption("a", false, "Option A");
 options.addOption("option-b", true, "Option B");

 Assert.assertEquals(2, options.getOptions().size());

 Option optA = options.getOption("a");
 Option optB = options.getOption("option-b");

 Assert.assertNull(optA.getShortOpt());
 Assert.assertFalse(optA.hasArg());
 Assert.assertFalse(optA.isRequired());

 Assert.assertEquals("option-b", optB.getShortOpt());
 Assert.assertTrue(optB.hasArg());
 Assert.assertFalse(optB.isRequired());
 }

 @Test
 public void testOptionsWithRequiredOptions() {
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("Required Options", Arrays.asList(
 new Option("r", "required", true, "Required option"),
 new Option("s", "second-required", true, "Second required option"))));

 Assert.assertEquals(2, options.getRequiredOptions().size());

 Option opt1 = (Option) options.getRequiredOptions().get(0);
 Option opt2 = (Option) options.getRequiredOptions().get(1);

 Assert.assertEquals("required", opt1.getKey());
 Assert.assertNull(opt1.getLongOpt());
 Assert.assertTrue(opt1.isRequired());
 Assert.assertNull(opt1.getValue());

 Assert.assertEquals("second-required", opt2.getKey());
 Assert.assertNull(opt2.getLongOpt());
 Assert.assertTrue(opt2.isRequired());
 Assert.assertNull(opt2.getValue());
 }

 @Test
 public void testOptionsWithOptionGroups() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("Group 1", Arrays.asList(
 new Option("g1", "group-option-1", false, "Group option 1"),
 new Option("g2", "group-option-2", false, "Group option 2")
 ));
 options.addOptionGroup(group);

 Assert.assertEquals(2, group.getOptions().size());

 Option opt1 = group.getOption("g1");
 Option opt2 = group.getOption("g2");

 Assert.assertEquals("Group 1", group.getKey());
 Assert.assertEquals(2, group.getOptions().size());

 Assert.assertEquals("group-option-1", opt1.getKey());
 Assert.assertNull(opt1.getLongOpt());
 Assert.assertFalse(opt1.isRequired());
 Assert.assertNull(opt1.getValue());

 Assert.assertEquals("group-option-2", opt2.getKey());
 Assert.assertNull(opt2.getLongOpt());
 Assert.assertFalse(opt2.isRequired());
 Assert.assertNull(opt2.getValue());
 }

 @Test
 public void testOptionsWithAllOptions() {
 Options options = new Options();

 options.addOption("a", false, "Option A");
 options.addOption("option-b", true, "Option B");
 options.addOptionGroup(new OptionGroup("Required Options", Arrays.asList(
 new Option("r", "required", true, "Required option"),
 new Option("s", "second-required", true, "Second required option"))));
 options.addOptionGroup(new OptionGroup("Group 1", Arrays.asList(
 new Option("g1", "group-option-1", false, "Group option 1"),
 new Option("g2", "group-option-2", false, "Group option 2")
 )));

 Assert.assertEquals(6, options.getOptions().size() + options.getRequiredOptions().size() + options.getOptionGroups().size());
 }

}