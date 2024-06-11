import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option opt = new Option("a", "optionA", false, "Description for option A");
 options.addOption(opt);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option opt = new Option("a", "optionA", true, "Description for option A");
 options.addOption(opt);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Option opt = new Option("a", "optionA", false, "Short description");
 opt.setLongOpt("longOptionA");
 options.addOption(opt);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("groupName", "Description for groupName");
 Option opt = new Option("a", "optionA", false, "Description for option A");
 group.addOption(opt);
 options.addOptionGroup(group);
 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(1, group.getOptions().size());
 }

 @Test
 public void testAddRequiredOption() {
 Option opt = new Option("a", "optionA", false, "Description for option A");
 options.addOption(opt);
 opt.setRequired(true);
 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertTrue(options.hasOption("a"));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Option opt1 = new Option("a", "optionA", false, "Description for option A");
 options.addOption(opt1);
 Option opt2 = new Option("a", "optionA", false, "Description for option A");
 options.addOption(opt2);
 }

 @Test
 public void testGetMatchingOptions() {
 Option opt1 = new Option("longOption1", "longOption1", false, "Description for longOption1");
 Option opt2 = new Option("longOption2", "longOption2", false, "Description for longOption2");
 Option opt3 = new Option("longOption1", "longOption1", false, "Description for longOption1");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> matchingOptions = options.getMatchingOptions("longOption1");
 Assert.assertEquals(2, matchingOptions.size());
 }

 @Test
 public void testToString() {
 options.addOption(new Option("a", "optionA", false, "Description for option A"));
 options.addOption(new Option("b", "optionB", false, "Description for option B"));
 Assert.assertEquals("[ Options: [ short {key='a', value='', longOpt=null, optional=true, hasArg=false, args=null, description='Description for option A'}, short {key='b', value='', longOpt=null, optional=true, hasArg=false, args=null, description='Description for option B'}] [ long {} ]]", options.toString());
 }
}