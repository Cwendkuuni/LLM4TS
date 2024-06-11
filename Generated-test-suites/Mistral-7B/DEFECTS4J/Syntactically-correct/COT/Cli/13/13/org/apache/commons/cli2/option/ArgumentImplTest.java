import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private ArgumentImpl argument;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testGetName() {
 assertEquals("argName", argument.getName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("description", argument.getDescription());
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, argument.getMinimum());
 }

 @Test
 public void testGetMaximum() {
 assertEquals(1, argument.getMaximum());
 }

 @Test
 public void testProcessValues() throws OptionException {
 List values = Collections.singletonList("value");
 commandLine.parseArgumentList(values.toArray(new String[0]), null);
 argument.process(commandLine, commandLine.getArguments().listIterator());
 assertEquals(1, commandLine.getValues(argument).size());
 assertEquals("value", commandLine.getValues(argument).get(0));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesExceedMaximum() throws OptionException {
 List values = Arrays.asList("value1", "value2", "value3");
 commandLine.parseArgumentList(values.toArray(new String[0]), null);
 argument.process(commandLine, commandLine.getArguments().listIterator());
 fail("Should have thrown OptionException with message 'ArgumentImpl UNEXPECTED_VALUE'");
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesInvalidValue() throws OptionException {
 List values = Arrays.asList("value1", "value2", "invalidValue");
 commandLine.parseArgumentList(values.toArray(new String[0]), null);
 argument.process(commandLine, commandLine.getArguments().listIterator());
 fail("Should have thrown OptionException with message 'ArgumentImpl UNEXPECTED_VALUE'");
 }

 @Test
 public void testCanProcess() {
 assertTrue(argument.canProcess(commandLine, "value"));
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = argument.getPrefixes();
 assertEquals(Collections.EMPTY_SET, prefixes);
 }

 @Test
 public void testProcess() throws OptionException {
 argument.process(commandLine, commandLine.getArguments().listIterator());
 }

 @Test
 public void testGetInitialSeparator() {
 assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
 }

 @Test
 public void testGetTriggers() {
 Set triggers = argument.getTriggers();
 assertEquals(Collections.EMPTY_SET, triggers);
 }

 @Test
 public void testGetConsumeRemaining() {
 assertEquals(null, argument.getConsumeRemaining());
 }

 @Test
 public void testGetDefaultValues() {
 assertEquals(null, argument.getDefaultValues());
 }

 @Test
 public void testGetValidator() {
 assertEquals(null, argument.getValidator());
 }

 @Test(expected = OptionException.class)
 public void testValidateFails() throws OptionException {
 List values = Collections.singletonList("value");
 commandLine.getValues().addAll(values);
 argument.validate(commandLine);
 fail("Should have thrown OptionException with message 'ArgumentImpl MISSING_VALUES'");
 }

 @Test
 public void testValidateSucceeds() throws OptionException {
 argument.validate(commandLine);
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 argument.appendUsage(buffer, null, null);
 assertEquals("argName", buffer.toString());
 }

}