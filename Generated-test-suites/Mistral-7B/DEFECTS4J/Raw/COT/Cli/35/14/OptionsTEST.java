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
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionalArgumentOption() {
 Option option = new Option("b", "boy", true, "Boy option with optional argument");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("b"));
 }

 @Test
 public void testAddRequiredArgumentOption() {
 Option option = new Option("c", "color", true, "Color option with required argument");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("c"));
 }

 @Test
 public void testAddLongOption() {
 Option option = new Option("d", "duration", true, "Duration option with long name");
 options.addOption(option);
 options.addOption("d", "duration", true, "Another duration option with same long name");
 List<Option> optionsWithLongName = options.helpOptions();
 int count = 0;
 for (Option option1 : optionsWithLongName) {
 if (option1.getLongOpt().equals("duration")) {
 count++;
 }
 }
 Assert.assertEquals("Only one option with long name 'duration' should be added", 1, count);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateShortOption() {
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 options.addOption("a", "anotherAlias", false, "Another alias option");
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("g", "group", false, "Group options");
 Option option1 = new Option("h", "height", true, "Height option");
 Option option2 = new Option("w", "width", true, "Width option");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 List<Option> optionsWithGroup = options.helpOptions();
 int count = 0;
 for (Option option : optionsWithGroup) {
 if (option.isMembersOnly()) {
 count++;
 }
 }
 Assert.assertEquals("Number of options in group should be 2", 2, count);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateLongOptionInGroup() {
 OptionGroup group = new OptionGroup("g", "group", false, "Group options");
 Option option1 = new Option("h", "height", true, "Height option");
 group.addOption(option1);
 Option option2 = new Option("h", "heightWithGroup", true, "Height option with group");
 group.addOption(option2);
 options.addOptionGroup(group);
 }

 @Test
 public void testParseWithValidOptions() {
 String[] args = {"-a", "1", "--duration", "5"};
 try {
 CommandLine cmd = options.parse(args);
 } catch (ParseException e) {
 Assert.fail("Parsing should succeed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptions() {
 String[] args = {"--invalidOption"};
 try {
 CommandLine cmd = options.parse(args);
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingArgumentOption() {
 String[] args = {"-c"};
 try {
 CommandLine cmd = options.parse(args);
 } catch (ParseException e) {
 Assert.assertEquals("Missing required argument for option: -c", e.getMessage());
 }
 }
}