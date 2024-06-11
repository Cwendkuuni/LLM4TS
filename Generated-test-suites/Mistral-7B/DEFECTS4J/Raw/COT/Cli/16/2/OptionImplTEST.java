import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 option = new OptionImpl(1, true) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return false;
 }
 };
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess_whenArgumentsPresent_returnsFalse() {
 assertFalse(option.canProcess(commandLine, commandLine.getParsedArgs().listIterator()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanProcess_whenArgumentsPresent_throwsException() {
 option.canProcess(commandLine, commandLine.getParsedArgs().listIterator());
 }

 @Test
 public void testToString() {
 String expected = "1:<required option>";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals_whenEqual_returnsTrue() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 assertTrue(option1.equals(option2));
 }

 @Test
 public void testEquals_whenNotEqual_returnsFalse() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(2, false);
 assertFalse(option1.equals(option2));
 }

 @Test
 public void testHashCode_whenSameValues_returnsSameHashCode() {
 OptionImpl option1 = new OptionImpl(1, true) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return false;
 }
 };
 OptionImpl option2 = new OptionImpl(1, true) {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return false;
 }
 };
 assertEquals(option1.hashCode(), option2.hashCode());
 }

 @Test
 public void testFindOption_whenTriggerMatches_returnsOption() {
 OptionImpl option = new OptionImpl(1, true);
 Option optionFound = option.findOption("1");
 assertEquals(option, optionFound);
 }

 @Test
 public void testFindOption_whenTriggerDoesNotMatch_returnsNull() {
 OptionImpl option = new OptionImpl(1, true);
 Option optionFound = option.findOption("2");
 assertNull(optionFound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixes_whenPrefixesContainsInvalidPrefix_throwsException() {
 Set prefixes = java.util.Collections.singleton("invalid");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixes_whenPrefixesContainsValidPrefixes_doesNotThrowException() {
 Set prefixes = java.util.Collections.singleton("1");
 option.checkPrefixes(prefixes);
 }
}