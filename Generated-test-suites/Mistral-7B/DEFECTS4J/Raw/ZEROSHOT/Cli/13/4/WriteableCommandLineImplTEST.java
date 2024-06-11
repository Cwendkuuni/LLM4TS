import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testWriteableCommandLineImpl() {
 // Given
 Option option = new Option("option", "an option", true, "defaultValue");
 List arguments = new ArrayList();
 arguments.add("argument1");
 arguments.add("argument2");
 commandLine = new WriteableCommandLineImpl(option, arguments);

 // When
 // No need to test the default constructor as it is already tested in CommandLineImpl

 // Then
 assertEquals("argument1 argument2", commandLine.toString());
 assertEquals(2, commandLine.getOptions().size());
 assertEquals("option", commandLine.getOptions().get(0).getLongOpt());
 assertEquals(true, commandLine.hasOption(option));
 assertEquals("defaultValue", commandLine.getProperty("defaultValue"));
 assertNull(commandLine.getProperty("nonExistentProperty"));
 assertEquals("option", commandLine.getOption(option.getLongOpt()).getLongOpt());
 assertNull(commandLine.getOption(null));
 assertEquals("defaultValue", commandLine.getValues(option, null).get(0));
 assertEquals(Collections.singletonList("defaultValue"), commandLine.getValues(option, Collections.emptyList()));
 assertFalse(commandLine.getSwitch(option, null));

 // When setting a switch
 commandLine.addSwitch(option, true);

 // Then
 assertTrue(commandLine.getSwitch(option, null));

 // When setting a value
 commandLine.addValue(option, "newValue");

 // Then
 assertEquals(Collections.singletonList("newValue"), commandLine.getValues(option, null));
 }

 @Test
 public void testWriteableCommandLineImplWithArgumentOption() {
 // Given
 Option option = new Option("opt", "option", Argument.VALUE_OPTIONAL, "default");
 Argument arg = new Argument("arg", 1, "a", "an argument");
 option.addArgument(arg);
 List arguments = new ArrayList();
 arguments.add("--opt=value");
 commandLine = new WriteableCommandLineImpl(option, arguments);

 // When

 // Then
 assertEquals("value", commandLine.getProperty("arg"));
 assertNull(commandLine.getProperty("nonExistentProperty"));
 }
}