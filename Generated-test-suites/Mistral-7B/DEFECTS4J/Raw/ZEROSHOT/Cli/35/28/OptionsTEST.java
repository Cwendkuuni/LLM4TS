import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOptionSingleShortName() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 assertEquals("optionA", options.getOption("a").getKey());
 }

 @Test
 public void testAddOptionSingleLongName() {
 Option option = new Option("myOption", "my long option", false, "This is my option");
 options.addOption(option);
 assertEquals("my long option", options.getOption("myOption").getLongOpt());
 }

 @Test
 public void testAddOptionSingleShortNameAndLongName() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 assertEquals("optionA", options.getOption("a").getKey());
 assertEquals("optionA", options.getOption("--optionA").getKey());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option option = new Option("a", "optionA", true, "This is option A");
 options.addOption(option);
 assertEquals("optionA", options.getOption("a").getKey());
 assertTrue(options.getOption("a").hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("mygroup", "My Group");
 Option option1 = new Option("g1", "groupOption1", false, "This is option 1 in group");
 Option option2 = new Option("g2", "groupOption2", false, "This is option 2 in group");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 assertEquals("mygroup", group.getKey());
 assertNotNull(options.getOption("g1"));
 assertNotNull(options.getOption("g2"));
 }

 @Test
 public void testGetRequiredOptions() {
 Option option1 = new Option("r1", "requiredOption1", false, "This is required option 1");
 Option option2 = new Option("r2", "requiredOption2", false, "This is required option 2");
 options.addOption(option1);
 options.addOption(option2);
 options.addOptionGroup(new OptionGroup("mygroup", "My Group"));
 options.addOption("a", "optionA", false, "This is option A");
 options.addOptionGroup(new OptionGroup("othergroup", "Other Group"));
 options.addOption("z", "optionZ", false, "This is option Z");
 List<String> requiredOptions = options.getRequiredOptions();
 assertEquals(2, requiredOptions.size());
 assertEquals("r1", requiredOptions.get(0));
 assertEquals("r2", requiredOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("--optionA"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 assertTrue(options.hasLongOption("optionA"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("mygroup", "My Group");
 Option option1 = new Option("g1", "groupOption1", false, "This is option 1 in group");
 Option option2 = new Option("g2", "groupOption2", false, "This is option 2 in group");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 Option option = new Option("o", "optionO", false, "This is option O");
 options.addOption(option);
 OptionGroup groupFromOption = options.getOptionGroup(option);
 assertEquals("mygroup", groupFromOption.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Option option = new Option("a", "optionA", false, "This is option A");
 options.addOption(option);
 options.addOption(option);
 }
}