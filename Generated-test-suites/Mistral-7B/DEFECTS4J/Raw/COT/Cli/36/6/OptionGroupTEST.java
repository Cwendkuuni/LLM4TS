import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;
 private Option option3;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1", true, "o1-desc");
 option2 = new Option("o2", "Option 2", true, "o2-desc");
 option3 = new Option("o3", "Option 3", true, "o3-desc");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testAddOption() {
 OptionGroup addedOptionGroup = optionGroup.addOption(option3);
 assertEquals(optionGroup, addedOptionGroup);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullOption() {
 optionGroup.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 OptionGroup duplicateOptionGroup = optionGroup.addOption(option1);
 optionGroup.addOption(option1);
 assertNotEquals(optionGroup, duplicateOptionGroup);
 }

 @Test
 public void testGetNames() {
 List<String> names = new ArrayList<>();
 names.add("o1");
 names.add("o2");
 names.add("o3");

 assertEquals(names, optionGroup.getNames());
 }

 @Test
 public void testGetOptions() {
 List<Option> options = new ArrayList<>();
 options.add(option1);
 options.add(option2);
 options.add(option3);

 assertEquals(options, optionGroup.getOptions());
 }

 @Test
 public void testSetSelectedNullOption() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup.setSelected(option2);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 Map<String, String> expectedOptions = new HashMap<>();
 expectedOptions.put("o1", "--o1-desc");
 expectedOptions.put("o2", "--o2-desc");
 expectedOptions.put("o3", "--o3-desc");

 StringBuilder expected = new StringBuilder();
 expected.append("[--o1-desc, --o2-desc, --o3-desc]");

 assertEquals(expected.toString(), optionGroup.toString());
 }
}