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
 public void testCanProcessTrue() {
 option = new OptionImpl(1, false);
 commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = commandLine.getArguments().listIterator();
 arguments.add("optionArgument");
 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testCanProcessFalse() {
 option = new OptionImpl(1, false);
 commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = commandLine.getArguments().listIterator();
 assertFalse(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testToString() {
 option = new OptionImpl(1, false);
 String expected = "OptionImpl[id=1, required=false]";
 assertEquals(expected, option.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesEmptyPrefixes() {
 option = new OptionImpl(1, false);
 Set prefixes = new java.util.HashSet<>();
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesValidPrefixes() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Arrays.asList("op", "opt", "option").spliterator().collect(Collectors.toSet());
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesInvalidTrigger() {
 option = new OptionImpl(1, false);
 Set prefixes = java.util.Arrays.asList("op", "opt", "invalidOptionTrigger").spliterator().collect(Collectors.toSet());
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testFindOptionValidTrigger() {
 option = new OptionImpl(1, false);
 Set triggers = java.util.Arrays.asList("option1", "option2", "option3").spliterator().collect(Collectors.toSet());
 option.setTriggers(triggers);
 Option found = option.findOption("option1");
 assertTrue(found == option);
 }

 @Test
 public void testFindOptionInvalidTrigger() {
 option = new OptionImpl(1, false);
 Set triggers = java.util.Arrays.asList("option1", "option2", "option3").spliterator().collect(Collectors.toSet());
 option.setTriggers(triggers);
 Option found = option.findOption("invalidOptionTrigger");
 assertNull(found);
 }

 @Test
 public void testIsRequired() {
 option = new OptionImpl(1, true);
 assertTrue(option.isRequired());
 option = new OptionImpl(1, false);
 assertFalse(option.isRequired());
 }

 @Test
 public void testDefaults() {
 option = new OptionImpl(1, false);
 option.defaults(commandLine);
 assertTrue(commandLine.getArguments().isEmpty());
 }
}