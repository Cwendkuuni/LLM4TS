import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 @Test
 public void testArgumentImplCreation() {
 ArgumentImpl argument = new ArgumentImpl("argName", "argDescription", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--remaining", Arrays.asList("val1", "val2"), 0);
 assertEquals("argName", argument.getPreferredName());
 assertEquals("argDescription", argument.getDescription());
 assertEquals(1, argument.getMinimum());
 assertEquals(3, argument.getMaximum());
 assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
 assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
 assertEquals(false, argument.isRequired());
 assertEquals("--remaining", argument.getConsumeRemaining());
 assertEquals(Arrays.asList("val1", "val2"), argument.getDefaultValues());
 }

 @Test
 public void testArgumentProcessValues() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 ArgumentImpl argument = new ArgumentImpl("argName", "argDescription", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--remaining", Arrays.asList("val1", "val2"), 0);
 List<String> arguments = Arrays.asList("--argName", "val3", "--consumeRemaining", "val4", "val5");
 argument.process(commandLine, arguments.listIterator());
 assertEquals(3, commandLine.getValues(argument).size());
 assertEquals("val3", commandLine.getValue(argument, 0));
 assertEquals("val4", commandLine.getValue(argument, 1));
 assertEquals("val5", commandLine.getValue(argument, 2));
 }

 @Test
 public void testArgumentValidator() {
 ArgumentImpl argument = new ArgumentImpl("argName", "argDescription", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new CustomValidator(), "--remaining", Arrays.asList("val1", "val2"), 0);
 List<String> validValues = Arrays.asList("val1", "val2");
 List<String> invalidValues = Arrays.asList("invalidVal1", "invalidVal2");
 WriteableCommandLine commandLine = new DefaultCommandLine();
 commandLine.setValues(argument, validValues);
 argument.validate(commandLine, argument);
 commandLine.setValues(argument, invalidValues);
 try {
 argument.validate(commandLine, argument);
 fail("Expected InvalidArgumentException");
 } catch (OptionException e) {
 assertEquals("InvalidArgumentException", e.getClass().getName());
 }
 }

 @Test
 public void testArgumentUsage() {
 ArgumentImpl argument = new ArgumentImpl("argName", "argDescription", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--remaining", Arrays.asList("val1", "val2"), 0);
 StringBuilder buffer = new StringBuilder();
 argument.appendUsage(buffer, null, null);
 assertTrue(buffer.toString().contains("argName"));
 assertTrue(buffer.toString().contains("argDescription"));
 }
}

class CustomValidator implements Validator {
 @Override
 public void validate(List<String> values) throws InvalidArgumentException {
 // Custom validation logic goes here
 }
}