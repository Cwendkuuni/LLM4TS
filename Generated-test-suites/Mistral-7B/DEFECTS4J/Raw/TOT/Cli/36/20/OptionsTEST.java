import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOptionShortOnly() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "This is option A.");

 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);

 assertTrue(optionA.isRequired());
 assertTrue(optionA.hasArg());
 assertTrue(optionA.isHidden());
 assertTrue(optionA.getOpt() != null);
 assertTrue(optionA.getLongOpt() == null);
 assertFalse(optionA.isValueSeparator());
 assertFalse(optionA.getValuesSeparator());
 assertNull(optionA.getValue());
 }

 @Test
 public void testAddOptionLongOnly() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "This is option A.", "aValue");

 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);

 assertTrue(optionA.isRequired());
 assertTrue(optionA.hasArg());
 assertTrue(optionA.isHidden());
 assertTrue(optionA.getOpt() == null);
 assertTrue(optionA.getLongOpt() != null);
 assertFalse(optionA.isValueSeparator());
 assertFalse(optionA.getValuesSeparator());
 assertNull(optionA.getValue());
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "This is option A.", "aValue");
 options.addOption("b", null, true, "This is option B.");
 options.addOption("c", null, false, "This is option C.");

 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);
 Option optionB = optionsList.get(1);
 Option optionC = optionsList.get(2);

 assertFalse(optionA.isRequired());
 assertFalse(optionA.hasArg());
 assertFalse(optionA.isHidden());
 assertNull(optionA.getOpt());
 assertNull(optionA.getLongOpt());
 assertFalse(optionA.isValueSeparator());
 assertFalse(optionA.getValuesSeparator());
 assertNull(optionA.getValue());

 assertFalse(optionB.isRequired());
 assertFalse(optionB.hasArg());
 assertFalse(optionB.isHidden());
 assertNull(optionB.getOpt());
 assertNull(optionB.getLongOpt());
 assertTrue(optionB.isValueSeparator());
 assertNull(optionB.getValue());

 assertFalse(optionC.isRequired());
 assertFalse(optionC.hasArg());
 assertFalse(optionC.isHidden());
 assertNull(optionC.getOpt());
 assertNull(optionC.getLongOpt());
 assertFalse(optionC.isValueSeparator());
 assertNull(optionC.getValue());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 group.setRequired(true);
 group.addOption(new Option("g", "groupOption", true, "This is a group option."));
 options.addOptionGroup(group);

 List<Option> optionsList = options.helpOptions();
 Option groupOption = null;
 for (Option option : optionsList) {
 if (option.getKey().equals("groupOption")) {
 groupOption = option;
 break;
 }
 }

 assertTrue(groupOption.isRequired());
 assertFalse(groupOption.hasArg());
 assertFalse(groupOption.isHidden());
 assertNull(groupOption.getOpt());
 assertNull(groupOption.getLongOpt());
 assertFalse(groupOption.isValueSeparator());
 assertFalse(groupOption.getValuesSeparator());
 assertNull(groupOption.getValue());
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "This is option A.");
 options.addOption("a", "optionA", false, "This is option A.");
 }
}