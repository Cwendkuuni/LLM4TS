import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 option = new OptionImpl(1, true);
 }

 @Test
 public void testConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = commandLine.getParsedArguments().listIterator();
 assertTrue(option.canProcess(commandLine, arguments));
 arguments.next();
 assertFalse(option.canProcess(commandLine, arguments));
 }

 @Test(expected = IllegalStateException.class)
 public void testToString() {
 OptionImpl option = new OptionImpl(1, false);
 option.appendUsage(new StringBuffer(), DisplaySetting.ALL, null);
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, true);
 OptionImpl option4 = new OptionImpl(1, true);

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 assertTrue(option1.equals(option4));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, "test");
 OptionImpl option2 = new OptionImpl(1, "test");
 OptionImpl option3 = new OptionImpl(2, "test");

 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, "test1");
 OptionImpl option2 = new OptionImpl(2, "test2");
 OptionImpl option3 = new OptionImpl(3, "test3");
 Set<String> triggers = new java.HashSet<>();
 triggers.add("test1");
 triggers.add("test2");
 triggers.add("test3");

 assertEquals(option1, option1.findOption("test1"));
 assertEquals(option1, option1.findOption("Test1"));
 assertEquals(option1, option1.findOption("1"));
 assertEquals(option1, option1.findOption(new String[]{"1"}));
 assertEquals(option1, option1.findOption(new String[]{"t", "est1"}));
 assertEquals(option1, option1.findOption(new String[]{"test1", "test2"}));
 assertEquals(option1, option1.findOption(triggers));
 assertNull(option1.findOption("test4"));
 assertNull(option1.findOption("test5"));
 assertNull(option1.findOption(new String[]{"test4"}));
 assertNull(option1.findOption(new String[]{"test5"}));
 assertNull(option1.findOption(new String[]{"test5", "test4"}));
 assertNull(option1.findOption(new String[]{"test5", "test6"}));
 }

 @Test
 public void testIsRequired() {
 OptionImpl option = new OptionImpl(1, true);
 assertTrue(option.isRequired());
 OptionImpl option2 = new OptionImpl(1, false);
 assertFalse(option2.isRequired());
 }

 @Test
 public void testCheckPrefixes() {
 OptionImpl option = new OptionImpl(1, "test");
 Set<String> prefixes = new java.HashSet<>();
 prefixes.add("tes");
 assertNull(option.findOption("test"));
 try {
 option.checkPrefixes(prefixes);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "test needs a prefix in [tes]";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 prefixes.add("test");
 assertEquals(option, option.findOption("test"));
 }
}