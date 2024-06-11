import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ArgumentImplTest {

 private WriteableCommandLine commandLine;
 private Argument option;

 @Before
 public void setUp() {
 commandLine = new CommandLine();
 option = new ArgumentImpl("argName", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 }

 @Test
 public void testProcessValues() {
 List<String> args = List.of("argValue1", "argValue2");
 argumentValues(commandLine, option, args);

 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("argValue1", commandLine.getValues(option).get(0));
 assertEquals("argValue2", commandLine.getValues(option).get(1));
 }

 @Test
 public void testProcessValuesWithConsumingOption() {
 List<String> args = List.of("--consumingOption", "argValue1", "argValue2");
 argumentValues(commandLine, option, args);

 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("argValue1", commandLine.getValues(option).get(0));
 assertEquals("argValue2", commandLine.getValues(option).get(1));
 }

 @Test
 public void testProcessValuesWithMaximumNumberOfValues() {
 List<String> args = List.of("argValue1", "argValue2", "argValue3");
 argumentValues(commandLine, option, args);

 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("argValue1", commandLine.getValues(option).get(0));
 assertEquals("argValue2", commandLine.getValues(option).get(1));
 assertNull(commandLine.getValues(option).get(2));
 }

 @Test
 public void testProcessValuesWithMinimumNumberOfValues() {
 List<String> args = List.of("argValue1");
 argumentValues(commandLine, option, args);

 assertEquals(1, commandLine.getValues(option).size());
 assertEquals("argValue1", commandLine.getValues(option).get(0));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesWithInvalidNumberOfValues() {
 List<String> args = List.of("argValue1", "argValue2", "argValue3", "argValue4");
 argumentValues(commandLine, option, args);
 }

 @Test
 public void testProcessValuesWithSubsequentSplit() {
 List<String> args = List.of("value1 argValue2 value3 argValue4");
 option = new ArgumentImpl("argName", "description", 1, 4, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ' ', null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 argumentValues(commandLine, option, args);

 assertEquals(4, commandLine.getValues(option).size());
 assertEquals("value1", commandLine.getValues(option).get(0));
 assertEquals("argValue2", commandLine.getValues(option).get(1));
 assertEquals("value3", commandLine.getValues(option).get(2));
 assertEquals("argValue4", commandLine.getValues(option).get(3));
 }

 @Test
 public void testCanProcess() {
 String arg = "argValue";
 assertTrue(option.canProcess(commandLine, arg));
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("argName", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testGetName() {
 assertEquals("argName", option.getName());
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 assertNotNull(triggers);
 assertEquals(0, triggers.size());
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 assertNotNull(prefixes);
 assertEquals(0, prefixes.size());
 }

 @Test
 public void testGetInitialSeparator() {
 assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, option.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, option.getSubsequentSeparator());
 }

 @Test
 public void testGetValidator() {
 assertNull(option.getValidator());
 }

 @Test
 public void testProcess() {
 List<String> args = List.of("argValue1", "argValue2");
 option.process(commandLine, args.listIterator());

 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("argValue1", commandLine.getValues(option).get(0));
 assertEquals("argValue2", commandLine.getValues(option).get(1));
 }

 @Test
 public void testValidate() {
 List<String> args = List.of("argValue1", "argValue2");
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidValue() {
 List<String> args = List.of("argValue1", "argValue2", "invalidValue");
 option.validate(commandLine);
 }

 @Test
 public void testValidateWithMaximumNumberOfValues() {
 List<String> args = List.of("argValue1", "argValue2", "argValue3", "argValue4");
 option = new ArgumentImpl("argName", "description", 2, 4, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 option.validate(commandLine);
 }

 @Test
 public void testValidateWithMinimumNumberOfValues() {
 List<String> args = List.of("argValue1");
 option = new ArgumentImpl("argName", "description", 2, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 option.validate(commandLine);
 }

 @Test
 public void testValidateWithNoValues() {
 option = new ArgumentImpl("argName", "description", 0, 0, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 option.validate(commandLine);
 }

 @Test
 public void testAppendUsage() {
 StringBuilder sb = new StringBuilder();
 option.appendUsage(sb, null, null);

 assertEquals("  [argName] <value> ...\n", sb.toString());
 }

 @Test
 public void testGetConsumeRemaining() {
 assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, option.getConsumeRemaining());
 }

 @Test
 public void testGetDefaultValues() {
 assertNull(option.getDefaultValues());
 }

 @Test
 public void testProcessWithDefaultValues() {
 option = new ArgumentImpl("argName", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 List.of("defaultValue1"), 0);
 List<String> args = List.of();
 option.process(commandLine, args.listIterator());

 assertEquals(1, commandLine.getValues(option).size());
 assertEquals("defaultValue1", commandLine.getValues(option).get(0));
 }

 @Test
 public void testProcessWithoutDefaultValues() {
 option = new ArgumentImpl("argName", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 List<String> args = List.of();
 option.process(commandLine, args.listIterator());

 assertNull(commandLine.getValues(option));
 }

 @Test
 public void testValidateWithDefaultValues() {
 List<String> args = List.of();
 option = new ArgumentImpl("argName", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 List.of("defaultValue1"), 0);
 option.validate(commandLine);
 }

 @Test
 public void testValidateWithNoDefaultValues() {
 option = new ArgumentImpl("argName", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
 option.validate(commandLine);
 }

 private void argumentValues(WriteableCommandLine commandLine, Argument option, List<String> args) {
 option.processValues(commandLine, args.listIterator(), option);
 }

}