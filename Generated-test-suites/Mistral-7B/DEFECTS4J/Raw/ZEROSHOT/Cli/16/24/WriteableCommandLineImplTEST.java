import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 Option optionA = new Option("a", true, "a");
 Option optionB = new Option("b", false, "b");
 Argent argumentA = new Argent("argA");
 Argent argumentB = new Argent("argB");

 List arguments = new ArrayList();
 arguments.add(argumentA);
 arguments.add(argumentB);

 commandLine = new WriteableCommandLineImpl(optionA, arguments);
 commandLine.addOption(optionB);
 }

 @Test
 public void testHasOption() {
 Option optionA = commandLine.getOption("a");
 Option optionB = commandLine.getOption("b");

 assertTrue(commandLine.hasOption(optionA));
 assertTrue(commandLine.hasOption(optionB));
 }

 @Test
 public void testGetOption() {
 Option optionA = commandLine.getOption("a");
 Option optionB = commandLine.getOption("b");

 assertEquals(optionA, commandLine.getOption("a"));
 assertEquals(optionB, commandLine.getOption("b"));
 }

 @Test
 public void testGetValues() {
 Argument argumentA = (Argument) commandLine.getValues(optionA, null).get(0);
 Argument argumentB = (Argument) commandLine.getValues(optionA, null).get(1);

 assertEquals("argA", argumentA.getValue());
 assertEquals("argB", argumentB.getValue());
 }

 @Test
 public void testGetSwitch() {
 boolean switchB = commandLine.getSwitch(optionB, false);

 assertFalse(switchB);
 }

 @Test
 public void testGetProperty() {
 Option propertyOption = new Option("p", true, "p");
 commandLine.addProperty(propertyOption, "property", "value");

 assertEquals("value", commandLine.getProperty(propertyOption, ""));
 }

 @Test
 public void testGetProperties() {
 Option propertyOption = new Option("p", true, "p");
 commandLine.addProperty(propertyOption, "property1", "value1");
 commandLine.addProperty(propertyOption, "property2", "value2");

 Set properties = commandLine.getProperties(propertyOption);

 assertEquals(2, properties.size());
 assertEquals("property1", properties.iterator().next());
 assertEquals("property2", properties.iterator().next());
 }

 @Test
 public void testToString() {
 assertEquals("argA argB", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 List options = commandLine.getOptions();
 Option optionA = (Option) options.get(0);
 Option optionB = (Option) options.get(1);

 assertEquals(optionA, optionA);
 assertEquals(optionB, optionB);
 }

 @Test
 public void testGetOptionTriggers() {
 Set triggers = commandLine.getOptionTriggers();

 assertEquals(2, triggers.size());
 assertTrue(triggers.contains("a"));
 assertTrue(triggers.contains("b"));
 }
}