import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOptionShort() {
 // test adding a simple option with no argument
 String optShort = "a";
 String optLong = "optionA";
 String description = "This is an option A";
 options.addOption(optShort, optLong, false, description);
 Option opt = options.getOption(optShort);
 assertEquals(optLong, opt.getLongOpt());
 assertEquals(false, opt.hasArg());
 assertEquals(description, opt.getDescription());
 }

 @Test
 public void testAddOptionShortWithArg() {
 // test adding a simple option with an argument
 String optShort = "b";
 String optLong = "optionB";
 String description = "This is an option B";
 options.addOption(optShort, optLong, true, description);
 Option opt = options.getOption(optShort);
 assertEquals(optLong, opt.getLongOpt());
 assertEquals(true, opt.hasArg());
 assertEquals(description, opt.getDescription());
 }

 @Test
 public void testAddOptionShortAndLong() {
 // test adding an option with both short and long names
 String optShort = "c";
 String optLong = "optionC";
 String description = "This is an option C";
 options.addOption(optShort, optLong, false, description);
 Option opt = options.getOption(optShort);
 assertEquals(optLong, opt.getLongOpt());
 assertEquals(false, opt.hasArg());
 assertEquals(description, opt.getDescription());
 opt = options.getOption(optLong);
 assertEquals(optShort, opt.getKey());
 assertEquals(optLong, opt.getLongOpt());
 assertEquals(false, opt.hasArg());
 assertEquals(description, opt.getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 // test adding an OptionGroup
 OptionGroup group = new OptionGroup("group1", "This is a group");
 String optShortGroup = "g";
 String optLongGroup = "group";
 String descriptionGroup = "This is a group option";
 Option optionGroup = new Option(optShortGroup, optLongGroup, false, descriptionGroup);
 group.addOption(optionGroup);
 options.addOptionGroup(group);
 Option optGroup = options.getOption(optShortGroup);
 assertEquals(optLongGroup, optGroup.getLongOpt());
 assertFalse(optGroup.hasArg());
 assertEquals(descriptionGroup, optGroup.getDescription());
 assertEquals(group, options.getOptionGroup(optGroup));
 }

 @Test
 public void testHasOption() {
 String optShort = "a";
 options.addOption(optShort, null, false, null);
 assertTrue(options.hasOption(optShort));
 assertFalse(options.hasOption("x"));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionShort() {
 String optShort = "a";
 options.addOption(optShort, null, false, null);
 assertThrows(ParseException.class, () -> options.addOption(optShort, null, false, null));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionLong() {
 String optLong = "optionA";
 options.addOption(null, optLong, false, null);
 assertThrows(ParseException.class, () -> options.addOption(null, optLong, false, null));
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithDuplicateLong() {
 String optShort = "a";
 String optLong = "optionA";
 options.addOption(optShort, optLong, false, null);
 assertThrows(ParseException.class, () -> options.addOption(null, optLong, false, null));
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithInvalidLong() {
 String optShort = "a";
 String optLong = null;
 options.addOption(optShort, optLong, false, null);
 }
}