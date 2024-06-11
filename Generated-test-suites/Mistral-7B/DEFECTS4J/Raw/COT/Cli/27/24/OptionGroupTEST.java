import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Test
 public void testAddOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection names = optionGroup.getNames();
 assertTrue(names.contains("option1"));
 assertTrue(names.contains("option2"));
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedExisting() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 assertNotNull(optionGroup.getSelected());
 assertEquals(option1.getOpt(), optionGroup.getSelected());

 try {
 optionGroup.setSelected(option2);
 fail("AlreadySelectedException was not thrown");
 } catch (AlreadySelectedException e) {
 // expected
 }
 }

 @Test
 public void testSetSelectedNullAfterSelected() throws AlreadySelectedException {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 assertNotNull(optionGroup.getSelected());
 assertEquals(option1.getOpt(), optionGroup.getSelected());

 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedNullOption() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = null;
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testToString() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description of option1");
 Option option2 = new Option("option2", "Description of option2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--option1 Description of option1, --option2 Description of option2]", optionGroup.toString());
 }

}