import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 optionGroup.addOption(new Option("a", "optionA", false, "This is option A"));
 optionGroup.addOption(new Option("b", "optionB", false, "This is option B"));
 optionGroup.addOption(new Option("c", "optionC", false, "This is option C"));
 optionGroup.setRequired(true);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertEquals("optionA", names.iterator().next());
 assertEquals("optionB", names.iterator().next());
 assertEquals("optionC", names.iterator().next());
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertEquals(optionGroup.optionMap.get("optionA"), options.iterator().next());
 assertEquals(optionGroup.optionMap.get("optionB"), options.iterator().next());
 assertEquals(optionGroup.optionMap.get("optionC"), options.iterator().next());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSame() {
 optionGroup.setSelected(optionGroup.optionMap.get("optionA"));
 assertEquals("optionA", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferent() {
 optionGroup.setSelected(optionGroup.optionMap.get("optionB"));
 optionGroup.setSelected(optionGroup.optionMap.get("optionA"));
 }

 @Test
 public void testIsRequired() {
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredFalse() {
 OptionGroup optionGroupFalse = new OptionGroup();
 optionGroupFalse.setRequired(false);
 assertFalse(optionGroupFalse.isRequired());
 }

 @Test
 public void testToString() {
 assertEquals("[--optionA This is option A, --optionB This is option B, --optionC This is option C]", optionGroup.toString());
 }
}