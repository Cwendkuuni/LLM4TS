import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Test
 public void testAddOption() {
 Option option = new Option("o", "option", true, "An option");
 commandLine.addOption(option);
 assertNotNull(commandLine.getOptions().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddOptionNullOption() {
 commandLine.addOption(null);
 }

 @Test
 public void testAddValue() {
 Option option = new Option("o", "option", true, "An option");
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 assertNotNull(commandLine.getValuesForOption("option"));
 assertFalse(commandLine.getValuesForOption("option").isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testAddValueNullOption() {
 Option option = new Option("o", "option", true, "An option");
 commandLine.addValue(option, null);
 }

 @Test
 public void testGetValuesForOption() {
 Option option = new Option("o", "option", true, "An option");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValuesForOption("option");
 assertFalse(values.isEmpty());
 assertNotNull(values.get(0));
 assertNotNull(values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("o", "option", true, "An option");
 List defaultValues = List.of("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValuesForOption("option");
 assertFalse(values.isEmpty());
 assertNotNull(values.get(0));
 assertNotNull(values.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent() && option.getValue().get().booleanValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchDuplicate() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addSwitch(option, true);
 try {
 commandLine.addSwitch(option, true);
 } catch (IllegalStateException e) {
 //expected
 }
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isPresent() && option.getValue().get().booleanValue());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertNotNull(commandLine.getPropertyValues("property"));
 assertFalse(commandLine.getPropertyValues("property").isEmpty());
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("value"));
 }
}