import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 @Test
 public void testOptionImplConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 OptionImpl option = new OptionImpl(1, true);
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

 // No arguments, should return false
 assertFalse(option.canProcess(commandLine, arguments));

 // Wrong number of arguments, should return false
 arguments.add("");
 arguments.next();
 arguments.previous();
 assertFalse(option.canProcess(commandLine, arguments));

 // Right number of arguments, should return true
 arguments.remove(); // remove previous empty element
 assertSame(option, option.findOption("trigger"));
 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testToString() {
 OptionImpl option = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption";
 }

 @Override
 public String getDescription() {
 return "Test option description";
 }

 @Override
 public String[] getPrefixes() {
 return new String[]{"--", "-t"};
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"trigger"};
 }
 };

 String expected = "testOption" +
 " [--testOption [ARG]]: Test option description" +
 " {--testOption, -t {ARG}}: Test option description";
 String output = option.toString();
 assertEquals(expected, output);
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption1";
 }
 };
 OptionImpl option2 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption1";
 }
 };
 OptionImpl option3 = new OptionBuilder()
 .withLongOpt("testOption3")
 .withDescription("Test option description")
 .build();

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption1";
 }
 };
 OptionImpl option2 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption1";
 }
 };
 OptionImpl option3 = new OptionBuilder()
 .withLongOpt("testOption3")
 .withDescription("Test option description")
 .build();

 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption1";
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"trigger1"};
 }
 };
 OptionImpl option2 = new OptionImpl(2, true) {
 @Override
 public String getPreferredName() {
 return "testOption2";
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"trigger2"};
 }
 };

 OptionImpl foundOption = option1.findOption("trigger1");
 assertEquals(option1, foundOption);

 foundOption = option2.findOption("trigger1");
 assertNull(foundOption);

 foundOption = option1.findOption("trigger2");
 assertNull(foundOption);
 }

 @Test
 public void testIsRequired() {
 OptionImpl option1 = new OptionImpl(1, true);
 assertTrue(option1.isRequired());

 OptionImpl option2 = new OptionImpl(2, false);
 assertFalse(option2.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesInvalid() {
 OptionImpl option = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "";
 }

 @Override
 public String[] getPrefixes() {
 return new String[]{"--", "-t"};
 }
 };
 Set<String> prefixes = java.util.Collections.singleton("--test");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesValid() {
 OptionImpl option = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "testOption";
 }

 @Override
 public String[] getPrefixes() {
 return new String[]{"--test"};
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"test"};
 }
 };
 Set<String> prefixes = java.util.Collections.singleton("--test");
 option.checkPrefixes(prefixes);
 }
}