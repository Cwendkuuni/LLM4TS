import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option);

 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", true, "Short option with argument");
 options.addOption(option);

 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithShortAndLongOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option);

 Option optionWithLongName = new Option("lopt", "long-opt", false, "Long option without short option");
 options.addOption(optionWithLongName);

 assertEquals(2, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();

 OptionGroup optionGroup = new OptionGroup("group", "Group name");
 options.addOptionGroup(optionGroup);

 assertEquals(1, options.getOptionGroups().size());
 assertEquals(optionGroup, options.getOptionGroups().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOption() {
 Options options = createOptionsUnderTest();

 Option option1 = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option1);

 Option option2 = new Option("o", "opt", false, "Duplicate short option");
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOption() {
 Options options = createOptionsUnderTest();

 Option option1 = new Option("lopt1", "long-opt-1", false, "Long option without short option");
 options.addOption(option1);

 Option option2 = new Option("lopt1", "long-opt-1", false, "Duplicate long option");
 options.addOption(option2);
 }

 @Test
 public void testGetOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option);

 assertEquals(option, options.getOption("o"));
 assertEquals(option, options.getOption("o"));
 assertEquals(option, options.getOption("opt"));
 assertEquals(option, options.getOption("OPT"));
 }

 @Test
 public void testGetLongOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "long-opt", false, "Long option without short option");
 options.addOption(option);

 assertEquals(option, options.getOption("long-opt"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();

 Option option1 = new Option("o1", "opt1", false, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");
 Option option3 = new Option("o1", "opt3", false, "Option 3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);

 List<String> matchingOptions = options.getMatchingOptions("o1");
 assertEquals(2, matchingOptions.size());
 assertEquals("opt1", matchingOptions.get(0));
 assertEquals("opt3", matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("opt"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "long-opt", false, "Long option without short option");
 options.addOption(option);

 assertTrue(options.hasLongOption("long-opt"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "opt", false, "Short option without long option");
 options.addOption(option);

 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testHasOptionGroup() {
 Options options = createOptionsUnderTest();

 OptionGroup optionGroup = new OptionGroup("group", "Group name");
 options.addOptionGroup(optionGroup);

 assertFalse(options.hasOptionGroup("another-group"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createOptionsUnderTest();

 OptionGroup optionGroup = new OptionGroup("group", "Group name");
 Option option = new Option("o", "opt", false, "Short option without long option");
 option.setOptionGroup(optionGroup);
 options.addOption(option);

 assertEquals(optionGroup, options.getOptionGroup(option));
 }
}