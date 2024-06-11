import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("a", "arg", true, "an argument"), new ArrayList<>());

 // Test adding an option
 Option option = new Option("t", "trigger", false, "a trigger option");
 cmdLine.addOption(option);
 assertTrue(cmdLine.hasOption(option));

 // Test adding a value to an option
 Argument arg = new Argument("value");
 cmdLine.addValue(option, arg);
 List values = cmdLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals(arg.getValue(), values.get(0));

 // Test adding a switch to an option
 cmdLine.addSwitch(option, true);
 Boolean switchValue = cmdLine.getSwitch(option, false);
 assertTrue(switchValue);

 // Test getting an option
 Option gotOption = cmdLine.getOption("trigger");
 assertEquals(option, gotOption);

 // Test getting properties
 cmdLine.addProperty("property", "value");
 String propertyValue = cmdLine.getProperty("property", "defaultValue");
 assertEquals("value", propertyValue);

 // Test getting all properties
 Set<String> properties = cmdLine.getProperties();
 assertTrue(properties.contains("property"));

 // Test toString method
 assertEquals("arg value", cmdLine.toString());
 }
}