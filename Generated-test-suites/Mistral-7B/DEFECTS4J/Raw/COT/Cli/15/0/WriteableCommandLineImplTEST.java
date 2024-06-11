import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 Assert.assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 commandLine.addValue(new Option("option"), "value");
 Assert.assertEquals("value", commandLine.getValues(new Option("option"), Collections.emptyList()).get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", false, "o", "opt"));
 commandLine.addSwitch(new Option("option"), true);
 Assert.assertTrue(commandLine.getSwitch(new Option("option"), false));
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 Assert.assertTrue(commandLine.hasOption(new Option("option")));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 Assert.assertEquals(new Option("option"), commandLine.getOption("opt"));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 commandLine.addValue(new Option("option"), "value1");
 commandLine.addValue(new Option("option"), "value2");
 List values = commandLine.getValues(new Option("option"), Collections.emptyList());
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", true, "o", "opt"));
 commandLine.addValue(new Option("option"), "value1");
 Assert.assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(new Option("option")));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", false, "o", "opt"));
 commandLine.addSwitch(new Option("option"), true);
 Assert.assertTrue(commandLine.getSwitch(new Option("option"), false) == null);
 Assert.assertTrue(commandLine.getSwitch(new Option("option"), true));
 }

 @Test
 public void testgetProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new PropertyOption("property", true, "p", "prop"));
 commandLine.addProperty(new PropertyOption("property"), "value");
 Assert.assertEquals("value", commandLine.getProperty(new PropertyOption("property"), "value"));
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new PropertyOption("property", true, "p", "prop"));
 commandLine.addProperty(new PropertyOption("property"), "value1");
 commandLine.addProperty(new PropertyOption("property"), "value2");
 Set properties = commandLine.getProperties(new PropertyOption("property"));
 Assert.assertEquals(2, properties.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", false, "o", "opt"));
 commandLine.addSwitch(new Option("option"), true);
 commandLine.addSwitch(new Option("option"), false);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueToNonArgument() {
 commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 commandLine.addOption(new Option("option", false, "o", "opt"));
 commandLine.addValue(new Option("option"), "value");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueToMultipleArguments() {
 commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList(), new Option("option2"), Collections.emptyList());
 commandLine.addOption(new Option("option1", false, "o1", "opt1"));
 commandLine.addOption(new Option("option2", false, "o2", "opt2"));
 commandLine.addValue(new Option("option1"), "value1");
 commandLine.addValue(new Option("option1"), "value2");
 commandLine.addValue(new Option("option2"), "value1");
 }
}