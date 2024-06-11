import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private static final String SHORT_OPTION = "s";
 private static final String LONG_OPTION = "longOption";
 private static final String ANOTHER_LONG_OPTION = "anotherLongOption";
 private static final String OPTION_GROUP_NAME = "optionGroup";

 @Test
 public void testAddOptionWithShortNameOnly() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, "short option description");

 Option option = options.getOption(SHORT_OPTION);
 Assert.assertEquals(SHORT_OPTION, option.getKey());
 Assert.assertEquals("short option description", option.getDescription());
 }

 @Test
 public void testAddOptionWithShortNameAndLongName() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, LONG_OPTION, false, "long option description");

 Option option = options.getOption(SHORT_OPTION);
 Assert.assertEquals(SHORT_OPTION, option.getKey());
 Assert.assertEquals(LONG_OPTION, option.getLongOpt());
 Assert.assertEquals(false, option.hasArg());
 Assert.assertEquals("long option description", option.getDescription());
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, LONG_OPTION, true, "long option description");

 Option option = options.getOption(SHORT_OPTION);
 Assert.assertEquals(SHORT_OPTION, option.getKey());
 Assert.assertEquals(LONG_OPTION, option.getLongOpt());
 Assert.assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("option group", "option group description");
 options.addOptionGroup(optionGroup);

 Option option = new Option("option", "option description");
 optionGroup.addOption(option);
 options.addOption(option);

 Option groupOption = options.getOption(OPTION_GROUP_NAME + ":");
 Assert.assertEquals(OPTION_GROUP_NAME, groupOption.getKey());
 Assert.assertEquals("option group description", groupOption.getDescription());

 Option groupOptionWithOption = options.getOption(option.getKey());
 Assert.assertEquals(option.getKey(), groupOptionWithOption.getKey());
 Assert.assertEquals(option.getDescription(), groupOptionWithOption.getDescription());
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionWithShortName() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, "short option description");
 options.addOption(SHORT_OPTION, "short option description");
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionWithLongName() {
 Options options = new Options();
 options.addOption(null, LONG_OPTION, false, "long option description");
 options.addOption(null, LONG_OPTION, false, "long option description");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithRequiredShortNameInOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("option group", "option group description");
 optionGroup.setRequired(true);
 optionGroup.addOption(new Option("option", "option description"));
 options.addOptionGroup(optionGroup);

 options.addOption(SHORT_OPTION, "short option description");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithRequiredShortNameWithoutOptionGroup() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, "short option description");
 options.addOption(SHORT_OPTION, "short option description");
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 options.addOption(SHORT_OPTION, "short option description");

 Assert.assertTrue(options.hasOption(SHORT_OPTION));
 Assert.assertFalse(options.hasOption("nonExistentOption"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 options.addOption(null, LONG_OPTION, false, "long option description");

 Assert.assertTrue(options.hasLongOption(LONG_OPTION));
 Assert.assertFalse(options.hasLongOption("nonExistentOption"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 options.addOption(null, "longOption1", false, "long option 1 description");
 options.addOption(null, "longOption2", false, "long option 2 description");
 options.addOption(null, "longOption3", false, "long option 3 description");

 List<String> matchingOptions = options.getMatchingOptions("longOption");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertEquals("longOption1", matchingOptions.get(0));
 Assert.assertEquals("longOption2", matchingOptions.get(1));
 }
}