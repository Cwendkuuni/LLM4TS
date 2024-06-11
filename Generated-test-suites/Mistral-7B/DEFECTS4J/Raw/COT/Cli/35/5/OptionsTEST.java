import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 Options options = new Options();

 options.addOption("o", "option", false, "Option description");
 options.addOption("l", "long-option", true, "Long option description");
 options.addOptionGroup(new OptionGroup("group1", "Group 1", true, "Description for group 1"));
 options.addOptionGroup(new OptionGroup("group2", "Group 2", false, "Description for group 2"));
 options.addOptionGroup(new OptionGroup("group3", "Group 3", false, "Description for group 3"));

 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);

 assertEquals("Option description", option.getDescription());
 assertEquals("o", option.getKey());
 assertEquals(false, option.isRequired());
 assertEquals(Collections.emptyList(), option.getValues());
 assertEquals(false, option.hasArg());

 assertNull(options.getOption("unknown-option"));
 assertEquals(1, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("o", "option", false, "Option description");
 Option option2 = new Option("o", "duplicate-option", false, "Duplicate option description");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testAddOptionWithDescriptionOnly() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "Option description");
 options.addOption(option);

 assertEquals("Option description", option.getDescription());
 assertEquals("option", option.getKey());
 assertEquals(false, option.isRequired());
 assertEquals(Collections.emptyList(), option.getValues());
 assertEquals(false, option.hasArg());
 }

 @Test
 public void testAddOptionWithLongOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("l", "long-option", true, "Long option description");
 options.addOption(option);

 assertEquals("Long option description", option.getDescription());
 assertEquals("long-option", option.getLongOpt());
 assertEquals(true, option.isRequired());
 assertEquals(Collections.emptyList(), option.getValues());
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionWithLongOptionAndValue() {
 Options options = createOptionsUnderTest();
 Option option = new Option("l", "long-option", true, "Long option description");
 options.addOption(option);
 option.setValues(Collections.singletonList("value"));

 assertEquals("Long option description", option.getDescription());
 assertEquals("long-option", option.getLongOpt());
 assertEquals(true, option.isRequired());
 assertEquals("value", option.getValue());
 assertEquals("value", option.getValues().get(0));
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionWithShortAndLongNames() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);

 assertEquals("Option description", option.getDescription());
 assertEquals("o", option.getKey());
 assertEquals("option", option.getLongOpt());
 assertEquals(false, option.isRequired());
 assertEquals(Collections.emptyList(), option.getValues());
 assertEquals(false, option.hasArg());
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);
 option.setValues(Collections.singletonList("value"));

 assertEquals("Option description", option.getDescription());
 assertEquals("o", option.getKey());
 assertEquals("option", option.getLongOpt());
 assertEquals(false, option.isRequired());
 assertEquals("value", option.getValue());
 assertEquals("value", option.getValues().get(0));
 assertEquals(false, option.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("group1", "Group 1", true, "Description for group 1");
 options.addOptionGroup(optionGroup);

 assertEquals("Description for group 1", optionGroup.getDescription());
 assertEquals("group1", optionGroup.getKey());
 assertEquals(true, optionGroup.isRequired());
 assertEquals(0, optionGroup.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithDuplicateKey() {
 Options options = createOptionsUnderTest();OptionGroup optionGroup1 = new OptionGroup("group1", "Group 1", true, "Description for group 1");
 options.addOptionGroup(optionGroup1);
 OptionGroup optionGroup2 = new OptionGroup("group1", "Group 1", true, "Another description for group 1");
 options.addOptionGroup(optionGroup2);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();

 assertEquals(0, options.getMatchingOptions("").size());
 assertEquals(1, options.getMatchingOptions("opt").size());
 assertEquals(1, options.getMatchingOptions("optio").size());
 assertEquals(1, options.getMatchingOptions("option").size());
 assertEquals(1, options.getMatchingOptions("long-opt").size());
 assertEquals(2, options.getMatchingOptions("group").size());
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("option"));
 assertTrue(options.hasOption("long-option"));
 assertTrue(options.hasLongOption("long-option"));
 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasOption("unknown"));
 assertFalse(options.hasLongOption("unknown"));
 assertFalse(options.hasShortOption("unknown"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "option", false, "Option description");
 options.addOption(option);

 Option retrievedOption = options.getOption("o");
 assertEquals(option, retrievedOption);
 Option retrievedOptionLong = options.getOption("option");
 assertEquals(option, retrievedOptionLong);
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionWithNullOption() {
 Options options = createOptionsUnderTest();
 assertNull(options.getOption(null));
 }

 @Test
 public void testGetOptions() {
 Options options = createOptionsUnderTest();

 assertEquals(3, options.getOptions().size());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptionsUnderTest();

 assertEquals(0, options.getRequiredOptions().size());
 }

 @Test
 public void testToString() {
 Options options = createOptionsUnderTest();

 String result = options.toString();
 String expected = "[ Options: [ short {o=Option(option, option, false, Option description)}] [ long {long-option=LongOption(long-option, long-option, true, Long option description)}] ]";
 assertEquals(expected, result);
 }
}