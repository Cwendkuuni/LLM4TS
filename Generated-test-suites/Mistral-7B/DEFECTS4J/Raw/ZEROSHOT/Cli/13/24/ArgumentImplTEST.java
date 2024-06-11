import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 @Test
 public void testArgumentImpl() {
 ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, ':', ';', null, "--", null, 0);

 assertEquals("argName", argument.getPreferredName());
 assertEquals("description", argument.getDescription());
 assertEquals(1, argument.getMinimum());
 assertEquals(3, argument.getMaximum());
 assertEquals(':', argument.getInitialSeparator());
 assertEquals(';', argument.getSubsequentSeparator());
 assertEquals(false, argument.isSubsequentSplit());
 assertEquals(null, argument.getValidator());
 assertEquals("--", argument.getConsumeRemaining());
 assertEquals(Collections.emptyList(), argument.getDefaultValues());

 ArgumentImpl argumentWithDefaults = new ArgumentImpl("argName", "description", 1, 3, ':', ';', null, "--", Arrays.asList("default1", "default2"), 0);

 assertEquals("argName", argumentWithDefaults.getPreferredName());
 assertEquals("description", argumentWithDefaults.getDescription());
 assertEquals(1, argumentWithDefaults.getMinimum());
 assertEquals(3, argumentWithDefaults.getMaximum());
 assertEquals(':', argumentWithDefaults.getInitialSeparator());
 assertEquals(';', argumentWithDefaults.getSubsequentSeparator());
 assertEquals(false, argumentWithDefaults.isSubsequentSplit());
 assertEquals(null, argumentWithDefaults.getValidator());
 assertEquals("--", argumentWithDefaults.getConsumeRemaining());
 assertEquals(Arrays.asList("default1", "default2"), argumentWithDefaults.getDefaultValues());
 }

 @Test
 public void testProcessValues() {
 WriteableCommandLine commandLine = new BasicCommandLine();
 List<String> arguments = Arrays.asList("--argName", "--argName value1", "--argName value2", "--argName value3", "--consume-remaining");

 ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, ':', ';', null, "--", null, 0);

 argument.process(commandLine, arguments.listIterator());

 assertEquals(2, commandLine.getValues(argument).size());
 assertEquals("value1", commandLine.getValues(argument).get(0));
 assertEquals("value2", commandLine.getValues(argument).get(1));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesWithInvalidArgument() {
 WriteableCommandLine commandLine = new BasicCommandLine();
 List<String> arguments = Arrays.asList("--argName", "--argName value1", "--argName value1 value2", "--argName value1 value2 value3");

 ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 2, ':', ';', null, "--", null, 0);

 try {
 argument.process(commandLine, arguments.listIterator());
 } catch (OptionException e) {
 assertEquals("argName: too many arguments (expected: 2, found: 3)", e.getMessage());
 }
 }

 @Test
 public void testCanProcess() {
 WriteableCommandLine commandLine = new BasicCommandLine();

 ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, ':', ';', null, "--", null, 0);

 assertTrue(argument.canProcess(commandLine, "argValue"));
 }

 @Test
 public void testGetPrefixes() {
 ArgumentImpl argument = new ArgumentImpl("argName", "description", 1, 3, ':', ';', null, "--", null, 0);

 assertEquals(Collections.emptySet(), argument.getPrefixes());
 }

}