import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1, option2, option3;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals(2, optionGroup.getNames().size());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 option2 = new Option("o2", "Option 2");
 option3 = new Option("o3", "Option 3");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 String expected = "[-o1 Option 1, -o2 Option 2, --o3 Option 3]";
 assertEquals(expected, optionGroup.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetOptionsConcurrentModification() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1");
 optionGroup.addOption(option1);
 optionGroup.getOptions().remove(option1);
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedNullOption() {
 optionGroup = new OptionGroup();
 optionGroup.setSelected(null);
 }
}