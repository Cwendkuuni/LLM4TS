import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgumentImplTest {

 private ArgumentImpl argument;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 argument = new ArgumentImpl("myArgument", "Description of myArgument", 1, 3, '--', ' ', null, "--", Collections.emptyList(0));
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("myArgument", argument.getPreferredName());
 }

 @Test
 public void testProcessValuesPositiveCase() {
 List values = Arrays.asList("value1", "value2", "value3");
 commandLine.parseArgument(argument.getName(), values);
 assertEquals(3, argument.getValueCount());
 assertEquals("value1", argument.getValue(0));
 assertEquals("value2", argument.getValue(1));
 assertEquals("value3", argument.getValue(2));
 }

 @Test
 public void testProcessValuesNegativeCase() {
 List values = Arrays.asList("value1", "value2", "value3", "value4");
 assertThrows(OptionException.class, () -> commandLine.parseArgument(argument.getName(), values));
 }

 @Test
 public void testProcessValuesInvalidValueType() {
 List values = Arrays.asList("value1", 2, "value3");
 assertThrows(OptionException.class, () -> commandLine.parseArgument(argument.getName(), values));
 }

 @Test
 public void testCanProcessPositiveCase() {
 assertTrue(argument.canProcess(commandLine, "value1"));
 }

 @Test
 public void testCanProcessNegativeCase() {
 assertFalse(argument.canProcess(commandLine, "--myArgument"));
 }

 @Test
 public void testGetPrefixesEmptySet() {
 assertEquals(Collections.emptySet(), argument.getPrefixes());
 }

 @Test
 public void testProcessPositiveCase() {
 argument.process(commandLine, commandLine.iterator());
 assertEquals(3, argument.getValueCount());
 }

 @Test
 public void testProcessNegativeCase() {
 List values = Arrays.asList("value1", "value2");
 assertThrows(OptionException.class, () -> argument.process(commandLine, commandLine.iterator(), null));
 }

 @Test
 public void testGetInitialSeparator() {
 assertEquals('--', argument.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 assertEquals(' ', argument.getSubsequentSeparator());
 }

 @Test
 public void testGetTriggersEmptySet() {
 assertEquals(Collections.emptySet(), argument.getTriggers());
 }

 @Test
 public void testGetConsumeRemaining() {
 assertEquals("--", argument.getConsumeRemaining());
 }

 @Test
 public void testGetDefaultValuesEmptyList() {
 assertEquals(Collections.emptyList(), argument.getDefaultValues());
 }

 @Test
 public void testValidatePositiveCase() throws OptionException {
 List values = Arrays.asList("value1", "value2");
 argument.validate(commandLine);
 }

 @Test
 public void testValidateNegativeCase() {
 List values = Arrays.asList("value1", "value2", "value3");
 assertThrows(OptionException.class, () -> argument.validate(commandLine));
 }

 @Test
 public void testValidateInvalidValueType() {
 List values = Arrays.asList("value1", 2, "value3");
 assertThrows(OptionException.class, () -> argument.validate(commandLine));
 }

 @Test
 public void testAppendUsagePositiveCase() {
 StringBuffer buffer = new StringBuffer();
 argument.appendUsage(buffer, Collections.emptySet(), null);
 assertEquals("--myArgument [value]", buffer.toString());
 }

 @Test
 public void testAppendUsageNegativeCase() {
 StringBuffer buffer = new StringBuffer();argument.appendUsage(buffer, Collections.emptySet(), null);
 assertEquals("--myArgument [value]", buffer.toString());
 }

 @Test
 public void testGetDescription() {
 assertEquals("Description of myArgument", argument.getDescription());
 }

 @Test
 public void testHelpLinesPositiveCase() {
 List<HelpLine> helpLines = argument.helpLines(1, Collections.emptySet(), null);
 assertEquals(1, helpLines.size());
 assertEquals("--myArgument [value]", helpLines.get(0).getLine());
 }

 @Test
 public void testGetMaximum() {
 assertEquals(3, argument.getMaximum());
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, argument.getMinimum());
 }

 @Test
 public void testStripBoundaryQuotesEmptyString() {
 assertEquals("", argument.stripBoundaryQuotes(""));
 }

 @Test
 public void testStripBoundaryQuotesStringWithQuotes() {
 assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
 }

 @Test
 public void testStripBoundaryQuotesStringWithoutQuotes() {
 assertEquals("value", argument.stripBoundaryQuotes("value"));
 }

 @Test
 public void testDefaultsPositiveCase() {
 argument.defaults(commandLine);
 assertEquals(Collections.emptyList(), commandLine.getValues(argument));
 }
}