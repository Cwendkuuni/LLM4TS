import org.apache.commons.cli.*; // for imports
import static org.junit.Assert.*; // for assertions
import java.util.Arrays; // for Arrays.asList()

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "Option 1", true, "o1-desc");
 Option option2 = new Option("o2", "Option 2", false, "o2-desc");
 Option option3 = new Option("o3", "Option 3", false, "o3-desc");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 // Test method for OptionGroup#addOption(Option)
 @Test
 public void testAddOption() {
 OptionGroup expectedOptionGroup = new OptionGroup();
 expectedOptionGroup.addOption(option1);
 expectedOptionGroup.addOption(option2);
 expectedOptionGroup.addOption(option3);

 assertEquals(expectedOptionGroup.toString(), optionGroup.toString());
 }

 // Test method for OptionGroup#getNames()
 @Test
 public void testGetNames() {
 Collection<String> expectedNames = Arrays.asList("o1", "o2", "o3");
 assertEquals(expectedNames, optionGroup.getNames());
 }

 // Test method for OptionGroup#getOptions()
 @Test
 public void testGetOptions() {
 Collection<Option> expectedOptions = Arrays.asList(option1, option2, option3);
 assertEquals(expectedOptions, optionGroup.getOptions());
 }

 // Test method for OptionGroup#setSelected(Option)
 @Test
 public void testSetSelected() {
 Option selectedOption = option1;
 optionGroup.setSelected(selectedOption);
 String expectedSelected = selectedOption.getKey();
 assertEquals(expectedSelected, optionGroup.getSelected());
 }

 // Test method for OptionGroup#setSelected(Option) with AlreadySelectedException
 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelectedException() {
 Option alreadySelectedOption = option1;
 optionGroup.setSelected(alreadySelectedOption);
 optionGroup.setSelected(alreadySelectedOption);
 }

 // Test method for OptionGroup#isRequired()
 @Test
 public void testIsRequired() {
 boolean expectedRequired = false;
 optionGroup.setRequired(expectedRequired);
 assertTrue(optionGroup.isRequired() == expectedRequired);
 }

 // Test method for OptionGroup#isRequired() with setRequired(true)
 @Test
 public void testIsRequiredTrue() {
 boolean expectedRequired = true;
 optionGroup.setRequired(expectedRequired);
 assertTrue(optionGroup.isRequired() == expectedRequired);
 }
}