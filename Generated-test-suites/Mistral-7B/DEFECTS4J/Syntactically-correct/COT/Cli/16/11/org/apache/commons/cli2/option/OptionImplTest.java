import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionImpl(1, true);
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testId() {
 assertEquals(1, option.getId());
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));

 String arg = "test";
 commandLine.addArgument(arg);
 assertTrue(option.canProcess(commandLine, arg));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanProcessWithNoArgument() {
 option.canProcess(commandLine, null);
 }

 @Test
 public void testToString() {
 String expected = "OptionImpl[id=1, preferredName=null, description=null, prefixes=[], triggers=[]]";
 String actual = option.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, true);

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, true);

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(2, false);
 OptionImpl option3 = new OptionImpl(1, true);
 Set<String> triggers1 = option1.getTriggers();
 Set<String> triggers2 = option2.getTriggers();
 Set<String> triggers3 = option3.getTriggers();

 triggers1.add("trigger1");
 triggers1.add("trigger2");
 triggers2.add("trigger3");

 Option foundOption = option1.findOption("trigger1");
 assertEquals(option1, foundOption);

 foundOption = option1.findOption("trigger3");
 assertNull(foundOption);

 foundOption = option2.findOption("trigger1");
 assertNull(foundOption);

 foundOption = option2.findOption("trigger3");
 assertNull(foundOption);

 foundOption = option3.findOption("trigger1");
 assertEquals(option1, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testCheckPrefixesEmptyPrefixes() {
 Set<String> prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithNoPrefix() {
 Set<String> prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 String trigger = "test";
 option.checkPrefix(prefixes, trigger);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithNoMatchingPrefix() {
 Set<String> prefixes = java.util.Collections.singleton("prefix1");
 option.checkPrefixes(prefixes);
 String trigger = "prefix2test";
 option.checkPrefix(prefixes, trigger);
 }

 @Test
 public void testCheckPrefixesWithMatchingPrefix() {
 Set<String> prefixes = java.util.Collections.singleton("prefix1");
 option.checkPrefixes(prefixes);
 String trigger = "prefix1test";
 option.checkPrefix(prefixes, trigger);
 }
}