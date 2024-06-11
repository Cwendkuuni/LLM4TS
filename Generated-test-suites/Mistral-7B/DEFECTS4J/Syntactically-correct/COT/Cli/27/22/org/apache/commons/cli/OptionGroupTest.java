import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 assertEquals(option2.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNamesUnmodifiable() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertFalse(optionGroup.getNames().add(new Object()));
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedNullOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals(option1.getOpt(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertNull(optionGroup.getSelected());
 optionGroup.setSelected(option2);
 assertEquals(option2.getOpt(), optionGroup.getSelected());
 }

 @Test
 public void testSetRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testIsRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals("[--opt1 Option 1, --opt2 Option 2]", optionGroup.toString());
 }

}