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
 commandLine = new WriteableCommandLine();
 option = new OptionImpl(1, false);
 assertFalse(option.canProcess(commandLine, null));

 String argument = "myArgument";
 commandLine.addArgument(argument);
 assertTrue(option.canProcess(commandLine, argument));
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
 OptionImpl option1 = new OptionImpl(1, "myOption");
 OptionImpl option2 = new OptionImpl(1, "myOtherOption");
 int expectedHashcode1 = 1 * 37 + "myOption".hashCode();
 int expectedHashcode2 = 1 * 37 + "myOtherOption".hashCode();
 assertEquals(expectedHashcode1, option1.hashCode());
 assertEquals(expectedHashcode2, option2.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionWithNoMatchingTrigger() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Collections.singleton("prefix");
 commandLine = new WriteableCommandLine();
 commandLine.addArgument("noMatchingTrigger");
 option.findOption("noMatchingTrigger");
 }

 @Test
 public void testFindOptionWithMatchingTrigger() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Collections.singleton("prefix");
 commandLine = new WriteableCommandLine();
 commandLine.addArgument("prefixOption");
 option.findOption("prefixOption");
 assertSame(option, option.findOption("prefixOption"));
 }

 @Test
 public void testIsRequired() {
 option = new OptionImpl(1, true);
 assertTrue(option.isRequired());
 option = new OptionImpl(1, false);
 assertFalse(option.isRequired());
 }

 @Test
 public void testCheckPrefixesEmptyPrefixes() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithNoMatchingPrefix() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Collections.singleton("prefix");
 option.checkPrefixes(prefixes);
 option.getTriggers();
 String trigger = "noMatchingTrigger";
 option.checkPrefix(prefixes, trigger);
 }

 @Test
 public void testCheckPrefixesWithMatchingPrefix() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Collections.singleton("prefix");
 option.checkPrefixes(prefixes);
 String trigger = "prefixOption";
 option.checkPrefix(prefixes, trigger);
 }
}