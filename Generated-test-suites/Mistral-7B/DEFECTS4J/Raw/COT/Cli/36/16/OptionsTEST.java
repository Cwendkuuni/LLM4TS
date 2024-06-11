import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options createOptions() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A");
 options.addOption("b", "optionB", false, "Option B");
 options.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", false, "Option C in group 1")
 .addOption("d", true, "Option D in group 1 with value"));
 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 assertEquals("Option A", optionA.getLongOpt());
 assertEquals(true, optionA.isRequired());
 }

 @Test
 public void testAddOptionWithShortNameOnly() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 assertEquals('a', optionA.getOpt());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 assertEquals("optionA", optionA.getLongOpt());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = createOptions();
 Option optionB = options.getOption("b");
 assertFalse(optionB.hasArg());
 }

 @Test
 public void testAddOptionWithArgumentValue() {
 Options options = createOptions();
 Option optionB = options.getOption("b");
 assertNull(optionB.getValue());

 Options optionsWithArgumentValue = new Options(options);
 optionsWithArgumentValue.parse("--optionB value");
 assertEquals("value", optionB.getValue());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group1 = options.getOptionGroup("group1");
 assertEquals("Group 1", group1.getLongOpt());
 }

 @Test
 public void testGetOptionGroups() {
 Options options = createOptions();
 List<OptionGroup> optionGroups = options.getOptionGroups();
 assertTrue(optionGroups.contains(options.getOptionGroup("group1")));
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();
 assertTrue(options.hasLongOption("optionA"));
 assertTrue(options.hasLongOption("optionB"));
 assertTrue(options.hasLongOption("optionC"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();
 assertTrue(options.hasShortOption('a'));
 assertTrue(options.hasShortOption('b'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("a", true, "Option A (duplicate)");
 }

 @Test
 public void testGetOptionWithNoSuchOption() {
 Options options = createOptions();
 assertNull(options.getOption("noOption"));
 }

 @Test
 public void testParseWithExtraArg() {
 Options options = createOptions();
 options.parse("--optionA --optionB --foo");
 assertEquals(2, options.getParsedArgs().size());
 }

 @Test
 public void testParseWithMissingRequiredArg() {
 Options options = new Options()
 .addOption("required", true, "Required Option");
 options.parse(new String[0]);
 assertFalse(options.isParsedSuccessfully());
 }

 @Test
 public void testParseWithMissingRequiredArgAndHelp() {
 Options options = new Options()
 .addOption("required", true, "Required Option");options.parse(new String[]{"--help"});
 assertFalse(options.isParsedSuccessfully());
 }

 @Test
 public void testParseWithMissingRequiredArgAndVersion() {
 Options options = new Options()
 .addOption("required", true, "Required Option");options.parse(new String[]{"--version"});
 assertFalse(options.isParsedSuccessfully());
 }

 @Test
 public void testParseWithExtraHelp() {
 Options options = new Options();
 options.parse(new String[]{"--help", "--help"});
 assertEquals(1, options.getParsedArgs().size());
 }

 @Test
 public void testParseWithExtraVersion() {
 Options options = new Options();
 options.parse(new String[]{"--version", "--version"});
 assertEquals(1, options.getParsedArgs().size());
 }

 @Test
 public void testParseWithMissingOption() {
 Options options = new Options()
 .addOption("a", true, "Option A");
 String[] args = new String[]{"--optionB"};
 options.parse(args);
 assertFalse(options.isParsedSuccessfully());
 }

 @Test
 public void testParseWithMissingOptionAndHelp() {
 Options options = new Options()
 .addOption("a", true, "Option A");
 String[] args = new String[]{"--help"};
 options.parse(args);
 assertFalse(options.isParsedSuccessfully());
 }

 @Test
 public void testParseWithMissingOptionAndVersion() {
 Options options = new Options()
 .addOption("a", true, "Option A");
 String[] args = new String[]{"--version"};
 options.parse(args);
 assertFalse(options.isParsedSuccessfully());
 }

}