import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 Options options = new Options();
 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "a", false, "Option A");
 Option option = options.getOption("a");
 assertTrue(option.isPresent());
 assertEquals("Option A", option.getDescription());
 assertFalse(option.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "a", true, "Option A with argument");
 Option option = options.getOption("a");
 assertTrue(option.isPresent());
 assertEquals("Option A with argument", option.getDescription());
 assertTrue(option.hasArg());
 }

 @Test
 public void testAddLongOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "long-option-a", false, "Option A with long name");
 Option option = options.getOption("long-option-a");
 assertTrue(option.isPresent());
 assertEquals("Option A with long name", option.getDescription());
 assertFalse(option.hasArg());
 }

 @Test
 public void testAddLongOptionWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "long-option-a", true, "Option A with long name and argument");
 Option option = options.getOption("long-option-a");
 assertTrue(option.isPresent());
 assertEquals("Option A with long name and argument", option.getDescription());
 assertTrue(option.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group");
 options.addOptionGroup(group);
 group.setRequired(false);
 Option option = new Option("a", "a", false, "Option A in group", group);
 group.addOption(option);
 Option groupOption = options.getOption("a");
 assertTrue(groupOption.isPresent());
 assertTrue(groupOption.getValues().isEmpty());
 assertEquals("Option A in group", groupOption.getDescription());
 assertFalse(groupOption.hasArg());
 assertNull(groupOption.getOptionGroup());
 }

 @Test
 public void testAddRequiredOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group");
 options.addOptionGroup(group);
 group.setRequired(true);
 Option option = new Option("a", "a", false, "Option A in group", group);
 group.addOption(option);
 Option groupOption = options.getOption("a");
 assertTrue(groupOption.isPresent());
 assertTrue(groupOption.getValues().isEmpty());
 assertEquals("Option A in group", groupOption.getDescription());
 assertFalse(groupOption.hasArg());
 assertNotNull(groupOption.getOptionGroup());
 assertEquals("group", groupOption.getOptionGroup().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "a", false, "Option A");
 options.addOption("a", "a", false, "Option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "long-option-a", false, "Option A with long name");
 options.addOption("long-option-a", "long-option-a", false, "Option A with long name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOptionWithArg() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "long-option-a", true, "Option A with long name and argument");
 options.addOption("long-option-a", "long-option-a", true, "Option A with long name and argument");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionWithSameDescription() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "a", false, "Option A");
 Option option2 = new Option("a", "a", false, "Option A");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOptionWithSameDescription() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "long-option-a", false, "Option A with long name");
 Option option2 = new Option("long-option-a", "long-option-a", false, "Option A with long name");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("a", "a", false, "Option A", new OptionGroup("group", "Group"));
 Option option2 = new Option("b", "b", false, "Option B");
 options.addOption(option1);
 options.addOption(option2);
 List<Option> requiredOptions = options.getRequiredOptions();
 List<Option> expected = new ArrayList<>();
 expected.add(option1);
 assertEquals(expected, requiredOptions);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();
 Option opt1 = new Option("opt", "Option 1");
 Option opt2 = new Option("optopt", "Option 1-2");
 Option opt3 = new Option("long-opt-1", "Long Option 1");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 List<String> matchingOptions = options.getMatchingOptions("opt");
 List<String> expected = new ArrayList<>();
 expected.add("opt");
 expected.add("optopt");
 assertEquals(expected, matchingOptions);
 }
}