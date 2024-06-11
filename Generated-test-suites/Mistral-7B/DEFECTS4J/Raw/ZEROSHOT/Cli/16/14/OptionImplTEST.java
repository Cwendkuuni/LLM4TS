import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.Option;
import org.apache.commons.cli2.option.OptionImpl;
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
 OptionImpl option = new OptionImpl(1, false);
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();
 CommandLine cl = new CommandLine();
 cl.parseArgument(new String[]{"test", "--option1"});
 arguments = cl.getParsedArgs().listIterator();

 assertTrue(option.canProcess(commandLine, arguments));
 assertEquals("--option1", arguments.next());
 assertFalse(arguments.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanProcessWithoutPrefix() {
 OptionImpl option = new OptionImpl(1, false);
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();
 CommandLine cl = new CommandLine();
 cl.parseArgument(new String[]{"test", "option"});
 arguments = cl.getParsedArgs().listIterator();

 option.canProcess(commandLine, arguments);
 }

 @Test
 public void testToString() {
 OptionImpl option = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };

 String optionString = option.toString();
 String expected = "Option 1 description\n" +
 "  --prefix,--OPT1,--option1,--opt1\n";
 assertEquals(expected, optionString);
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };
 OptionImpl option2 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };
 OptionImpl option3 = new OptionImpl(2, false) {
 @Override
 public String getPreferredName() {
 return "option2";
 }

 @Override
 public String getDescription() {
 return "Option 2 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix2");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option2", "opt2");
 }
 };

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };
 OptionImpl option2 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option2", "opt2");
 }
 };
 OptionImpl option3 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix3");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };

 int option1HashCode = option1.hashCode();
 int option2HashCode = option2.hashCode();
 int option3HashCode = option3.hashCode();

 assertEquals(option1HashCode, option1.hashCode());
 assertEquals(option1HashCode, option2.hashCode());
 assertNotEquals(option1HashCode, option3.hashCode());
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };
 OptionImpl option2 = new OptionImpl(2, false) {
 @Override
 public String getPreferredName() {
 return "option2";
 }

 @Override
 public String getDescription() {
 return "Option 2 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix2");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option2", "opt2");
 }
 };
 OptionImpl option3 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "Option 1 description";
 }

 @Override
 public Iterable<String> getPrefixes() {
 return Iterable.of("--prefix");
 }

 @Override
 public Iterable<String> getTriggers() {
 return Iterable.of("option1", "opt1");
 }
 };

 Option foundOption = option1.findOption("option1");
 assertSame(option1, foundOption);

 foundOption = option1.findOption("opt1");
 assertSame(option1, foundOption);

 foundOption = option1.findOption("option2");
 assertNull(foundOption);
 }
}