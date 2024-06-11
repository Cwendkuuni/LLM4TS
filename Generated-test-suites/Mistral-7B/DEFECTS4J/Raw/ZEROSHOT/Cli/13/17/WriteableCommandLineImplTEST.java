import org.apache.commons.cli2.Option;
import org.junit.Test;
import org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl cmdLine;
 private Option option;

 @Test
 public void testWriteableCommandLineImpl() {
 // Test constructor
 cmdLine = new WriteableCommandLineImpl(new Option("a", "arg", false, "Argument A"), new ArrayList());

 // Test addOption
 option = new Option("b", "bool", false, "Boolean Option");
 cmdLine.addOption(option);
 assertTrue(cmdLine.hasOption(option));

 // Test addValue
 List values = cmdLine.getValues(option, null);
 cmdLine.addValue(option, "test");
 assertEquals("test", values.get(0));

 // Test addSwitch
 cmdLine.addSwitch(option, true);
 assertTrue(cmdLine.getSwitch(option, false));

 // Test getOption
 Option getOption = cmdLine.getOption("b");
 assertEquals(option, getOption);

 // Test getValues
 List getValues = cmdLine.getValues(option, null);
 assertEquals(1, getValues.size());
 assertEquals("test", getValues.get(0));

 // Test getSwitch
 assertTrue(cmdLine.getSwitch(option, false));

 // Test addProperty
 cmdLine.addProperty("testProperty", "testValue");
 assertEquals("testValue", cmdLine.getProperty("testProperty", ""));

 // Test getProperties
 Set properties = cmdLine.getProperties();
 assertEquals(1, properties.size());
 assertEquals("testProperty", properties.iterator().next());

 // Test toString
 assertEquals("-b test --arg", cmdLine.toString());
 }
}