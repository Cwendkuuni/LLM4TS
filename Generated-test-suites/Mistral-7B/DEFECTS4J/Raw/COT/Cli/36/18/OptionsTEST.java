import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOptionShort() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", false, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionLong() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("output", "optOutput", false, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasOption("output"));
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("output", "optOutput", true, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasOption("output"));
 }

 @Test
 public void testAddOptionWithArgAndNoShort() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("output", "optOutput", true, "Output file path.");
 options.addOption(opt);
 assertFalse(options.hasOption("o"));
 }

 @Test
 public void testAddOptionWithLongAndShort() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 options.addOption(opt);
 Option optLong = new Option("output", "optOutput", true, "Output file path.");
 options.addOption(optLong);
 assertEquals(2, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("g", "group");
 Option opt = new Option("o", "output", true, "Output file path.");
 group.addOption(opt);
 options.addOptionGroup(group);
 assertTrue(options.getOptionGroups().contains(group));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = createOptionsUnderTest();
 Option opt1 = new Option("o", "output", true, "Output file path.");
 options.addOption(opt1);
 Option opt2 = new Option("o", "output", true, "Output file path.");
 options.addOption(opt2);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionToRequiedOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("g", "group");
 Option opt = new Option("o", "output", true, "Output file path.");
 opt.setRequired(true);
 group.addOption(opt);
 options.addOptionGroup(group);
 opt.setRequired(false);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();
 Option opt1 = new Option("o", "output1", false, "Option1");
 Option opt2 = new Option("opt", "output2", false, "Option2");
 Option opt3 = new Option("op", "output3", false, "Option3");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> optionsMatched = options.getMatchingOptions("op");
 assertEquals(1, optionsMatched.size());
 assertEquals("op", optionsMatched.get(0));
 }

 @Test
 public void testGetMatchingOptionsEmptyList() {
 Options options = createOptionsUnderTest();
 List<String> optionsMatched = options.getMatchingOptions("notanoption");
 assertTrue(optionsMatched.isEmpty());
 }

 @Test
 public void testGetMatchingOptionsMultipleMatches() {
 Options options = createOptionsUnderTest();
 Option opt1 = new Option("o", "option1", false, "Option1");
 Option opt2 = new Option("o", "option2", false, "Option2");
 Option opt3 = new Option("o", "option3", false, "Option3");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> optionsMatched = options.getMatchingOptions("o");
 assertEquals(3, optionsMatched.size());
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", false, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testHasOptionFalse() {
 Options options = createOptionsUnderTest();
 assertFalse(options.hasOption("notanoption"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("output", "outputOption", false, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasLongOption("outputOption"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "outputOption", false, "Output file path.");
 options.addOption(opt);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testAddOptionToRequiedOption() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 options.addOption(opt);
 assertFalse(options.isValid());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 options.addOption(opt);
 opt.setValue("output.txt");
 assertNotNull(opt.getValue());
 assertEquals("output.txt", opt.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionToRequiedOptionWithValue() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 opt.setRequired(true);
 options.addOption(opt);
 opt.setValue("output.txt");
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptionsUnderTest();
 Option opt = new Option("o", "output", true, "Output file path.");
 options.addOption(opt);
 List<Option> optionsRequired = options.getRequiredOptions();
 assertEquals(1, optionsRequired.size());
 assertEquals(opt, optionsRequired.get(0));
 }

 @Test
 public void testGetRequiredOptionsEmptyList() {
 Options options = createOptionsUnderTest();
 List<Option> optionsRequired = options.getRequiredOptions();
 assertTrue(optionsRequired.isEmpty());
 }

 @Test
 public void testAddOptionGroupWithRequiedOptions() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("g", "group");
 Option opt = new Option("o", "output", true, "Output file path.");
 group.addOption(opt);
 opt.setRequired(true);
 options.addOptionGroup(group);
 assertFalse(options.isValid());
 }

 @Test
 public void testToString() {
 Options options = createOptionsUnderTest();
 Option opt1 = new Option("o", "output1", false, "Option1");
 Option opt2 = new Option("o", "output2", false, "Option2");
 Option opt3 = new Option("o", "output3", false, "Option3");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 String optionsString = options.toString();
 assertTrue(optionsString.contains("short"));
 assertTrue(optionsString.contains("long"));
 }
}