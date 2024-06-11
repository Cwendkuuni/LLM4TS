import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionImpl(1, "opt", true, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return super.canProcess(commandLine, arguments);
 }
 };
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testCanProcessWithOptionPresent() {
 commandLine.addArgument("arg1");
 commandLine.addOption(option);
 commandLine.addArgument("arg2");

 assertTrue(option.canProcess(commandLine, commandLine.getArguments().listIterator()));
 }

 @Test
 public void testCanProcessWithoutOption() {
 commandLine.addArgument("arg1");
 commandLine.addArgument("arg2");

 assertFalse(option.canProcess(commandLine, commandLine.getArguments().listIterator()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithNoPrefixes() {
 option.checkPrefixes(java.util.Collections.emptySet());
 }

 @Test
 public void testCheckPrefixesWithPrefixes() {
 option.checkPrefixes(java.util.Arrays.asList("--", "--opt"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithTriggerWithoutPrefix() {
 option.checkPrefixes(java.util.Arrays.asList("--other"));
 }

 @Test
 public void testEquals() {
 Option option2 = new OptionImpl(2, "opt2", false, "o2") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return super.canProcess(commandLine, arguments);
 }
 };

 assertFalse(option.equals(option2));

 Option option3 = new OptionImpl(1, "opt", true, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return super.canProcess(commandLine, arguments);
 }
 };

 assertTrue(option.equals(option3));
 }

 @Test
 public void testFindOption() {
 Option opt = option.findOption("o");
 assertNotNull(opt);
 assertNotNull(opt.getPreferredName());
 assertNotNull(opt.getDescription());
 assertFalse(opt.isRequired());
 }

 @Test
 public void testFindOptionWithNull() {
 Option opt = option.findOption(null);
 assertNull(opt);
 }

 @Test
 public void testFindOptionWithWrongTrigger() {
 Option opt = option.findOption("o2");
 assertNull(opt);
 }

 @Test
 public void testGetId() {
 assertEquals(1, option.getId());
 }

 @Test
 public void testHashCode() {
 OptionImpl option2 = new OptionImpl(2, "opt2", false, "o2") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return super.canProcess(commandLine, arguments);
 }
 };

 int hashCode1 = option.hashCode();
 int hashCode2 = option2.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "--o <option>";
 String actual = option.toString();

 assertEquals(expected, actual);
 }
}