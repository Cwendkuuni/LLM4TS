import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 @Test
 public void testAddOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 Assert.assertEquals(option, writeableCommandLine.getOption("optionName"));
 }

 @Test
 public void testAddValue() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 List values = writeableCommandLine.getValues(option, null);
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addSwitch(option, true);
 boolean switchValue = writeableCommandLine.getSwitch(option, false);
 Assert.assertTrue(switchValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchExisting() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addSwitch(option, true);
 writeableCommandLine.addSwitch(option, false);
 }

 @Test
 public void testHasOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 Assert.assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testNoOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Assert.assertFalse(writeableCommandLine.hasOption(OptionBuilder.withName("nonExistingOption").build()));
 }

 @Test
 public void testGetOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 Assert.assertEquals(option, writeableCommandLine.getOption("optionName"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionNull() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Assert.assertNull(writeableCommandLine.getOption(null));
 }

 @Test
 public void testGetValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 Assert.assertEquals(2, values.size());
 Assert.assertEquals("value1", values.get(0));
 Assert.assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 List values = writeableCommandLine.getUndefaultedValues(option);
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addSwitch(option, true);
 boolean switchValue = writeableCommandLine.getSwitch(option, false);
 Assert.assertTrue(switchValue);
 }

 @Test
 public void testGetSwitchFalse() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = OptionBuilder.withName("optionName").build();
 writeableCommandLine.addSwitch(option, false);
 boolean switchValue = writeableCommandLine.getSwitch(option, true);
 Assert.assertFalse(switchValue);
 }

 @Test
 public void testGetProperty() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = new PropertyOption("propertyName", true, "defaultValue");
 writeableCommandLine.addProperty(option, "value1");
 String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");
 Assert.assertEquals("value1", propertyValue);
 }

 @Test
 public void testGetPropertyDefault() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = new PropertyOption("propertyName", true, "defaultValue");
 String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");
 Assert.assertEquals("defaultValue", propertyValue);
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = new PropertyOption("propertyName", true, "defaultValue");
 writeableCommandLine.addProperty(option, "value1");
 Map<String, String> properties = writeableCommandLine.getProperties(option);
 Assert.assertEquals(1, properties.size());
 Assert.assertEquals("value1", properties.get("propertyName"));
 }

 @Test
 public void testGetProperties() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 Option option = new PropertyOption("propertyName1", true, "defaultValue1");
 Option option2 = new PropertyOption("propertyName2", true, "defaultValue2");
 writeableCommandLine.addProperty(option, "value1");
 writeableCommandLine.addProperty(option2, "value2");
 Set<String> properties = writeableCommandLine.getProperties(new PropertyOption());
 Assert.assertEquals(2, properties.size());
 Assert.assertTrue(properties.containsAll(Arrays.asList("propertyName1", "propertyName2")));
 }

 @Test
 public void testToString() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
 Assert.assertEquals("arg1 arg2", writeableCommandLine.toString());
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-optionName", "value1", "-propertyName=value2"};
 WriteableCommandLine writeableCommandLine = null;
 try {
 writeableCommandLine = (WriteableCommandLine) parser.parse(WriteableCommandLine.class, args);
 } catch (ParseException e) {
 //Ignore
 }
 Option option = OptionBuilder.withName("optionName").build();
 Assert.assertEquals(option, writeableCommandLine.getOption("optionName"));
 List values = writeableCommandLine.getValues(option, null);
 Assert.assertEquals(1, values.size());
 Assert.assertEquals("value1", values.get(0));
 Option propertyOption = new PropertyOption("propertyName", true, "defaultValue");
 Map<String, String> properties = writeableCommandLine.getProperties(propertyOption);
 Assert.assertEquals(1, properties.size());
 Assert.assertEquals("value2", properties.get("propertyName"));
 }

 @Test
 public void testParseNoOption() {
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"arg1", "arg2"};
 WriteableCommandLine writeableCommandLine = null;
 try {
 writeableCommandLine = (WriteableCommandLine) parser.parse(WriteableCommandLine.class, args);
 } catch (ParseException e) {
 //Ignore
 }
 Assert.assertEquals(args.length, writeableCommandLine.getOptions().size());
 }
}