import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testCreateOptionGroupAndAddOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "Option 1");
 Option option2 = new Option("o2", "Option 2");
 Option option3 = new Option("o3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals(3, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "Option 1");
 Option option2 = new Option("o2", "Option 2");
 Option option3 = new Option("o3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals(option1.getOpt(), optionGroup.getNames().toArray()[0]);
 assertEquals(option2.getOpt(), optionGroup.getNames().toArray()[1]);
 assertEquals(option3.getOpt(), optionGroup.getNames().toArray()[2]);
 }

 @Test
 public void testSetAndGetSelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "Option 1");
 Option option2 = new Option("o2", "Option 2");
 Option option3 = new Option("o3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertNull(optionGroup.getSelected());

 optionGroup.setSelected(option1);
 assertEquals(option1, optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals(option2, optionGroup.getSelected());

 assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
 }

 @Test
 public void testSetAndGetRequired() {
 OptionGroup optionGroup = new OptionGroup();

 assertFalse(optionGroup.isRequired());
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "Option 1");
 Option option2 = new Option("o2", "Option 2");
 Option option3 = new Option("o3", "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals("[--o1 Option 1, --o2 Option 2, --o3 Option 3]", optionGroup.toString());
 }
}