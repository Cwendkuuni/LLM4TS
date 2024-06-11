import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionImpl(1, "testOption", true, "t") {
 @Override
 public String getDescription() {
 return "Test Option";
 }
 };
 }
 };
 commandLine = new CommandLine();
 }

 @Test
 public void testOptionImplConstructor() {
 Option option = new OptionImpl(1, "testOption", true, "t");
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));
 assertFalse(option.canProcess(commandLine, ""));
 assertFalse(option.canProcess(commandLine, "testOption"));
 commandLine.addOption("--testOption");
 commandLine.addOption("--anotherOption");
 assertTrue(option.canProcess(commandLine, "--testOption"));
 assertFalse(option.canProcess(commandLine, "--anotherOption"));
 }

 @Test
 public void testToString() {
 String expected = "Test Option\n" +
 "  This is a test option.\n" +
 "  -t, --testOption   Test Option\n";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 Option option2 = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionImpl(1, "testOption", true, "t") {
 @Override
 public String getDescription() {
 return "Test Option";
 }
 };
 }
 };
 Option option3 = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionImpl(2, "testOption", true, "t") {
 @Override
 public String getDescription() {
 return "Test Option";
 }
 };
 }
 };
 Option option4 = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionBuilder("testOption2")
 .longOpt("testOption2")
 .required(true)
 .build();
 }
 };
 Option option5 = new OptionBuilder("testOption3")
 .longOpt("testOption3")
 .build();
 Option option6 = new OptionBuilder("testOption")
 .build();
 assertEquals(option, option);
 assertEquals(option, this.option);
 assertEquals(this.option, option);
 assertEquals(option, new OptionImpl(1, "testOption", true, "t"));
 assertFalse(option.equals(null));
 assertFalse(option.equals(new Object()));
 assertFalse(option.equals(option2));
 assertFalse(option.equals(option3));
 assertFalse(option.equals(option4));
 assertFalse(option.equals(option5));
 }

 @Test
 public void testHashCode() {
 Option option2 = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionImpl(1, "testOption", true, "t") {
 @Override
 public String getDescription() {
 return "Test Option";
 }
 };
 }
 };
 Option option3 = new AbstractOption() {
 @Override
 public Option build() {
 return new OptionImpl(1, "testOption2", true, "t") {
 @Override
 public String getDescription() {
 return "Test Option";
 }
 };
 }
 };
 int hashCode = option.hashCode();
 assertEquals(hashCode, option.hashCode());
 assertEquals(hashCode, this.option.hashCode());
 assertEquals(this.option.hashCode(), option.hashCode());
 assertEquals(option.hashCode(), new OptionImpl(1, "testOption", true, "t").hashCode());
 assertNotSame(option.hashCode(), option2.hashCode());
 assertNotSame(option.hashCode(), option3.hashCode());
 }

 @Test
 public void testFindOption() {
 Option testOption = new OptionImpl(1, "testOption", true, "t") {
 @Override
 public String getDescription() {return "Test Option";}
 };
 Option testOption2 = new OptionImpl(2, "testOption2", true, "t") {
 @Override
 public String getDescription() {return "Test Option 2";}
 };
 Set<String> triggers = new java.util.HashSet<>();
 triggers.add("t");
 triggers.add("testOption");
 triggers.add("--test-option");
 triggers.add("--test-option2");
 TestableOption testableOption = new TestableOption();
 testableOption.addTriggers(triggers);
 Option result = testableOption.findOption(commandLine, "t");
 assertEquals(testOption, result);
 result = testableOption.findOption(commandLine, "testOption");
 assertEquals(testOption, result);
 result = testableOption.findOption(commandLine, "--test-option");
 assertEquals(testOption, result);
 result = testableOption.findOption(commandLine, "testOption2");
 assertNull(result);
 result = testableOption.findOption(commandLine, "nonexistentOption");
 assertNull(result);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("testOption"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixes() {
 Set<String> prefixes = new java.util.HashSet<>();
 prefixes.add("test");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesNoError() {
 Set<String> prefixes = new java.util.HashSet<>();
 prefixes.add("--test");
 option.checkPrefixes(prefixes);
 }
}

class AbstractOption {
 public abstract Option build();
}

class TestableOption extends OptionImpl implements Option {
 Set<String> triggers;

 public TestableOption() {
 super(1, "testOption", true, "t");
 }

 public Option build() {
 return this;
 }

 public void addTriggers(Set<String> triggers) {
 this.triggers = triggers;
 }

 @Override
 public Set<String> getTriggers() {
 return triggers;
 }
}