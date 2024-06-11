import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();

 Option option1 = new Option("o1", "Option 1", true, "o1-value");
 Option option2 = new Option("o2", "Option 2", true, "o2-value");
 Option option3 = new Option("o3", "Option 3", false, "o3-value");
 Option option4 = new Option("o4", "Option 4", false, "o4-value");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 optionGroup.addOption(option4);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();

 assertEquals(4, names.size());
 assertEquals("o1", names.iterator().next());
 assertEquals("o2", names.iterator().next());
 assertEquals("o3", names.iterator().next());
 assertEquals("o4", names.iterator().next());
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();

 assertEquals(4, options.size());

 Option option1 = options.iterator().next();
 Option option2 = options.iterator().next();
 Option option3 = options.iterator().next();
 Option option4 = options.iterator().next();

 assertEquals("o1", option1.getKey());
 assertEquals("Option 1", option1.getLongOpt());
 assertEquals(true, option1.isRequired());
 assertEquals("o1-value", option1.getValue());

 assertEquals("o2", option2.getKey());
 assertEquals("Option 2", option2.getLongOpt());
 assertEquals(true, option2.isRequired());
 assertEquals("o2-value", option2.getValue());

 assertEquals("o3", option3.getKey());
 assertEquals("Option 3", option3.getLongOpt());
 assertEquals(false, option3.isRequired());
 assertEquals(null, option3.getValue());

 assertEquals("o4", option4.getKey());
 assertEquals("Option 4", option4.getLongOpt());
 assertEquals(false, option4.isRequired());
 assertEquals(null, option4.getValue());
 }

 @Test
 public void testSetSelected() {
 Option selectedOption = optionGroup.getOptions().stream()
 .filter(option -> "o1".equals(option.getKey()))
 .findFirst()
 .orElse(null);

 assertNull(optionGroup.getSelected());

 optionGroup.setSelected(selectedOption);
 assertEquals("o1", optionGroup.getSelected());

 optionGroup.setSelected(selectedOption);
 assertSame(selectedOption, optionGroup.getSelected());

 Option otherOption = optionGroup.getOptions().stream()
 .filter(option -> "o2".equals(option.getKey()))
 .findFirst()
 .orElse(null);

 assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(otherOption));
 }

 @Test
 public void testIsRequired() {
 assertFalse(optionGroup.isRequired());

 OptionGroup requiredOptionGroup = new OptionGroup();
 requiredOptionGroup.setRequired(true);
 assertTrue(requiredOptionGroup.isRequired());
 }

 @Test
 public void testToString() {
 Map<String, String> expectedOptionsDescription = new HashMap<>();
 expectedOptionsDescription.put("o1", "Option 1");
 expectedOptionsDescription.put("o2", "Option 2");
 expectedOptionsDescription.put("o3", "Option 3");
 expectedOptionsDescription.put("o4", "Option 4");

 String expectedString = "[-o1 Option 1, --o2 Option 2, -o3, --o4 Option 4]";

 assertEquals(expectedString, optionGroup.toString());
 }
}