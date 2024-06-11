import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testWriteableCommandLineImpl() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 // When
 commandLine.addValue(option, "output.txt");
 // Then
 assertEquals("output.txt", commandLine.getValues(option, null).get(0));
 }

 @Test
 public void testHasOption() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 // When
 commandLine.addValue(option, "output.txt");
 // Then
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addValue(option, "output.txt");
 // When
 Option result = commandLine.getOption("o");
 // Then
 assertEquals(option, result);
 }

 @Test
 public void testGetValues() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addValue(option, "output.txt");
 // When
 List result = commandLine.getValues(option, null);
 // Then
 assertEquals("output.txt", result.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithAlreadySetSwitch() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addSwitch(option, true);
 // When
 commandLine.addSwitch(option, false);
 // Then
 fail("Switch should not be set twice");
 }

 @Test
 public void testGetSwitch() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addSwitch(option, true);
 // When
 Boolean result = commandLine.getSwitch(option, null);
 // Then
 assertTrue(result);
 }

 @Test
 public void testAddProperty() {
 // Given
 commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 // When
 commandLine.addProperty("property", "value");
 // Then
 assertEquals("value", commandLine.getProperty("property", ""));
 }

 @Test
 public void testGetProperties() {
 // Given
 commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 commandLine.addProperty("property1", "value1");
 commandLine.addProperty("property2", "value2");
 // When
 Set result = commandLine.getProperties();
 // Then
 assertEquals(2, result.size());
 }

 @Test
 public void testLooksLikeOption() {
 // Given
 commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 // When
 assertTrue(commandLine.looksLikeOption("o"));
 // Then
 // When
 assertFalse(commandLine.looksLikeOption("x"));
 // Then
 }

 @Test
 public void testToString() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addValue(option, "output.txt");
 // When
 String result = commandLine.toString();
 // Then
 assertEquals("output.txt", result);
 }

 @Test
 public void testGetOptions() {
 // Given
 Option option1 = new Option("o", "output1", true, "Output file 1");
 Option option2 = new Option("o", "output2", true, "Output file 2");
 commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
 commandLine.addOption(option2);
 // When
 List result = commandLine.getOptions();
 // Then
 assertEquals(2, result.size());
 }

 @Test
 public void testGetOptionTriggers() {
 // Given
 Option option1 = new Option("o", "output1", true, "Output file 1");
 Option option2 = new Option("o", "output2", true, "Output file 2");
 commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
 commandLine.addOption(option2);
 // When
 Set result = commandLine.getOptionTriggers();
 // Then
 assertEquals(2, result.size());
 }

 @Test
 public void testSetDefaultValues() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 List defaultValues = new ArrayList();
 defaultValues.add("default1.txt");
 commandLine.setDefaultValues(option, defaultValues);
 // When
 List result = commandLine.getValues(option, null);
 // Then
 assertEquals("default1.txt", result.get(0));
 }

 @Test
 public void testSetDefaultSwitch() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.setDefaultSwitch(option, true);
 // When
 Boolean result = commandLine.getSwitch(option, null);
 // Then
 assertTrue(result);
 }

 @Test
 public void testGetNormalised() {
 // Given
 Option option = new Option("o", "output", true, "Output file");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addValue(option, "output.txt");
 // When
 List result = commandLine.getNormalised();
 // Then
 assertEquals(1, result.size());
 assertEquals("output.txt", result.get(0));
 }
}