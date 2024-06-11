import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private CommandLine cmd;
 private Argument argument;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("arg", argument.getPreferredName());
 }

 @Test
 public void testProcessValues() {
 String[] args = {"--arg", "value1", "value2"};
 argument.processValues(cmd, TestUtils.list(args), null);
 assertEquals(2, cmd.getValues("arg").size());
 assertEquals("value1", cmd.getValues("arg").get(0));
 assertEquals("value2", cmd.getValues("arg").get(1));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesWithInvalidNumberOfArguments() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
 String[] args = {"--arg", "value1"};
 argument.processValues(cmd, TestUtils.list(args), null);
 assertEquals(1, cmd.getValues("arg").size());
 argument.processValues(cmd, TestUtils.list("value2"), null);
 throw new AssertionError("Should have thrown OptionException");
 }

 @Test
 public void testCanProcess() {
 assertTrue(argument.canProcess(cmd, "value"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidArgument() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
 throw new AssertionError("Should have thrown OptionException");
 }

 @Test
 public void testGetTriggers() {
 assertEquals(Collections.EMPTY_SET, argument.getTriggers());
 }

 @Test
 public void testProcessWithSubsequentSeparator() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ';', null, "--", null, 0);
 String[] args = {"--arg", "value1;value2"};
 argument.processValues(cmd, TestUtils.list(args), null);
 assertEquals(2, cmd.getValues("arg").size());
 assertEquals("value1", cmd.getValues("arg").get(0));
 assertEquals("value2", cmd.getValues("arg").get(1));
 }

 @Test
 public void testValidate() {
 String[] args = {"--arg", "value1", "value2"};
 argument.processValues(cmd, TestUtils.list(args), null);
 try {
 argument.validate(cmd);
 } catch (OptionException e) {
 fail("Should not have thrown OptionException");
 }
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidNumberOfArguments() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
 String[] args = {"--arg", "value1"};
 argument.processValues(cmd, TestUtils.list(args), null);
 try {
 argument.validate(cmd);
 } catch (OptionException e) {
 assertEquals("arg: 1 required arguments but only 0 were provided", e.getMessage());
 }
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidArgumentType() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0);
 String[] args = {"--arg", "value1", "option"};
 argument.processValues(cmd, TestUtils.list(args), null);
 try {
 argument.validate(cmd);
 } catch (OptionException e) {
 assertEquals("arg: unexpected value 'option'", e.getMessage());
 }
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidArgumentValue() {
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', new MyValidator(), "--", null, 0);
 String[] args = {"--arg", "value1", "invalidValue"};
 argument.processValues(cmd, TestUtils.list(args), null);
 try {
 argument.validate(cmd);
 } catch (OptionException e) {
 assertEquals("arg: invalid value 'invalidValue'", e.getMessage());
 }
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 argument.appendUsage(buffer, null, null);
 assertEquals("arg\n", buffer.toString());
 }

 @Test
 public void testAppendUsageWithNumberedArguments() {
 StringBuffer buffer = new StringBuffer();argument.appendUsage(buffer, null, null);
 assertEquals("arg 1\n", buffer.toString());
 }

 @Test
 public void testAppendUsageWithOptionalArgument() {
 StringBuffer buffer = new StringBuffer();
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0, DisplaySetting.DISPLAY_OPTIONAL);
 argument.appendUsage(buffer, null, null);
 assertEquals("[arg]\n", buffer.toString());
 }

 @Test
 public void testAppendUsageWithBracketedArguments() {
 StringBuffer buffer = new StringBuffer();
 argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null, 0, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
 argument.appendUsage(buffer, null, null);
 assertEquals("<arg>\n", buffer.toString());
 }

 @Test
 public void testGetDescription() {
 assertEquals("description", argument.getDescription());
 }

 @Test
 public void testHelpLines() {
 HelpFormatter formatter = new HelpFormatter();
 List<HelpLine> helpLines = argument.helpLines(1, null, null);
 formatter.printHelp("test", "test", helpLines.toArray(new HelpLine[0]));
 }

 @Test
 public void testGetMaximum() {
 assertEquals(1, argument.getMaximum());
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, argument.getMinimum());
 }

}

class MyValidator implements Validator {

 public void validate(List values) throws InvalidArgumentException {
 if (values.size() > 0 && !values.get(0).toString().equals("validValue")) {
 throw new InvalidArgumentException("Invalid value");
 }
 }

}