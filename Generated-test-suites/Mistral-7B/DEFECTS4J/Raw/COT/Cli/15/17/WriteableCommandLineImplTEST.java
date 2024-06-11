import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 Option option1 = new Option("o", "output", true, "Output directory");
 Option option2 = new Option("i", "input", true, "Input file");
 Option option3 = new Option("p", "prefix", false, "Show prefixes");

 commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 }

 @Test
 public void testAddOption() {
 Option option = new Option("test", "test", false, "Test option");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 Option option = new Option("test", "test", false, "Test option");
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 commandLine.addValue(option, "testOutput");
 assertEquals("testOutput", commandLine.getValues(option, null).get(0));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 Option returnedOption = commandLine.getOption("o");
 assertEquals(option, returnedOption);
 }

 @Test
 public void testGetValues() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 commandLine.addValue(option, "testOutput");
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("testOutput", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 List values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 Option option = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 Option option = new Option("prop", "property", true, "Property name");
 commandLine.addOption(option);
 Map properties = new HashMap();
 properties.put("test", "testValue");
 commandLine.addProperty(option, "test", "testValue");
 assertEquals("testValue", commandLine.getProperty(option, ""));
 }

 @Test
 public void testAddProperty() {
 Option option = new Option("prop", "property", true, "Property name");
 commandLine.addOption(option);
 Map properties = new HashMap();
 properties.put("test", "testValue");
 commandLine.addProperty(option, "test", "testValue");
 Map commandLineProperties = commandLine.getProperties(option);
 assertEquals(1, commandLineProperties.size());
 assertEquals("testValue", commandLineProperties.get("test"));
 }

 @Test
 public void testGetProperties() {
 Option option = new Option("prop", "property", true, "Property name");
 commandLine.addOption(option);
 Map properties = new HashMap();
 properties.put("test1", "testValue1");
 properties.put("test2", "testValue2");
 commandLine.addProperty(option, "test1", "testValue1");
 commandLine.addProperty(option, "test2", "testValue2");
 Set propertiesSet = commandLine.getProperties(option);
 assertEquals(2, propertiesSet.size());
 assertTrue(propertiesSet.contains("test1"));
 assertTrue(propertiesSet.contains("test2"));
 }

 @Test
 public void testToString() {
 Option option1 = new Option("o", "output", true, "Output directory");
 Option option2 = new Option("i", "input", true, "Input file");
 Option option3 = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 commandLine.addValue(option1, "testOutput");
 commandLine.addValue(option2, "testInput");
 commandLine.addSwitch(option3, true);
 String expected = "--output testOutput --input testInput --prefix";
 assertEquals(expected, commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 Option option1 = new Option("o", "output", true, "Output directory");
 Option option2 = new Option("i", "input", true, "Input file");
 Option option3 = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 List options = commandLine.getOptions();
 assertEquals(3, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 assertTrue(options.contains(option3));
 }

 @Test
 public void testGetOptionTriggers() {
 Option option1 = new Option("o", "output", true, "Output directory");
 Option option2 = new Option("i", "input", true, "Input file");
 Option option3 = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 Set triggers = commandLine.getOptionTriggers();
 assertEquals(3, triggers.size());
 assertTrue(triggers.contains("o"));
 assertTrue(triggers.contains("i"));
 assertTrue(triggers.contains("p"));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("o", "output", true, "Output directory");
 commandLine.addOption(option);
 List defaultValues = new ArrayList();
 defaultValues.add("default1");
 defaultValues.add("default2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertTrue(values.contains("default1"));
 assertTrue(values.contains("default2"));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 Option option1 = new Option("o", "output", true, "Output directory");
 Option option2 = new Option("i", "input", true, "Input file");
 Option option3 = new Option("p", "prefix", false, "Show prefixes");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.addOption(option3);
 commandLine.addValue(option1, "testOutput");
 commandLine.addValue(option2, "testInput");
 commandLine.addSwitch(option3, true);
 List normalised = commandLine.getNormalised();
 assertEquals(3, normalised.size());
 assertTrue(normalised.contains("--output testOutput"));
 assertTrue(normalised.contains("--input testInput"));
 assertTrue(normalised.contains("--prefix"));
 }
}