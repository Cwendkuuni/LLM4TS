import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Testutils;
import org.apache.commons.cli2.validation.IntRangeValidator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

 private CommandLine cmd;
 private Argument arg;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 arg = new ArgumentImpl("arg1", "Test Argument", 1, 2, 'a', 'b', new IntRangeValidator(1, 2), "--value", null);
 }

 @Test
 public void testConstruction() {
 assertEquals("arg1", arg.getName());
 assertEquals("Test Argument", arg.getDescription());
 assertEquals(1, arg.getMinimum());
 assertEquals(2, arg.getMaximum());
 assertEquals('a', arg.getInitialSeparator());
 assertEquals('b', arg.getSubsequentSeparator());
 assertFalse(arg.getSubsequentSplit());
 assertNull(arg.getValidator());
 assertEquals("--value", arg.getConsumeRemaining());
 assertNull(arg.getDefaultValues());
 }

 @Test
 public void testGetters() {
 assertEquals("arg1", arg.getPreferredName());
 assertEquals("Test Argument", arg.getDescription());
 assertEquals(1, arg.getMinimum());
 assertEquals(2, arg.getMaximum());
 assertEquals('a', arg.getInitialSeparator());
 assertEquals('b', arg.getSubsequentSeparator());
 assertTrue(arg.getSubsequentSplit());
 assertNotNull(arg.getValidator());
 assertEquals("--value", arg.getConsumeRemaining());
 assertNotNull(arg.getDefaultValues());
 }

 @Test
 public void testProcessValues() {
 List values = Testutils.asList("value1", "value2");
 arg.process(cmd, values.listIterator());
 assertEquals(2, cmd.getValues(arg).size());
 assertEquals("value1", cmd.getValues(arg).get(0));
 assertEquals("value2", cmd.getValues(arg).get(1));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesInvalid() {
 List values = Testutils.asList("value1", "value2", "value3");
 arg.process(cmd, values.listIterator());
 }

 @Test
 public void testCanProcess() {
 assertTrue(arg.canProcess(cmd, "value"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessInvalid() {
 arg.canProcess(cmd, "--option");
 }

 @Test
 public void testGetPrefixes() {
 assertEquals(Collections.EMPTY_SET, arg.getPrefixes());
 }

 @Test
 public void testProcess() {
 List values = Testutils.asList("value1", "value2");
 arg.process(cmd, values.listIterator());
 }

 @Test(expected = OptionException.class)
 public void testProcessInvalid() {
 List values = Testutils.asList("value1", "--option", "value3");
 arg.process(cmd, values.listIterator());
 }

 @Test
 public void testGetInitialSeparator() {
 assertEquals('a', arg.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 assertEquals('b', arg.getSubsequentSeparator());
 }

 @Test
 public void testGetTriggers() {
 assertEquals(Collections.EMPTY_SET, arg.getTriggers());
 }

 @Test
 public void testGetConsumeRemaining() {
 assertEquals("--value", arg.getConsumeRemaining());
 }

 @Test
 public void testGetDefaultValues() {
 assertNull(arg.getDefaultValues());
 }

 @Test
 public void testValidate() {
 List values = Testutils.asList("value1", "value2");
 arg.validate(cmd);
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalid() {
 List values = Testutils.asList("value1", "value2", "value3");
 arg.validate(cmd);
 }

 @Test
 public void testValidateWithValidator() {
 List values = Testutils.asList("value1", "2", "value3");
 arg = new ArgumentImpl("arg1", "Test Argument", 1, 2, 'a', 'b', new IntRangeValidator(1, 2), "--value", null);
 arg.validate(cmd);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidValidator() {
 List values = Testutils.asList("value1", "2", "value3");
 arg = new ArgumentImpl("arg1", "Test Argument", 1, 2, 'a', 'b', new IntRangeValidator(-1, 2), "--value", null);
 arg.validate(cmd);
 }

 @Test
 public void testAppendUsage() {
 StringBuilder sb = new StringBuilder();
 arg.appendUsage(sb, null, null);
 assertEquals("  [--arg1 <value1> <value2>]", sb.toString());
 }
}