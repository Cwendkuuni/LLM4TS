import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Before;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionImpl(1, true);
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testOptionImplConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));

 String argument = "value";
 commandLine.addArgument(argument);
 assertTrue(option.canProcess(commandLine, argument));
 commandLine.removeArgument(argument);
 }

 @Test
 public void testToString() {
 String expected = "OptionImpl[id=1, required=true]";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, false);

 assertEquals(option1, option1);
 assertEquals(option1, option2);
 assertNotSame(option1, option3);
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, false);

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testFindOption() {
 OptionImpl option = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(2, false);

 assertNull(option.findOption(null));
 assertNull(option.findOption(""));
 assertSame(option, option.findOption("1"));
 assertNull(option.findOption("2"));
 assertNull(option2.findOption("1"));
 assertSame(option2, option2.findOption("2"));
 }

 @Test
 public void testIsRequired() {
 OptionImpl option = new OptionImpl(1, true);
 assertTrue(option.isRequired());
 OptionImpl option2 = new OptionImpl(2, false);
 assertFalse(option2.isRequired());
 }

 @Test
 public void testCheckPrefixes() {
 Set<String> prefixes = new HashSet(Arrays.asList("--prefix", "-p"));
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 Set<String> prefixes = new HashSet(Arrays.asList("--prefix", "-x"));
 option.checkPrefixes(prefixes);
 }
}