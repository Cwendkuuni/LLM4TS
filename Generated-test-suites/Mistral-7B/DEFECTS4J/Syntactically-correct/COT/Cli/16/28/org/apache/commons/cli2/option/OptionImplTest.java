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

 commandLine.addArgument("argument");
 assertTrue(option.canProcess(commandLine, "option"));
 }

 @Test
 public void testToString() {
 option = new OptionImpl(1, false);
 String expected = "OptionImpl[id=1, required=false]";
 assertEquals(expected, option.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionWithInvalidTrigger() {
 option = new OptionImpl(1, false);
 assertNull(option.findOption("invalidTrigger"));
 }

 @Test
 public void testFindOptionWithValidTrigger() {
 option = new OptionImpl(1, false);
 option.addTrigger("trigger");
 assertEquals(option, option.findOption("trigger"));
 }

 @Test
 public void testCheckPrefixesWithEmptyPrefixSet() {
 option = new OptionImpl(1, false);
 Set<String> prefixes = java.util.Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 option = new OptionImpl(1, false);
 Set<String> prefixes = java.util.Collections.singleton("prefix");
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidTriggerInPrefixes() {
 option = new OptionImpl(1, false);
 option.addPrefix("trigger");
 Set<String> prefixes = java.util.Collections.singleton("prefix");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesWithValidPrefixes() {
 option = new OptionImpl(1, false);
 Set<String> prefixes = java.util.Collections.singleton("prefix");
 option.addPrefix("prefix");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesWithValidTriggerInTriggers() {
 option = new OptionImpl(1, false);
 Set<String> triggers = java.util.Collections.singleton("trigger");
 option.addTrigger("trigger");
 option.checkPrefixes(java.util.Collections.emptySet());
 }

 @Test
 public void testCheckPrefixesWithValidPrefixAndTrigger() {
 option = new OptionImpl(1, false);
 Set<String> prefixes = java.util.Collections.singleton("prefix");
 Set<String> triggers = java.util.Collections.singleton("trigger");
 option.addPrefix("prefix");
 option.addTrigger("trigger");
 option.checkPrefixes(prefixes);
 }
}