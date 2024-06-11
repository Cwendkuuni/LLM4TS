import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl writeableCommandLine;

 @Before
 public void setUp() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
 }

 // Basic test cases for addOption method
 @Test
 public void testAddOption() {
 Option option = new Option("test", true, "t");
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionWithDuplicateTrigger() {
 Option option1 = new Option("test", true, "t");
 Option option2 = new Option("test", true, "t");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);
 }

 @Test
 public void testGetOption() {
 Option option = new Option("test", true, "t");
 writeableCommandLine.addOption(option);
 assertEquals(option, writeableCommandLine.getOption("test"));
 }

 @Test
 public void testGetOptionWithInvalidTrigger() {
 Option option = new Option("test", true, "t");
 writeableCommandLine.addOption(option);
 assertNull(writeableCommandLine.getOption("invalidTrigger"));
 }

 // Basic test cases for addValue method
 @Test
 public void testAddValueWithSingleValue() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value");
 assertEquals("value", writeableCommandLine.getValues(option, null).get(0));
 }

 @Test
 public void testAddValueWithMultipleValues() {
 Option option = new Option("test", true, "t");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 assertEquals(Arrays.asList("value1", "value2"), writeableCommandLine.getValues(option, null));
 }

 // Basic test cases for addSwitch method
 @Test
 public void testAddSwitchWithFalseValue() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, false);
 assertFalse(writeableCommandLine.getSwitch(option, null));
 }

 @Test
 public void testAddSwitchWithTrueValue() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, null));
 }

 // Basic test cases for getValues method
 @Test
 public void testGetValuesWithOptionWithNoValues() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 assertEquals(Collections.EMPTY_LIST, writeableCommandLine.getValues(option, null));
 }

 @Test
 public void testGetValuesWithOptionWithSingleValue() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value");
 assertEquals(Arrays.asList("value"), writeableCommandLine.getValues(option, null));
 }

 @Test
 public void testGetValuesWithOptionWithMultipleValues() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 assertEquals(Arrays.asList("value1", "value2"), writeableCommandLine.getValues(option, null));
 }

 @Test
 public void testGetValuesWithOptionWithDefaultValues() {
 Option option = new Option("test", true, "t");
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
 assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), writeableCommandLine.getValues(option, null));
 }

 // Basic test cases for getSwitch method
 @Test
 public void testGetSwitchWithOptionWithNoSwitch() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 assertEquals(null, writeableCommandLine.getSwitch(option, null));
 }

 @Test
 public void testGetSwitchWithOptionWithFalseSwitch() {
 Option option = new Option("test", true);
 writeableCommandLine.addSwitch(option, false);
 assertFalse(writeableCommandLine.getSwitch(option, null));
 }

 @Test
 public void testGetSwitchWithOptionWithTrueSwitch() {
 Option option = new Option("test", true);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, null));
 }

 @Test
 public void testGetSwitchWithOptionWithDefaultSwitch() {
 Option option = new Option("test", true);
 writeableCommandLine.setDefaultSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, null));
 }

 // Edge cases and exception handling tests
 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOption() {
 writeableCommandLine.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNullOption() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSwitchWithNullOption() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSwitchWithNullValue() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithAlreadySetSwitch() {
 Option option = new Option("test", true);
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 writeableCommandLine.addSwitch(option, false);
 }
}