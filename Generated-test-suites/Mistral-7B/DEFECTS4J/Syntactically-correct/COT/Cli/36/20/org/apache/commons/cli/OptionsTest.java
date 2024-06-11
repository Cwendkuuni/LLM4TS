import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 }

 @Test
 public void testAddOptionWithShortName() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortNameAndDescription() {
 Option option = options.addOption('a', false, "Description for option a");
 assertEquals(1, options.getOptions().size());
 assertEquals("alias", option.getKey());
 }

 @Test
 public void testAddOptionWithShortNameAndDescriptionAndValue() {
 Option option = options.addOption('a', 'v', "value", "Description for option a with value");
 assertEquals(1, options.getOptions().size());
 assertEquals("alias", option.getKey());
 assertEquals("value", option.getValueDescription());
 }

 @Test
 public void testAddOptionWithShortNameAndLongName() {
 Option option = options.addOption("long", "alias", false, "Description for option a with long name");
 option.setShortOpt("a");
 assertEquals(1, options.getOptions().size());
 assertEquals("alias", option.getKey());
 assertEquals("long", option.getLongOpt());
 }

 @Test
 public void testAddOptionWithShortNameAndLongNameAndDescriptionAndValue() {
 Option option = options.addOption("long", "alias", 'v', "Description for option a with long name and value");
 option.setShortOpt('a');
 assertEquals(1, options.getOptions().size());
 assertEquals("alias", option.getKey());
 assertEquals("long", option.getLongOpt());
 assertEquals("value", option.getValueDescription());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Description for group");
 Option option = new Option("g", "groupOption", false, "Description for group option");
 group.addOption(option);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size());
 assertEquals(1, group.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupTwice() {
 OptionGroup group = new OptionGroup("group", "Description for group");
 Option option = new Option("g", "groupOption", false, "Description for group option");
 group.addOption(option);
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortName() {
 Option option1 = options.addOption('a', false, "Description for option a");
 Option option2 = options.addOption('a', false, "Description for option a");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongName() {
 Option option1 = options.addOption("long", false, "Description for option a with long name");
 Option option2 = options.addOption("long", false, "Description for option a with long name");
 }

 @Test
 public void testGetRequiredOptions() {
 Option option = new Option("r", "required", true, "Description for required option");
 options.addOption(option);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertTrue(requiredOptions.get(0).isRequired());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyARequiredOption() {
 Option option = new Option("r", "required", true, "Description for required option");
 options.addOption(option);
 option.setRequired(false);
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Description for group");
 Option option = new Option("g", "groupOption", false, "Description for group option");
 group.addOption(option);
 options.addOptionGroup(group);
 OptionGroup retrievedGroup = options.getOptionGroup(option);
 assertEquals(group, retrievedGroup);
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionGroupWithNullOption() {
 options.getOptionGroup(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithNullKey() {
 options.getOption(null);
 }

 @Test
 public void testGetOptionWithValidKey() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 Option retrievedOption = options.getOption("a");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetOptionWithLongName() {
 Option option = new Option("long", "alias", false, "Description for option a with long name");
 options.addOption(option);
 Option retrievedOption = options.getOption("long");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetOptionWithInvalidKey() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 assertNull(options.getOption("invalid"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("short", "shortOption1", false, "Description for short option 1");
 Option option2 = new Option("long", "longOption", false, "Description for long option");
 Option option3 = new Option("short", "shortOption2", false, "Description for short option 2");
 Option option4 = new Option("long", "longOption2", false, "Description for long option 2");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOption(option4);
 List<String> result = options.getMatchingOptions("sho");
 assertEquals(2, result.size());
 assertEquals("shortOption1", result.get(0));
 assertEquals("shortOption2", result.get(1));
 }

 @Test
 public void testHasOptionWithShortName() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasOptionWithLongName() {
 Option option = new Option("long", "alias", false, "Description for option a with long name");
 options.addOption(option);
 assertTrue(options.hasOption("long"));
 }

 @Test
 public void testHasOptionWithInvalidName() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 assertFalse(options.hasOption("invalid"));
 }

 @Test
 public void testHasLongOptionWithLongName() {
 Option option = new Option("long", "alias", false, "Description for option a with long name");
 options.addOption(option);
 assertTrue(options.hasLongOption("long"));
 }

 @Test
 public void testHasShortOptionWithShortName() {
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Description for option a with alias1");
 Option option2 = new Option("b", "alias2", false, "Description for option b with alias2");
 options.addOption(option1);
 options.addOption(option2);
 OptionGroup group = new OptionGroup("group", "Description for group");
 options.addOptionGroup(group);
 assertEquals("[ Options: [ short {\"a\":Option(alias=alias1, description=Description for option a with alias1, required=false)}, {\"b\":Option(alias=alias2, description=Description for option b with alias2, required=false)} ] [ long null ]", options.toString());
 }
}