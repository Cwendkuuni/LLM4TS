import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option optionWithValue;
 private Option optionWithArgument;
 private Option optionWithSwitch;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("testOption")
 .longOpt("test")
 .build(), new ArrayList<String>());
 optionWithValue = new Option.Builder("valueOption")
 .longOpt("value")
 .valueSeparator('=')
 .hasArg()
 .build();
 optionWithArgument = new Option.Builder("argOption")
 .longOpt("arg")
 .argName("argName")
 .hasArg()
 .required()
 .build();
 optionWithSwitch = new Option.Builder("switchOption")
 .longOpt("switch")
 .build();
 commandLine.addOption(optionWithValue);
 commandLine.addOption(optionWithArgument);
 commandLine.addOption(optionWithSwitch);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(optionWithValue, "testValue");
 List values = commandLine.getValues(optionWithValue, null);
 assertEquals("testValue", values.get(0));
 }

 @Test
 public void testAddArgument() {
 commandLine.addArgument(optionWithArgument.getOpt(), "testArg");
 List args = commandLine.getValues(optionWithArgument, null);
 assertEquals("testArg", args.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(optionWithSwitch, true);
 assertTrue(commandLine.getSwitch(optionWithSwitch, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(optionWithSwitch, true);
 commandLine.addSwitch(optionWithSwitch, false);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(optionWithValue));
 assertTrue(commandLine.hasOption(optionWithArgument));
 assertTrue(commandLine.hasOption(optionWithSwitch));
 }

 @Test
 public void testGetOption() {
 Option option = commandLine.getOption("value");
 assertEquals(optionWithValue, option);
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(optionWithValue, null);
 assertEquals("testValue", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 assertTrue(commandLine.getSwitch(optionWithSwitch, false));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty("testProperty", "testValue");
 Set properties = commandLine.getProperties();
 assertEquals("testProperty", properties.iterator().next());
 }

 @Test
 public void testToString() {
 String expected = "testOption test valueOption=testValue argOption argName=testArg";
 assertEquals(expected, commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 List options = commandLine.getOptions();
 assertTrue(options.contains(optionWithValue));
 assertTrue(options.contains(optionWithArgument));
 assertTrue(options.contains(optionWithSwitch));
 }

 @Test
 public void testGetOptionTriggers() {
 Set triggers = commandLine.getOptionTriggers();
 assertTrue(triggers.contains("value"));
 assertTrue(triggers.contains("arg"));
 assertTrue(triggers.contains("switch"));
 }

 @Test
 public void testSetDefaultValues() {
 commandLine.setDefaultValues(optionWithValue, new ArrayList<String>() {{
 add("defaultValue");
 }});
 List values = commandLine.getValues(optionWithValue, null);
 assertEquals("defaultValue", values.get(0));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(optionWithSwitch, true);
 assertTrue(commandLine.getSwitch(optionWithSwitch, false));
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 assertEquals("testArg", normalised.get(2));
 }
}