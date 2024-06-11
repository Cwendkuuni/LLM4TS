import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private static Option createTestOption() {
 return new OptionImpl(1, "testOption", true, "t") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }

 @Override
 public String getPreferredName() {
 return "testOption";
 }

 @Override
 public String getDescription() {
 return "This is a test option";
 }

 @Override
 public String[] getPrefixes() {
 return new String[]{"-t", "--test"};
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"test", "opt"};
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 // no default value set
 }
 };
 }

 @Test
 public void testId() {
 Option option = createTestOption();
 int id = option.getId();
 assertEquals(1, id);
 }

 @Test
 public void testEquals() {
 Option option1 = createTestOption();
 Option option2 = createTestOption();
 assertTrue(option1.equals(option2));

 Option option3 = new OptionImpl(2, "testOption2", false, "to2") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }
 };
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 Option option1 = createTestOption();
 Option option2 = createTestOption();
 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 assertEquals(hashCode1, hashCode2);

 Option option3 = new OptionImpl(3, "testOption3", false, "to3") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }
 };
 assertNotEquals(hashCode1, option3.hashCode());
 }

 @Test
 public void testFindOption() {
 Option option = createTestOption();
 Option foundOption = option.findOption("t");
 assertTrue(foundOption == option);

 Option option2 = new OptionImpl(2, "testOption2", false, "to2") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }
 };
 Option notFoundOption = option.findOption("n");
 assertNull(notFoundOption);
 }

 @Test
 public void testIsRequired() {
 Option option = createTestOption();
 assertTrue(option.isRequired());
 }

 @Test
 public void testToString() {
 Option option = createTestOption();
 String expected = "Test option [--test, -t]\nDescription:\nThis is a test option\n";
 String actual = option.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testCanProcess() {
 Option option = createTestOption();
 WriteableCommandLine commandLine = TestUtils.createMockCommandLine();
 ListIterator<String> arguments = TestUtils.createMockArgumentIterator();
 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithEmptyPrefixList() {
 Option option = createTestOption();
 Set<String> prefixes = TestUtils.createEmptySet();
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixes() {
 Option option = createTestOption();
 Set<String> prefixes = TestUtils.createSetWithElements("--test", "-t");
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 Option option = createTestOption();
 Set<String> prefixes = TestUtils.createSetWithElements("--invalid", "-t");
 option.checkPrefixes(prefixes);
 }
}