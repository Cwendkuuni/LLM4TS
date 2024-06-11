import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", true, "defaultValue");
 commandLine.addOption(option);
 Assert.assertEquals(1, commandLine.getOptions().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", true, "defaultValue");
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", true, "defaultValue"));
 commandLine.addValue(commandLine.getOption("option"), "value");
 Assert.assertEquals(1, commandLine.getValues(commandLine.getOption("option"), new ArrayList()).size());
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", false, "defaultValue"));
 commandLine.addSwitch(commandLine.getOption("option"), true);
 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", false, "defaultValue"));
 Assert.assertTrue(commandLine.hasOption(commandLine.getOption("option")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", false, "defaultValue"));
 Assert.assertEquals(commandLine.getOption("option"), commandLine.getOption("option"));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", true, "defaultValue");
 commandLine.addOption(option);
 commandLine.addValue(commandLine.getOption("option"), "value1");
 commandLine.addValue(commandLine.getOption("option"), "value2");
 Assert.assertEquals(2, commandLine.getValues(commandLine.getOption("option"), new ArrayList()).size());
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", true, "defaultValue");
 commandLine.addOption(option);
 commandLine.addValue(commandLine.getOption("option"), "value1");
 Assert.assertEquals(1, commandLine.getUndefaultedValues(option).size());
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", false, "defaultValue");
 commandLine.addOption(option);
 commandLine.addSwitch(commandLine.getOption("option"), true);
 Assert.assertEquals(true, commandLine.getSwitch(commandLine.getOption("option"), false));
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", false, "defaultValue");
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 Assert.assertEquals("value", commandLine.getProperty(commandLine.getOption("option"), "property", "defaultValue"));
 }

 @Test
 public void testGetProperties() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", false, "defaultValue");
 commandLine.addOption(option);
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Set<String> properties = commandLine.getProperties(option);
 Assert.assertEquals(2, properties.size());
 Assert.assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", false, "defaultValue"));
 Assert.assertTrue(commandLine.looksLikeOption("option"));
 Assert.assertFalse(commandLine.looksLikeOption("otherOption"));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option", "description", false, "defaultValue"));
 commandLine.addOption(new Option("option2", "description2", false, "defaultValue2"));
 commandLine.addValue(commandLine.getOption("option"), "value1");
 commandLine.addValue(commandLine.getOption("option2"), "value2");
 String actual = commandLine.toString();
 String expected = "\"option\" value1 \"option2\" value2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option1", "description1", false, "defaultValue1"));
 commandLine.addOption(new Option("option2", "description2", false, "defaultValue2"));
 List<Option> options = commandLine.getOptions();
 Assert.assertEquals(2, options.size());
 Assert.assertEquals(commandLine.getOption("option1"), options.get(0));
 Assert.assertEquals(commandLine.getOption("option2"), options.get(1));
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option1", "description1", false, "defaultValue1"));
 commandLine.addOption(new Option("option2", "description2", false, "defaultValue2"));
 Set<String> triggers = commandLine.getOptionTriggers();
 Assert.assertEquals(2, triggers.size());
 Assert.assertEquals("option1", triggers.iterator().next());
 Assert.assertEquals("option2", triggers.iterator().next());
 }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", true, "defaultValue");
 commandLine.addOption(option);
 List defaults = new ArrayList();
 defaults.add("defaultValue1");
 defaults.add("defaultValue2");
 commandLine.setDefaultValues(option, defaults);
 Assert.assertEquals(2, commandLine.getDefaultValues(option).size());
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 Option option = new Option("option", "description", false, "defaultValue");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 Assert.assertEquals(true, commandLine.getDefaultSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
 commandLine.addOption(new Option("option1", "description1", false, "defaultValue1"));
 commandLine.addOption(new Option("option2", "description2", false, "defaultValue2"));
 commandLine.addValue(commandLine.getOption("option1"), "value1");
 commandLine.addValue(commandLine.getOption("option2"), "value2");
 List normalised = commandLine.getNormalised();
 String expected = "option1 value1 option2 value2";
 Assert.assertEquals(expected, commandLine.toString());
 }
}