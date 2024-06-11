import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @Test
 public void testOptionImplConstructor() {
 option = new OptionImpl(1, false);
 assertEquals(1, option.getId());
 assertFalse(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 option = new OptionImpl(1, false);
 commandLine = new WriteableCommandLine();
 assertFalse(option.canProcess(commandLine, null));

 commandLine.addArgument("arg");
 assertTrue(option.canProcess(commandLine, "option"));
 }

 @Test
 public void testToString() {
 option = new OptionImpl(1, false);
 String expected = "OptionImpl[id=1, required=false]";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, false);
 OptionImpl option2 = new OptionImpl(1, false);
 OptionImpl option3 = new OptionImpl(2, false);

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, "opt");
 OptionImpl option2 = new OptionImpl(1, "opt");
 OptionImpl option3 = new OptionImpl(2, "opt");

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionNull() {
 option = new OptionImpl(1, false);
 assertNull(option.findOption(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionEmptyTriggers() {
 option = new OptionImpl(1, false);
 assertNull(option.findOption(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionNoMatch() {
 option = new OptionImpl(1, false);
 option.getTriggers().add("trigger1");
 assertNull(option.findOption("trigger2"));
 }

 @Test
 public void testFindOptionMatch() {
 option = new OptionImpl(1, false);
 option.getTriggers().add("trigger");
 option.getTriggers().add("trigger1");
 assertEquals(option, option.findOption("trigger"));
 assertEquals(option, option.findOption("trigger1"));
 }

 @Test
 public void testIsRequired() {
 OptionImpl option1 = new OptionImpl(1, false);
 OptionImpl option2 = new OptionImpl(2, true);

 assertFalse(option1.isRequired());
 assertTrue(option2.isRequired());
 }

 @Test
 public void testCheckPrefixesEmptyPrefixes() {
 option = new OptionImpl(1, "opt");
 Set prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesNullPrefixes() {
 option = new OptionImpl(1, "opt");
 try {
 option.checkPrefixes(null);
 } catch (IllegalArgumentException e) {
 fail("IllegalArgumentException expected, but was: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesNoMatch() {
 option = new OptionImpl(1, "opt");
 Set prefixes = java.util.Arrays.asList("pref");
 option.getTriggers().add("opt");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesMatch() {
 option = new OptionImpl(1, "opt");
 Set prefixes = java.util.Arrays.asList("opt", "pref-opt");
 option.getTriggers().add("opt");
 option.getTriggers().add("pref-opt");
 option.checkPrefixes(prefixes);
 }

}