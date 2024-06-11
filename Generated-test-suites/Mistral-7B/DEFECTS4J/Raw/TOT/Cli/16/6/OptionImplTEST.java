import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private CommandLineParser parser;

 @Before
 public void setUp() {
 option = new MyOption(1, "myOption", "m", "Description of myOption");
 parser = new DefaultParser();
 }

 @Test
 public void testCanProcess() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 ListIterator<String> arguments = commandLine.getArguments().listIterator();

 // valid usage
 arguments.add("mArgument");
 arguments.add("--myOption");
 assertTrue(option.canProcess(commandLine, arguments));

 // invalid usage, missing option
 arguments.clear();
 arguments.add("mArgument");
 assertFalse(option.canProcess(commandLine, arguments));

 // invalid usage, incorrect option
 arguments.clear();
 arguments.add("--wrongOption");
 assertFalse(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testToString() {
 String expected = "m (-m) Description of myOption";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl otherOption = new MyOption(2, "myOption2", "m2", "Description of myOption2");
 assertFalse(option.equals(otherOption));

 MyOption sameOption = new MyOption(1, "myOption", "m", "Description of myOption");
 assertTrue(option.equals(sameOption));
 }

 @Test
 public void testHashCode() {
 OptionImpl otherOption = new MyOption(2, "myOption2", "m2", "Description of myOption2");
 int expectedHashCode = option.hashCode();
 int otherHashCode = otherOption.hashCode();
 assertNotEquals(expectedHashCode, otherHashCode);

 MyOption sameOption = new MyOption(1, "myOption", "m", "Description of myOption");
 assertEquals(expectedHashCode, sameOption.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionMissingTrigger() {
 Option missingOption = new MyOptionMissingTrigger(1, "missingOption", "m", "Description of missingOption");
 assertNull(missingOption.findOption("invalidTrigger"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionInvalidPrefix() {
 Option invalidPrefixOption = new MyOptionInvalidPrefix(1, "invalidPrefixOption", "i", "Description of invalidPrefixOption");
 String[] invalidPrefixes = {"m", "inv"};
 invalidPrefixOption.checkPrefixes(asList(invalidPrefixes));
 }

 @Test
 public void testFindOptionValidTrigger() {
 Option validOption = new MyOptionValidTrigger(1, "validOption", "v", "Description of validOption");
 assertEquals(validOption, validOption.findOption("v"));
 }
}

class MyOption extends OptionImpl {
 MyOption(int id, String name, String longName, String description) {
 super(id, false);
 setName(name);
 setLongOpt(longName);
 setDescription(description);
 }
}

class MyOptionMissingTrigger extends OptionImpl {
 MyOptionMissingTrigger(int id, String name, String longName, String description) {
 super(id, false);
 setName(name);
 setLongOpt(longName);
 setDescription(description);
 setTriggers(asList("missingTrigger"));
 }
}

class MyOptionInvalidPrefix extends OptionImpl {
 MyOptionInvalidPrefix(int id, String name, String longName, String description) {
 super(id, false);
 setName(name);
 setLongOpt(longName);
 setDescription(description);
 setPrefixes(asList("inv"));
 }
}

class MyOptionValidTrigger extends OptionImpl {
 MyOptionValidTrigger(int id, String name, String longName, String description) {
 super(id, false);
 setName(name);
 setLongOpt(longName);
 setDescription(description);
 setTriggers(asList("v"));
 }
}