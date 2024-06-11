import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;

 @Before
 public void setup() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1", false, '1');
 option2 = new Option("o2", "Option 2", false, '2');
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals("Size of OptionGroup names should be 2", 2, names.size());
 assertEquals("Option names should contain 'o1'", "o1", names.iterator().next());
 assertEquals("Option names should contain 'o2'", "o2", names.iterator().next());
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals("Size of OptionGroup options should be 2", 2, options.size());
 assertEquals("Option 1 should be in the OptionGroup", option1, options.iterator().next());
 assertEquals("Option 2 should be in the OptionGroup", option2, options.iterator().next());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 }

 @Test
 public void testSetSelectedOption1() throws AlreadySelectedException {
 optionGroup.setSelected(option1);
 assertEquals("Selected option should be 'o1'", "o1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedOption1Twice() throws AlreadySelectedException {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testSetSelectedOption2() throws AlreadySelectedException {
 optionGroup.setSelected(option2);
 assertEquals("Selected option should be 'o2'", "o2", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedOption2Twice() throws AlreadySelectedException {
 optionGroup.setSelected(option2);
 optionGroup.setSelected(option2);
 }
}